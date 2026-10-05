package fr.akkun.newmeriacore.datagen;

import fr.akkun.newmeriacore.block.ModBlocks;
import fr.akkun.newmeriacore.item.ModItems;
import net.minecraft.advancements.predicates.StatePropertiesPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.BeetrootBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.List;
import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    public ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        add(ModBlocks.RICE_CROP.get(), createCropDrops(ModBlocks.RICE_CROP.get(),
                ModItems.RICE_SHOOT.get(), ModItems.RICE_SHOOT.get(), LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.RICE_CROP.get())
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, 7))));

        add(ModBlocks.CHILI_CROP.get(), createCropDrops(ModBlocks.CHILI_CROP.get(),
                ModItems.CHILI_PEPPER.get(), ModItems.CHILI_SEEDS.get(), LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.CHILI_CROP.get())
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BeetrootBlock.AGE, 3))));

        add(ModBlocks.SAPPHIRE_ORE.get(), createOreDrop(ModBlocks.SAPPHIRE_ORE.get(), ModItems.SAPPHIRE.get()));
        add(ModBlocks.DEEPSLATE_SAPPHIRE_ORE.get(), createOreDrop(ModBlocks.DEEPSLATE_SAPPHIRE_ORE.get(), ModItems.SAPPHIRE.get()));
        dropSelf(ModBlocks.SAPPHIRE_BLOCK.get());

        List<Block> selfDropping = List.of(
                ModBlocks.BLACK_SAND.get(), ModBlocks.BLACK_SANDSTONE.get(), ModBlocks.CHISELED_BLACK_SANDSTONE.get(),
                ModBlocks.CUT_BLACK_SANDSTONE.get(), ModBlocks.SMOOTH_BLACK_SANDSTONE.get(), ModBlocks.BLACK_SANDSTONE_STAIRS.get(),
                ModBlocks.SMOOTH_BLACK_SANDSTONE_STAIRS.get(), ModBlocks.BLACK_SANDSTONE_WALL.get(),
                ModBlocks.COBBLED_MARBLE.get(), ModBlocks.MARBLE_STAIRS.get(), ModBlocks.MARBLE_BRICKS.get(),
                ModBlocks.CHISELED_MARBLE_BRICKS.get(), ModBlocks.CRACKED_MARBLE_BRICKS.get(), ModBlocks.MARBLE_BRICK_STAIRS.get(),
                ModBlocks.MARBLE_BRICK_WALL.get(), ModBlocks.MOSSY_MARBLE_BRICKS.get(), ModBlocks.MOSSY_MARBLE_BRICK_STAIRS.get(),
                ModBlocks.MOSSY_MARBLE_BRICK_WALL.get(), ModBlocks.COBBLED_MARBLE_STAIRS.get(), ModBlocks.COBBLED_MARBLE_WALL.get(),
                ModBlocks.MARBLE_PRESSURE_PLATE.get(), ModBlocks.MARBLE_BUTTON.get()
        );
        selfDropping.forEach(this::dropSelf);

        add(ModBlocks.SHORT_DRY_BLACK_GRASS.get(), createShearsOrSilkTouchOnlyDrop(ModBlocks.SHORT_DRY_BLACK_GRASS.get()));
        add(ModBlocks.TALL_DRY_BLACK_GRASS.get(), createShearsOrSilkTouchOnlyDrop(ModBlocks.TALL_DRY_BLACK_GRASS.get()));

        add(ModBlocks.MARBLE.get(), createSingleItemTableWithSilkTouch(ModBlocks.MARBLE.get(), ModBlocks.COBBLED_MARBLE.get()));

        List<Block> slabs = List.of(
                ModBlocks.BLACK_SANDSTONE_SLAB.get(), ModBlocks.CUT_BLACK_SANDSTONE_SLAB.get(), ModBlocks.SMOOTH_BLACK_SANDSTONE_SLAB.get(),
                ModBlocks.MARBLE_SLAB.get(), ModBlocks.MARBLE_BRICK_SLAB.get(), ModBlocks.MOSSY_MARBLE_BRICK_SLAB.get(),
                ModBlocks.COBBLED_MARBLE_SLAB.get()
        );
        slabs.forEach(slab -> add(slab, createSlabItemTable(slab)));

        // Same "drops nothing without Silk Touch" convention vanilla's own glass blocks use.
        add(ModBlocks.GREEN_SCREEN_BLOCK.get(), createSilkTouchOnlyTable(ModBlocks.GREEN_SCREEN_BLOCK.get()));
    }

    protected LootTable.Builder createMultipleOreDrops(Block block, Item item, float minDrops, float maxDrops) {
        HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createSilkTouchDispatchTable(block, this.applyExplosionDecay(block,
                LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(minDrops, maxDrops)))
                        .apply(ApplyBonusCount.addOreBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
