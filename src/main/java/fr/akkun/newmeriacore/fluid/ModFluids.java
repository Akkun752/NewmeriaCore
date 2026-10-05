package fr.akkun.newmeriacore.fluid;

import fr.akkun.newmeriacore.NewmeriaCore;
import fr.akkun.newmeriacore.block.ModBlocks;
import fr.akkun.newmeriacore.item.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModFluids {
    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, NewmeriaCore.MOD_ID);
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(BuiltInRegistries.FLUID, NewmeriaCore.MOD_ID);

    public static final DeferredHolder<FluidType, FluidType> OIL_TYPE = FLUID_TYPES.register("oil",
            () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.newmeriacore.oil")
                    .density(850)
                    .viscosity(1500)
                    .canDrown(true)
                    .canSwim(true)
                    .canExtinguish(false)
                    .canConvertToSource(false)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<Fluid, OilFluid.Source> OIL_SOURCE = FLUIDS.register("oil",
            () -> new OilFluid.Source(oilProperties()));
    public static final DeferredHolder<Fluid, OilFluid.Flowing> OIL_FLOWING = FLUIDS.register("flowing_oil",
            () -> new OilFluid.Flowing(oilProperties()));

    // Same spread speed as vanilla water: tickRate 5, slopeFindDistance 4, levelDecreasePerBlock 1.
    private static BaseFlowingFluid.Properties oilProperties() {
        return new BaseFlowingFluid.Properties(OIL_TYPE::get, OIL_SOURCE::get, OIL_FLOWING::get)
                .bucket(ModItems.OIL_BUCKET::get)
                .block(ModBlocks.OIL::get)
                .slopeFindDistance(4)
                .levelDecreasePerBlock(1)
                .explosionResistance(100.0F)
                .tickRate(5);
    }

    public static void register(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
        FLUIDS.register(eventBus);
    }
}
