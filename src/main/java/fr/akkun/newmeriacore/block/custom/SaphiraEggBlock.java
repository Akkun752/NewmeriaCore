package fr.akkun.newmeriacore.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.DragonEggBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Behaves exactly like the vanilla Dragon Egg: it falls, and teleports away when hit or
 * right-clicked, so it can only be picked up the indirect ways (dropped onto a torch, pushed by a
 * piston...). Only its texture and the colour of its falling dust differ.
 */
public class SaphiraEggBlock extends DragonEggBlock {
    public SaphiraEggBlock(Properties properties) {
        super(properties);
    }

    @Override
    public int getDustColor(BlockState blockState, BlockGetter level, BlockPos pos) {
        return 0xFF2040C0;
    }
}
