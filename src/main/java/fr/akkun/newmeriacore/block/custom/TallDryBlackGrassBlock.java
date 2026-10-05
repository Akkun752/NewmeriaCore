package fr.akkun.newmeriacore.block.custom;

import fr.akkun.newmeriacore.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.TallDryGrassBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/** Black Desert counterpart of vanilla's Tall Dry Grass: identical, except bone meal spreads the
 *  black short variant around it, and its flammability is declared here (vanilla's is a hardcoded table). */
public class TallDryBlackGrassBlock extends TallDryGrassBlock {
    public TallDryBlackGrassBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return BonemealableBlock.hasSpreadableNeighbourPos(level, pos, ModBlocks.SHORT_DRY_BLACK_GRASS.get().defaultBlockState());
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        BlockState shortGrass = ModBlocks.SHORT_DRY_BLACK_GRASS.get().defaultBlockState();
        BonemealableBlock.findSpreadableNeighbourPos(level, pos, shortGrass)
                .ifPresent(blockPos -> level.setBlockAndUpdate(blockPos, shortGrass));
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
