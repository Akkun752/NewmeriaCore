package fr.akkun.newmerias2core.datagen;

import fr.akkun.newmerias2core.NewmeriaS2Core;
import fr.akkun.newmerias2core.block.ModBlocks;
import fr.akkun.newmerias2core.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new ModRecipeProvider(registries, output);
        }

        @Override
        public String getName() {
            return "TutorialMod Recipes";
        }
    }

    @Override
    protected void buildRecipes() {
        shapeless(RecipeCategory.FOOD, ModItems.RICE.get(), 4)
                .requires(ModItems.RICE_SHOOT)
                .unlockedBy(getHasName(ModItems.RICE_SHOOT.get()), has(ModItems.RICE_SHOOT))
                .save(output);

        shapeless(RecipeCategory.FOOD, ModItems.CHILI_RICE.get())
                .requires(Items.BOWL)
                .requires(ModItems.RICE)
                .requires(ModItems.CHILI_PEPPER)
                .unlockedBy(getHasName(ModItems.CHILI_PEPPER.get()), has(ModItems.CHILI_PEPPER))
                .save(output);

        shapeless(RecipeCategory.FOOD, ModItems.CHILI_SEEDS.get(), 2)
                .requires(ModItems.CHILI_PEPPER)
                .unlockedBy(getHasName(ModItems.CHILI_PEPPER.get()), has(ModItems.CHILI_PEPPER))
                .save(output);

        shapeless(RecipeCategory.FOOD, ModItems.SANDWICH.get())
                .requires(Items.BREAD)
                .requires(Items.PORKCHOP)
                .unlockedBy(getHasName(Items.PORKCHOP), has(Items.PORKCHOP))
                .save(output);

        shaped(RecipeCategory.MISC, ModItems.OBSIDIAN_STICK.get(), 4)
                .pattern("#")
                .pattern("#")
                .define('#', Items.OBSIDIAN)
                .unlockedBy(getHasName(Items.OBSIDIAN), has(Items.OBSIDIAN))
                .save(output);

        shaped(RecipeCategory.COMBAT, ModItems.SAPPHIRE_SWORD.get())
                .pattern("#")
                .pattern("#")
                .pattern("X")
                .define('#', ModItems.SAPPHIRE.get())
                .define('X', ModItems.OBSIDIAN_STICK.get())
                .unlockedBy(getHasName(ModItems.SAPPHIRE.get()), has(ModItems.SAPPHIRE.get()))
                .save(output);

        shaped(RecipeCategory.TOOLS, ModItems.WOODEN_SPATULA.get())
                .pattern("#")
                .pattern("X")
                .define('#', ItemTags.PLANKS)
                .define('X', ModItems.OBSIDIAN_STICK.get())
                .unlockedBy("has_planks", has(ItemTags.PLANKS))
                .save(output);

        shaped(RecipeCategory.TOOLS, ModItems.STONE_SPATULA.get())
                .pattern("#")
                .pattern("X")
                .define('#', ItemTags.STONE_TOOL_MATERIALS)
                .define('X', ModItems.OBSIDIAN_STICK.get())
                .unlockedBy(getHasName(Blocks.COBBLESTONE), has(Blocks.COBBLESTONE))
                .save(output);

        shaped(RecipeCategory.TOOLS, ModItems.COPPER_SPATULA.get())
                .pattern("#")
                .pattern("X")
                .define('#', Items.COPPER_INGOT)
                .define('X', ModItems.OBSIDIAN_STICK.get())
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT))
                .save(output);

        shaped(RecipeCategory.TOOLS, ModItems.IRON_SPATULA.get())
                .pattern("#")
                .pattern("X")
                .define('#', Items.IRON_INGOT)
                .define('X', ModItems.OBSIDIAN_STICK.get())
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(output);

        shaped(RecipeCategory.TOOLS, ModItems.GOLDEN_SPATULA.get())
                .pattern("#")
                .pattern("X")
                .define('#', Items.GOLD_INGOT)
                .define('X', ModItems.OBSIDIAN_STICK.get())
                .unlockedBy(getHasName(Items.GOLD_INGOT), has(Items.GOLD_INGOT))
                .save(output);

        shaped(RecipeCategory.TOOLS, ModItems.DIAMOND_SPATULA.get())
                .pattern("#")
                .pattern("X")
                .define('#', Items.DIAMOND)
                .define('X', ModItems.OBSIDIAN_STICK.get())
                .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND))
                .save(output);

        // Not using the vanilla netheriteSmithing() helper: it saves via a bare name parsed by
        // Identifier.parse, which defaults to the "minecraft" namespace instead of ours.
        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(ModItems.DIAMOND_SPATULA.get()),
                        tag(ItemTags.NETHERITE_TOOL_MATERIALS), RecipeCategory.TOOLS, ModItems.NETHERITE_SPATULA.get())
                .unlocks("has_netherite_ingot", has(ItemTags.NETHERITE_TOOL_MATERIALS))
                .save(output, NewmeriaS2Core.MOD_ID + ":" + getItemName(ModItems.NETHERITE_SPATULA.get()) + "_smithing");

        shaped(RecipeCategory.TOOLS, ModItems.SAPPHIRE_SPATULA.get())
                .pattern("#")
                .pattern("X")
                .define('#', ModItems.SAPPHIRE.get())
                .define('X', ModItems.OBSIDIAN_STICK.get())
                .unlockedBy(getHasName(ModItems.SAPPHIRE.get()), has(ModItems.SAPPHIRE.get()))
                .save(output);

        shaped(RecipeCategory.COMBAT, ModItems.HELL_SWORD.get())
                .pattern("#")
                .pattern("#")
                .pattern("X")
                .define('#', Items.NETHERITE_INGOT)
                .define('X', Items.BLAZE_ROD)
                .unlockedBy(getHasName(Items.NETHERITE_INGOT), has(Items.NETHERITE_INGOT))
                .save(output);

        shaped(RecipeCategory.TOOLS, ModItems.HELL_SPATULA.get())
                .pattern("#")
                .pattern("X")
                .define('#', Items.NETHERITE_INGOT)
                .define('X', Items.BLAZE_ROD)
                .unlockedBy(getHasName(Items.NETHERITE_INGOT), has(Items.NETHERITE_INGOT))
                .save(output);

        // Same diagonal shape vanilla uses for its own spears (e.g. diamond_spear.json).
        shaped(RecipeCategory.COMBAT, ModItems.SAPPHIRE_SPEAR.get())
                .pattern("  #")
                .pattern(" X ")
                .pattern("X  ")
                .define('#', ModItems.SAPPHIRE.get())
                .define('X', ModItems.OBSIDIAN_STICK.get())
                .unlockedBy(getHasName(ModItems.SAPPHIRE.get()), has(ModItems.SAPPHIRE.get()))
                .save(output);

        shaped(RecipeCategory.TOOLS, ModItems.SAPPHIRE_PICKAXE.get())
                .pattern("###")
                .pattern(" X ")
                .pattern(" X ")
                .define('#', ModItems.SAPPHIRE.get())
                .define('X', ModItems.OBSIDIAN_STICK.get())
                .unlockedBy(getHasName(ModItems.SAPPHIRE.get()), has(ModItems.SAPPHIRE.get()))
                .save(output);

        shaped(RecipeCategory.TOOLS, ModItems.SAPPHIRE_AXE.get())
                .pattern("##")
                .pattern("#X")
                .pattern(" X")
                .define('#', ModItems.SAPPHIRE.get())
                .define('X', ModItems.OBSIDIAN_STICK.get())
                .unlockedBy(getHasName(ModItems.SAPPHIRE.get()), has(ModItems.SAPPHIRE.get()))
                .save(output);

        shaped(RecipeCategory.TOOLS, ModItems.SAPPHIRE_SHOVEL.get())
                .pattern("#")
                .pattern("X")
                .pattern("X")
                .define('#', ModItems.SAPPHIRE.get())
                .define('X', ModItems.OBSIDIAN_STICK.get())
                .unlockedBy(getHasName(ModItems.SAPPHIRE.get()), has(ModItems.SAPPHIRE.get()))
                .save(output);

        shaped(RecipeCategory.TOOLS, ModItems.SAPPHIRE_HOE.get())
                .pattern("##")
                .pattern(" X")
                .pattern(" X")
                .define('#', ModItems.SAPPHIRE.get())
                .define('X', ModItems.OBSIDIAN_STICK.get())
                .unlockedBy(getHasName(ModItems.SAPPHIRE.get()), has(ModItems.SAPPHIRE.get()))
                .save(output);

        // Hammer: the axe shape mirrored onto both sides (5 material + 2 sticks) instead of one.
        shaped(RecipeCategory.TOOLS, ModItems.WOODEN_HAMMER.get())
                .pattern("###")
                .pattern("#X#")
                .pattern(" X ")
                .define('#', ItemTags.PLANKS)
                .define('X', Items.STICK)
                .unlockedBy("has_planks", has(ItemTags.PLANKS))
                .save(output);

        shaped(RecipeCategory.TOOLS, ModItems.STONE_HAMMER.get())
                .pattern("###")
                .pattern("#X#")
                .pattern(" X ")
                .define('#', ItemTags.STONE_TOOL_MATERIALS)
                .define('X', Items.STICK)
                .unlockedBy(getHasName(Blocks.COBBLESTONE), has(Blocks.COBBLESTONE))
                .save(output);

        shaped(RecipeCategory.TOOLS, ModItems.COPPER_HAMMER.get())
                .pattern("###")
                .pattern("#X#")
                .pattern(" X ")
                .define('#', Items.COPPER_INGOT)
                .define('X', Items.STICK)
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT))
                .save(output);

        shaped(RecipeCategory.TOOLS, ModItems.IRON_HAMMER.get())
                .pattern("###")
                .pattern("#X#")
                .pattern(" X ")
                .define('#', Items.IRON_INGOT)
                .define('X', Items.STICK)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(output);

        shaped(RecipeCategory.TOOLS, ModItems.GOLDEN_HAMMER.get())
                .pattern("###")
                .pattern("#X#")
                .pattern(" X ")
                .define('#', Items.GOLD_INGOT)
                .define('X', Items.STICK)
                .unlockedBy(getHasName(Items.GOLD_INGOT), has(Items.GOLD_INGOT))
                .save(output);

        shaped(RecipeCategory.TOOLS, ModItems.DIAMOND_HAMMER.get())
                .pattern("###")
                .pattern("#X#")
                .pattern(" X ")
                .define('#', Items.DIAMOND)
                .define('X', Items.STICK)
                .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND))
                .save(output);

        shaped(RecipeCategory.TOOLS, ModItems.SAPPHIRE_HAMMER.get())
                .pattern("###")
                .pattern("#X#")
                .pattern(" X ")
                .define('#', ModItems.SAPPHIRE.get())
                .define('X', ModItems.OBSIDIAN_STICK.get())
                .unlockedBy(getHasName(ModItems.SAPPHIRE.get()), has(ModItems.SAPPHIRE.get()))
                .save(output);

        // Not using the vanilla netheriteSmithing() helper: see the netherite spatula comment above.
        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(ModItems.DIAMOND_HAMMER.get()),
                        tag(ItemTags.NETHERITE_TOOL_MATERIALS), RecipeCategory.TOOLS, ModItems.NETHERITE_HAMMER.get())
                .unlocks("has_netherite_ingot", has(ItemTags.NETHERITE_TOOL_MATERIALS))
                .save(output, NewmeriaS2Core.MOD_ID + ":" + getItemName(ModItems.NETHERITE_HAMMER.get()) + "_smithing");

        // Wand: same diagonal shape as the spear, but with a single stick instead of two.
        shaped(RecipeCategory.MISC, ModItems.WOODEN_WAND.get())
                .pattern(" #")
                .pattern("X ")
                .define('#', ItemTags.PLANKS)
                .define('X', Items.STICK)
                .unlockedBy("has_planks", has(ItemTags.PLANKS))
                .save(output);

        shaped(RecipeCategory.MISC, ModItems.STONE_WAND.get())
                .pattern(" #")
                .pattern("X ")
                .define('#', ItemTags.STONE_TOOL_MATERIALS)
                .define('X', Items.STICK)
                .unlockedBy(getHasName(Blocks.COBBLESTONE), has(Blocks.COBBLESTONE))
                .save(output);

        shaped(RecipeCategory.MISC, ModItems.COPPER_WAND.get())
                .pattern(" #")
                .pattern("X ")
                .define('#', Items.COPPER_INGOT)
                .define('X', Items.STICK)
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT))
                .save(output);

        shaped(RecipeCategory.MISC, ModItems.IRON_WAND.get())
                .pattern(" #")
                .pattern("X ")
                .define('#', Items.IRON_INGOT)
                .define('X', Items.STICK)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(output);

        shaped(RecipeCategory.MISC, ModItems.GOLDEN_WAND.get())
                .pattern(" #")
                .pattern("X ")
                .define('#', Items.GOLD_INGOT)
                .define('X', Items.STICK)
                .unlockedBy(getHasName(Items.GOLD_INGOT), has(Items.GOLD_INGOT))
                .save(output);

        shaped(RecipeCategory.MISC, ModItems.DIAMOND_WAND.get())
                .pattern(" #")
                .pattern("X ")
                .define('#', Items.DIAMOND)
                .define('X', Items.STICK)
                .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND))
                .save(output);

        shaped(RecipeCategory.MISC, ModItems.SAPPHIRE_WAND.get())
                .pattern(" #")
                .pattern("X ")
                .define('#', ModItems.SAPPHIRE.get())
                .define('X', ModItems.OBSIDIAN_STICK.get())
                .unlockedBy(getHasName(ModItems.SAPPHIRE.get()), has(ModItems.SAPPHIRE.get()))
                .save(output);

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(ModItems.DIAMOND_WAND.get()),
                        tag(ItemTags.NETHERITE_TOOL_MATERIALS), RecipeCategory.MISC, ModItems.NETHERITE_WAND.get())
                .unlocks("has_netherite_ingot", has(ItemTags.NETHERITE_TOOL_MATERIALS))
                .save(output, NewmeriaS2Core.MOD_ID + ":" + getItemName(ModItems.NETHERITE_WAND.get()) + "_smithing");

        shaped(RecipeCategory.COMBAT, ModItems.SAPPHIRE_HELMET.get())
                .pattern("###")
                .pattern("# #")
                .define('#', ModItems.SAPPHIRE.get())
                .unlockedBy(getHasName(ModItems.SAPPHIRE.get()), has(ModItems.SAPPHIRE.get()))
                .save(output);

        shaped(RecipeCategory.COMBAT, ModItems.SAPPHIRE_CHESTPLATE.get())
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .define('#', ModItems.SAPPHIRE.get())
                .unlockedBy(getHasName(ModItems.SAPPHIRE.get()), has(ModItems.SAPPHIRE.get()))
                .save(output);

        shaped(RecipeCategory.COMBAT, ModItems.SAPPHIRE_LEGGINGS.get())
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .define('#', ModItems.SAPPHIRE.get())
                .unlockedBy(getHasName(ModItems.SAPPHIRE.get()), has(ModItems.SAPPHIRE.get()))
                .save(output);

        shaped(RecipeCategory.COMBAT, ModItems.SAPPHIRE_BOOTS.get())
                .pattern("# #")
                .pattern("# #")
                .define('#', ModItems.SAPPHIRE.get())
                .unlockedBy(getHasName(ModItems.SAPPHIRE.get()), has(ModItems.SAPPHIRE.get()))
                .save(output);

        // Any arrangement works - shapeless recipes don't care about slot positions.
        shapeless(RecipeCategory.MISC, ModItems.OIL_BUCKET.get())
                .requires(Items.WATER_BUCKET)
                .requires(Items.WHEAT_SEEDS, 8)
                .unlockedBy(getHasName(Items.WATER_BUCKET), has(Items.WATER_BUCKET))
                .save(output);

        // Vanilla has no crafting recipe for chainmail armor (only villager trades) - adding our own,
        // using iron nuggets as the "ingot" stand-in, in the standard armor shapes. Saved explicitly
        // under our own namespace: the no-arg save(output) would otherwise derive the recipe id from
        // the crafted item's OWN registry key, i.e. "minecraft:chainmail_helmet".
        shaped(RecipeCategory.COMBAT, Items.CHAINMAIL_HELMET)
                .pattern("###")
                .pattern("# #")
                .define('#', Items.IRON_NUGGET)
                .unlockedBy(getHasName(Items.IRON_NUGGET), has(Items.IRON_NUGGET))
                .save(output, NewmeriaS2Core.MOD_ID + ":" + getItemName(Items.CHAINMAIL_HELMET));

        shaped(RecipeCategory.COMBAT, Items.CHAINMAIL_CHESTPLATE)
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .define('#', Items.IRON_NUGGET)
                .unlockedBy(getHasName(Items.IRON_NUGGET), has(Items.IRON_NUGGET))
                .save(output, NewmeriaS2Core.MOD_ID + ":" + getItemName(Items.CHAINMAIL_CHESTPLATE));

        shaped(RecipeCategory.COMBAT, Items.CHAINMAIL_LEGGINGS)
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .define('#', Items.IRON_NUGGET)
                .unlockedBy(getHasName(Items.IRON_NUGGET), has(Items.IRON_NUGGET))
                .save(output, NewmeriaS2Core.MOD_ID + ":" + getItemName(Items.CHAINMAIL_LEGGINGS));

        shaped(RecipeCategory.COMBAT, Items.CHAINMAIL_BOOTS)
                .pattern("# #")
                .pattern("# #")
                .define('#', Items.IRON_NUGGET)
                .unlockedBy(getHasName(Items.IRON_NUGGET), has(Items.IRON_NUGGET))
                .save(output, NewmeriaS2Core.MOD_ID + ":" + getItemName(Items.CHAINMAIL_BOOTS));

        twoByTwoPacker(RecipeCategory.BUILDING_BLOCKS, ModBlocks.BLACK_SANDSTONE.get(), ModBlocks.BLACK_SAND.get());
        smeltingResultFromBase(ModBlocks.SMOOTH_BLACK_SANDSTONE.get(), ModBlocks.BLACK_SANDSTONE.get());

        generateRecipes(ModBlockFamilies.BLACK_SANDSTONE, FeatureFlags.REGISTRY.allFlags());
        generateRecipes(ModBlockFamilies.CUT_BLACK_SANDSTONE, FeatureFlags.REGISTRY.allFlags());
        generateRecipes(ModBlockFamilies.SMOOTH_BLACK_SANDSTONE, FeatureFlags.REGISTRY.allFlags());
        generateRecipes(ModBlockFamilies.MARBLE, FeatureFlags.REGISTRY.allFlags());
        generateRecipes(ModBlockFamilies.MARBLE_BRICKS, FeatureFlags.REGISTRY.allFlags());
        generateRecipes(ModBlockFamilies.MOSSY_MARBLE_BRICKS, FeatureFlags.REGISTRY.allFlags());
        generateRecipes(ModBlockFamilies.COBBLED_MARBLE, FeatureFlags.REGISTRY.allFlags());

        shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MOSSY_MARBLE_BRICKS.get())
                .requires(ModBlocks.MARBLE_BRICKS.get())
                .requires(Blocks.VINE)
                .unlockedBy(getHasName(ModBlocks.MARBLE_BRICKS.get()), has(ModBlocks.MARBLE_BRICKS.get()))
                .save(output, getConversionRecipeName(ModBlocks.MOSSY_MARBLE_BRICKS.get(), Blocks.VINE));

        shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MOSSY_MARBLE_BRICKS.get())
                .requires(ModBlocks.MARBLE_BRICKS.get())
                .requires(Blocks.MOSS_BLOCK)
                .unlockedBy(getHasName(ModBlocks.MARBLE_BRICKS.get()), has(ModBlocks.MARBLE_BRICKS.get()))
                .save(output, getConversionRecipeName(ModBlocks.MOSSY_MARBLE_BRICKS.get(), Blocks.MOSS_BLOCK));

        shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.GREEN_SCREEN_BLOCK.get())
                .pattern("###")
                .pattern("#X#")
                .pattern("###")
                .define('#', Blocks.STAINED_GLASS.green())
                .define('X', Blocks.REDSTONE_LAMP)
                .unlockedBy(getHasName(Blocks.REDSTONE_LAMP), has(Blocks.REDSTONE_LAMP))
                .save(output);
    }

    @Override
    protected <T extends AbstractCookingRecipe> void oreCooking(AbstractCookingRecipe.Factory<T> factory, List<ItemLike> smeltables,
                                                                RecipeCategory craftingCategory, CookingBookCategory cookingCategory, ItemLike result,
                                                                float experience, int cookingTime, String group, String fromDesc) {
        for(ItemLike itemlike : smeltables) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), craftingCategory, cookingCategory, result, experience, cookingTime, factory).group(group).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(output, NewmeriaS2Core.MOD_ID + ":" + getItemName(result) + fromDesc + "_" + getItemName(itemlike));
        }
    }
}
