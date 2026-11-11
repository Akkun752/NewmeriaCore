package fr.akkun.newmeriacore.client.entity;

import fr.akkun.newmeriacore.NewmeriaCore;
import fr.akkun.newmeriacore.entity.ModEntityTypes;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

/**
 * Every entity type needs a renderer: Saphira's is the vanilla Ender Dragon one until NewmeriaBosses,
 * loaded after NewmeriaCore, registers her own in its place (see {@link ModEntityTypes#SAPHIRA}).
 */
@EventBusSubscriber(modid = NewmeriaCore.MOD_ID, value = Dist.CLIENT)
public class SaphiraFallbackClientEvents {
    @SubscribeEvent
    static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.SAPHIRA.get(), EnderDragonRenderer::new);
    }
}
