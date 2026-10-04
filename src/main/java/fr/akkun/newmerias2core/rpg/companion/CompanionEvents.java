package fr.akkun.newmerias2core.rpg.companion;

import fr.akkun.newmerias2core.NewmeriaS2Core;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingBreatheEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingConversionEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Enforces the companion combat rules on top of whatever the chosen form's own AI would otherwise
 * do: never attacks its owner, always attacks hostile mobs, and otherwise only attacks whoever its
 * owner strikes or whoever strikes its owner - it never retaliates against its own attacker unless
 * the owner also targeted them. That "provoked" entity is tracked here, in memory only (ephemeral
 * combat state, doesn't need to survive a save/reload or be synced).
 */
@EventBusSubscriber(modid = NewmeriaS2Core.MOD_ID)
public class CompanionEvents {
    private static final Map<UUID, UUID> PROVOKED_TARGET = new HashMap<>();
    private static final float INK_SAC_HEAL_AMOUNT = 5.0F;

    @SubscribeEvent
    public static void onChangeTarget(LivingChangeTargetEvent event) {
        LivingEntity entity = event.getEntity();
        var data = entity.getExistingData(CompanionAttachments.COMPANION_DATA);
        if (data.isEmpty()) {
            return;
        }
        LivingEntity proposed = event.getNewAboutToBeSetTarget();
        if (proposed == null) {
            return;
        }
        if (proposed.getUUID().equals(data.get().ownerId())) {
            event.setCanceled(true);
            return;
        }
        boolean isHostile = proposed instanceof Monster || proposed instanceof Enemy;
        boolean isProvoked = proposed.getUUID().equals(PROVOKED_TARGET.get(entity.getUUID()));
        if (!isHostile && !isProvoked) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onDamage(LivingDamageEvent.Post event) {
        LivingEntity victim = event.getEntity();
        Entity attackerEntity = event.getSource().getEntity();

        if (attackerEntity instanceof ServerPlayer owner) {
            provoke(owner, victim);
        }
        if (victim instanceof ServerPlayer owner && attackerEntity instanceof LivingEntity attacker) {
            provoke(owner, attacker);
        }
    }

    private static void provoke(ServerPlayer owner, LivingEntity target) {
        Mob companion = CompanionManager.findCompanion(owner);
        if (companion == null || companion == target) {
            return;
        }
        PROVOKED_TARGET.put(companion.getUUID(), target.getUUID());
        companion.setTarget(target);
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        var data = entity.getExistingData(CompanionAttachments.COMPANION_DATA);
        if (data.isEmpty()) {
            return;
        }
        PROVOKED_TARGET.remove(entity.getUUID());

        Player owner = entity.level().getPlayerByUUID(data.get().ownerId());
        if (owner != null) {
            owner.sendSystemMessage(Component.translatable("rpg.newmerias2core.companion.died"));
        }
    }

    /** Regardless of form, a companion never drowns - its air supply never depletes. */
    @SubscribeEvent
    public static void onBreathe(LivingBreatheEvent event) {
        if (event.getEntity().getExistingData(CompanionAttachments.COMPANION_DATA).isPresent()) {
            event.setCanBreathe(true);
        }
    }

    /** Blocks any vanilla self-conversion (e.g. a Zombie companion turning into a Drowned after
     *  spending too long underwater) - companions keep their chosen form forever. */
    @SubscribeEvent
    public static void onConversion(LivingConversionEvent.Pre event) {
        if (event.getEntity().getExistingData(CompanionAttachments.COMPANION_DATA).isPresent()) {
            event.setCanceled(true);
        }
    }

    /** Companions have no items of their own, so a death (including being replaced by a new
     *  summon) should drop nothing - not a golem's iron/poppies, not a zombie's rotten flesh, etc. */
    @SubscribeEvent
    public static void onDrops(LivingDropsEvent event) {
        if (event.getEntity().getExistingData(CompanionAttachments.COMPANION_DATA).isPresent()) {
            event.setCanceled(true);
        }
    }

    /** Right-clicking your own companion with an ink sac heals it 5 HP (consuming the sac outside of
     *  creative mode) - a small, thematically fitting top-up rather than a full heal, since companions
     *  otherwise have no way to recover health besides not getting hit. */
    @SubscribeEvent
    public static void onInteract(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) {
            return;
        }
        if (!(event.getTarget() instanceof Mob companion)) {
            return;
        }
        var data = companion.getExistingData(CompanionAttachments.COMPANION_DATA);
        if (data.isEmpty() || !data.get().ownerId().equals(player.getUUID())) {
            return;
        }

        ItemStack stack = player.getItemInHand(event.getHand());
        if (!stack.is(Items.INK_SAC) || companion.getHealth() >= companion.getMaxHealth()) {
            return;
        }

        companion.heal(INK_SAC_HEAL_AMOUNT);
        if (!player.isCreative()) {
            stack.shrink(1);
        }
        ((ServerLevel) companion.level()).sendParticles(ParticleTypes.HEART,
                companion.getX(), companion.getY(0.75), companion.getZ(), 6, 0.3, 0.3, 0.3, 0.0);
        companion.level().playSound(null, companion.blockPosition(), SoundEvents.GENERIC_EAT.value(), SoundSource.NEUTRAL, 0.7F, 1.2F);

        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }
}
