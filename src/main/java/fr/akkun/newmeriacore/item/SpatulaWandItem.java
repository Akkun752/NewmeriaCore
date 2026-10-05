package fr.akkun.newmeriacore.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * Both a fully working Spatula and a Wand. Finishing a lit campfire's cooking always takes
 * priority (inherited unchanged from {@link SpatulaItem#useOn}). Only when there is nothing to
 * finish-cook does the right click fall back to casting the selected spell.
 */
public class SpatulaWandItem extends SpatulaItem {
    public SpatulaWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        // SpatulaItem#useOn always reports PASS on the client (it can only resolve the campfire
        // recipe against a ServerLevel), so the client-side interaction loop falls through and
        // fires use() right alongside it even when useOn is about to finish-cook server-side.
        // Without this check that meant every successful campfire-finish also cast a spell.
        // Re-checking the same "looking at a lit campfire" condition here keeps the two paths
        // from double-firing.
        if (isLookingAtLitCampfire(level, player)) {
            return InteractionResult.PASS;
        }
        return WandItem.castSpell(level, player, hand);
    }

    private static boolean isLookingAtLitCampfire(Level level, Player player) {
        HitResult hit = player.pick(player.blockInteractionRange(), 1.0F, false);
        return hit instanceof BlockHitResult blockHit
                && CampfireBlock.isLitCampfire(level.getBlockState(blockHit.getBlockPos()));
    }
}
