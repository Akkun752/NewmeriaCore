package fr.akkun.newmeriacore.item;

import com.google.common.collect.Maps;
import fr.akkun.newmeriacore.NewmeriaCore;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;

import java.util.Map;

public class ModArmorMaterials {
    public static final ResourceKey<? extends Registry<EquipmentAsset>> ROOT_ID = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("equipment_asset"));

    public static final ResourceKey<EquipmentAsset> SAPPHIRE_ASSET_ID =
            ResourceKey.create(ROOT_ID, Identifier.fromNamespaceAndPath(NewmeriaCore.MOD_ID, "sapphire"));

    // Netherite's defense/toughness/knockback resistance/enchantability, but diamond's durability -
    // same design as ModToolTiers.SAPPHIRE.
    public static final ArmorMaterial SAPPHIRE = new ArmorMaterial(
            33, makeDefense(3, 6, 8, 3, 19), 15, SoundEvents.ARMOR_EQUIP_NETHERITE,
            3.0F, 0.1F, ModToolTiers.SAPPHIRE_TOOL_MATERIALS, SAPPHIRE_ASSET_ID);

    public static final ResourceKey<EquipmentAsset> ENDERITE_ASSET_ID =
            ResourceKey.create(ROOT_ID, Identifier.fromNamespaceAndPath(NewmeriaCore.MOD_ID, "enderite"));

    // Twice netherite's durability (37). Armor points are netherite's (already the most a full set
    // can usefully give); toughness and knockback resistance each go one step further (3.0F, 0.1F).
    public static final ArmorMaterial ENDERITE = new ArmorMaterial(
            74, makeDefense(3, 6, 8, 3, 19), 15, SoundEvents.ARMOR_EQUIP_NETHERITE,
            4.0F, 0.2F, ModToolTiers.ENDERITE_TOOL_MATERIALS, ENDERITE_ASSET_ID);

    private static Map<ArmorType, Integer> makeDefense(int boots, int legs, int chest, int helm, int body) {
        return Maps.newEnumMap(
                Map.of(ArmorType.BOOTS, boots, ArmorType.LEGGINGS, legs, ArmorType.CHESTPLATE, chest, ArmorType.HELMET, helm, ArmorType.BODY, body)
        );
    }
}
