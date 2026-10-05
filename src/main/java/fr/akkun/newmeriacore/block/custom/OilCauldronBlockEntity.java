package fr.akkun.newmeriacore.block.custom;

import fr.akkun.newmeriacore.block.ModBlockEntities;
import fr.akkun.newmeriacore.frying.FryingAttachments;
import fr.akkun.newmeriacore.frying.FryingRecipes;
import fr.akkun.newmeriacore.frying.OilFryProgress;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

/**
 * Fries raw food items sitting in the oil: each tick, if a heat source is directly below, every
 * {@link ItemEntity} overlapping this block advances its own {@link OilFryProgress} (rolled once,
 * 10-15s / 200-300 ticks, per item). Progress simply isn't advanced on ticks without a heat source
 * below - it pauses rather than resets, and picks back up once heat returns.
 */
public class OilCauldronBlockEntity extends BlockEntity {
    private static final int MIN_FRY_TICKS = 200;
    private static final int MAX_FRY_TICKS = 300;

    public OilCauldronBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.OIL_CAULDRON.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, OilCauldronBlockEntity blockEntity) {
        if (level.isClientSide() || !hasHeatSourceBelow(level, pos)) {
            return;
        }
        for (ItemEntity itemEntity : level.getEntitiesOfClass(ItemEntity.class, new AABB(pos))) {
            tickItem(level, itemEntity);
        }
    }

    private static void tickItem(Level level, ItemEntity itemEntity) {
        ItemStack stack = itemEntity.getItem();
        Item friedResult = FryingRecipes.getFriedResult(stack.getItem());
        if (friedResult == null) {
            return;
        }
        OilFryProgress progress = itemEntity.getData(FryingAttachments.OIL_FRY_PROGRESS);
        int target = progress.targetTicks();
        if (target < 0) {
            target = MIN_FRY_TICKS + level.getRandom().nextInt(MAX_FRY_TICKS - MIN_FRY_TICKS + 1);
        }
        int newProgress = progress.progressTicks() + 1;
        if (newProgress >= target) {
            itemEntity.setItem(new ItemStack(friedResult, stack.getCount()));
            itemEntity.setData(FryingAttachments.OIL_FRY_PROGRESS, OilFryProgress.NONE);
        } else {
            itemEntity.setData(FryingAttachments.OIL_FRY_PROGRESS, new OilFryProgress(newProgress, target));
        }
    }

    private static boolean hasHeatSourceBelow(Level level, BlockPos cauldronPos) {
        BlockPos belowPos = cauldronPos.below();
        BlockState below = level.getBlockState(belowPos);
        if (below.is(Blocks.FIRE) || below.is(Blocks.SOUL_FIRE) || below.is(Blocks.MAGMA_BLOCK)) {
            return true;
        }
        if (CampfireBlock.isLitCampfire(below)) {
            return true;
        }
        return level.getFluidState(belowPos).is(FluidTags.LAVA);
    }
}
