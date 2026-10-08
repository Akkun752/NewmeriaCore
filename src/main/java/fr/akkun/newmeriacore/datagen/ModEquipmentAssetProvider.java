package fr.akkun.newmeriacore.datagen;

import fr.akkun.newmeriacore.NewmeriaCore;
import fr.akkun.newmeriacore.item.ModArmorMaterials;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class ModEquipmentAssetProvider implements DataProvider {
    private final PackOutput.PathProvider pathProvider;

    public ModEquipmentAssetProvider(PackOutput packOutput) {
        this.pathProvider = packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "equipment");
    }

    private static void bootstrap(BiConsumer<ResourceKey<EquipmentAsset>, EquipmentClientInfo> output) {
        Identifier sapphireTexture = Identifier.fromNamespaceAndPath(NewmeriaCore.MOD_ID, "sapphire");
        // Same single asset bundles every layer type, exactly like vanilla's own equipment/diamond.json
        // (humanoid + horse_body + nautilus_body all in one file, reusing the same texture id).
        output.accept(ModArmorMaterials.SAPPHIRE_ASSET_ID, EquipmentClientInfo.builder()
                .addHumanoidLayers(sapphireTexture)
                .addLayers(EquipmentClientInfo.LayerType.HORSE_BODY, new EquipmentClientInfo.Layer(sapphireTexture))
                .addLayers(EquipmentClientInfo.LayerType.NAUTILUS_BODY, new EquipmentClientInfo.Layer(sapphireTexture))
                .build());

        Identifier enderiteTexture = Identifier.fromNamespaceAndPath(NewmeriaCore.MOD_ID, "enderite");
        output.accept(ModArmorMaterials.ENDERITE_ASSET_ID, EquipmentClientInfo.builder()
                .addHumanoidLayers(enderiteTexture)
                .addLayers(EquipmentClientInfo.LayerType.HORSE_BODY, new EquipmentClientInfo.Layer(enderiteTexture))
                .addLayers(EquipmentClientInfo.LayerType.NAUTILUS_BODY, new EquipmentClientInfo.Layer(enderiteTexture))
                .build());
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        Map<ResourceKey<EquipmentAsset>, EquipmentClientInfo> equipmentAssets = new HashMap<>();
        bootstrap((id, asset) -> {
            if (equipmentAssets.putIfAbsent(id, asset) != null) {
                throw new IllegalStateException("Tried to register equipment asset twice for id: " + id);
            }
        });
        return DataProvider.saveAll(cache, EquipmentClientInfo.CODEC, this.pathProvider::json, equipmentAssets);
    }

    @Override
    public String getName() {
        return "TutorialMod Equipment Definitions";
    }
}
