package fr.akkun.newmeriacore;

import fr.akkun.newmeriacore.datagen.ModBlockLootTableProvider;
import fr.akkun.newmeriacore.datagen.ModBlockTagsProvider;
import fr.akkun.newmeriacore.datagen.ModDatapackProvider;
import fr.akkun.newmeriacore.datagen.ModEntityLootTableProvider;
import fr.akkun.newmeriacore.datagen.ModEquipmentAssetProvider;
import fr.akkun.newmeriacore.datagen.ModItemTagsProvider;
import fr.akkun.newmeriacore.datagen.ModModelProvider;
import fr.akkun.newmeriacore.datagen.ModRecipeProvider;
import fr.akkun.newmeriacore.datagen.ModSoundsProvider;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;

@EventBusSubscriber(modid = NewmeriaCore.MOD_ID)
public class NewmeriaCoreDataGen {
    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent.Client event) {
        event.createProvider(ModModelProvider::new);
        event.createProvider(ModEquipmentAssetProvider::new);
        event.createProvider(ModSoundsProvider::new);

        // Loot tables, recipes and the advancements unlocking them are "reloadable registry" content since 26.3.
        event.createReloadableRegistryObjects(new RegistrySetBuilder()
                .add(Registries.LOOT_TABLE, new LootTableProvider(Set.of(), List.of(
                        new LootTableProvider.SubProviderEntry(ModBlockLootTableProvider::new, LootContextParamSets.BLOCK),
                        new LootTableProvider.SubProviderEntry(ModEntityLootTableProvider::new, LootContextParamSets.ENTITY))))
                .add(RecipeProvider.asBootstrap(ModRecipeProvider::new)),
                // "minecraft" too: the stonecutting and conversion recipes of our blocks are saved under it.
                Set.of(NewmeriaCore.MOD_ID, "minecraft"));

        // Paintings, jukebox songs, damage types.
        event.createWorldRegistryObjects(ModDatapackProvider.BUILDER);

        event.createBlockAndItemTags(ModBlockTagsProvider::new, (output, lookup, blockTags) -> new ModItemTagsProvider(output, lookup));
    }
}
