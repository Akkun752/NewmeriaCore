package fr.akkun.newmeriacore.menu;

import fr.akkun.newmeriacore.NewmeriaCore;
import fr.akkun.newmeriacore.block.custom.ChestTier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(Registries.MENU, NewmeriaCore.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<LargeChestMenu>> GOLDEN_CHEST = register("golden_chest", ChestTier.GOLDEN);
    public static final DeferredHolder<MenuType<?>, MenuType<LargeChestMenu>> DIAMOND_CHEST = register("diamond_chest", ChestTier.DIAMOND);

    private static DeferredHolder<MenuType<?>, MenuType<LargeChestMenu>> register(String name, ChestTier tier) {
        return MENU_TYPES.register(name, () -> new MenuType<>(
                (containerId, inventory) -> new LargeChestMenu(tier, containerId, inventory), FeatureFlags.DEFAULT_FLAGS));
    }

    public static void register(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }
}
