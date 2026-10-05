package fr.akkun.newmeriacore.block.custom;

import fr.akkun.newmeriacore.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.ShortDryGrassBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/** Black Desert counterpart of vanilla's Short Dry Grass: identical, except bone meal grows it into
 *  the black tall variant, and its flammability is declared here (vanilla's is a hardcoded table). */
public class ShortDryBlackGrassBlock extends ShortDryGrassBlock {
    public ShortDryBlackGrassBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        level.setBlockAndUpdate(pos, ModBlocks.TALL_DRY_BLACK_GRASS.get().defaultBlockState());
    }

    // Same odds as vanilla's FireBlock gives both dry grasses (ignite 60, burn 100).
    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 60;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 100;
    }
}
