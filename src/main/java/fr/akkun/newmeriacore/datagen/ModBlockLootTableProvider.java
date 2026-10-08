package fr.akkun.newmeriacore.datagen;

import fr.akkun.newmeriacore.block.ModBlocks;
import fr.akkun.newmeriacore.item.ModItems;
import net.minecraft.advancements.predicates.StatePropertiesPredicate;
import net.minecraft.core.Holder;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.BeetrootBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchBlock;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

import java.util.List;
import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    public ModBlockLootTableProvider(LootTableSubProvider.Context output) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), output);
    }

    @Override
    protected void generate() {
        add(ModBlocks.RICE_CROP.get(), createCropDrops(ModBlocks.RICE_CROP.get(),
                ModItems.RICE_SHOOT.get(), ModItems.RICE_SHOOT.get(), atAge(ModBlocks.RICE_CROP.get(), CropBlock.AGE, 7)));

        add(ModBlocks.CHILI_CROP.get(), createCropDrops(ModBlocks.CHILI_CROP.get(),
                ModItems.CHILI_PEPPER.get(), ModItems.CHILI_SEEDS.get(), atAge(ModBlocks.CHILI_CROP.get(), BeetrootBlock.AGE, 3)));

        // Fully grown: 2-4 onions, each count equally likely. Otherwise just the onion that was planted.
        add(ModBlocks.ONIONS.get(), ripeOrElse(ModBlocks.ONIONS.get(), BeetrootBlock.AGE, 3,
                ModItems.ONION.get(), 2, 4, ModItems.ONION.get()));
        // Fully grown: 3-5 tomatoes, each count equally likely. Otherwise just the seed back.
        add(ModBlocks.TOMATOES.get(), ripeOrElse(ModBlocks.TOMATOES.get(), CropBlock.AGE, 7,
                ModItems.TOMATO.get(), 3, 5, ModItems.TOMATO_SEEDS.get()));

        add(ModBlocks.SAPPHIRE_ORE.get(), createOreDrop(ModBlocks.SAPPHIRE_ORE.get(), ModItems.SAPPHIRE.get()));
        add(ModBlocks.DEEPSLATE_SAPPHIRE_ORE.get(), createOreDrop(ModBlocks.DEEPSLATE_SAPPHIRE_ORE.get(), ModItems.SAPPHIRE.get()));
        dropSelf(ModBlocks.SAPPHIRE_BLOCK.get());
        dropSelf(ModBlocks.SAPHIRA_EGG.get());
        // Like the vanilla chest: the dropped item keeps the name given on an anvil.
        add(ModBlocks.IRON_CHEST.get(), this::createNameableBlockEntityTable);
        add(ModBlocks.GOLDEN_CHEST.get(), this::createNameableBlockEntityTable);
        add(ModBlocks.DIAMOND_CHEST.get(), this::createNameableBlockEntityTable);

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

    protected LootTable.Builder createMultipleOreDrops(Block block, Item item, int minDrops, int maxDrops) {
        return this.createSilkTouchDispatchTable(block, this.applyExplosionDecay(block,
                LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(ContextIntProviders.between(minDrops, maxDrops)))
                        .apply(ApplyBonusCount.addOreBonusCount(this.enchantments.getOrThrow(Enchantments.FORTUNE)))));
    }

    /** A crop dropping {@code min}-{@code max} of {@code ripeDrop} (uniformly, no Fortune bonus) at
     *  {@code maxAge}, and a single {@code unripeDrop} at any earlier age. */
    private LootTable.Builder ripeOrElse(Block crop, IntegerProperty ageProperty, int maxAge, Item ripeDrop, int min, int max, Item unripeDrop) {
        return applyExplosionDecay(crop, LootTable.lootTable().withPool(LootPool.lootPool().add(
                LootItem.lootTableItem(ripeDrop)
                        .when(atAge(crop, ageProperty, maxAge))
                        .apply(SetItemCountFunction.setCount(ContextIntProviders.between(min, max)))
                        .otherwise(LootItem.lootTableItem(unripeDrop)))));
    }

    private LootItemCondition.Builder atAge(Block crop, IntegerProperty ageProperty, int age) {
        return MatchBlock.blockMatches(this.blocks, crop, StatePropertiesPredicate.Builder.properties().hasProperty(ageProperty, age));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
