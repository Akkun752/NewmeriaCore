package fr.akkun.newmeriacore.client.block;

import fr.akkun.newmeriacore.NewmeriaCore;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

// The vanilla chest renderer (model, lid animation) with one of our textures/entity/chest/*.png.
public class ModChestRenderer<T extends ChestBlockEntity> extends ChestRenderer<T> {
    public static final Identifier IRON = texture("iron");
    public static final Identifier GOLD = texture("gold");
    public static final Identifier DIAMOND = texture("diamond");

    private final SpriteId sprite;

    public ModChestRenderer(BlockEntityRendererProvider.Context context, Identifier texture) {
        super(context);
        this.sprite = Sheets.CHEST_MAPPER.apply(texture);
    }

    private static Identifier texture(String name) {
        return Identifier.fromNamespaceAndPath(NewmeriaCore.MOD_ID, name);
    }

    @Override
    protected SpriteId getCustomSprite(T blockEntity, ChestRenderState renderState) {
        return this.sprite;
    }
}
