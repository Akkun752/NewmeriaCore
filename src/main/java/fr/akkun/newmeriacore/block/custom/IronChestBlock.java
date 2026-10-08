package fr.akkun.newmeriacore.block.custom;

import fr.akkun.newmeriacore.block.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

// A vanilla chest in every way (lid, sounds, hoppers, comparators, cats and solid blocks blocking it)
// but with 4 rows of slots - see IronChestBlockEntity.
public class IronChestBlock extends ChestBlock {
    public IronChestBlock(Properties properties) {
        super(ModBlockEntities.IRON_CHEST::get, SoundEvents.CHEST_OPEN, SoundEvents.CHEST_CLOSE, properties);
    }

    // Never joins a neighbour into a double chest: it always stays a single block.
    @Override
    public boolean chestCanConnectTo(BlockState blockState) {
        return false;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new IronChestBlockEntity(worldPosition, blockState);
    }
}
