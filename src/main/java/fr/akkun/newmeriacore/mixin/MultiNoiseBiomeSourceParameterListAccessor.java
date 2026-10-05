package fr.akkun.newmeriacore.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Lets {@code BlackDesertWorldgen} hand part of the vanilla Overworld biome layout to the Black Desert. */
@Mixin(MultiNoiseBiomeSourceParameterList.class)
public interface MultiNoiseBiomeSourceParameterListAccessor {
    @Mutable
    @Accessor("parameters")
    void newmeriacore$setParameters(Climate.ParameterList<Holder<Biome>> parameters);
}
