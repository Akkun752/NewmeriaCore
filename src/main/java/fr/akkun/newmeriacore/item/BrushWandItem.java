package fr.akkun.newmeriacore.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BrushItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BrushableBlock;

/**
 * Both a fully working vanilla Brush and a Wand. Brushing always takes priority: brushing a
 * suspicious block or an Armadillo (the latter goes through {@code canPerformAction}, inherited
 * unchanged from {@link BrushItem}) behaves exactly like the vanilla Brush. Only when there is
 * nothing to brush does the right click fall back to casting the selected spell.
 */
public class BrushWandItem extends BrushItem {
    public BrushWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getLevel().getBlockState(context.getClickedPos()).getBlock() instanceof BrushableBlock) {
            return super.useOn(context);
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        return WandItem.castSpell(level, player, hand);
    }
}
