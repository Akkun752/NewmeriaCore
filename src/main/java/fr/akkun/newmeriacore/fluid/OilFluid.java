package fr.akkun.newmeriacore.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

/**
 * Oil never mixes with water: it refuses to be pushed out by water from any direction, and refuses
 * to spread downward into a space water already occupies - so poured over water, it simply pools on
 * the surface instead of sinking into or displacing it (the same technique vanilla lava uses to avoid
 * flowing into water, minus the obsidian/stone conversion).
 */
public abstract class OilFluid extends BaseFlowingFluid {
    protected OilFluid(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean canBeReplacedWith(FluidState fluidState, BlockGetter level, BlockPos pos, Fluid fluid, Direction direction) {
        if (fluid.is(FluidTags.WATER)) {
            return false;
        }
        return super.canBeReplacedWith(fluidState, level, pos, fluid, direction);
    }

    @Override
    protected void spreadTo(LevelAccessor level, BlockPos pos, BlockState state, Direction direction, FluidState fluidState) {
        if (direction == Direction.DOWN && level.getFluidState(pos).is(FluidTags.WATER)) {
            return;
        }
        super.spreadTo(level, pos, state, direction, fluidState);
    }

    public static class Source extends OilFluid {
        public Source(Properties properties) {
            super(properties);
        }

        @Override
        public int getAmount(FluidState state) {
            return 8;
        }

        @Override
        public boolean isSource(FluidState state) {
            return true;
        }
    }

    public static class Flowing extends OilFluid {
        public Flowing(Properties properties) {
            super(properties);
            this.registerDefaultState(this.getStateDefinition().any().setValue(LEVEL, 7));
        }

        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }

        @Override
        public boolean isSource(FluidState state) {
            return false;
        }
    }
}
