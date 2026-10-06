package fr.akkun.newmeriacore.creativemodetab;

import fr.akkun.newmeriacore.NewmeriaCore;
import fr.akkun.newmeriacore.item.ModItems;
import fr.akkun.newmeriacore.potion.ModPotions;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, NewmeriaCore.MOD_ID);

    public static final Supplier<CreativeModeTab> NEWMERIA_TAB = CREATIVE_MODE_TABS.register("newmeria_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.SAPPHIRE.get()))
                    .title(Component.translatable("creativetab.newmeriacore.newmeria"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.PEER);
                        output.accept(ModItems.TOMATO);
                        output.accept(ModItems.TOMATO_SEEDS);
                        output.accept(ModItems.ONION);
                        output.accept(ModItems.RICE_SHOOT);
                        output.accept(ModItems.RICE);
                        output.accept(ModItems.CHILI_PEPPER);
                        output.accept(ModItems.CHILI_RICE);
                        output.accept(ModItems.CHILI_SEEDS);
                        output.accept(ModItems.SANDWICH);
                        output.accept(ModItems.FRIED_BEEF);
                        output.accept(ModItems.FRIED_CHICKEN);
                        output.accept(ModItems.FRIED_COD);
                        output.accept(ModItems.FRIED_MUTTON);
                        output.accept(ModItems.FRIED_PORKCHOP);
                        output.accept(ModItems.FRIED_RABBIT);
                        output.accept(ModItems.FRIED_SALMON);
                        output.accept(ModItems.RAW_DUCK);
                        output.accept(ModItems.COOKED_DUCK);
                        output.accept(ModItems.FRIED_DUCK);
                        output.accept(ModItems.RAW_SAUSAGE);
                        output.accept(ModItems.SAUSAGE);
                        output.accept(ModItems.FRIED_SAUSAGE);

                        output.accept(ModPotions.createStack(Items.POTION, ModPotions.LIMONADE, ModPotions.LIMONADE_COLOR));
                        output.accept(ModPotions.createStack(Items.POTION, ModPotions.DIABOLO_SWEET_BERRIES, ModPotions.DIABOLO_SWEET_BERRIES_COLOR));
                        output.accept(ModPotions.createStack(Items.POTION, ModPotions.DIABOLO_GLOW_BERRIES, ModPotions.DIABOLO_GLOW_BERRIES_COLOR));

                        output.accept(ModItems.SAPPHIRE);
                        output.accept(ModItems.SAPPHIRE_ORE);
                        output.accept(ModItems.DEEPSLATE_SAPPHIRE_ORE);
                        output.accept(ModItems.SAPPHIRE_BLOCK);
                        output.accept(ModItems.GREEN_SCREEN_BLOCK);
                        output.accept(ModItems.OBSIDIAN_STICK);
                        output.accept(ModItems.SAPPHIRE_SWORD);
                        output.accept(ModItems.SNOW_WALKER_SPAWN_EGG);
                        output.accept(ModItems.HELL_ZOMBIE_SPAWN_EGG);
                        output.accept(ModItems.DUCK_SPAWN_EGG);

                        output.accept(ModItems.WOODEN_SPATULA);
                        output.accept(ModItems.STONE_SPATULA);
                        output.accept(ModItems.COPPER_SPATULA);
                        output.accept(ModItems.IRON_SPATULA);
                        output.accept(ModItems.GOLDEN_SPATULA);
                        output.accept(ModItems.DIAMOND_SPATULA);
                        output.accept(ModItems.NETHERITE_SPATULA);
                        output.accept(ModItems.SAPPHIRE_SPATULA);
                        output.accept(ModItems.HELL_SWORD);
                        output.accept(ModItems.HELL_SPATULA);

                        output.accept(ModItems.SAPPHIRE_SPEAR);
                        output.accept(ModItems.SAPPHIRE_PICKAXE);
                        output.accept(ModItems.SAPPHIRE_AXE);
                        output.accept(ModItems.SAPPHIRE_SHOVEL);
                        output.accept(ModItems.SAPPHIRE_HOE);

                        output.accept(ModItems.WOODEN_HAMMER);
                        output.accept(ModItems.STONE_HAMMER);
                        output.accept(ModItems.COPPER_HAMMER);
                        output.accept(ModItems.IRON_HAMMER);
                        output.accept(ModItems.GOLDEN_HAMMER);
                        output.accept(ModItems.DIAMOND_HAMMER);
                        output.accept(ModItems.NETHERITE_HAMMER);
                        output.accept(ModItems.SAPPHIRE_HAMMER);
                        output.accept(ModItems.SAPPHIRE_HELMET);
                        output.accept(ModItems.SAPPHIRE_CHESTPLATE);
                        output.accept(ModItems.SAPPHIRE_LEGGINGS);
                        output.accept(ModItems.SAPPHIRE_BOOTS);
                        output.accept(ModItems.SAPPHIRE_HORSE_ARMOR);
                        output.accept(ModItems.SAPPHIRE_NAUTILUS_ARMOR);

                        output.accept(ModItems.WOODEN_WAND);
                        output.accept(ModItems.STONE_WAND);
                        output.accept(ModItems.COPPER_WAND);
                        output.accept(ModItems.IRON_WAND);
                        output.accept(ModItems.GOLDEN_WAND);
                        output.accept(ModItems.DIAMOND_WAND);
                        output.accept(ModItems.NETHERITE_WAND);
                        output.accept(ModItems.SAPPHIRE_WAND);

                        output.accept(ModItems.OIL_BUCKET);

                        output.accept(ModItems.BLACK_SAND);
                        output.accept(ModItems.SHORT_DRY_BLACK_GRASS);
                        output.accept(ModItems.TALL_DRY_BLACK_GRASS);
                        output.accept(ModItems.BLACK_SANDSTONE);
                        output.accept(ModItems.CHISELED_BLACK_SANDSTONE);
                        output.accept(ModItems.CUT_BLACK_SANDSTONE);
                        output.accept(ModItems.SMOOTH_BLACK_SANDSTONE);
                        output.accept(ModItems.BLACK_SANDSTONE_SLAB);
                        output.accept(ModItems.CUT_BLACK_SANDSTONE_SLAB);
                        output.accept(ModItems.SMOOTH_BLACK_SANDSTONE_SLAB);
                        output.accept(ModItems.BLACK_SANDSTONE_STAIRS);
                        output.accept(ModItems.SMOOTH_BLACK_SANDSTONE_STAIRS);
                        output.accept(ModItems.BLACK_SANDSTONE_WALL);

                        output.accept(ModItems.MARBLE);
                        output.accept(ModItems.COBBLED_MARBLE);
                        output.accept(ModItems.COBBLED_MARBLE_STAIRS);
                        output.accept(ModItems.COBBLED_MARBLE_SLAB);
                        output.accept(ModItems.COBBLED_MARBLE_WALL);
                        output.accept(ModItems.MARBLE_PRESSURE_PLATE);
                        output.accept(ModItems.MARBLE_BUTTON);
                        output.accept(ModItems.MARBLE_SLAB);
                        output.accept(ModItems.MARBLE_STAIRS);
                        output.accept(ModItems.MARBLE_BRICKS);
                        output.accept(ModItems.CHISELED_MARBLE_BRICKS);
                        output.accept(ModItems.CRACKED_MARBLE_BRICKS);
                        output.accept(ModItems.MARBLE_BRICK_SLAB);
                        output.accept(ModItems.MARBLE_BRICK_STAIRS);
                        output.accept(ModItems.MARBLE_BRICK_WALL);
                        output.accept(ModItems.MOSSY_MARBLE_BRICKS);
                        output.accept(ModItems.MOSSY_MARBLE_BRICK_SLAB);
                        output.accept(ModItems.MOSSY_MARBLE_BRICK_STAIRS);
                        output.accept(ModItems.MOSSY_MARBLE_BRICK_WALL);

                        output.accept(ModItems.AKKUN_S1_TOTEM);
                        output.accept(ModItems.BATS_S1_TOTEM);
                        output.accept(ModItems.FALNIX_S1_TOTEM);
                        output.accept(ModItems.RAPHAAILE_S1_TOTEM);
                        output.accept(ModItems.WOOHTYTI_S1_TOTEM);

                        output.accept(ModItems.VASSILY_BRUSH);
                        output.accept(ModItems.SKY_SPATULA);
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
