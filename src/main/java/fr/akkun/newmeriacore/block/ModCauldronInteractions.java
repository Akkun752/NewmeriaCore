package fr.akkun.newmeriacore.block;

import fr.akkun.newmeriacore.NewmeriaCore;
import fr.akkun.newmeriacore.item.ModItems;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.cauldron.CauldronInteractions;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.RegisterCauldronInteractionEvent;

public class ModCauldronInteractions {
    public static final CauldronInteraction.Dispatcher OIL = new CauldronInteraction.Dispatcher();

    private static void onRegisterDispatcher(RegisterCauldronInteractionEvent.Dispatcher event) {
        event.register(Identifier.fromNamespaceAndPath(NewmeriaCore.MOD_ID, "oil"), OIL);
    }

    private static void onRegisterInteraction(RegisterCauldronInteractionEvent.Interaction event) {
        // Empty cauldron + oil bucket -> OIL_CAULDRON, player keeps an empty bucket.
        event.register(Identifier.withDefaultNamespace("empty"), ModItems.OIL_BUCKET.get(),
                (state, level, pos, player, hand, stack) -> CauldronInteractions.emptyBucket(
                        level, pos, player, hand, stack, ModBlocks.OIL_CAULDRON.get().defaultBlockState(), SoundEvents.BUCKET_EMPTY));

        // OIL_CAULDRON + empty bucket -> back to an empty cauldron, player gets an oil bucket.
        event.register(Identifier.fromNamespaceAndPath(NewmeriaCore.MOD_ID, "oil"), Items.BUCKET,
                (state, level, pos, player, hand, stack) -> CauldronInteractions.fillBucket(
                        state, level, pos, player, hand, stack, new ItemStack(ModItems.OIL_BUCKET.get()), s -> true, SoundEvents.BUCKET_FILL));
    }

    public static void register(IEventBus eventBus) {
        eventBus.addListener(ModCauldronInteractions::onRegisterDispatcher);
        eventBus.addListener(ModCauldronInteractions::onRegisterInteraction);
    }
}
