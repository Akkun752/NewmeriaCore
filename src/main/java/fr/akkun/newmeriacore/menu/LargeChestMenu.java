package fr.akkun.newmeriacore.menu;

import fr.akkun.newmeriacore.block.custom.ChestTier;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

// Vanilla's ChestMenu, 12 columns wide instead of 9. The player inventory stays 9 wide, centred below.
public class LargeChestMenu extends AbstractContainerMenu {
    public static final int CHEST_LEFT = 8;
    public static final int CHEST_TOP = 18;
    // How far the 9-column player inventory is shifted right to sit centred under the 12 columns.
    public static final int INVENTORY_OFFSET = (ChestTier.COLUMNS - 9) * 18 / 2;

    private final Container container;
    private final ChestTier tier;

    // Client side: the slots are filled in by the server.
    public LargeChestMenu(ChestTier tier, int containerId, Inventory inventory) {
        this(tier, containerId, inventory, new SimpleContainer(tier.slotCount()));
    }

    public LargeChestMenu(ChestTier tier, int containerId, Inventory inventory, Container container) {
        super(tier.menuType(), containerId);
        checkContainerSize(container, tier.slotCount());
        this.container = container;
        this.tier = tier;
        container.startOpen(inventory.player);
        for (int y = 0; y < tier.rows(); y++) {
            for (int x = 0; x < ChestTier.COLUMNS; x++) {
                this.addSlot(new Slot(container, x + y * ChestTier.COLUMNS, CHEST_LEFT + x * 18, CHEST_TOP + y * 18));
            }
        }
        this.addStandardInventorySlots(inventory, CHEST_LEFT + INVENTORY_OFFSET, CHEST_TOP + tier.rows() * 18 + 13);
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack clicked = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            clicked = stack.copy();
            int chestSlots = this.tier.slotCount();
            if (slotIndex < chestSlots) {
                if (!this.moveItemStackTo(stack, chestSlots, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stack, 0, chestSlots, false)) {
                return ItemStack.EMPTY;
            }

            if (stack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return clicked;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }

    public Container getContainer() {
        return this.container;
    }

    public ChestTier getTier() {
        return this.tier;
    }
}
