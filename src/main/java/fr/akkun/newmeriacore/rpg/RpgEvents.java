package fr.akkun.newmeriacore.rpg;

import fr.akkun.newmeriacore.NewmeriaCore;
import fr.akkun.newmeriacore.entity.SnowWalker;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;

@EventBusSubscriber(modid = NewmeriaCore.MOD_ID)
public class RpgEvents {
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        RpgAttributeModifiers.apply(player, player.getData(RpgAttachments.RPG_DATA));
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        RpgAttributeModifiers.apply(player, player.getData(RpgAttachments.RPG_DATA));
    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        RpgData data = event.getEntity().getData(RpgAttachments.RPG_DATA);
        double multiplier = RpgAttributeModifiers.miningSpeedMultiplier(data.forceLevel());
        if (multiplier != 1.0) {
            event.setNewSpeed((float) (event.getNewSpeed() * multiplier));
        }
    }

    @SubscribeEvent
    public static void onXpChange(PlayerXpEvent.XpChange event) {
        if (!(event.getEntity() instanceof ServerPlayer player) || event.getAmount() <= 0) {
            return;
        }
        double multiplier = RpgAttributeModifiers.xpMultiplier(player.getData(RpgAttachments.RPG_DATA).magicLevel());
        if (multiplier != 1.0) {
            event.setAmount((int) Math.round(event.getAmount() * multiplier));
        }
    }

    @SubscribeEvent
    public static void onLivingDamagePre(LivingDamageEvent.Pre event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        RpgData data = player.getData(RpgAttachments.RPG_DATA);
        DamageSource source = event.getSource();
        float damage = event.getNewDamage();
        if (data.resistanceLevel() >= 1 && source.is(DamageTypeTags.IS_PROJECTILE)) {
            damage /= 2f;
        }
        if (data.resistanceLevel() >= 3 && source.is(DamageTypeTags.IS_EXPLOSION)) {
            damage /= 2f;
        }
        if (data.resistanceLevel() >= 5 && source.is(DamageTypeTags.IS_FIRE)) {
            damage /= 2f;
        }
        if (damage != event.getNewDamage()) {
            event.setNewDamage(damage);
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }
        if (!(event.getSource().getEntity() instanceof ServerPlayer killer)) {
            return;
        }
        LivingEntity victim = event.getEntity();
        int gained = pointsForKill(victim);
        if (gained > 0) {
            grantPoints(killer, gained);
        }
    }

    private static int pointsForKill(LivingEntity victim) {
        if (victim instanceof Player player) {
            return player.getData(RpgAttachments.RPG_DATA).level() * 10;
        }
        if (victim instanceof EnderDragon || victim instanceof WitherBoss
                || victim instanceof Warden) {
            return 100;
        }
        if (victim instanceof ElderGuardian) {
            return 20;
        }
        if (victim instanceof IronGolem) {
            return 10;
        }
        if (victim instanceof SnowWalker) {
            return 5;
        }
        if (victim instanceof Monster || victim instanceof Enemy) {
            return 1;
        }
        return 0;
    }

    private static void grantPoints(ServerPlayer player, int amount) {
        if (TotemBonuses.hasMatchingTotem(player)) {
            amount *= 2;
        }
        RpgData data = player.getData(RpgAttachments.RPG_DATA);
        RpgData updated = data.addPoints(amount);
        if (updated.equals(data)) {
            return;
        }
        player.setData(RpgAttachments.RPG_DATA, updated);
        if (updated.level() > data.level()) {
            player.sendSystemMessage(Component.translatable("rpg.newmeriacore.level_up",
                    updated.level(), updated.unspentStatPoints()), true);
        }
    }
}
