package fr.akkun.newmeriacore.datagen;

import fr.akkun.newmeriacore.NewmeriaCore;
import fr.akkun.newmeriacore.item.ModItems;
import fr.akkun.newmeriacore.item.ModToolTiers;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, NewmeriaCore.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        tag(ItemTags.STONE_TOOL_MATERIALS).add(ModItems.getRK(ModItems.COBBLED_MARBLE.get()));
        tag(ItemTags.SMELTS_TO_GLASS).add(ModItems.getRK(ModItems.BLACK_SAND.get()));

        tag(ModToolTiers.SAPPHIRE_TOOL_MATERIALS).add(ModItems.getRK(ModItems.SAPPHIRE.get()));
        tag(ModToolTiers.SAPPHIRE_TOOLS).add(
                ModItems.getRK(ModItems.SAPPHIRE_SWORD.get()), ModItems.getRK(ModItems.SAPPHIRE_SPATULA.get()),
                ModItems.getRK(ModItems.SAPPHIRE_SPEAR.get()), ModItems.getRK(ModItems.SAPPHIRE_PICKAXE.get()),
                ModItems.getRK(ModItems.SAPPHIRE_AXE.get()), ModItems.getRK(ModItems.SAPPHIRE_SHOVEL.get()),
                ModItems.getRK(ModItems.SAPPHIRE_HOE.get()), ModItems.getRK(ModItems.SAPPHIRE_HAMMER.get()),
                ModItems.getRK(ModItems.SAPPHIRE_WAND.get()));

        tag(ModToolTiers.HELL_TOOLS).add(ModItems.getRK(ModItems.HELL_SWORD.get()), ModItems.getRK(ModItems.HELL_SPATULA.get()));

        tag(ItemTags.SPEARS).add(ModItems.getRK(ModItems.SAPPHIRE_SPEAR.get()));

        // Enchantments follow these vanilla tags: a Spatula takes everything a sword does, a Hammer
        // everything a pickaxe does.
        tag(ItemTags.SWORDS).add(
                ModItems.getRK(ModItems.WOODEN_SPATULA.get()), ModItems.getRK(ModItems.STONE_SPATULA.get()),
                ModItems.getRK(ModItems.COPPER_SPATULA.get()), ModItems.getRK(ModItems.IRON_SPATULA.get()),
                ModItems.getRK(ModItems.GOLDEN_SPATULA.get()), ModItems.getRK(ModItems.DIAMOND_SPATULA.get()),
                ModItems.getRK(ModItems.NETHERITE_SPATULA.get()), ModItems.getRK(ModItems.SAPPHIRE_SPATULA.get()),
                ModItems.getRK(ModItems.HELL_SPATULA.get()), ModItems.getRK(ModItems.SKY_SPATULA.get()));
        tag(ItemTags.PICKAXES).add(
                ModItems.getRK(ModItems.WOODEN_HAMMER.get()), ModItems.getRK(ModItems.STONE_HAMMER.get()),
                ModItems.getRK(ModItems.COPPER_HAMMER.get()), ModItems.getRK(ModItems.IRON_HAMMER.get()),
                ModItems.getRK(ModItems.GOLDEN_HAMMER.get()), ModItems.getRK(ModItems.DIAMOND_HAMMER.get()),
                ModItems.getRK(ModItems.NETHERITE_HAMMER.get()), ModItems.getRK(ModItems.SAPPHIRE_HAMMER.get()));
        // Wands only wear out: Unbreaking and Mending (and, since vanilla ties it to the same tag,
        // Curse of Vanishing).
        tag(ItemTags.DURABILITY_ENCHANTABLE).add(
                ModItems.getRK(ModItems.WOODEN_WAND.get()), ModItems.getRK(ModItems.STONE_WAND.get()),
                ModItems.getRK(ModItems.COPPER_WAND.get()), ModItems.getRK(ModItems.IRON_WAND.get()),
                ModItems.getRK(ModItems.GOLDEN_WAND.get()), ModItems.getRK(ModItems.DIAMOND_WAND.get()),
                ModItems.getRK(ModItems.NETHERITE_WAND.get()), ModItems.getRK(ModItems.SAPPHIRE_WAND.get()));
    }
}
