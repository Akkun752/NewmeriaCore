package fr.akkun.newmeriacore.client.fluid;

import fr.akkun.newmeriacore.NewmeriaCore;
import fr.akkun.newmeriacore.fluid.ModFluids;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(modid = NewmeriaCore.MOD_ID, value = Dist.CLIENT)
public class ModFluidClientEvents {
    @SubscribeEvent
    static void onRegisterFluidModels(RegisterFluidModelsEvent event) {
        Material still = new Material(Identifier.fromNamespaceAndPath(NewmeriaCore.MOD_ID, "block/oil_still"));
        Material flowing = new Material(Identifier.fromNamespaceAndPath(NewmeriaCore.MOD_ID, "block/oil_flow"));
        FluidModel.Unbaked model = new FluidModel.Unbaked(still, flowing, still, BlockTintSources.constant(0xFFFFFF));
        event.register(model, ModFluids.OIL_SOURCE.get(), ModFluids.OIL_FLOWING.get());
    }

    @SubscribeEvent
    static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerFluidType(new IClientFluidTypeExtensions() {
            private final Identifier overlay = Identifier.fromNamespaceAndPath(NewmeriaCore.MOD_ID, "block/oil_still");

            @Override
            public Identifier getRenderOverlayTexture(net.minecraft.client.Minecraft minecraft) {
                return overlay;
            }
        }, ModFluids.OIL_TYPE.get());
    }
}
