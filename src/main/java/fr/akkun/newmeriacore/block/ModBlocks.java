package fr.akkun.newmeriacore.block;

import fr.akkun.newmeriacore.NewmeriaCore;
import fr.akkun.newmeriacore.block.custom.ChestTier;
import fr.akkun.newmeriacore.block.custom.IronChestBlock;
import fr.akkun.newmeriacore.block.custom.LargeChestBlock;
import fr.akkun.newmeriacore.block.custom.RiceCropBlock;
import fr.akkun.newmeriacore.block.custom.SaphiraEggBlock;
import fr.akkun.newmeriacore.block.custom.ChiliCropBlock;
import fr.akkun.newmeriacore.block.custom.OilCauldronBlock;
import fr.akkun.newmeriacore.block.custom.OnionCropBlock;
import fr.akkun.newmeriacore.block.custom.OilFluidBlock;
import fr.akkun.newmeriacore.block.custom.TomatoCropBlock;
import fr.akkun.newmeriacore.block.custom.ShortDryBlackGrassBlock;
import fr.akkun.newmeriacore.block.custom.TallDryBlackGrassBlock;
import fr.akkun.newmeriacore.fluid.ModFluids;
import fr.akkun.newmeriacore.item.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ColorRGBA;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.SandBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Consumer;
import java.util.function.Function;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(NewmeriaCore.MOD_ID);

    public static ResourceKey<Block> getRK(Block block) {
        return BuiltInRegistries.BLOCK.getResourceKey(block).get();
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> function, Component... components) {
        DeferredBlock<T> toReturn = BLOCKS.registerBlock(name, function);
        registerBlockItem(name, toReturn, components);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block, Component... components) {
        ModItems.ITEMS.registerItem(name, properties -> new BlockItem(block.get(), properties.useBlockDescriptionPrefix()) {
            @Override
            public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
                for(var component : components) {
                    builder.accept(component);
                }
                super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
            }
        });
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> function) {
        DeferredBlock<T> toReturn = BLOCKS.registerBlock(name, function);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.registerItem(name, properties -> new BlockItem(block.get(), properties.useBlockDescriptionPrefix()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    public static final DeferredBlock<Block> RICE_CROP = BLOCKS.registerBlock("rice_crop",
            properties -> new RiceCropBlock(properties.randomTicks().sound(SoundType.CROP)
                    .instabreak().noCollision().pushReaction(PushReaction.POPPED)));

    public static final DeferredBlock<Block> CHILI_CROP = BLOCKS.registerBlock("chili_crop",
            properties -> new ChiliCropBlock(properties.randomTicks().sound(SoundType.CROP)
                    .instabreak().noCollision().pushReaction(PushReaction.POPPED)));

    public static final DeferredBlock<Block> ONIONS = BLOCKS.registerBlock("onions",
            properties -> new OnionCropBlock(properties.randomTicks().sound(SoundType.CROP)
                    .instabreak().noCollision().pushReaction(PushReaction.POPPED)));

    public static final DeferredBlock<Block> TOMATOES = BLOCKS.registerBlock("tomatoes",
            properties -> new TomatoCropBlock(properties.randomTicks().sound(SoundType.CROP)
                    .instabreak().noCollision().pushReaction(PushReaction.POPPED)));

    public static final DeferredBlock<Block> SAPPHIRE_ORE = BLOCKS.registerBlock("sapphire_ore",
            properties -> new DropExperienceBlock(UniformInt.of(3, 7), properties.mapColor(MapColor.STONE)
                    .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F)));

    public static final DeferredBlock<Block> DEEPSLATE_SAPPHIRE_ORE = BLOCKS.registerBlock("deepslate_sapphire_ore",
            properties -> new DropExperienceBlock(UniformInt.of(3, 7), properties.mapColor(MapColor.DEEPSLATE)
                    .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE)));

    public static final DeferredBlock<Block> SAPPHIRE_BLOCK = BLOCKS.registerBlock("sapphire_block",
            properties -> new Block(properties.mapColor(MapColor.DIAMOND)
                    .instrument(NoteBlockInstrument.BIT).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));

    // Same values as the vanilla Dragon Egg.
    public static final DeferredBlock<Block> SAPHIRA_EGG = BLOCKS.registerBlock("saphira_egg",
            properties -> new SaphiraEggBlock(properties.mapColor(MapColor.COLOR_BLUE).strength(3.0F, 9.0F)
                    .lightLevel(state -> 1).noOcclusion().pushReaction(PushReaction.POPPED)));

    // ---- Black sand family (same values as vanilla sand/sandstone family) ----

    public static final DeferredBlock<Block> BLACK_SAND = BLOCKS.registerBlock("black_sand",
            properties -> new SandBlock(new ColorRGBA(0x1B1B1B), properties.mapColor(MapColor.COLOR_BLACK)
                    .instrument(NoteBlockInstrument.SNARE).strength(0.5F).sound(SoundType.SAND)));

    // Black Desert dry grasses (same values as vanilla short_dry_grass / tall_dry_grass).
    public static final DeferredBlock<Block> SHORT_DRY_BLACK_GRASS = BLOCKS.registerBlock("short_dry_black_grass",
            properties -> new ShortDryBlackGrassBlock(properties.mapColor(MapColor.COLOR_BLACK).replaceable().noCollision()
                    .instabreak().sound(SoundType.GRASS).ignitedByLava().offsetType(BlockBehaviour.OffsetType.XYZ)
                    .pushReaction(PushReaction.POPPED)));

    public static final DeferredBlock<Block> TALL_DRY_BLACK_GRASS = BLOCKS.registerBlock("tall_dry_black_grass",
            properties -> new TallDryBlackGrassBlock(properties.mapColor(MapColor.COLOR_BLACK).replaceable().noCollision()
                    .instabreak().sound(SoundType.GRASS).ignitedByLava().offsetType(BlockBehaviour.OffsetType.XYZ)
                    .pushReaction(PushReaction.POPPED)));

    public static final DeferredBlock<Block> BLACK_SANDSTONE = BLOCKS.registerBlock("black_sandstone",
            properties -> new Block(properties.mapColor(MapColor.COLOR_BLACK)
                    .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));

    public static final DeferredBlock<Block> CHISELED_BLACK_SANDSTONE = BLOCKS.registerBlock("chiseled_black_sandstone",
            properties -> new Block(properties.mapColor(MapColor.COLOR_BLACK)
                    .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));

    public static final DeferredBlock<Block> CUT_BLACK_SANDSTONE = BLOCKS.registerBlock("cut_black_sandstone",
            properties -> new Block(properties.mapColor(MapColor.COLOR_BLACK)
                    .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));

    public static final DeferredBlock<Block> SMOOTH_BLACK_SANDSTONE = BLOCKS.registerBlock("smooth_black_sandstone",
            properties -> new Block(properties.mapColor(MapColor.COLOR_BLACK)
                    .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.0F, 6.0F)));

    public static final DeferredBlock<Block> BLACK_SANDSTONE_SLAB = BLOCKS.registerBlock("black_sandstone_slab",
            properties -> new SlabBlock(properties.mapColor(MapColor.COLOR_BLACK)
                    .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.0F, 6.0F)));

    public static final DeferredBlock<Block> CUT_BLACK_SANDSTONE_SLAB = BLOCKS.registerBlock("cut_black_sandstone_slab",
            properties -> new SlabBlock(properties.mapColor(MapColor.COLOR_BLACK)
                    .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.0F, 6.0F)));

    public static final DeferredBlock<Block> SMOOTH_BLACK_SANDSTONE_SLAB = BLOCKS.registerBlock("smooth_black_sandstone_slab",
            properties -> new SlabBlock(properties.mapColor(MapColor.COLOR_BLACK)
                    .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.0F, 6.0F)));

    public static final DeferredBlock<Block> BLACK_SANDSTONE_STAIRS = BLOCKS.registerBlock("black_sandstone_stairs",
            properties -> new StairBlock(BLACK_SANDSTONE.get().defaultBlockState(), properties.mapColor(MapColor.COLOR_BLACK)
                    .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));

    public static final DeferredBlock<Block> SMOOTH_BLACK_SANDSTONE_STAIRS = BLOCKS.registerBlock("smooth_black_sandstone_stairs",
            properties -> new StairBlock(SMOOTH_BLACK_SANDSTONE.get().defaultBlockState(), properties.mapColor(MapColor.COLOR_BLACK)
                    .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.0F, 6.0F)));

    public static final DeferredBlock<Block> BLACK_SANDSTONE_WALL = BLOCKS.registerBlock("black_sandstone_wall",
            properties -> new WallBlock(properties.mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops().strength(0.8F).forceSolidOn()));

    // ---- Marble family (variant of stone, 1.5x hardness/resistance, needs stone tool, no smooth variant) ----

    public static final DeferredBlock<Block> MARBLE = BLOCKS.registerBlock("marble",
            properties -> new Block(properties.mapColor(MapColor.QUARTZ)
                    .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.25F, 9.0F)));

    public static final DeferredBlock<Block> COBBLED_MARBLE = BLOCKS.registerBlock("cobbled_marble",
            properties -> new Block(properties.mapColor(MapColor.QUARTZ)
                    .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 9.0F)));

    public static final DeferredBlock<Block> COBBLED_MARBLE_STAIRS = BLOCKS.registerBlock("cobbled_marble_stairs",
            properties -> new StairBlock(COBBLED_MARBLE.get().defaultBlockState(), properties.mapColor(MapColor.QUARTZ)
                    .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 9.0F)));

    public static final DeferredBlock<Block> COBBLED_MARBLE_SLAB = BLOCKS.registerBlock("cobbled_marble_slab",
            properties -> new SlabBlock(properties.mapColor(MapColor.QUARTZ)
                    .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 9.0F)));

    public static final DeferredBlock<Block> COBBLED_MARBLE_WALL = BLOCKS.registerBlock("cobbled_marble_wall",
            properties -> new WallBlock(properties.mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops().strength(3.0F, 9.0F).forceSolidOn()));

    public static final DeferredBlock<Block> MARBLE_PRESSURE_PLATE = BLOCKS.registerBlock("marble_pressure_plate",
            properties -> new PressurePlateBlock(BlockSetType.STONE, properties.mapColor(MapColor.QUARTZ).forceSolidOn()
                    .instrument(NoteBlockInstrument.BASEDRUM).noCollision().strength(0.75F).pushReaction(PushReaction.POPPED)));

    public static final DeferredBlock<Block> MARBLE_BUTTON = BLOCKS.registerBlock("marble_button",
            properties -> new ButtonBlock(BlockSetType.STONE, 20, properties.noCollision().strength(0.75F).pushReaction(PushReaction.POPPED)));

    public static final DeferredBlock<Block> MARBLE_SLAB = BLOCKS.registerBlock("marble_slab",
            properties -> new SlabBlock(properties.mapColor(MapColor.QUARTZ)
                    .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 9.0F)));

    public static final DeferredBlock<Block> MARBLE_STAIRS = BLOCKS.registerBlock("marble_stairs",
            properties -> new StairBlock(MARBLE.get().defaultBlockState(), properties.mapColor(MapColor.QUARTZ)
                    .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.25F, 9.0F)));

    public static final DeferredBlock<Block> MARBLE_BRICKS = BLOCKS.registerBlock("marble_bricks",
            properties -> new Block(properties.mapColor(MapColor.QUARTZ)
                    .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.25F, 9.0F)));

    public static final DeferredBlock<Block> CHISELED_MARBLE_BRICKS = BLOCKS.registerBlock("chiseled_marble_bricks",
            properties -> new Block(properties.mapColor(MapColor.QUARTZ)
                    .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.25F, 9.0F)));

    public static final DeferredBlock<Block> CRACKED_MARBLE_BRICKS = BLOCKS.registerBlock("cracked_marble_bricks",
            properties -> new Block(properties.mapColor(MapColor.QUARTZ)
                    .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.25F, 9.0F)));

    public static final DeferredBlock<Block> MARBLE_BRICK_SLAB = BLOCKS.registerBlock("marble_brick_slab",
            properties -> new SlabBlock(properties.mapColor(MapColor.QUARTZ)
                    .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 9.0F)));

    public static final DeferredBlock<Block> MARBLE_BRICK_STAIRS = BLOCKS.registerBlock("marble_brick_stairs",
            properties -> new StairBlock(MARBLE_BRICKS.get().defaultBlockState(), properties.mapColor(MapColor.QUARTZ)
                    .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.25F, 9.0F)));

    public static final DeferredBlock<Block> MARBLE_BRICK_WALL = BLOCKS.registerBlock("marble_brick_wall",
            properties -> new WallBlock(properties.mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops().strength(2.25F, 9.0F).forceSolidOn()));

    public static final DeferredBlock<Block> MOSSY_MARBLE_BRICKS = BLOCKS.registerBlock("mossy_marble_bricks",
            properties -> new Block(properties.mapColor(MapColor.QUARTZ)
                    .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.25F, 9.0F)));

    public static final DeferredBlock<Block> MOSSY_MARBLE_BRICK_SLAB = BLOCKS.registerBlock("mossy_marble_brick_slab",
            properties -> new SlabBlock(properties.mapColor(MapColor.QUARTZ)
                    .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.25F, 9.0F)));

    public static final DeferredBlock<Block> MOSSY_MARBLE_BRICK_STAIRS = BLOCKS.registerBlock("mossy_marble_brick_stairs",
            properties -> new StairBlock(MOSSY_MARBLE_BRICKS.get().defaultBlockState(), properties.mapColor(MapColor.QUARTZ)
                    .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.25F, 9.0F)));

    public static final DeferredBlock<Block> MOSSY_MARBLE_BRICK_WALL = BLOCKS.registerBlock("mossy_marble_brick_wall",
            properties -> new WallBlock(properties.mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops().strength(2.25F, 9.0F).forceSolidOn()));

    // NeoForge's own BLOCKS.registerBlock (not this file's registerBlock wrapper): gives a properly
    // id-bound Properties like every other block here, but skips the wrapper's extra registerBlockItem
    // call - a fluid block has no BlockItem of its own, it's placed/picked up with the bucket instead,
    // same as vanilla water/lava.
    public static final DeferredBlock<OilFluidBlock> OIL = BLOCKS.registerBlock("oil",
            properties -> new OilFluidBlock(ModFluids.OIL_SOURCE.get(), properties
                    .mapColor(MapColor.COLOR_BLACK).replaceable().noCollision().strength(100.0F)
                    .pushReaction(PushReaction.POPPED).noLootTable().liquid().sound(SoundType.EMPTY)));

    // Reached only by pouring an oil bucket into a plain vanilla cauldron - never placed directly, so
    // (like OIL above) no BlockItem of its own.
    public static final DeferredBlock<OilCauldronBlock> OIL_CAULDRON = BLOCKS.registerBlock("oil_cauldron",
            properties -> new OilCauldronBlock(properties.mapColor(MapColor.METAL).strength(2.0F)
                    .requiresCorrectToolForDrops().sound(SoundType.METAL).noOcclusion().noLootTable()));

    // Mined with a pickaxe (iron or better, see the block tags) and a little softer than the vanilla
    // chest (2.5F); as blast-resistant as an iron block.
    public static final DeferredBlock<IronChestBlock> IRON_CHEST = BLOCKS.registerBlock("iron_chest",
            properties -> new IronChestBlock(properties.mapColor(MapColor.METAL).strength(2.0F, 6.0F)
                    .requiresCorrectToolForDrops().sound(SoundType.METAL)));

    public static final DeferredBlock<LargeChestBlock> GOLDEN_CHEST = BLOCKS.registerBlock("golden_chest",
            properties -> new LargeChestBlock(ChestTier.GOLDEN, properties.mapColor(MapColor.GOLD).strength(2.0F, 6.0F)
                    .requiresCorrectToolForDrops().sound(SoundType.METAL)));

    public static final DeferredBlock<LargeChestBlock> DIAMOND_CHEST = BLOCKS.registerBlock("diamond_chest",
            properties -> new LargeChestBlock(ChestTier.DIAMOND, properties.mapColor(MapColor.DIAMOND).strength(2.0F, 6.0F)
                    .requiresCorrectToolForDrops().sound(SoundType.METAL)));

    // Same strength (0.3F) and sound (GLASS) vanilla's own redstone_lamp uses, same unconditional
    // light level (15) vanilla's own glowstone uses.
    public static final DeferredBlock<Block> GREEN_SCREEN_BLOCK = BLOCKS.registerBlock("green_screen_block",
            properties -> new Block(properties.mapColor(MapColor.COLOR_GREEN)
                    .strength(0.3F).sound(SoundType.GLASS).lightLevel(state -> 15)));
}
