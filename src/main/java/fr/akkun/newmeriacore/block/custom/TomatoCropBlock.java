package fr.akkun.newmeriacore.block.custom;

import fr.akkun.newmeriacore.item.ModItems;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;

/**
 * A regular 8-age crop (0-7) planted with Tomato Seeds. Only its looks are unusual: ages 0-3 show
 * the plant itself growing ("budding" textures) and ages 4-7 the tomatoes growing on it - see
 * {@link #FRUIT_TEXTURES_START_AGE}. Drops are in the block's loot table.
 */
public class TomatoCropBlock extends CropBlock {
    /** First age using the tomatoes_stageN textures instead of the budding_tomatoes_stageN ones. */
    public static final int FRUIT_TEXTURES_START_AGE = 4;

    public TomatoCropBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.TOMATO_SEEDS.get();
    }
}
