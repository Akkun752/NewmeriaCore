package fr.akkun.newmeriacore.worldgen;

import fr.akkun.newmeriacore.NewmeriaCore;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

/** Keys of the mod's biomes. The biomes themselves are plain datapack JSON, under
 *  {@code data/newmeriacore/worldgen/biome/}. */
public class ModBiomes {
    public static final ResourceKey<Biome> BLACK_DESERT = ResourceKey.create(Registries.BIOME,
            Identifier.fromNamespaceAndPath(NewmeriaCore.MOD_ID, "black_desert"));
}
