package fr.akkun.newmerias2core.client.entity;

import fr.akkun.newmerias2core.NewmeriaS2Core;
import fr.akkun.newmerias2core.entity.ModEntityTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = NewmeriaS2Core.MOD_ID, value = Dist.CLIENT)
public class HellZombieClientEvents {
    @SubscribeEvent
    static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.HELL_ZOMBIE.get(), HellZombieRenderer::new);
    }
}
