package fr.akkun.newmeriacore.block.custom;

import fr.akkun.newmeriacore.block.ModBlockEntities;
import fr.akkun.newmeriacore.menu.LargeChestMenu;
import fr.akkun.newmeriacore.menu.ModMenuTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;

// The chests that are 12 slots wide, too wide for vanilla's 9-column chest menu and screen.
public enum ChestTier {
    GOLDEN("golden_chest", 4),
    DIAMOND("diamond_chest", 5);

    public static final int COLUMNS = 12;

    private final Component defaultName;
    private final int rows;

    ChestTier(String blockName, int rows) {
        this.defaultName = Component.translatable("block.newmeriacore." + blockName);
        this.rows = rows;
    }

    public Component defaultName() {
        return this.defaultName;
    }

    public int rows() {
        return this.rows;
    }

    public int slotCount() {
        return this.rows * COLUMNS;
    }

    public BlockEntityType<LargeChestBlockEntity> blockEntityType() {
        return switch (this) {
            case GOLDEN -> ModBlockEntities.GOLDEN_CHEST.get();
            case DIAMOND -> ModBlockEntities.DIAMOND_CHEST.get();
        };
    }

    public MenuType<LargeChestMenu> menuType() {
        return switch (this) {
            case GOLDEN -> ModMenuTypes.GOLDEN_CHEST.get();
            case DIAMOND -> ModMenuTypes.DIAMOND_CHEST.get();
        };
    }
}
