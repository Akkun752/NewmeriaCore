package fr.akkun.newmeriacore.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import java.util.List;

/**
 * A vanilla shapeless recipe that leaves nothing behind in the crafting grid: ingredients that
 * would normally hand back a container (a Water Bucket returning its empty Bucket) are fully
 * consumed. Meant for recipes whose result already is that container, e.g. the Oil Bucket - which
 * would otherwise duplicate a bucket on every craft.
 */
public class NoRemainderShapelessRecipe extends ShapelessRecipe {
    // Copies of what ShapelessRecipe keeps private, only needed to write the recipe back out.
    private final ItemStackTemplate result;
    private final List<Ingredient> ingredients;

    public static final MapCodec<ShapelessRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Recipe.CommonInfo.MAP_CODEC.forGetter(recipe -> self(recipe).commonInfo),
            CraftingRecipe.CraftingBookInfo.MAP_CODEC.forGetter(recipe -> self(recipe).bookInfo),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(recipe -> self(recipe).result),
            Ingredient.CODEC.listOf(1, 9).fieldOf("ingredients").forGetter(recipe -> self(recipe).ingredients)
    ).apply(instance, NoRemainderShapelessRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ShapelessRecipe> STREAM_CODEC = StreamCodec.composite(
            Recipe.CommonInfo.STREAM_CODEC, recipe -> self(recipe).commonInfo,
            CraftingRecipe.CraftingBookInfo.STREAM_CODEC, recipe -> self(recipe).bookInfo,
            ItemStackTemplate.STREAM_CODEC, recipe -> self(recipe).result,
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), recipe -> self(recipe).ingredients,
            NoRemainderShapelessRecipe::new
    );

    // Typed on ShapelessRecipe because that is what ShapelessRecipe#getSerializer has to return.
    public static final RecipeSerializer<ShapelessRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    public NoRemainderShapelessRecipe(Recipe.CommonInfo commonInfo, CraftingRecipe.CraftingBookInfo bookInfo, ItemStackTemplate result, List<Ingredient> ingredients) {
        super(commonInfo, bookInfo, result, ingredients);
        this.result = result;
        this.ingredients = ingredients;
    }

    private static NoRemainderShapelessRecipe self(ShapelessRecipe recipe) {
        return (NoRemainderShapelessRecipe) recipe;
    }

    @Override
    public RecipeSerializer<ShapelessRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        return NonNullList.withSize(input.size(), ItemStack.EMPTY);
    }
}
