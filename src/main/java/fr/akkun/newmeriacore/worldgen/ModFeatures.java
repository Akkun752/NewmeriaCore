package fr.akkun.newmeriacore.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.akkun.newmeriacore.NewmeriaCore;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, NewmeriaCore.MOD_ID);

    /**
     * Highest size {@link #LARGE_ORE} accepts. A vein reaches up to {@code ceil(size / 8) +
     * ceil((size / 8 + 1) / 2)} blocks away from its origin, and a feature may only write up to 16
     * blocks outside its own chunk - with the usual {@code in_square} placement (origin anywhere in
     * the chunk) that caps the reach at 16 blocks, i.e. this size.
     */
    public static final int MAX_LARGE_ORE_SIZE = 80;

    // Vanilla's own OreConfiguration codec, minus its hard cap of 64 on the vein size.
    private static final Codec<OreConfiguration> LARGE_ORE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.list(OreConfiguration.TargetBlockState.CODEC).fieldOf("targets").forGetter(config -> config.targetStates),
            Codec.intRange(0, MAX_LARGE_ORE_SIZE).fieldOf("size").forGetter(config -> config.size),
            Codec.floatRange(0.0F, 1.0F).fieldOf("discard_chance_on_air_exposure").forGetter(config -> config.discardChanceOnAirExposure)
    ).apply(instance, OreConfiguration::new));

    /** The vanilla ore feature, unchanged, for veins bigger than vanilla's JSON format allows (64 is
     *  both the cap and what granite/diorite/andesite already use). */
    public static final DeferredHolder<Feature<?>, OreFeature> LARGE_ORE = FEATURES.register("large_ore", () -> new OreFeature(LARGE_ORE_CODEC));

    public static void register(IEventBus eventBus) {
        FEATURES.register(eventBus);
    }
}
