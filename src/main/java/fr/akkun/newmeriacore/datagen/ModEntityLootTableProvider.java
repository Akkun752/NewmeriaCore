package fr.akkun.newmeriacore.datagen;

import fr.akkun.newmeriacore.entity.ModEntityTypes;
import fr.akkun.newmeriacore.item.ModItems;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

import java.util.stream.Stream;

public class ModEntityLootTableProvider extends EntityLootSubProvider {
    public ModEntityLootTableProvider(LootTableSubProvider.Context output) {
        super(FeatureFlags.REGISTRY.allFlags(), output);
    }

    /**
     * Only the entity types given a loot table in {@link #generate()}. The vanilla default is every
     * entity type known to the game, each of which then has to have one - which only makes sense for
     * the single provider meant to cover the whole game, not a mod-scoped provider like this one
     * (our other mobs drop nothing, or borrow a vanilla table).
     */
    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return Stream.of(ModEntityTypes.SNOW_WALKER.get(), ModEntityTypes.DUCK.get());
    }

    @Override
    public void generate() {
        // Two independent rolls: 4% chance for 1 sapphire, and separately 1% chance for 2 more.
        add(ModEntityTypes.SNOW_WALKER.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ContextIntProviders.exactly(1))
                        .add(LootItem.lootTableItem(ModItems.SAPPHIRE.get())
                                .apply(SetItemCountFunction.setCount(ContextIntProviders.exactly(1)))
                                .when(LootItemRandomChanceCondition.randomChance(0.04F))))
                .withPool(LootPool.lootPool()
                        .setRolls(ContextIntProviders.exactly(1))
                        .add(LootItem.lootTableItem(ModItems.SAPPHIRE.get())
                                .apply(SetItemCountFunction.setCount(ContextIntProviders.exactly(2)))
                                .when(LootItemRandomChanceCondition.randomChance(0.01F)))));

        // Same as the vanilla chicken table, with Raw Duck in place of Raw Chicken (dropped already
        // cooked when the Duck dies on fire, through the raw_duck -> cooked_duck smelting recipe).
        add(ModEntityTypes.DUCK.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ContextIntProviders.exactly(1))
                        .add(LootItem.lootTableItem(Items.FEATHER)
                                .apply(SetItemCountFunction.setCount(ContextIntProviders.between(0, 2)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.enchantments, ContextFloatProviders.between(0.0F, 1.0F)))))
                .withPool(LootPool.lootPool()
                        .setRolls(ContextIntProviders.exactly(1))
                        .add(LootItem.lootTableItem(ModItems.RAW_DUCK.get())
                                .apply(SmeltItemFunction.smelted().when(this.shouldSmeltLoot()))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.enchantments, ContextFloatProviders.between(0.0F, 1.0F))))));
    }
}
