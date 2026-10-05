package fr.akkun.newmeriacore.potion;

import fr.akkun.newmeriacore.NewmeriaCore;
import fr.akkun.newmeriacore.item.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.brewing.IBrewingRecipe;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;

@EventBusSubscriber(modid = NewmeriaCore.MOD_ID)
public class ModBrewingRecipes {
    @SubscribeEvent
    public static void onRegisterBrewingRecipes(RegisterBrewingRecipesEvent event) {
        event.getBuilder().addRecipe(new IBrewingRecipe() {
            @Override
            public boolean isInput(ItemStack input) {
                return input.getItem() == Items.POTION
                        && input.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).is(Potions.WATER);
            }

            @Override
            public boolean isIngredient(ItemStack ingredient) {
                return ingredient.getItem() == ModItems.RICE.get();
            }

            @Override
            public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
                return isInput(input) && isIngredient(ingredient)
                        ? ModPotions.createStack(Items.POTION, ModPotions.LIMONADE, ModPotions.LIMONADE_COLOR)
                        : ItemStack.EMPTY;
            }
        });

        event.getBuilder().addRecipe(new IBrewingRecipe() {
            @Override
            public boolean isInput(ItemStack input) {
                return input.getItem() == Items.POTION
                        && input.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).is(ModPotions.LIMONADE);
            }

            @Override
            public boolean isIngredient(ItemStack ingredient) {
                return ingredient.getItem() == Items.SWEET_BERRIES;
            }

            @Override
            public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
                return isInput(input) && isIngredient(ingredient)
                        ? ModPotions.createStack(Items.POTION, ModPotions.DIABOLO_SWEET_BERRIES, ModPotions.DIABOLO_SWEET_BERRIES_COLOR)
                        : ItemStack.EMPTY;
            }
        });

        event.getBuilder().addRecipe(new IBrewingRecipe() {
            @Override
            public boolean isInput(ItemStack input) {
                return input.getItem() == Items.POTION
                        && input.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).is(ModPotions.LIMONADE);
            }

            @Override
            public boolean isIngredient(ItemStack ingredient) {
                return ingredient.getItem() == Items.GLOW_BERRIES;
            }

            @Override
            public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
                return isInput(input) && isIngredient(ingredient)
                        ? ModPotions.createStack(Items.POTION, ModPotions.DIABOLO_GLOW_BERRIES, ModPotions.DIABOLO_GLOW_BERRIES_COLOR)
                        : ItemStack.EMPTY;
            }
        });
    }
}
