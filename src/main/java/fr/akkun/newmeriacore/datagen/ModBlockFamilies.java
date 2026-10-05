package fr.akkun.newmeriacore.datagen;

import fr.akkun.newmeriacore.block.ModBlocks;
import net.minecraft.data.BlockFamily;

/** Shared {@link BlockFamily} definitions, reused by both the model provider (blockstates/models) and the recipe provider. */
public class ModBlockFamilies {
    public static final BlockFamily BLACK_SANDSTONE = new BlockFamily.Builder(ModBlocks.BLACK_SANDSTONE.get())
            .wall(ModBlocks.BLACK_SANDSTONE_WALL.get())
            .stairs(ModBlocks.BLACK_SANDSTONE_STAIRS.get())
            .slab(ModBlocks.BLACK_SANDSTONE_SLAB.get())
            .chiseled(ModBlocks.CHISELED_BLACK_SANDSTONE.get())
            .cut(ModBlocks.CUT_BLACK_SANDSTONE.get())
            .generateStonecutterRecipe()
            .getFamily();

    public static final BlockFamily CUT_BLACK_SANDSTONE = new BlockFamily.Builder(ModBlocks.CUT_BLACK_SANDSTONE.get())
            .slab(ModBlocks.CUT_BLACK_SANDSTONE_SLAB.get())
            .generateStonecutterRecipe()
            .getFamily();

    public static final BlockFamily SMOOTH_BLACK_SANDSTONE = new BlockFamily.Builder(ModBlocks.SMOOTH_BLACK_SANDSTONE.get())
            .slab(ModBlocks.SMOOTH_BLACK_SANDSTONE_SLAB.get())
            .stairs(ModBlocks.SMOOTH_BLACK_SANDSTONE_STAIRS.get())
            .generateStonecutterRecipe()
            .getFamily();

    public static final BlockFamily MARBLE = new BlockFamily.Builder(ModBlocks.MARBLE.get())
            .slab(ModBlocks.MARBLE_SLAB.get())
            .stairs(ModBlocks.MARBLE_STAIRS.get())
            .bricks(ModBlocks.MARBLE_BRICKS.get())
            .cobbled(ModBlocks.COBBLED_MARBLE.get())
            .pressurePlate(ModBlocks.MARBLE_PRESSURE_PLATE.get())
            .button(ModBlocks.MARBLE_BUTTON.get())
            .generateStonecutterRecipe()
            .getFamily();

    public static final BlockFamily COBBLED_MARBLE = new BlockFamily.Builder(ModBlocks.COBBLED_MARBLE.get())
            .stairs(ModBlocks.COBBLED_MARBLE_STAIRS.get())
            .slab(ModBlocks.COBBLED_MARBLE_SLAB.get())
            .wall(ModBlocks.COBBLED_MARBLE_WALL.get())
            .generateStonecutterRecipe()
            .getFamily();

    public static final BlockFamily MARBLE_BRICKS = new BlockFamily.Builder(ModBlocks.MARBLE_BRICKS.get())
            .wall(ModBlocks.MARBLE_BRICK_WALL.get())
            .stairs(ModBlocks.MARBLE_BRICK_STAIRS.get())
            .slab(ModBlocks.MARBLE_BRICK_SLAB.get())
            .chiseled(ModBlocks.CHISELED_MARBLE_BRICKS.get())
            .cracked(ModBlocks.CRACKED_MARBLE_BRICKS.get())
            .generateStonecutterRecipe()
            .getFamily();

    public static final BlockFamily MOSSY_MARBLE_BRICKS = new BlockFamily.Builder(ModBlocks.MOSSY_MARBLE_BRICKS.get())
            .wall(ModBlocks.MOSSY_MARBLE_BRICK_WALL.get())
            .stairs(ModBlocks.MOSSY_MARBLE_BRICK_STAIRS.get())
            .slab(ModBlocks.MOSSY_MARBLE_BRICK_SLAB.get())
            .generateStonecutterRecipe()
            .getFamily();
}
