package fr.akkun.newmeriacore.client.block;

import fr.akkun.newmeriacore.block.custom.ChestTier;
import fr.akkun.newmeriacore.menu.LargeChestMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

// The screen of the 12-column chests. It has no texture of its own: the background is assembled
// from pieces of vanilla's double chest texture, so it follows resource packs.
public class LargeChestScreen extends AbstractContainerScreen<LargeChestMenu> {
    private static final Identifier CONTAINER_BACKGROUND = Identifier.withDefaultNamespace("textures/gui/container/generic_54.png");
    // Layout of the vanilla texture: a 176 wide panel with a 7 pixel border, a 17 pixel header above
    // the chest rows, and at v = 126 the 96 pixel tall player inventory half.
    private static final int VANILLA_WIDTH = 176;
    private static final int BORDER = 7;
    private static final int HEADER_HEIGHT = 17;
    private static final int INVENTORY_V = 126;
    private static final int INVENTORY_HEIGHT = 96;
    private static final int EXTRA_WIDTH = (ChestTier.COLUMNS - 9) * 18;

    private final int rows;

    public LargeChestScreen(LargeChestMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, VANILLA_WIDTH + EXTRA_WIDTH, 114 + menu.getTier().rows() * 18);
        this.rows = menu.getTier().rows();
        this.inventoryLabelX += LargeChestMenu.INVENTORY_OFFSET;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        int xo = (this.width - this.imageWidth) / 2;
        int yo = (this.height - this.imageHeight) / 2;
        int chestHeight = HEADER_HEIGHT + this.rows * 18;
        int inventoryY = yo + chestHeight;

        // Chest half: the vanilla panel without its right border, then its last columns and right
        // border again to make up the extra width.
        int leftWidth = VANILLA_WIDTH - BORDER;
        blit(graphics, xo, yo, 0, 0, leftWidth, chestHeight);
        blit(graphics, xo + leftWidth, yo, VANILLA_WIDTH - BORDER - EXTRA_WIDTH, 0, EXTRA_WIDTH + BORDER, chestHeight);

        // Player inventory half, centred.
        blit(graphics, xo + LargeChestMenu.INVENTORY_OFFSET, inventoryY, 0, INVENTORY_V, VANILLA_WIDTH, INVENTORY_HEIGHT);

        // Bottom edge of the chest half where it sticks out on each side of the inventory half.
        int bottomV = INVENTORY_V + INVENTORY_HEIGHT - BORDER;
        blit(graphics, xo, inventoryY, 0, bottomV, LargeChestMenu.INVENTORY_OFFSET, BORDER);
        blit(graphics, xo + this.imageWidth - LargeChestMenu.INVENTORY_OFFSET, inventoryY,
                VANILLA_WIDTH - LargeChestMenu.INVENTORY_OFFSET, bottomV, LargeChestMenu.INVENTORY_OFFSET, BORDER);
    }

    private static void blit(GuiGraphicsExtractor graphics, int x, int y, int u, int v, int width, int height) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, CONTAINER_BACKGROUND, x, y, u, v, width, height, 256, 256);
    }
}
