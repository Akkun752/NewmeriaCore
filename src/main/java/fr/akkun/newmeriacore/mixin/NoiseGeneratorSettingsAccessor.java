package fr.akkun.newmeriacore.mixin;

import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Lets {@code BlackDesertWorldgen} prepend the Black Desert surface rule to the vanilla one. */
@Mixin(NoiseGeneratorSettings.class)
public interface NoiseGeneratorSettingsAccessor {
    @Mutable
    @Accessor("surfaceRule")
    void newmeriacore$setSurfaceRule(SurfaceRules.RuleSource surfaceRule);
}
