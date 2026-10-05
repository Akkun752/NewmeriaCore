package fr.akkun.newmeriacore.datagen;

import fr.akkun.newmeriacore.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.concurrent.CompletableFuture;

public class ModDataMapProvider extends DataMapProvider {
    public ModDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        // Same values as vanilla short_dry_grass / tall_dry_grass: half an item smelted, 30% compost chance.
        builder(NeoForgeDataMaps.FURNACE_FUELS)
                .add(ModItems.SHORT_DRY_BLACK_GRASS, new FurnaceFuel(100), false)
                .add(ModItems.TALL_DRY_BLACK_GRASS, new FurnaceFuel(100), false);
        builder(NeoForgeDataMaps.COMPOSTABLES)
                .add(ModItems.SHORT_DRY_BLACK_GRASS, new Compostable(0.3F), false)
                .add(ModItems.TALL_DRY_BLACK_GRASS, new Compostable(0.3F), false);
    }
}
