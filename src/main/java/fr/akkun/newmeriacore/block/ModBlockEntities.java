package fr.akkun.newmeriacore.block;

import fr.akkun.newmeriacore.NewmeriaCore;
import fr.akkun.newmeriacore.block.custom.ChestTier;
import fr.akkun.newmeriacore.block.custom.IronChestBlockEntity;
import fr.akkun.newmeriacore.block.custom.LargeChestBlockEntity;
import fr.akkun.newmeriacore.block.custom.OilCauldronBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;

import java.util.Set;

@EventBusSubscriber(modid = NewmeriaCore.MOD_ID)
public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, NewmeriaCore.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<OilCauldronBlockEntity>> OIL_CAULDRON =
            BLOCK_ENTITIES.register("oil_cauldron",
                    () -> new BlockEntityType<>(OilCauldronBlockEntity::new, Set.of(ModBlocks.OIL_CAULDRON.get())));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<IronChestBlockEntity>> IRON_CHEST =
            BLOCK_ENTITIES.register("iron_chest",
                    () -> new BlockEntityType<>(IronChestBlockEntity::new, Set.of(ModBlocks.IRON_CHEST.get())));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LargeChestBlockEntity>> GOLDEN_CHEST =
            BLOCK_ENTITIES.register("golden_chest",
                    () -> new BlockEntityType<>((pos, state) -> new LargeChestBlockEntity(ChestTier.GOLDEN, pos, state), Set.of(ModBlocks.GOLDEN_CHEST.get())));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LargeChestBlockEntity>> DIAMOND_CHEST =
            BLOCK_ENTITIES.register("diamond_chest",
                    () -> new BlockEntityType<>((pos, state) -> new LargeChestBlockEntity(ChestTier.DIAMOND, pos, state), Set.of(ModBlocks.DIAMOND_CHEST.get())));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

    // Lets hoppers and other mods' pipes fill and empty the chests, as NeoForge does for vanilla containers.
    @SubscribeEvent
    static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.Item.BLOCK, IRON_CHEST.get(), (container, _) -> VanillaContainerWrapper.of(container));
        event.registerBlockEntity(Capabilities.Item.BLOCK, GOLDEN_CHEST.get(), (container, _) -> VanillaContainerWrapper.of(container));
        event.registerBlockEntity(Capabilities.Item.BLOCK, DIAMOND_CHEST.get(), (container, _) -> VanillaContainerWrapper.of(container));
    }
}
