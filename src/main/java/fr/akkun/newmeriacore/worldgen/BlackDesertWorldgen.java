package fr.akkun.newmeriacore.worldgen;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import fr.akkun.newmeriacore.NewmeriaCore;
import fr.akkun.newmeriacore.block.ModBlocks;
import fr.akkun.newmeriacore.mixin.BiomeSourceAccessor;
import fr.akkun.newmeriacore.mixin.MultiNoiseBiomeSourceParameterListAccessor;
import fr.akkun.newmeriacore.mixin.NoiseGeneratorSettingsAccessor;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterLists;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Makes the Black Desert biome (plain datapack JSON, a copy of the vanilla Desert) actually show up
 * in the Overworld. Vanilla has no data-driven hook for either half of that - the Overworld biome
 * layout and its surface rules are both baked from code - so both are patched in memory, on the
 * freshly loaded registries, right before each server (integrated or dedicated) starts:
 *
 * <ul>
 *   <li><b>Placement</b>: in the vanilla Overworld biome layout, every Desert slot in the driest of
 *       the five humidity bands becomes Black Desert. Black Deserts therefore generate exactly where
 *       (part of) a Desert would have, usually bordering a regular one.</li>
 *   <li><b>Surface</b>: a rule mirroring what vanilla does for the Desert, with Black Sand / Black
 *       Sandstone, is put in front of the vanilla Overworld surface rules.</li>
 * </ul>
 *
 * Nothing is written to the world save: chunks generated while the mod is absent simply get the
 * vanilla Desert there instead.
 */
@EventBusSubscriber(modid = NewmeriaCore.MOD_ID)
public class BlackDesertWorldgen {
    /** First entry of {@code OverworldBiomeBuilder#humidities}. */
    private static final Climate.Parameter DRIEST_HUMIDITY = Climate.Parameter.span(-1.0F, -0.35F);

    private static final List<ResourceKey<NoiseGeneratorSettings>> OVERWORLD_NOISE_SETTINGS = List.of(
            NoiseGeneratorSettings.OVERWORLD, NoiseGeneratorSettings.LARGE_BIOMES, NoiseGeneratorSettings.AMPLIFIED);

    // Guards against stacking the surface rule twice on the same settings object, should the same
    // registries ever be reused by a second server start.
    private static final Set<NoiseGeneratorSettings> PATCHED_SETTINGS = Collections.newSetFromMap(new IdentityHashMap<>());

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

        Registry<NoiseGeneratorSettings> noiseSettings = registries.lookupOrThrow(Registries.NOISE_SETTINGS);
        SurfaceRules.RuleSource blackDesertRule = surfaceRule(biomes);
        for (ResourceKey<NoiseGeneratorSettings> key : OVERWORLD_NOISE_SETTINGS) {
            noiseSettings.get(key).ifPresent(settings -> addSurfaceRule(settings.value(), blackDesertRule));
        }
    }

    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event) {
        PATCHED_SETTINGS.clear();
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

    private static void addSurfaceRule(NoiseGeneratorSettings settings, SurfaceRules.RuleSource blackDesertRule) {
        if (PATCHED_SETTINGS.add(settings)) {
            ((NoiseGeneratorSettingsAccessor) (Object) settings).newmeriacore$setSurfaceRule(
                    SurfaceRules.sequence(blackDesertRule, settings.surfaceRule()));
        }
    }

    /**
     * Same layering vanilla's {@code SurfaceRuleData#overworldLike} ends up applying to the Desert:
     * sand on and just under the surface (sandstone where that surface is a ceiling, so it can't
     * fall), then sandstone much deeper down. Anything this doesn't cover (e.g. the gravel floor
     * under deep water) falls through to the vanilla rules.
     */
    private static SurfaceRules.RuleSource surfaceRule(Registry<Biome> biomes) {
        SurfaceRules.RuleSource blackSandstone = SurfaceRules.state(ModBlocks.BLACK_SANDSTONE.get().defaultBlockState());
        SurfaceRules.RuleSource blackSand = SurfaceRules.state(ModBlocks.BLACK_SAND.get().defaultBlockState());
        SurfaceRules.RuleSource sandOrSandstoneIfCeiling = SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, blackSandstone), blackSand);
        SurfaceRules.ConditionSource notUnderwater = SurfaceRules.waterBlockCheck(-1, 0);
        SurfaceRules.ConditionSource notUnderDeepWater = SurfaceRules.waterStartCheck(-6, -1);

        return SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, ModBiomes.BLACK_DESERT),
                SurfaceRules.ifTrue(SurfaceRules.abovePreliminarySurface(), SurfaceRules.sequence(
                        SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.ifTrue(notUnderwater, sandOrSandstoneIfCeiling)),
                        SurfaceRules.ifTrue(notUnderDeepWater, SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, sandOrSandstoneIfCeiling),
                                SurfaceRules.ifTrue(SurfaceRules.VERY_DEEP_UNDER_FLOOR, blackSandstone))))));
    }
}
