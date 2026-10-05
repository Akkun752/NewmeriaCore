package fr.akkun.newmeriacore.block.custom;

import fr.akkun.newmeriacore.item.ModItems;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.BeetrootBlock;

/**
 * 4 growth stages (0-3, same as beetroot) instead of wheat's 8. BeetrootBlock's randomTick
 * already only attempts growth 2/3 of the time, i.e. a built-in 1.5x growth-duration multiplier -
 * needed here too since 4 stages would otherwise grow much faster than wheat's 8.
 */
public class ChiliCropBlock extends BeetrootBlock {
    public ChiliCropBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.CHILI_SEEDS.get();
    }
}
