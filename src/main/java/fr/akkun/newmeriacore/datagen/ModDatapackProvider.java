package fr.akkun.newmeriacore.datagen;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

/** The mod's world ("datapack") registry content made by datagen: see {@code NewmeriaCoreDataGen}. */
public class ModDatapackProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.PAINTING_VARIANT, ModPaintings::bootstrap)
            .add(Registries.JUKEBOX_SONG, ModJukeboxSongs::bootstrap)
            .add(Registries.DAMAGE_TYPE, ModDamageTypes::bootstrap);
}
