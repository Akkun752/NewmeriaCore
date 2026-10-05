package fr.akkun.newmeriacore.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

/**
 * A sword-tier tool (same damage formula, half the durability) that can also instantly finish
 * cooking whatever food is on a lit campfire, at the cost of 1 durability point - same result the
 * campfire would have produced on its own once its timer ran out, just immediate.
 */
public class SpatulaItem extends Item {
    private final boolean ignitesOnHit;

    public SpatulaItem(Properties properties) {
        this(properties, false);
    }

    /** @param ignitesOnHit whether this Spatula also sets whatever it hits on fire for 12 seconds (Hell tier). */
    public SpatulaItem(Properties properties, boolean ignitesOnHit) {
        super(properties);
        this.ignitesOnHit = ignitesOnHit;
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        super.hurtEnemy(stack, target, attacker);
        if (ignitesOnHit) {
            target.igniteForSeconds(12.0F);
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        Player player = context.getPlayer();
        if (!(level instanceof ServerLevel serverLevel) || player == null || !CampfireBlock.isLitCampfire(state)) {
            return super.useOn(context);
        }
        if (!(level.getBlockEntity(pos) instanceof CampfireBlockEntity campfire)) {
            return super.useOn(context);
        }

        NonNullList<ItemStack> items = campfire.getItems();
        for (int slot = 0; slot < items.size(); slot++) {
            ItemStack cooking = items.get(slot);
            if (cooking.isEmpty()) {
                continue;
            }
            SingleRecipeInput input = new SingleRecipeInput(cooking);
            Optional<RecipeHolder<CampfireCookingRecipe>> recipe =
                    serverLevel.recipeAccess().getRecipeFor(RecipeType.CAMPFIRE_COOKING, input, serverLevel);
            if (recipe.isEmpty()) {
                continue;
            }
            ItemStack result = recipe.get().value().assemble(input);
            if (result.isEmpty()) {
                continue;
            }

            items.set(slot, ItemStack.EMPTY);
            campfire.setChanged();
            level.sendBlockUpdated(pos, state, state, 3);
            Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5, result.copy());
            context.getItemInHand().hurtAndBreak(1, player, context.getHand());
            return InteractionResult.SUCCESS_SERVER;
        }
        return super.useOn(context);
    }
}
