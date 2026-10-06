package fr.akkun.newmeriacore.block.custom;

import fr.akkun.newmeriacore.item.ModItems;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.BeetrootBlock;

/** 4 growth stages (0-3), see {@link ChiliCropBlock} for why this builds on BeetrootBlock. Planted
 *  with the Onion itself, like a vanilla carrot or potato. Drops are in the block's loot table. */
public class OnionCropBlock extends BeetrootBlock {
    public OnionCropBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.ONION.get();
    }
}
