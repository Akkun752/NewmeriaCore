package fr.akkun.newmeriacore.item;

import fr.akkun.newmeriacore.NewmeriaCore;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;

/**
 * Paladium-style hammer: breaking a pickaxe-mineable block with one also breaks the 3x3 area of
 * pickaxe-mineable blocks around it, in the plane facing the player (worked out from the direction
 * from the player's eyes to the block, since {@link BreakBlockEvent} doesn't carry the click face).
 * Each extra block costs 1 durability, with proper tool-aware drops (fortune, silk touch, etc).
 */
@EventBusSubscriber(modid = NewmeriaCore.MOD_ID)
public class HammerEvents {
    @SubscribeEvent
    public static void onBreakBlock(BreakBlockEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level) || !(event.getPlayer() instanceof ServerPlayer player)) {
            return;
        }
        ItemStack tool = player.getMainHandItem();
        if (!(tool.getItem() instanceof HammerItem) || !event.getState().is(BlockTags.MINEABLE_WITH_PICKAXE)) {
            return;
        }

        BlockPos center = event.getPos();
        Vec3 towardBlock = Vec3.atCenterOf(center).subtract(player.getEyePosition());
        Direction.Axis axis = Direction.getApproximateNearest(towardBlock).getAxis();

        for (int a = -1; a <= 1 && !tool.isEmpty(); a++) {
            for (int b = -1; b <= 1 && !tool.isEmpty(); b++) {
                if (a == 0 && b == 0) {
                    continue;
                }
                mineExtra(level, player, tool, offsetInPlane(center, axis, a, b));
            }
        }
    }

    private static BlockPos offsetInPlane(BlockPos center, Direction.Axis axis, int a, int b) {
        return switch (axis) {
            case X -> center.offset(0, a, b);
            case Y -> center.offset(a, 0, b);
            case Z -> center.offset(a, b, 0);
        };
    }

    private static void mineExtra(ServerLevel level, ServerPlayer player, ItemStack tool, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.isAir() || state.getDestroySpeed(level, pos) < 0.0F || !state.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
            return;
        }
        if (state.canHarvestBlock(level, pos, player)) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            Block.dropResources(state, level, pos, blockEntity, player, tool);
        }
        level.removeBlock(pos, false);
        tool.hurtAndBreak(1, player, InteractionHand.MAIN_HAND);
    }
}
