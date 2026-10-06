package fr.akkun.newmeriacore.recipe;

import fr.akkun.newmeriacore.NewmeriaCore;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, NewmeriaCore.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ShapelessRecipe>> CRAFTING_SHAPELESS_NO_REMAINDER =
            RECIPE_SERIALIZERS.register("crafting_shapeless_no_remainder", () -> NoRemainderShapelessRecipe.SERIALIZER);

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
