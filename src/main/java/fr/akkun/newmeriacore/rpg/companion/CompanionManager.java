package fr.akkun.newmeriacore.rpg.companion;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.UUID;

/**
 * Server-side lifecycle for Ink Friend companions: summoning (replacing any existing one first) and
 * applying the shared rules on top of whatever form was picked - fixed 20 HP, ownership (using each
 * form's own vanilla taming where it exists), and baseline hostile-mob aggro. See {@code
 * CompanionEvents} for the rest of the targeting rules (never the owner, otherwise only provoked
 * targets).
 */
public class CompanionManager {
    public static final float COMPANION_MAX_HEALTH = 30.0F;

    public static void summon(ServerPlayer player, CompanionForm form) {
        ServerLevel level = player.level();
        killExistingCompanion(player, level);

        Entity spawned = form.entityType().create(level, EntitySpawnReason.MOB_SUMMONED);
        if (!(spawned instanceof Mob companion)) {
            return;
        }

        // At the player's own feet, not projected out along their look angle - looking down at the
        // ground would otherwise spawn the companion embedded in a block.
        Vec3 spawnPos = player.position();
        companion.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
        companion.setYRot(player.getYRot());

        AttributeInstance maxHealth = companion.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealth != null) {
            maxHealth.setBaseValue(COMPANION_MAX_HEALTH);
        }
        companion.setHealth(COMPANION_MAX_HEALTH);

        applyOwnership(companion, player);
        companion.setData(CompanionAttachments.COMPANION_DATA, new CompanionData(player.getUUID(), form));
        companion.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(companion, Monster.class, true));
        // Every form should be comfortable in water regardless of its vanilla nature - AI won't avoid
        // pathing through it (actual no-drown/no-conversion handling is in CompanionEvents).
        companion.setPathfindingMalus(PathType.WATER, 0.0F);
        companion.setPathfindingMalus(PathType.WATER_BORDER, 0.0F);
        // Nautilus is happy staying submerged - every other form always surfaces, in any fluid.
        if (form != CompanionForm.NAUTILUS) {
            companion.goalSelector.addGoal(0, new CompanionFloatGoal(companion));
        }
        // Always follows its owner (teleporting over when left too far behind), regardless of form.
        companion.goalSelector.addGoal(4, new CompanionFollowGoal(companion, 1.0));

        level.addFreshEntity(companion);
        player.setData(CompanionAttachments.PLAYER_COMPANION, PlayerCompanionData.of(companion.getUUID()));

        level.sendParticles(ParticleTypes.HAPPY_VILLAGER, spawnPos.x, spawnPos.y + 1.0, spawnPos.z, 15, 0.4, 0.4, 0.4, 0.0);
        level.playSound(null, companion.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.NEUTRAL, 0.7F, 1.2F);
    }

    private static void applyOwnership(Mob companion, ServerPlayer owner) {
        if (companion instanceof TamableAnimal tamable) {
            tamable.tame(owner);
        } else if (companion instanceof AbstractHorse horse) {
            horse.setTamed(true);
            horse.setOwner(owner);
        }
        if (companion instanceof IronGolem golem) {
            // Suppresses the golem's default "hostile to any nearby player" behaviour.
            golem.setPlayerCreated(true);
        }
    }

    private static void killExistingCompanion(ServerPlayer player, ServerLevel level) {
        Optional<UUID> existingId = player.getData(CompanionAttachments.PLAYER_COMPANION).companionId();
        if (existingId.isEmpty()) {
            return;
        }
        Entity existing = level.getEntity(existingId.get());
        if (existing instanceof LivingEntity livingExisting && !existing.isRemoved()) {
            // Lethal, armor/invulnerability-bypassing damage (same damage type the /kill command
            // uses) - triggers the normal death pipeline. CompanionEvents#onDrops strips out any
            // vanilla loot-table drops, so this doesn't leave items behind.
            livingExisting.hurtServer(level, level.damageSources().genericKill(), Float.MAX_VALUE);
        }
    }

    public static Mob findCompanion(ServerPlayer player) {
        Optional<UUID> id = player.getData(CompanionAttachments.PLAYER_COMPANION).companionId();
        if (id.isEmpty()) {
            return null;
        }
        Entity entity = player.level().getEntity(id.get());
        return entity instanceof Mob mob && !mob.isRemoved() ? mob : null;
    }
}
