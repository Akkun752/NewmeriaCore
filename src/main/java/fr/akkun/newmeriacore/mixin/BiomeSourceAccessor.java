package fr.akkun.newmeriacore.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

/** Lets {@code BlackDesertWorldgen} drop a biome source's memoized "possible biomes" set, which the
 *  client already computes (from the unpatched vanilla layout) while opening or creating a world. */
@Mixin(BiomeSource.class)
public interface BiomeSourceAccessor {
    @Mutable
    @Accessor("possibleBiomes")
    void newmeriacore$setPossibleBiomes(Supplier<Set<Holder<Biome>>> possibleBiomes);

    @Invoker("collectPossibleBiomes")
    Stream<Holder<Biome>> newmeriacore$collectPossibleBiomes();
}
