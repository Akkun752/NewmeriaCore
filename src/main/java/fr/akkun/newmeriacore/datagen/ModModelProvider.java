package fr.akkun.newmeriacore.datagen;

import fr.akkun.newmeriacore.NewmeriaCore;
import fr.akkun.newmeriacore.block.ModBlocks;
import fr.akkun.newmeriacore.block.custom.TomatoCropBlock;
import fr.akkun.newmeriacore.item.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BeetrootBlock;
import net.minecraft.world.level.block.CropBlock;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, NewmeriaCore.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        itemModels.generateFlatItem(ModItems.PEER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.RICE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.CHILI_PEPPER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.CHILI_RICE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.SANDWICH.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.FRIED_BEEF.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.FRIED_CHICKEN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.FRIED_COD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.FRIED_MUTTON.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.FRIED_PORKCHOP.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.FRIED_RABBIT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.FRIED_SALMON.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.RAW_DUCK.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.COOKED_DUCK.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.FRIED_DUCK.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.RAW_SAUSAGE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.SAUSAGE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.FRIED_SAUSAGE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.TOMATO.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.TOMATO_SEEDS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.NEWMERIA_MANUAL.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.PINE_CONE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.HAMBURGER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.SAUSAGE_ROUGAIL.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.HOT_DOG.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.FRUIT_SALAD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.SAPPHIRE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.OBSIDIAN_STICK.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.SAPPHIRE_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        // Custom flat texture instead of vanilla's two-colour spawn egg rendering.
        itemModels.generateFlatItem(ModItems.SNOW_WALKER_SPAWN_EGG.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.HELL_ZOMBIE_SPAWN_EGG.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.DUCK_SPAWN_EGG.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.SAPHIRA_SPAWN_EGG.get(), ModelTemplates.FLAT_ITEM);

        itemModels.generateFlatItem(ModItems.WOODEN_SPATULA.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.STONE_SPATULA.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.COPPER_SPATULA.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.IRON_SPATULA.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.GOLDEN_SPATULA.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.DIAMOND_SPATULA.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.NETHERITE_SPATULA.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.SAPPHIRE_SPATULA.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.HELL_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.HELL_SPATULA.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

        // Real vanilla spear item: separate GUI icon vs. in-hand textures, dispatched by display context.
        itemModels.generateSpear(ModItems.SAPPHIRE_SPEAR.get());
        itemModels.generateFlatItem(ModItems.SAPPHIRE_PICKAXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.SAPPHIRE_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.SAPPHIRE_SHOVEL.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.SAPPHIRE_HOE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

        itemModels.generateFlatItem(ModItems.WOODEN_HAMMER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.STONE_HAMMER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.COPPER_HAMMER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.IRON_HAMMER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.GOLDEN_HAMMER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.DIAMOND_HAMMER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.NETHERITE_HAMMER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.SAPPHIRE_HAMMER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

        itemModels.generateFlatItem(ModItems.WOODEN_WAND.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.STONE_WAND.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.COPPER_WAND.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.IRON_WAND.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.GOLDEN_WAND.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.DIAMOND_WAND.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.NETHERITE_WAND.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.SAPPHIRE_WAND.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

        itemModels.generateFlatItem(ModItems.SAPPHIRE_HELMET.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.SAPPHIRE_CHESTPLATE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.SAPPHIRE_LEGGINGS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.SAPPHIRE_BOOTS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.SAPPHIRE_HORSE_ARMOR.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.SAPPHIRE_NAUTILUS_ARMOR.get(), ModelTemplates.FLAT_ITEM);

        itemModels.generateFlatItem(ModItems.OIL_BUCKET.get(), ModelTemplates.FLAT_ITEM);
        // Fluid blocks render entirely through the FluidModel/RegisterFluidModelsEvent system, not the
        // normal blockstate pipeline - but datagen still validates that every registered block has SOME
        // blockstate entry, so we give it a trivial particle-only one (same technique vanilla uses for
        // water/lava themselves).
        blockModels.createParticleOnlyBlock(ModBlocks.OIL.get(), ModBlocks.OIL.get());
        // Real model this time (unlike the fluid block above): the cauldron itself renders through the
        // normal block model pipeline, just reusing vanilla's cauldron shape/textures with our oil_still
        // texture as the "content" - same technique vanilla uses for lava_cauldron.
        TextureMapping oilCauldronMapping = TextureMapping.cauldron(TextureMapping.getBlockTexture(ModBlocks.OIL.get(), "_still"));
        blockModels.new BlockFamilyProvider(oilCauldronMapping)
                .fullBlock(ModBlocks.OIL_CAULDRON.get(), ModelTemplates.CAULDRON_FULL);

        blockModels.createCropBlock(ModBlocks.RICE_CROP.get(), CropBlock.AGE, 0, 1, 2, 3, 4, 5, 6, 7);
        blockModels.createCropBlock(ModBlocks.CHILI_CROP.get(), BeetrootBlock.AGE, 0, 1, 2, 3);
        blockModels.createCropBlock(ModBlocks.ONIONS.get(), BeetrootBlock.AGE, 0, 1, 2, 3);

        // Tomatoes: one crop model per age like createCropBlock, but the two halves of the growth use two
        // separate texture sets - budding_tomatoes_stage0-3 (ages 0-3), then tomatoes_stage0-3 (ages 4-7).
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(ModBlocks.TOMATOES.get())
                .with(PropertyDispatch.initial(CropBlock.AGE).generate(age -> {
                    String name = age < TomatoCropBlock.FRUIT_TEXTURES_START_AGE
                            ? "budding_tomatoes_stage" + age
                            : "tomatoes_stage" + (age - TomatoCropBlock.FRUIT_TEXTURES_START_AGE);
                    Identifier id = Identifier.fromNamespaceAndPath(NewmeriaCore.MOD_ID, "block/" + name);
                    return BlockModelGenerators.plainVariant(ModelTemplates.CROP.create(id, TextureMapping.crop(new Material(id)), blockModels.modelOutput));
                })));

        blockModels.createTrivialCube(ModBlocks.SAPPHIRE_ORE.get());
        blockModels.createTrivialCube(ModBlocks.DEEPSLATE_SAPPHIRE_ORE.get());
        blockModels.createTrivialCube(ModBlocks.SAPPHIRE_BLOCK.get());
        // Hand-written model (models/block/saphira_egg.json): the vanilla dragon egg shape with our texture.
        blockModels.createNonTemplateModelBlock(ModBlocks.SAPHIRA_EGG.get());
        blockModels.registerSimpleItemModel(ModBlocks.SAPHIRA_EGG.get(), ModelLocationUtils.getModelLocation(ModBlocks.SAPHIRA_EGG.get()));
        blockModels.createTrivialCube(ModBlocks.GREEN_SCREEN_BLOCK.get());
        blockModels.createChest(ModBlocks.IRON_CHEST.get(), Blocks.IRON_BLOCK, Identifier.fromNamespaceAndPath(NewmeriaCore.MOD_ID, "iron"), false);
        blockModels.createChest(ModBlocks.GOLDEN_CHEST.get(), Blocks.GOLD_BLOCK, Identifier.fromNamespaceAndPath(NewmeriaCore.MOD_ID, "gold"), false);
        blockModels.createChest(ModBlocks.DIAMOND_CHEST.get(), Blocks.DIAMOND_BLOCK, Identifier.fromNamespaceAndPath(NewmeriaCore.MOD_ID, "diamond"), false);

        blockModels.createTrivialCube(ModBlocks.BLACK_SAND.get());
        blockModels.createCrossBlockWithDefaultItem(ModBlocks.SHORT_DRY_BLACK_GRASS.get(), BlockModelGenerators.PlantType.NOT_TINTED);
        blockModels.createCrossBlockWithDefaultItem(ModBlocks.TALL_DRY_BLACK_GRASS.get(), BlockModelGenerators.PlantType.NOT_TINTED);

        // black_sandstone needs proper top/bottom/side textures (like vanilla sandstone), not the default
        // single cube_all texture the family system would otherwise use, so we build its TexturedModel by
        // hand - same for chiseled/cut/smooth below, all reusing black_sandstone's own top texture where
        // vanilla sandstone does too.
        TexturedModel blackSandstoneModel = TexturedModel.TOP_BOTTOM_WITH_WALL.get(ModBlocks.BLACK_SANDSTONE.get());
        blockModels.new BlockFamilyProvider(blackSandstoneModel.getMapping())
                .fullBlock(ModBlocks.BLACK_SANDSTONE.get(), blackSandstoneModel.getTemplate())
                .wall(ModBlocks.BLACK_SANDSTONE_WALL.get())
                .stairs(ModBlocks.BLACK_SANDSTONE_STAIRS.get())
                .slab(ModBlocks.BLACK_SANDSTONE_SLAB.get());

        TexturedModel chiseledBlackSandstoneModel = TexturedModel.COLUMN.get(ModBlocks.CHISELED_BLACK_SANDSTONE.get())
                .updateTextures(mapping -> {
                    mapping.put(TextureSlot.END, TextureMapping.getBlockTexture(ModBlocks.BLACK_SANDSTONE.get(), "_top"));
                    mapping.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(ModBlocks.CHISELED_BLACK_SANDSTONE.get()));
                });
        blockModels.new BlockFamilyProvider(chiseledBlackSandstoneModel.getMapping())
                .fullBlock(ModBlocks.CHISELED_BLACK_SANDSTONE.get(), chiseledBlackSandstoneModel.getTemplate());

        TexturedModel cutBlackSandstoneModel = TexturedModel.COLUMN.get(ModBlocks.BLACK_SANDSTONE.get())
                .updateTextures(mapping -> mapping.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(ModBlocks.CUT_BLACK_SANDSTONE.get())));
        blockModels.new BlockFamilyProvider(cutBlackSandstoneModel.getMapping())
                .fullBlock(ModBlocks.CUT_BLACK_SANDSTONE.get(), cutBlackSandstoneModel.getTemplate())
                .slab(ModBlocks.CUT_BLACK_SANDSTONE_SLAB.get());

        TexturedModel smoothBlackSandstoneModel = TexturedModel.createAllSame(TextureMapping.getBlockTexture(ModBlocks.BLACK_SANDSTONE.get(), "_top"));
        blockModels.new BlockFamilyProvider(smoothBlackSandstoneModel.getMapping())
                .fullBlock(ModBlocks.SMOOTH_BLACK_SANDSTONE.get(), smoothBlackSandstoneModel.getTemplate())
                .slab(ModBlocks.SMOOTH_BLACK_SANDSTONE_SLAB.get())
                .stairs(ModBlocks.SMOOTH_BLACK_SANDSTONE_STAIRS.get());

        // marble_bricks and cobbled_marble each get their own full family call below (base cube + their own
        // sub-variants, correctly textured from their own block) - so here we only wire up marble's own
        // slab/stairs/pressure plate/button, to avoid duplicate/mistextured models for the other two.
        blockModels.family(ModBlocks.MARBLE.get())
                .slab(ModBlocks.MARBLE_SLAB.get())
                .stairs(ModBlocks.MARBLE_STAIRS.get())
                .pressurePlate(ModBlocks.MARBLE_PRESSURE_PLATE.get())
                .button(ModBlocks.MARBLE_BUTTON.get());
        blockModels.family(ModBlocks.MARBLE_BRICKS.get()).generateFor(ModBlockFamilies.MARBLE_BRICKS);
        blockModels.family(ModBlocks.MOSSY_MARBLE_BRICKS.get()).generateFor(ModBlockFamilies.MOSSY_MARBLE_BRICKS);
        blockModels.family(ModBlocks.COBBLED_MARBLE.get()).generateFor(ModBlockFamilies.COBBLED_MARBLE);

        itemModels.generateFlatItem(ModItems.AKKUN_S1_TOTEM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.FALNIX_S1_TOTEM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.RAPHAAILE_S1_TOTEM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.WOOHTYTI_S1_TOTEM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.BATS_S1_TOTEM.get(), ModelTemplates.FLAT_ITEM);

        itemModels.generateFlatItem(ModItems.VASSILY_BRUSH.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.SKY_SPATULA.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
    }
}
