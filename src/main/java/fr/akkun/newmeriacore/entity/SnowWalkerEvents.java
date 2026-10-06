package fr.akkun.newmeriacore.entity;

import fr.akkun.newmeriacore.NewmeriaCore;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.Weighted;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

@EventBusSubscriber(modid = NewmeriaCore.MOD_ID)
public class SnowWalkerEvents {
    // As heavy as in the snowy biomes they used to be limited to: 1.5x a zombie, in packs of 4.
    private static final int NATURAL_SPAWN_WEIGHT = 143;
    private static final int NATURAL_SPAWN_PACK_SIZE = 4;

    /**
     * Snow Walkers are not on any biome spawn list: they are offered as a possible monster spawn at
     * every position that is on snow or ice, in any biome. Doing it per position rather than adding
     * them to every biome keeps them from eating into the other monsters wherever there is no snow.
     */
    @SubscribeEvent
    public static void onPotentialSpawns(LevelEvent.PotentialSpawns event) {
        if (event.getMobCategory() == MobCategory.MONSTER && SnowWalker.isOnSnowOrIce(event.getLevel(), event.getPos())) {
            event.addSpawnerData(new Weighted<>(new MobSpawnSettings.SpawnerData(
                    ModEntityTypes.SNOW_WALKER.get(), NATURAL_SPAWN_PACK_SIZE, NATURAL_SPAWN_PACK_SIZE), NATURAL_SPAWN_WEIGHT));
        }
    }

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
