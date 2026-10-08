package fr.akkun.newmeriacore.worldgen;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import fr.akkun.newmeriacore.NewmeriaCore;
import fr.akkun.newmeriacore.mixin.BiomeSourceAccessor;
import fr.akkun.newmeriacore.mixin.MultiNoiseBiomeSourceParameterListAccessor;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterLists;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Makes the Black Desert biome (plain datapack JSON, a copy of the vanilla Desert) actually show up
 * in the Overworld. Its ground is pure data since 26.3 (see the {@code black_desert_surface}
 * material rule, plugged into our copy of {@code minecraft:overworld}), but vanilla still has no
 * data-driven hook for <i>where</i> biomes go: the Overworld biome layout is baked from code. It is
 * patched in memory, on the freshly loaded registries, right before each server (integrated or
 * dedicated) starts: every Desert slot in the driest of the five humidity bands becomes Black
 * Desert. Black Deserts therefore generate exactly where (part of) a Desert would have, usually
 * bordering a regular one.
 *
 * <p>Nothing is written to the world save: chunks generated while the mod is absent simply get the
 * vanilla Desert there instead.
 */
@EventBusSubscriber(modid = NewmeriaCore.MOD_ID)
public class BlackDesertWorldgen {
    /** First entry of {@code OverworldBiomeBuilder#humidities}. */
    private static final Climate.Parameter DRIEST_HUMIDITY = Climate.Parameter.span(-1.0F, -0.35F);

    @SubscribeEvent
    public static void onServerAboutToStart(ServerAboutToStartEvent event) {
        RegistryAccess registries = event.getServer().registryAccess();
        Registry<Biome> biomes = registries.lookupOrThrow(Registries.BIOME);
        Optional<Holder.Reference<Biome>> blackDesert = biomes.get(ModBiomes.BLACK_DESERT);
        if (blackDesert.isEmpty()) {
            return;
        }

        registries.lookupOrThrow(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST)
                .get(MultiNoiseBiomeSourceParameterLists.OVERWORLD)
                .ifPresent(list -> placeInOverworld(list.value(), blackDesert.get()));
        registries.lookupOrThrow(Registries.LEVEL_STEM).forEach(stem -> refreshPossibleBiomes(stem.generator()));
    }

    private static void placeInOverworld(MultiNoiseBiomeSourceParameterList list, Holder<Biome> blackDesert) {
        List<Pair<Climate.ParameterPoint, Holder<Biome>>> values = new ArrayList<>(list.parameters().values().size());
        int replaced = 0;
        for (Pair<Climate.ParameterPoint, Holder<Biome>> entry : list.parameters().values()) {
            if (entry.getSecond().is(Biomes.DESERT) && entry.getFirst().humidity().equals(DRIEST_HUMIDITY)) {
                values.add(Pair.of(entry.getFirst(), blackDesert));
                replaced++;
            } else {
                values.add(entry);
            }
        }
        if (replaced > 0) {
            ((MultiNoiseBiomeSourceParameterListAccessor) list).newmeriacore$setParameters(new Climate.ParameterList<>(values));
        }
        NewmeriaCore.LOGGER.info("Black Desert: took over {} of the Overworld's Desert biome slots", replaced);
    }

    /**
     * Every biome source memoizes the set of biomes it can produce, and the client already forces
     * that computation (through {@code ChunkGenerator#validate}) while opening or creating a world,
     * i.e. before this patch runs. Left stale, the Black Desert would be missing from it: no
     * decoration in the biome, and {@code /locate biome} giving up immediately.
     */
    private static void refreshPossibleBiomes(ChunkGenerator generator) {
        BiomeSourceAccessor biomeSource = (BiomeSourceAccessor) generator.getBiomeSource();
        biomeSource.newmeriacore$setPossibleBiomes(Suppliers.memoize(
                () -> biomeSource.newmeriacore$collectPossibleBiomes().distinct().collect(ImmutableSet.toImmutableSet())));
        generator.refreshFeaturesPerStep();
    }
}
