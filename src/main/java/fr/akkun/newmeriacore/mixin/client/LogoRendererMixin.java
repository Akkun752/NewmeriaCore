package fr.akkun.newmeriacore.mixin.client;

import fr.akkun.newmeriacore.NewmeriaCore;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The Newmeria logo in place of the whole vanilla one - "Minecraft" (and its rare "Minceraft" easter egg)
 * and "Java Edition" below it - on the title screen and everywhere else the game draws its logo.
 *
 * <p>Any resolution works: the image is shown {@link #newmeriacore$WIDTH} wide, as wide as the vanilla
 * logo, and as high as its proportions make it. If it is replaced by an image of another size, update
 * {@link #newmeriacore$TEXTURE_WIDTH} and {@link #newmeriacore$TEXTURE_HEIGHT}.
 */
@Mixin(LogoRenderer.class)
public abstract class LogoRendererMixin {
    @Unique
    private static final Identifier newmeriacore$LOGO =
            Identifier.fromNamespaceAndPath(NewmeriaCore.MOD_ID, "textures/title/newmeria.png");
    @Unique
    private static final int newmeriacore$TEXTURE_WIDTH = 1024;
    @Unique
    private static final int newmeriacore$TEXTURE_HEIGHT = 184;
    @Unique
    private static final int newmeriacore$WIDTH = 256;
    @Unique
    private static final int newmeriacore$HEIGHT = Math.round((float) newmeriacore$WIDTH * newmeriacore$TEXTURE_HEIGHT / newmeriacore$TEXTURE_WIDTH);

    @Shadow
    @Final
    private boolean keepLogoThroughFade;

    @Inject(method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IFI)V", at = @At("HEAD"), cancellable = true)
    private void newmeriacore$drawNewmeriaLogo(GuiGraphicsExtractor graphics, int width, float alpha, int heightOffset, CallbackInfo ci) {
        // Same fade-in as the vanilla logo.
        int color = ARGB.white(this.keepLogoThroughFade ? 1.0F : alpha);
        int x = width / 2 - newmeriacore$WIDTH / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, newmeriacore$LOGO, x, heightOffset, 0.0F, 0.0F,
                newmeriacore$WIDTH, newmeriacore$HEIGHT, newmeriacore$WIDTH, newmeriacore$HEIGHT, color);
        ci.cancel();
    }
}
