package fr.akkun.newmeriacore.client.entity;

import fr.akkun.newmeriacore.NewmeriaCore;
import fr.akkun.newmeriacore.entity.ModEntityTypes;
import fr.akkun.newmeriacore.entity.Saphira;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = NewmeriaCore.MOD_ID, value = Dist.CLIENT)
public class SaphiraClientEvents {
    // Vanilla's neutral (white) boss bar sprites, tinted: its own "blue" bar is closer to cyan.
    private static final Identifier BAR_BACKGROUND = Identifier.withDefaultNamespace("boss_bar/white_background");
    private static final Identifier BAR_PROGRESS = Identifier.withDefaultNamespace("boss_bar/white_progress");
    /** Opaque, slightly dark blue. */
    private static final int BAR_COLOR = 0xFF2040C0;
    // Same layout as vanilla's BossHealthOverlay.
    private static final int BAR_WIDTH = 182;
    private static final int BAR_HEIGHT = 5;

    @SubscribeEvent
    static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.SAPHIRA.get(), SaphiraRenderer::new);
    }

    /** Draws Saphira's boss bar ourselves, in place of the vanilla one. Her bar is recognised by its
     *  id, which is her own entity UUID - she is always loaded client-side while her bar is shown. */
    @SubscribeEvent
    static void onBossBar(CustomizeGuiOverlayEvent.BossEventProgress event) {
        Minecraft minecraft = Minecraft.getInstance();
        LerpingBossEvent bossEvent = event.getBossEvent();
        if (minecraft.level == null || !(minecraft.level.getEntity(bossEvent.getId()) instanceof Saphira)) {
            return;
        }
        event.setCanceled(true);

        GuiGraphicsExtractor graphics = event.getGuiGraphics();
        int x = event.getX();
        int y = event.getY();
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BAR_BACKGROUND, BAR_WIDTH, BAR_HEIGHT, 0, 0, x, y, BAR_WIDTH, BAR_HEIGHT, BAR_COLOR);
        int progressWidth = Mth.lerpDiscrete(bossEvent.getProgress(), 0, BAR_WIDTH);
        if (progressWidth > 0) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BAR_PROGRESS, BAR_WIDTH, BAR_HEIGHT, 0, 0, x, y, progressWidth, BAR_HEIGHT, BAR_COLOR);
        }
        Component name = bossEvent.getName();
        graphics.text(minecraft.font, name, graphics.guiWidth() / 2 - minecraft.font.width(name) / 2, y - 9, -1);
    }
}
