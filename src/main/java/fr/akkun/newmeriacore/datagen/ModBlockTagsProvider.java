package fr.akkun.newmeriacore.datagen;

import fr.akkun.newmeriacore.NewmeriaCore;
import fr.akkun.newmeriacore.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, NewmeriaCore.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        addAll(BlockTags.MINEABLE_WITH_SHOVEL, ModBlocks.BLACK_SAND.get());
        addAll(BlockTags.SAND, ModBlocks.BLACK_SAND.get());

        // Same tags as vanilla short_dry_grass / tall_dry_grass.
        for (TagKey<Block> tag : List.of(BlockTags.REPLACEABLE, BlockTags.REPLACEABLE_BY_TREES, BlockTags.REPLACEABLE_BY_MUSHROOMS, BlockTags.EDIBLE_FOR_SHEEP)) {
            addAll(tag, ModBlocks.SHORT_DRY_BLACK_GRASS.get(), ModBlocks.TALL_DRY_BLACK_GRASS.get());
        }

        // Worldgen/ambience tags vanilla fills with sand and sandstone by name rather than through #sand,
        // needed for the Black Desert: caves carving through it, rabbits spawning, desert ambient sounds.
        addAll(BlockTags.OVERWORLD_CARVER_REPLACEABLES, ModBlocks.BLACK_SANDSTONE.get());
        addAll(BlockTags.RABBITS_SPAWNABLE_ON, ModBlocks.BLACK_SAND.get());
        addAll(BlockTags.TRIGGERS_AMBIENT_DESERT_SAND_BLOCK_SOUNDS, ModBlocks.BLACK_SAND.get());
        addAll(BlockTags.TRIGGERS_AMBIENT_DESERT_DRY_VEGETATION_BLOCK_SOUNDS, ModBlocks.BLACK_SAND.get());

        addAll(BlockTags.MINEABLE_WITH_PICKAXE,
                ModBlocks.SAPPHIRE_ORE.get(), ModBlocks.DEEPSLATE_SAPPHIRE_ORE.get(), ModBlocks.SAPPHIRE_BLOCK.get(),

                ModBlocks.BLACK_SANDSTONE.get(), ModBlocks.CHISELED_BLACK_SANDSTONE.get(), ModBlocks.CUT_BLACK_SANDSTONE.get(),
                ModBlocks.SMOOTH_BLACK_SANDSTONE.get(), ModBlocks.BLACK_SANDSTONE_SLAB.get(), ModBlocks.CUT_BLACK_SANDSTONE_SLAB.get(),
                ModBlocks.SMOOTH_BLACK_SANDSTONE_SLAB.get(), ModBlocks.BLACK_SANDSTONE_STAIRS.get(), ModBlocks.SMOOTH_BLACK_SANDSTONE_STAIRS.get(),
                ModBlocks.BLACK_SANDSTONE_WALL.get(),

                ModBlocks.MARBLE.get(), ModBlocks.COBBLED_MARBLE.get(), ModBlocks.MARBLE_SLAB.get(), ModBlocks.MARBLE_STAIRS.get(),
                ModBlocks.MARBLE_BRICKS.get(), ModBlocks.CHISELED_MARBLE_BRICKS.get(), ModBlocks.CRACKED_MARBLE_BRICKS.get(),
                ModBlocks.MARBLE_BRICK_SLAB.get(), ModBlocks.MARBLE_BRICK_STAIRS.get(), ModBlocks.MARBLE_BRICK_WALL.get(),
                ModBlocks.MOSSY_MARBLE_BRICKS.get(), ModBlocks.MOSSY_MARBLE_BRICK_SLAB.get(), ModBlocks.MOSSY_MARBLE_BRICK_STAIRS.get(),
                ModBlocks.MOSSY_MARBLE_BRICK_WALL.get(), ModBlocks.COBBLED_MARBLE_STAIRS.get(), ModBlocks.COBBLED_MARBLE_SLAB.get(),
                ModBlocks.COBBLED_MARBLE_WALL.get(), ModBlocks.MARBLE_PRESSURE_PLATE.get(), ModBlocks.MARBLE_BUTTON.get());

        addAll(BlockTags.NEEDS_STONE_TOOL,
                ModBlocks.MARBLE.get(), ModBlocks.COBBLED_MARBLE.get(), ModBlocks.MARBLE_SLAB.get(), ModBlocks.MARBLE_STAIRS.get(),
                ModBlocks.MARBLE_BRICKS.get(), ModBlocks.CHISELED_MARBLE_BRICKS.get(), ModBlocks.CRACKED_MARBLE_BRICKS.get(),
                ModBlocks.MARBLE_BRICK_SLAB.get(), ModBlocks.MARBLE_BRICK_STAIRS.get(), ModBlocks.MARBLE_BRICK_WALL.get(),
                ModBlocks.MOSSY_MARBLE_BRICKS.get(), ModBlocks.MOSSY_MARBLE_BRICK_SLAB.get(), ModBlocks.MOSSY_MARBLE_BRICK_STAIRS.get(),
                ModBlocks.MOSSY_MARBLE_BRICK_WALL.get(), ModBlocks.COBBLED_MARBLE_STAIRS.get(), ModBlocks.COBBLED_MARBLE_SLAB.get(),
                ModBlocks.COBBLED_MARBLE_WALL.get(), ModBlocks.MARBLE_PRESSURE_PLATE.get(), ModBlocks.MARBLE_BUTTON.get());

        addAll(BlockTags.WALLS,
                ModBlocks.BLACK_SANDSTONE_WALL.get(), ModBlocks.MARBLE_BRICK_WALL.get(),
                ModBlocks.MOSSY_MARBLE_BRICK_WALL.get(), ModBlocks.COBBLED_MARBLE_WALL.get());

        addAll(Tags.Blocks.STONES,
                ModBlocks.MARBLE.get(), ModBlocks.COBBLED_MARBLE.get(), ModBlocks.MARBLE_SLAB.get(), ModBlocks.MARBLE_STAIRS.get(),
                ModBlocks.MARBLE_BRICKS.get(), ModBlocks.CHISELED_MARBLE_BRICKS.get(), ModBlocks.CRACKED_MARBLE_BRICKS.get(),
                ModBlocks.MARBLE_BRICK_SLAB.get(), ModBlocks.MARBLE_BRICK_STAIRS.get(), ModBlocks.MARBLE_BRICK_WALL.get(),
                ModBlocks.MOSSY_MARBLE_BRICKS.get(), ModBlocks.MOSSY_MARBLE_BRICK_SLAB.get(), ModBlocks.MOSSY_MARBLE_BRICK_STAIRS.get(),
                ModBlocks.MOSSY_MARBLE_BRICK_WALL.get(), ModBlocks.COBBLED_MARBLE_STAIRS.get(), ModBlocks.COBBLED_MARBLE_SLAB.get(),
                ModBlocks.COBBLED_MARBLE_WALL.get(), ModBlocks.MARBLE_PRESSURE_PLATE.get(), ModBlocks.MARBLE_BUTTON.get());
    }

    @SafeVarargs
    private final void addAll(TagKey<Block> tag, Block... blocks) {
        TagAppender<Block> appender = tag(tag);
        ResourceKey<Block>[] keys = Arrays.stream(blocks).map(ModBlocks::getRK).toArray(ResourceKey[]::new);
        appender.add(keys);
    }
}
