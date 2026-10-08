package fr.akkun.newmeriacore.block.custom;

import fr.akkun.newmeriacore.block.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class IronChestBlockEntity extends ChestBlockEntity {
    public static final int ROWS = 4;
    private static final Component DEFAULT_NAME = Component.translatable("block.newmeriacore.iron_chest");

    public IronChestBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.IRON_CHEST.get(), worldPosition, blockState);
        // The vanilla chest starts with a 27-slot list.
        this.setItems(NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY));
    }

    @Override
    public int getContainerSize() {
        return ROWS * 9;
    }

    @Override
    protected Component getDefaultName() {
        return DEFAULT_NAME;
    }

    // Vanilla's own 9x4 menu and screen, which no vanilla block uses.
    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new ChestMenu(MenuType.GENERIC_9x4, containerId, inventory, this, ROWS);
    }
}
