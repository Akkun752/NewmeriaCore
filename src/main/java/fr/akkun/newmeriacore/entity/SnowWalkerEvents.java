package fr.akkun.newmeriacore.entity;

import fr.akkun.newmeriacore.NewmeriaCore;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

@EventBusSubscriber(modid = NewmeriaCore.MOD_ID)
public class SnowWalkerEvents {
    @SubscribeEvent
    public static void onSnowWalkerDamagePre(LivingDamageEvent.Pre event) {
        if (event.getEntity() instanceof SnowWalker snowWalker && !snowWalker.acceptsDamage(event.getSource())) {
            event.setNewDamage(0f);
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity victim = event.getEntity();
        if (!(victim.level() instanceof ServerLevel level)) {
            return;
        }
        if (!(event.getSource().getEntity() instanceof SnowWalker)) {
            return;
        }
        if (!(victim instanceof Player) && !(victim instanceof Villager)) {
            return;
        }

        SnowWalker newWalker = ModEntityTypes.SNOW_WALKER.get().create(level, EntitySpawnReason.TRIGGERED);
        if (newWalker == null) {
            return;
        }
        newWalker.setPos(victim.getX(), victim.getY(), victim.getZ());
        newWalker.finalizeSpawn(level, level.getCurrentDifficultyAt(newWalker.blockPosition()), EntitySpawnReason.TRIGGERED, null);
        level.addFreshEntity(newWalker);
    }
}
