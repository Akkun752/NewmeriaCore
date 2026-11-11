package fr.akkun.newmeriacore.datagen;

import fr.akkun.newmeriacore.NewmeriaCore;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;

import java.util.Optional;

/**
 * The paintings of Newmeria. Each one needs its texture at {@code assets/newmeriacore/textures/painting/<id>.png}
 * (any resolution, as long as its width/height ratio is the same as the size in blocks), its title (and
 * author, if it has one) in the language files, and, to be picked when placing a random painting, its id in
 * {@code data/minecraft/tags/painting_variant/placeable.json}.
 */
public class ModPaintings {
    public static final ResourceKey<PaintingVariant> MEMORY = create("memory");

    public static void bootstrap(BootstrapContext<PaintingVariant> context) {
        register(context, MEMORY, 4, 2, false);
    }

    private static ResourceKey<PaintingVariant> create(final String id) {
        return ResourceKey.create(Registries.PAINTING_VARIANT, Identifier.fromNamespaceAndPath(NewmeriaCore.MOD_ID, id));
    }

    private static void register(final BootstrapContext<PaintingVariant> context, final ResourceKey<PaintingVariant> key, final int width,
                                 final int height, final boolean hasAuthor
    ) {
        context.register(key, new PaintingVariant(width, height, key.identifier(),
                Optional.of(Component.translatable(key.identifier().toLanguageKey("painting", "title")).withStyle(ChatFormatting.YELLOW)),
                hasAuthor ? Optional.of(Component.translatable(key.identifier().toLanguageKey("painting", "author")).withStyle(ChatFormatting.GRAY)) : Optional.empty()));
    }
}
