package fr.akkun.newmeriacore.client.block;

import fr.akkun.newmeriacore.NewmeriaCore;
import fr.akkun.newmeriacore.block.ModBlockEntities;
import fr.akkun.newmeriacore.menu.ModMenuTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = NewmeriaCore.MOD_ID, value = Dist.CLIENT)
public class ModChestClientEvents {
    @SubscribeEvent
    static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.IRON_CHEST.get(), context -> new ModChestRenderer<>(context, ModChestRenderer.IRON));
        event.registerBlockEntityRenderer(ModBlockEntities.GOLDEN_CHEST.get(), context -> new ModChestRenderer<>(context, ModChestRenderer.GOLD));
        event.registerBlockEntityRenderer(ModBlockEntities.DIAMOND_CHEST.get(), context -> new ModChestRenderer<>(context, ModChestRenderer.DIAMOND));
    }

    @SubscribeEvent
    static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.GOLDEN_CHEST.get(), LargeChestScreen::new);
        event.register(ModMenuTypes.DIAMOND_CHEST.get(), LargeChestScreen::new);
    }
}
