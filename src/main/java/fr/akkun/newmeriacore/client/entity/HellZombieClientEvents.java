package fr.akkun.newmeriacore.client.entity;

import fr.akkun.newmeriacore.NewmeriaCore;
import fr.akkun.newmeriacore.entity.ModEntityTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = NewmeriaCore.MOD_ID, value = Dist.CLIENT)
public class HellZombieClientEvents {
    @SubscribeEvent
    static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.HELL_ZOMBIE.get(), HellZombieRenderer::new);
    }
}
