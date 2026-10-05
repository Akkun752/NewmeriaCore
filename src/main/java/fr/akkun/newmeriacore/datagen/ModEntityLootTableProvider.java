package fr.akkun.newmeriacore.datagen;

import fr.akkun.newmeriacore.entity.ModEntityTypes;
import fr.akkun.newmeriacore.item.ModItems;
import net.minecraft.advancements.predicates.DataComponentMatchers;
import net.minecraft.advancements.predicates.EnchantmentPredicate;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.advancements.predicates.entity.EntityEquipmentPredicate;
import net.minecraft.advancements.predicates.entity.EntityFlagsPredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.predicates.DataComponentPredicates;
import net.minecraft.core.component.predicates.EnchantmentsPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * Implements the plain {@link LootTableSubProvider} interface directly instead of extending vanilla's
 * {@link net.minecraft.data.loot.EntityLootSubProvider}: that base class validates that every entity
 * type known to the game (including all vanilla mobs) has a registered loot table, which only makes
 * sense for the single provider meant to cover the whole game - not a mod-scoped provider like this one.
 */
public class ModEntityLootTableProvider implements LootTableSubProvider {
    private final HolderLookup.Provider registries;

    public ModEntityLootTableProvider(HolderLookup.Provider registries) {
        this.registries = registries;
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        ResourceKey<LootTable> snowWalkerTable = ModEntityTypes.SNOW_WALKER.get().getDefaultLootTable().orElseThrow();
        // Two independent rolls: 4% chance for 1 sapphire, and separately 1% chance for 2 more.
        output.accept(snowWalkerTable, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(ModItems.SAPPHIRE.get())
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                                .when(LootItemRandomChanceCondition.randomChance(0.04F))))
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(ModItems.SAPPHIRE.get())
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))
                                .when(LootItemRandomChanceCondition.randomChance(0.01F)))));

        ResourceKey<LootTable> duckTable = ModEntityTypes.DUCK.get().getDefaultLootTable().orElseThrow();
        // Same as the vanilla chicken table, with Raw Duck in place of Raw Chicken (dropped already
        // cooked when the Duck dies on fire, through the raw_duck -> cooked_duck smelting recipe).
        output.accept(duckTable, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(Items.FEATHER)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries, UniformGenerator.between(0.0F, 1.0F)))))
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(ModItems.RAW_DUCK.get())
                                .apply(SmeltItemFunction.smelted().when(shouldSmeltLoot()))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries, UniformGenerator.between(0.0F, 1.0F))))));
    }

    /** Copy of vanilla's {@code EntityLootSubProvider#shouldSmeltLoot}: the entity is on fire, or was
     *  killed with a weapon carrying a "smelts loot" enchantment (Fire Aspect). */
    private AnyOfCondition.Builder shouldSmeltLoot() {
        HolderLookup.RegistryLookup<Enchantment> enchantments = registries.lookupOrThrow(Registries.ENCHANTMENT);
        return AnyOfCondition.anyOf(
                LootItemEntityPropertyCondition.hasProperties(
                        LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(true))),
                LootItemEntityPropertyCondition.hasProperties(
                        LootContext.EntityTarget.DIRECT_ATTACKER,
                        EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().mainhand(
                                ItemPredicate.Builder.item().withComponents(DataComponentMatchers.Builder.components()
                                        .partial(DataComponentPredicates.ENCHANTMENTS, EnchantmentsPredicate.enchantments(List.of(
                                                new EnchantmentPredicate(enchantments.getOrThrow(EnchantmentTags.SMELTS_LOOT), MinMaxBounds.Ints.ANY))))
                                        .build())))));
    }
}
