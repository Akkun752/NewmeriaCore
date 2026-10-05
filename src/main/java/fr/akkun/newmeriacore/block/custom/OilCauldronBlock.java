package fr.akkun.newmeriacore.block.custom;

import com.mojang.serialization.MapCodec;
import fr.akkun.newmeriacore.block.ModBlockEntities;
import fr.akkun.newmeriacore.block.ModCauldronInteractions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/** Always-full cauldron (like lava/powder snow, not the leveled water cauldron), filled via {@code OIL_BUCKET}. */
public class OilCauldronBlock extends AbstractCauldronBlock implements EntityBlock {
    public static final MapCodec<OilCauldronBlock> CODEC = simpleCodec(OilCauldronBlock::new);

    public OilCauldronBlock(BlockBehaviour.Properties properties) {
        super(properties, ModCauldronInteractions.OIL);
    }

    @Override
    protected MapCodec<? extends AbstractCauldronBlock> codec() {
        return CODEC;
    }

    @Override
    public boolean isFull(BlockState state) {
        return true;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new OilCauldronBlockEntity(pos, state);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == ModBlockEntities.OIL_CAULDRON.get()
                ? (BlockEntityTicker<T>) (BlockEntityTicker<OilCauldronBlockEntity>) OilCauldronBlockEntity::tick
                : null;
    }
}
