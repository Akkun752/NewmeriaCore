package fr.akkun.newmeriacore.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

// Like IronChestBlock, a vanilla chest that never joins a neighbour - but 12 slots wide, see ChestTier.
public class LargeChestBlock extends ChestBlock {
    private final ChestTier tier;

    public LargeChestBlock(ChestTier tier, Properties properties) {
        super(tier::blockEntityType, SoundEvents.CHEST_OPEN, SoundEvents.CHEST_CLOSE, properties);
        this.tier = tier;
    }

    @Override
    public boolean chestCanConnectTo(BlockState blockState) {
        return false;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new LargeChestBlockEntity(this.tier, worldPosition, blockState);
    }
}
