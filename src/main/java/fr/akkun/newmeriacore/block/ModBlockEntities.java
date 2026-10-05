package fr.akkun.newmeriacore.block;

import fr.akkun.newmeriacore.NewmeriaCore;
import fr.akkun.newmeriacore.block.custom.OilCauldronBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, NewmeriaCore.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<OilCauldronBlockEntity>> OIL_CAULDRON =
            BLOCK_ENTITIES.register("oil_cauldron",
                    () -> new BlockEntityType<>(OilCauldronBlockEntity::new, Set.of(ModBlocks.OIL_CAULDRON.get())));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
