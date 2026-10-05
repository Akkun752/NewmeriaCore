package fr.akkun.newmeriacore.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import fr.akkun.newmeriacore.client.rpg.CompanionTextures;
import net.minecraft.client.renderer.entity.HorseRenderer;
import net.minecraft.client.renderer.entity.state.HorseRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.animal.equine.Horse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HorseRenderer.class)
public abstract class HorseRendererMixin {
    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void newmeriacore$captureCompanionTexture(Horse entity, HorseRenderState state, float partialTicks, CallbackInfo ci) {
        CompanionTextures.capture(entity, state);
    }

    @ModifyReturnValue(method = "getTextureLocation", at = @At("RETURN"))
    private Identifier newmeriacore$companionTexture(Identifier original, HorseRenderState state) {
        return CompanionTextures.resolve(state, original);
    }
}
