package fr.akkun.newmeriacore.enchantment;

import fr.akkun.newmeriacore.NewmeriaCore;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockDropsEvent;

import java.util.Optional;

/**
 * The Smelt enchantment ("Cuisson"), for pickaxes and hammers: whatever a block drops when mined
 * with the tool comes out already smelted, as a furnace would have made it - for every block the
 * tool breaks, including the extra ones of a hammer. Drops with no smelting recipe are untouched.
 *
 * <p>The enchantment itself (cost, rarity, which items take it, incompatibility with Silk Touch) is
 * plain data: {@code data/newmeriacore/enchantment/smelt.json}. Only this effect needs code, vanilla
 * having no data-driven way to smelt block drops. It costs no durability of its own.
 */
@EventBusSubscriber(modid = NewmeriaCore.MOD_ID)
public class SmeltEnchantment {
    public static final ResourceKey<Enchantment> KEY = ResourceKey.create(Registries.ENCHANTMENT,
            Identifier.fromNamespaceAndPath(NewmeriaCore.MOD_ID, "smelt"));

    @SubscribeEvent
    static void onBlockDrops(BlockDropsEvent event) {
        ItemStack tool = event.getTool();
        if (tool.isEmpty() || event.getDrops().isEmpty()) {
            return;
        }
        ServerLevel level = event.getLevel();
        Optional<Holder.Reference<Enchantment>> smelt = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(KEY);
        if (smelt.isEmpty() || EnchantmentHelper.getItemEnchantmentLevel(smelt.get(), tool) <= 0) {
            return;
        }

        for (ItemEntity drop : event.getDrops()) {
            ItemStack raw = drop.getItem();
            SingleRecipeInput input = new SingleRecipeInput(raw);
            Optional<RecipeHolder<SmeltingRecipe>> recipe = level.recipeAccess().getRecipeFor(RecipeType.SMELTING, input, level);
            if (recipe.isEmpty()) {
                continue;
            }
            ItemStack smelted = recipe.get().value().assemble(input);
            if (smelted.isEmpty()) {
                continue;
            }
            // One smelted result per raw item of the stack, like a furnace fed the whole stack.
            smelted.setCount(Math.min(smelted.getCount() * raw.getCount(), smelted.getMaxStackSize()));
            drop.setItem(smelted);
        }
    }
}
