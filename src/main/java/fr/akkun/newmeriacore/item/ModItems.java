package fr.akkun.newmeriacore.item;

import fr.akkun.newmeriacore.NewmeriaCore;
import fr.akkun.newmeriacore.block.ModBlocks;
import fr.akkun.newmeriacore.entity.ModEntityTypes;
import fr.akkun.newmeriacore.fluid.ModFluids;
import fr.akkun.newmeriacore.food.ModFoods;
import fr.akkun.newmeriacore.item.custom.TotemItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Unit;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(NewmeriaCore.MOD_ID);

    public static final DeferredItem<Item> PEER = ITEMS.registerItem("peer",
            properties -> new Item(properties.food(ModFoods.PEER)));

    public static final DeferredItem<Item> RICE_SHOOT = ITEMS.registerItem("rice_shoot",
            properties -> new BlockItem(ModBlocks.RICE_CROP.get(), properties));

    public static final DeferredItem<Item> RICE = ITEMS.registerItem("rice",
            properties -> new Item(properties.food(ModFoods.RICE)));

    public static final DeferredItem<Item> CHILI_PEPPER = ITEMS.registerItem("chili_pepper",
            properties -> new Item(properties.food(ModFoods.CHILI_PEPPER)));

    public static final DeferredItem<Item> CHILI_SEEDS = ITEMS.registerItem("chili_seeds",
            properties -> new BlockItem(ModBlocks.CHILI_CROP.get(), properties));

    public static final DeferredItem<Item> CHILI_RICE = ITEMS.registerItem("chili_rice",
            properties -> new Item(properties.stacksTo(1).food(ModFoods.CHILI_RICE, ModFoods.CHILI_RICE_CONSUMABLE).usingConvertsTo(Items.BOWL)));

    public static final DeferredItem<Item> SANDWICH = ITEMS.registerItem("sandwich",
            properties -> new Item(properties.food(ModFoods.SANDWICH)));

    public static final DeferredItem<Item> FRIED_BEEF = ITEMS.registerItem("fried_beef",
            properties -> new Item(properties.food(ModFoods.FRIED_BEEF)));
    public static final DeferredItem<Item> FRIED_CHICKEN = ITEMS.registerItem("fried_chicken",
            properties -> new Item(properties.food(ModFoods.FRIED_CHICKEN)));
    public static final DeferredItem<Item> FRIED_COD = ITEMS.registerItem("fried_cod",
            properties -> new Item(properties.food(ModFoods.FRIED_COD)));
    public static final DeferredItem<Item> FRIED_MUTTON = ITEMS.registerItem("fried_mutton",
            properties -> new Item(properties.food(ModFoods.FRIED_MUTTON)));
    public static final DeferredItem<Item> FRIED_PORKCHOP = ITEMS.registerItem("fried_porkchop",
            properties -> new Item(properties.food(ModFoods.FRIED_PORKCHOP)));
    public static final DeferredItem<Item> FRIED_RABBIT = ITEMS.registerItem("fried_rabbit",
            properties -> new Item(properties.food(ModFoods.FRIED_RABBIT)));
    public static final DeferredItem<Item> FRIED_SALMON = ITEMS.registerItem("fried_salmon",
            properties -> new Item(properties.food(ModFoods.FRIED_SALMON)));

    // Raw duck carries the same food-poisoning chance as raw chicken.
    public static final DeferredItem<Item> RAW_DUCK = ITEMS.registerItem("raw_duck",
            properties -> new Item(properties.food(ModFoods.RAW_DUCK, Consumables.CHICKEN)));
    public static final DeferredItem<Item> COOKED_DUCK = ITEMS.registerItem("cooked_duck",
            properties -> new Item(properties.food(ModFoods.COOKED_DUCK)));
    public static final DeferredItem<Item> FRIED_DUCK = ITEMS.registerItem("fried_duck",
            properties -> new Item(properties.food(ModFoods.FRIED_DUCK)));

    public static final DeferredItem<Item> RAW_SAUSAGE = ITEMS.registerItem("raw_sausage",
            properties -> new Item(properties.food(ModFoods.RAW_SAUSAGE)));
    public static final DeferredItem<Item> SAUSAGE = ITEMS.registerItem("sausage",
            properties -> new Item(properties.food(ModFoods.SAUSAGE)));
    public static final DeferredItem<Item> FRIED_SAUSAGE = ITEMS.registerItem("fried_sausage",
            properties -> new Item(properties.food(ModFoods.FRIED_SAUSAGE)));

    public static final DeferredItem<Item> TOMATO = ITEMS.registerItem("tomato",
            properties -> new Item(properties.food(ModFoods.TOMATO)));

    public static final DeferredItem<Item> TOMATO_SEEDS = ITEMS.registerItem("tomato_seeds",
            properties -> new BlockItem(ModBlocks.TOMATOES.get(), properties));

    // Planted directly, like a vanilla carrot or potato.
    public static final DeferredItem<Item> ONION = ITEMS.registerItem("onion",
            properties -> new BlockItem(ModBlocks.ONIONS.get(), properties.food(ModFoods.ONION)));

    public static final DeferredItem<Item> SAPPHIRE = ITEMS.registerItem("sapphire",
            properties -> new Item(properties));

    public static final DeferredItem<Item> SAPPHIRE_SWORD = ITEMS.registerItem("sapphire_sword",
            properties -> new Item(properties.sword(ModToolTiers.SAPPHIRE, 3.0F, -2.4F)));

    public static final DeferredItem<Item> SNOW_WALKER_SPAWN_EGG = ITEMS.registerItem("snow_walker_spawn_egg",
            properties -> new SpawnEggItem(properties.spawnEgg(ModEntityTypes.SNOW_WALKER.get())));

    public static final DeferredItem<Item> HELL_ZOMBIE_SPAWN_EGG = ITEMS.registerItem("hell_zombie_spawn_egg",
            properties -> new SpawnEggItem(properties.spawnEgg(ModEntityTypes.HELL_ZOMBIE.get())));

    public static final DeferredItem<Item> DUCK_SPAWN_EGG = ITEMS.registerItem("duck_spawn_egg",
            properties -> new SpawnEggItem(properties.spawnEgg(ModEntityTypes.DUCK.get())));

    public static final DeferredItem<Item> WOODEN_SPATULA = ITEMS.registerItem("wooden_spatula",
            properties -> new SpatulaItem(properties.sword(ModToolTiers.halfDurability(ToolMaterial.WOOD), 3.0F, -2.4F)));
    public static final DeferredItem<Item> STONE_SPATULA = ITEMS.registerItem("stone_spatula",
            properties -> new SpatulaItem(properties.sword(ModToolTiers.halfDurability(ToolMaterial.STONE), 3.0F, -2.4F)));
    public static final DeferredItem<Item> COPPER_SPATULA = ITEMS.registerItem("copper_spatula",
            properties -> new SpatulaItem(properties.sword(ModToolTiers.halfDurability(ToolMaterial.COPPER), 3.0F, -2.4F)));
    public static final DeferredItem<Item> IRON_SPATULA = ITEMS.registerItem("iron_spatula",
            properties -> new SpatulaItem(properties.sword(ModToolTiers.halfDurability(ToolMaterial.IRON), 3.0F, -2.4F)));
    public static final DeferredItem<Item> GOLDEN_SPATULA = ITEMS.registerItem("golden_spatula",
            properties -> new SpatulaItem(properties.sword(ModToolTiers.halfDurability(ToolMaterial.GOLD), 3.0F, -2.4F)));
    public static final DeferredItem<Item> DIAMOND_SPATULA = ITEMS.registerItem("diamond_spatula",
            properties -> new SpatulaItem(properties.sword(ModToolTiers.halfDurability(ToolMaterial.DIAMOND), 3.0F, -2.4F)));
    public static final DeferredItem<Item> NETHERITE_SPATULA = ITEMS.registerItem("netherite_spatula",
            properties -> new SpatulaItem(properties.fireResistant().sword(ModToolTiers.halfDurability(ToolMaterial.NETHERITE), 3.0F, -2.4F)));

    public static final DeferredItem<Item> SAPPHIRE_SPATULA = ITEMS.registerItem("sapphire_spatula",
            properties -> new SpatulaItem(properties.sword(ModToolTiers.halfDurability(ModToolTiers.SAPPHIRE), 3.0F, -2.4F)));

    // Same combat/kinetic-charge parameters as the vanilla netherite spear (matches this mod's
    // "sapphire = netherite stats" design elsewhere), just on the sapphire material.
    public static final DeferredItem<Item> SAPPHIRE_SPEAR = ITEMS.registerItem("sapphire_spear",
            properties -> new Item(properties.spear(ModToolTiers.SAPPHIRE,
                    1.15F, 1.2F, 0.4F, 2.5F, 9.0F, 5.5F, 5.1F, 8.75F, 4.6F)));
    public static final DeferredItem<Item> SAPPHIRE_PICKAXE = ITEMS.registerItem("sapphire_pickaxe",
            properties -> new Item(properties.pickaxe(ModToolTiers.SAPPHIRE, 1.0F, -2.8F)));
    public static final DeferredItem<Item> SAPPHIRE_AXE = ITEMS.registerItem("sapphire_axe",
            properties -> new AxeItem(ModToolTiers.SAPPHIRE, 5.0F, -3.0F, properties));
    public static final DeferredItem<Item> SAPPHIRE_SHOVEL = ITEMS.registerItem("sapphire_shovel",
            properties -> new ShovelItem(ModToolTiers.SAPPHIRE, 1.5F, -3.0F, properties));
    public static final DeferredItem<Item> SAPPHIRE_HOE = ITEMS.registerItem("sapphire_hoe",
            properties -> new HoeItem(ModToolTiers.SAPPHIRE, -4.0F, 0.0F, properties));

    // 3x3 AoE mining (see HammerEvents) - pickaxe-tier combat/mining stats on every material.
    public static final DeferredItem<Item> WOODEN_HAMMER = ITEMS.registerItem("wooden_hammer",
            properties -> new HammerItem(properties.pickaxe(ToolMaterial.WOOD, 1.0F, -2.8F)));
    public static final DeferredItem<Item> STONE_HAMMER = ITEMS.registerItem("stone_hammer",
            properties -> new HammerItem(properties.pickaxe(ToolMaterial.STONE, 1.0F, -2.8F)));
    public static final DeferredItem<Item> COPPER_HAMMER = ITEMS.registerItem("copper_hammer",
            properties -> new HammerItem(properties.pickaxe(ToolMaterial.COPPER, 1.0F, -2.8F)));
    public static final DeferredItem<Item> IRON_HAMMER = ITEMS.registerItem("iron_hammer",
            properties -> new HammerItem(properties.pickaxe(ToolMaterial.IRON, 1.0F, -2.8F)));
    public static final DeferredItem<Item> GOLDEN_HAMMER = ITEMS.registerItem("golden_hammer",
            properties -> new HammerItem(properties.pickaxe(ToolMaterial.GOLD, 1.0F, -2.8F)));
    public static final DeferredItem<Item> DIAMOND_HAMMER = ITEMS.registerItem("diamond_hammer",
            properties -> new HammerItem(properties.pickaxe(ToolMaterial.DIAMOND, 1.0F, -2.8F)));
    public static final DeferredItem<Item> NETHERITE_HAMMER = ITEMS.registerItem("netherite_hammer",
            properties -> new HammerItem(properties.fireResistant().pickaxe(ToolMaterial.NETHERITE, 1.0F, -2.8F)));
    public static final DeferredItem<Item> SAPPHIRE_HAMMER = ITEMS.registerItem("sapphire_hammer",
            properties -> new HammerItem(properties.pickaxe(ModToolTiers.SAPPHIRE, 1.0F, -2.8F)));

    // Casts the selected spell on right click (see WandItem); durability = same as that tier's tool.
    public static final DeferredItem<Item> WOODEN_WAND = ITEMS.registerItem("wooden_wand",
            properties -> new WandItem(properties.durability(ToolMaterial.WOOD.durability())));
    public static final DeferredItem<Item> STONE_WAND = ITEMS.registerItem("stone_wand",
            properties -> new WandItem(properties.durability(ToolMaterial.STONE.durability())));
    public static final DeferredItem<Item> COPPER_WAND = ITEMS.registerItem("copper_wand",
            properties -> new WandItem(properties.durability(ToolMaterial.COPPER.durability())));
    public static final DeferredItem<Item> IRON_WAND = ITEMS.registerItem("iron_wand",
            properties -> new WandItem(properties.durability(ToolMaterial.IRON.durability())));
    public static final DeferredItem<Item> GOLDEN_WAND = ITEMS.registerItem("golden_wand",
            properties -> new WandItem(properties.durability(ToolMaterial.GOLD.durability())));
    public static final DeferredItem<Item> DIAMOND_WAND = ITEMS.registerItem("diamond_wand",
            properties -> new WandItem(properties.durability(ToolMaterial.DIAMOND.durability())));
    public static final DeferredItem<Item> NETHERITE_WAND = ITEMS.registerItem("netherite_wand",
            properties -> new WandItem(properties.fireResistant().durability(ToolMaterial.NETHERITE.durability())));
    public static final DeferredItem<Item> SAPPHIRE_WAND = ITEMS.registerItem("sapphire_wand",
            properties -> new WandItem(properties.durability(ModToolTiers.SAPPHIRE.durability())));

    public static final DeferredItem<Item> SAPPHIRE_HELMET = ITEMS.registerItem("sapphire_helmet",
            properties -> new Item(properties.humanoidArmor(ModArmorMaterials.SAPPHIRE, ArmorType.HELMET)));
    public static final DeferredItem<Item> SAPPHIRE_CHESTPLATE = ITEMS.registerItem("sapphire_chestplate",
            properties -> new Item(properties.humanoidArmor(ModArmorMaterials.SAPPHIRE, ArmorType.CHESTPLATE)));
    public static final DeferredItem<Item> SAPPHIRE_LEGGINGS = ITEMS.registerItem("sapphire_leggings",
            properties -> new Item(properties.humanoidArmor(ModArmorMaterials.SAPPHIRE, ArmorType.LEGGINGS)));
    public static final DeferredItem<Item> SAPPHIRE_BOOTS = ITEMS.registerItem("sapphire_boots",
            properties -> new Item(properties.humanoidArmor(ModArmorMaterials.SAPPHIRE, ArmorType.BOOTS)));

    // Same ArmorMaterial as the humanoid pieces above - vanilla itself reuses one material across
    // humanoid/horse/nautilus armor (e.g. ArmorMaterials.DIAMOND for all of them), no separate stats.
    public static final DeferredItem<Item> SAPPHIRE_HORSE_ARMOR = ITEMS.registerItem("sapphire_horse_armor",
            properties -> new Item(properties.horseArmor(ModArmorMaterials.SAPPHIRE)));
    public static final DeferredItem<Item> SAPPHIRE_NAUTILUS_ARMOR = ITEMS.registerItem("sapphire_nautilus_armor",
            properties -> new Item(properties.nautilusArmor(ModArmorMaterials.SAPPHIRE)));

    public static final DeferredItem<Item> OIL_BUCKET = ITEMS.registerItem("oil_bucket",
            properties -> new BucketItem(ModFluids.OIL_SOURCE.get(), properties.craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final DeferredItem<Item> HELL_SWORD = ITEMS.registerItem("hell_sword",
            properties -> new HellSwordItem(properties.fireResistant().sword(ModToolTiers.HELL, 3.0F, -2.4F)));
    public static final DeferredItem<Item> HELL_SPATULA = ITEMS.registerItem("hell_spatula",
            properties -> new SpatulaItem(properties.fireResistant().sword(ModToolTiers.halfDurability(ModToolTiers.HELL), 3.0F, -2.4F), true));

    public static final DeferredItem<Item> SAPPHIRE_ORE = ITEMS.registerItem("sapphire_ore",
            properties -> new BlockItem(ModBlocks.SAPPHIRE_ORE.get(), properties.useBlockDescriptionPrefix()));

    public static final DeferredItem<Item> DEEPSLATE_SAPPHIRE_ORE = ITEMS.registerItem("deepslate_sapphire_ore",
            properties -> new BlockItem(ModBlocks.DEEPSLATE_SAPPHIRE_ORE.get(), properties.useBlockDescriptionPrefix()));

    public static final DeferredItem<Item> SAPPHIRE_BLOCK = ITEMS.registerItem("sapphire_block",
            properties -> new BlockItem(ModBlocks.SAPPHIRE_BLOCK.get(), properties.useBlockDescriptionPrefix()));

    public static final DeferredItem<Item> GREEN_SCREEN_BLOCK = ITEMS.registerItem("green_screen_block",
            properties -> new BlockItem(ModBlocks.GREEN_SCREEN_BLOCK.get(), properties.useBlockDescriptionPrefix()));

    public static final DeferredItem<Item> OBSIDIAN_STICK = ITEMS.registerItem("obsidian_stick",
            properties -> new Item(properties));

    public static final DeferredItem<Item> BLACK_SAND = ITEMS.registerItem("black_sand",
            properties -> new BlockItem(ModBlocks.BLACK_SAND.get(), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<Item> SHORT_DRY_BLACK_GRASS = ITEMS.registerItem("short_dry_black_grass",
            properties -> new BlockItem(ModBlocks.SHORT_DRY_BLACK_GRASS.get(), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<Item> TALL_DRY_BLACK_GRASS = ITEMS.registerItem("tall_dry_black_grass",
            properties -> new BlockItem(ModBlocks.TALL_DRY_BLACK_GRASS.get(), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<Item> BLACK_SANDSTONE = ITEMS.registerItem("black_sandstone",
            properties -> new BlockItem(ModBlocks.BLACK_SANDSTONE.get(), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<Item> CHISELED_BLACK_SANDSTONE = ITEMS.registerItem("chiseled_black_sandstone",
            properties -> new BlockItem(ModBlocks.CHISELED_BLACK_SANDSTONE.get(), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<Item> CUT_BLACK_SANDSTONE = ITEMS.registerItem("cut_black_sandstone",
            properties -> new BlockItem(ModBlocks.CUT_BLACK_SANDSTONE.get(), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<Item> SMOOTH_BLACK_SANDSTONE = ITEMS.registerItem("smooth_black_sandstone",
            properties -> new BlockItem(ModBlocks.SMOOTH_BLACK_SANDSTONE.get(), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<Item> BLACK_SANDSTONE_SLAB = ITEMS.registerItem("black_sandstone_slab",
            properties -> new BlockItem(ModBlocks.BLACK_SANDSTONE_SLAB.get(), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<Item> CUT_BLACK_SANDSTONE_SLAB = ITEMS.registerItem("cut_black_sandstone_slab",
            properties -> new BlockItem(ModBlocks.CUT_BLACK_SANDSTONE_SLAB.get(), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<Item> SMOOTH_BLACK_SANDSTONE_SLAB = ITEMS.registerItem("smooth_black_sandstone_slab",
            properties -> new BlockItem(ModBlocks.SMOOTH_BLACK_SANDSTONE_SLAB.get(), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<Item> BLACK_SANDSTONE_STAIRS = ITEMS.registerItem("black_sandstone_stairs",
            properties -> new BlockItem(ModBlocks.BLACK_SANDSTONE_STAIRS.get(), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<Item> SMOOTH_BLACK_SANDSTONE_STAIRS = ITEMS.registerItem("smooth_black_sandstone_stairs",
            properties -> new BlockItem(ModBlocks.SMOOTH_BLACK_SANDSTONE_STAIRS.get(), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<Item> BLACK_SANDSTONE_WALL = ITEMS.registerItem("black_sandstone_wall",
            properties -> new BlockItem(ModBlocks.BLACK_SANDSTONE_WALL.get(), properties.useBlockDescriptionPrefix()));

    public static final DeferredItem<Item> MARBLE = ITEMS.registerItem("marble",
            properties -> new BlockItem(ModBlocks.MARBLE.get(), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<Item> COBBLED_MARBLE = ITEMS.registerItem("cobbled_marble",
            properties -> new BlockItem(ModBlocks.COBBLED_MARBLE.get(), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<Item> COBBLED_MARBLE_STAIRS = ITEMS.registerItem("cobbled_marble_stairs",
            properties -> new BlockItem(ModBlocks.COBBLED_MARBLE_STAIRS.get(), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<Item> COBBLED_MARBLE_SLAB = ITEMS.registerItem("cobbled_marble_slab",
            properties -> new BlockItem(ModBlocks.COBBLED_MARBLE_SLAB.get(), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<Item> COBBLED_MARBLE_WALL = ITEMS.registerItem("cobbled_marble_wall",
            properties -> new BlockItem(ModBlocks.COBBLED_MARBLE_WALL.get(), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<Item> MARBLE_PRESSURE_PLATE = ITEMS.registerItem("marble_pressure_plate",
            properties -> new BlockItem(ModBlocks.MARBLE_PRESSURE_PLATE.get(), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<Item> MARBLE_BUTTON = ITEMS.registerItem("marble_button",
            properties -> new BlockItem(ModBlocks.MARBLE_BUTTON.get(), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<Item> MARBLE_SLAB = ITEMS.registerItem("marble_slab",
            properties -> new BlockItem(ModBlocks.MARBLE_SLAB.get(), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<Item> MARBLE_STAIRS = ITEMS.registerItem("marble_stairs",
            properties -> new BlockItem(ModBlocks.MARBLE_STAIRS.get(), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<Item> MARBLE_BRICKS = ITEMS.registerItem("marble_bricks",
            properties -> new BlockItem(ModBlocks.MARBLE_BRICKS.get(), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<Item> CHISELED_MARBLE_BRICKS = ITEMS.registerItem("chiseled_marble_bricks",
            properties -> new BlockItem(ModBlocks.CHISELED_MARBLE_BRICKS.get(), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<Item> CRACKED_MARBLE_BRICKS = ITEMS.registerItem("cracked_marble_bricks",
            properties -> new BlockItem(ModBlocks.CRACKED_MARBLE_BRICKS.get(), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<Item> MARBLE_BRICK_SLAB = ITEMS.registerItem("marble_brick_slab",
            properties -> new BlockItem(ModBlocks.MARBLE_BRICK_SLAB.get(), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<Item> MARBLE_BRICK_STAIRS = ITEMS.registerItem("marble_brick_stairs",
            properties -> new BlockItem(ModBlocks.MARBLE_BRICK_STAIRS.get(), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<Item> MARBLE_BRICK_WALL = ITEMS.registerItem("marble_brick_wall",
            properties -> new BlockItem(ModBlocks.MARBLE_BRICK_WALL.get(), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<Item> MOSSY_MARBLE_BRICKS = ITEMS.registerItem("mossy_marble_bricks",
            properties -> new BlockItem(ModBlocks.MOSSY_MARBLE_BRICKS.get(), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<Item> MOSSY_MARBLE_BRICK_SLAB = ITEMS.registerItem("mossy_marble_brick_slab",
            properties -> new BlockItem(ModBlocks.MOSSY_MARBLE_BRICK_SLAB.get(), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<Item> MOSSY_MARBLE_BRICK_STAIRS = ITEMS.registerItem("mossy_marble_brick_stairs",
            properties -> new BlockItem(ModBlocks.MOSSY_MARBLE_BRICK_STAIRS.get(), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<Item> MOSSY_MARBLE_BRICK_WALL = ITEMS.registerItem("mossy_marble_brick_wall",
            properties -> new BlockItem(ModBlocks.MOSSY_MARBLE_BRICK_WALL.get(), properties.useBlockDescriptionPrefix()));

    public static final DeferredItem<Item> AKKUN_S1_TOTEM = ITEMS.registerItem("akkun_s1_totem",
            properties -> new TotemItem(properties.rarity(Rarity.EPIC).stacksTo(1), "item.newmeriacore.akkun_s1_totem.description"));
    public static final DeferredItem<Item> FALNIX_S1_TOTEM = ITEMS.registerItem("falnix_s1_totem",
            properties -> new TotemItem(properties.rarity(Rarity.EPIC).stacksTo(1), "item.newmeriacore.falnix_s1_totem.description"));
    public static final DeferredItem<Item> RAPHAAILE_S1_TOTEM = ITEMS.registerItem("raphaaile_s1_totem",
            properties -> new TotemItem(properties.rarity(Rarity.EPIC).stacksTo(1), "item.newmeriacore.raphaaile_s1_totem.description"));
    public static final DeferredItem<Item> WOOHTYTI_S1_TOTEM = ITEMS.registerItem("woohtyti_s1_totem",
            properties -> new TotemItem(properties.rarity(Rarity.EPIC).stacksTo(1), "item.newmeriacore.woohtyti_s1_totem.description"));
    public static final DeferredItem<Item> BATS_S1_TOTEM = ITEMS.registerItem("bats_s1_totem",
            properties -> new TotemItem(properties.rarity(Rarity.EPIC).stacksTo(1), "item.newmeriacore.bats_s1_totem.description"));

    // Special items, uncraftable like the totems above: a Brush/Spatula that also works as a Wand
    // (their normal tool behaviour always takes priority; casting is only a fallback), and never
    // breaks - UNBREAKABLE makes ItemStack.isDamageableItem() false, so the hurtAndBreak call on a
    // successful cast (or on brushing/finish-cooking) is a no-op.
    // The Sky Spatula is also a weapon like every other Spatula, with the Golden Spatula's values.
    public static final DeferredItem<Item> VASSILY_BRUSH = ITEMS.registerItem("vassily_brush",
            properties -> new BrushWandItem(properties.rarity(Rarity.EPIC).durability(ToolMaterial.DIAMOND.durability())
                    .component(DataComponents.UNBREAKABLE, Unit.INSTANCE)));
    public static final DeferredItem<Item> SKY_SPATULA = ITEMS.registerItem("sky_spatula",
            properties -> new SpatulaWandItem(properties.rarity(Rarity.EPIC)
                    .sword(ModToolTiers.halfDurability(ToolMaterial.GOLD), 3.0F, -2.4F)
                    .component(DataComponents.UNBREAKABLE, Unit.INSTANCE)));

    public static ResourceKey<Item> getRK(Item item) {
        return BuiltInRegistries.ITEM.getResourceKey(item).get();
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
