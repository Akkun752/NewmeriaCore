package fr.akkun.newmeriacore.item;

import fr.akkun.newmeriacore.rpg.SpellCasting;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

/**
 * Casts the player's currently selected spell (see {@code MagicWheelScreen}) on right click.
 * Durability is only consumed when a spell is actually cast - not when none is selected and not
 * while the spell is still on cooldown.
 */
public class WandItem extends Item {
    public WandItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        return castSpell(level, player, hand);
    }

    /** Shared by any item that should also cast the selected spell on right click (see BrushWandItem,
     *  SpatulaWandItem) as a fallback once their primary behaviour didn't handle the interaction. */
    static InteractionResult castSpell(Level level, Player player, InteractionHand hand) {
        if (!(level instanceof ServerLevel) || !(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResult.PASS;
        }
        if (SpellCasting.cast(serverPlayer)) {
            player.getItemInHand(hand).hurtAndBreak(1, player, hand);
            return InteractionResult.SUCCESS_SERVER;
        }
        return InteractionResult.PASS;
    }
}
