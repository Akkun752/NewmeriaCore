package fr.akkun.newmeriacore.rpg;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import fr.akkun.newmeriacore.rpg.companion.network.OpenCompanionMenuPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** Server-side spell execution and (in-memory, non-persisted) per-player cooldown tracking. */
public class SpellCasting {
    // General cooldown (not per-spell): casting any spell locks out every spell until it runs out.
    private static final Map<UUID, Long> COOLDOWN_END_TICK = new HashMap<>();
    // Damage/blast size multiplier over vanilla lightning/ghast-fireball defaults.
    private static final float POWER_MULTIPLIER = 3.0F;

    /** @return whether a spell was actually cast (false if none selected, locked, or still on cooldown). */
    public static boolean cast(ServerPlayer player) {
        RpgData data = player.getData(RpgAttachments.RPG_DATA);
        if (data.selectedSpell() == RpgData.NO_SPELL) {
            return false;
        }
        RpgSpell spell = RpgSpell.values()[data.selectedSpell()];
        if (!spell.isUnlocked(data.magicLevel())) {
            return false;
        }

        if (!player.isCreative()) {
            long now = player.level().getGameTime();
            Long endTick = COOLDOWN_END_TICK.get(player.getUUID());
            if (endTick != null && now < endTick) {
                long remainingSeconds = (endTick - now) / 20 + 1;
                player.sendSystemMessage(Component.translatable("rpg.newmeriacore.spell.on_cooldown", spell.displayName(), remainingSeconds), true);
                return false;
            }
            COOLDOWN_END_TICK.put(player.getUUID(), now + cooldownTicks(data.magicLevel()));
        }

        switch (spell) {
            case LIGHTNING -> castLightning(player);
            case TELEPORT -> castTeleport(player);
            case FRIENDSHIP -> castFriendship(player);
            case FIREBALL -> castFireball(player);
            case INK_FRIEND -> castInkFriend(player);
        }
        return true;
    }

    /** General cooldown length, driven purely by Magic level (0: 70s, 1: 60s, 2: 45s, 3: 30s, 4: 20s, 5+: 10s). */
    private static int cooldownTicks(int magicLevel) {
        int seconds = switch (magicLevel) {
            case 0 -> 70;
            case 1 -> 60;
            case 2 -> 45;
            case 3 -> 30;
            case 4 -> 20;
            default -> 10;
        };
        return seconds * 20;
    }

    private static void castLightning(ServerPlayer player) {
        ServerLevel level = player.level();
        // Player.pick() only ray-traces blocks; use the entity-aware variant so an entity in the
        // crosshair gets struck directly instead of the ground behind/beyond it.
        HitResult hit = ProjectileUtil.getHitResultOnViewVector(player, EntitySelector.CAN_BE_PICKED, 100.0);
        Vec3 target = hit.getLocation();
        LightningBolt bolt = EntityTypes.LIGHTNING_BOLT.create(level, EntitySpawnReason.TRIGGERED);
        if (bolt == null) {
            return;
        }
        bolt.setPos(target.x, target.y, target.z);
        bolt.setCause(player);
        bolt.setDamage(bolt.getDamage() * POWER_MULTIPLIER);
        level.addFreshEntity(bolt);
    }

    private static void castTeleport(ServerPlayer player) {
        TeleportTransition transition = player.findRespawnPositionAndUseSpawnBlock(false, TeleportTransition.DO_NOTHING);
        ServerPlayer teleported = player.teleport(transition);
        if (teleported == null) {
            return;
        }
        ServerLevel level = teleported.level();
        Vec3 pos = teleported.position();
        level.sendParticles(ParticleTypes.PORTAL, pos.x, pos.y + 1.0, pos.z, 32, 0.5, 1.0, 0.5, 0.3);
        level.playSound(null, pos.x, pos.y, pos.z, SoundEvents.PLAYER_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    /** Instantly tames whatever tameable mob the player is looking at, no food/riding grind needed. */
    private static void castFriendship(ServerPlayer player) {
        HitResult hit = ProjectileUtil.getHitResultOnViewVector(player, EntitySelector.CAN_BE_PICKED, player.entityInteractionRange());
        if (!(hit instanceof EntityHitResult entityHit)) {
            return;
        }
        Entity target = entityHit.getEntity();
        if (target instanceof TamableAnimal tamable && !tamable.isTame()) {
            tamable.tame(player);
            celebrateTaming(player.level(), target);
        } else if (target instanceof AbstractHorse horse && !horse.isTamed()) {
            horse.setTamed(true);
            horse.setOwner(player);
            celebrateTaming(player.level(), target);
        }
    }

    private static void celebrateTaming(ServerLevel level, Entity tamed) {
        level.sendParticles(ParticleTypes.HEART, tamed.getX(), tamed.getY() + tamed.getBbHeight() * 0.75, tamed.getZ(), 10, 0.3, 0.3, 0.3, 0.0);
        level.playSound(null, tamed.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.NEUTRAL, 0.7F, 1.4F);
    }

    private static void castFireball(ServerPlayer player) {
        ServerLevel level = player.level();
        Vec3 direction = player.getLookAngle();
        // Vanilla ghast fireballs: explosion power 1, 6.0F direct-hit damage.
        int explosionPower = Math.round(1 * POWER_MULTIPLIER);
        float directHitDamage = 6.0F * POWER_MULTIPLIER;
        RpgLargeFireball fireball = new RpgLargeFireball(level, player, direction, explosionPower, directHitDamage);
        fireball.setPos(player.getX(), player.getEyeY(), player.getZ());
        level.addFreshEntity(fireball);
    }

    /** Opens the companion-selection menu; the actual summon happens once the player picks a form
     *  (see SummonCompanionPayload) - the cooldown/durability cost is already spent at this point,
     *  matching every other spell, regardless of what the player does with the menu afterward. */
    private static void castInkFriend(ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, new OpenCompanionMenuPayload());
    }
}
