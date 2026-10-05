package fr.akkun.newmeriacore.rpg.companion;

import fr.akkun.newmeriacore.NewmeriaCore;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;

import java.util.Locale;
import java.util.function.Supplier;

/**
 * The 5 possible Ink Friend companion shapes. Each reuses the real vanilla entity type and model
 * unchanged, just with a special texture (see {@link #texture()}) - {@code CompanionManager} applies
 * the shared companion rules (health, ownership, targeting) on top, regardless of form.
 *
 * <p>Required special-texture pixel dimensions (must match the vanilla model's own UV layout):
 * WOLF 64x32, ZOMBIE 64x64, HORSE 64x64, IRON_GOLEM 128x128, NAUTILUS 128x128.
 */
public enum CompanionForm {
    WOLF(() -> EntityTypes.WOLF, "rpg.newmeriacore.companion.wolf"),
    ZOMBIE(() -> EntityTypes.ZOMBIE, "rpg.newmeriacore.companion.zombie"),
    HORSE(() -> EntityTypes.HORSE, "rpg.newmeriacore.companion.horse"),
    IRON_GOLEM(() -> EntityTypes.IRON_GOLEM, "rpg.newmeriacore.companion.iron_golem"),
    NAUTILUS(() -> EntityTypes.NAUTILUS, "rpg.newmeriacore.companion.nautilus");

    private final Supplier<EntityType<?>> entityType;
    private final String translationKey;
    private final Identifier icon;
    private final Identifier texture;

    CompanionForm(Supplier<EntityType<?>> entityType, String translationKey) {
        this.entityType = entityType;
        this.translationKey = translationKey;
        String name = name().toLowerCase(Locale.ROOT);
        this.icon = Identifier.fromNamespaceAndPath(NewmeriaCore.MOD_ID, "textures/gui/companion/" + name + ".png");
        this.texture = Identifier.fromNamespaceAndPath(NewmeriaCore.MOD_ID, "textures/entity/companion/" + name + ".png");
    }

    public EntityType<?> entityType() {
        return entityType.get();
    }

    public Component displayName() {
        return Component.translatable(translationKey);
    }

    /** 16x16 icon, expected at {@code assets/newmeriacore/textures/gui/companion/<name>.png}. */
    public Identifier icon() {
        return icon;
    }

    /** Full mob skin reusing the vanilla model's UV layout - see the class javadoc for the required
     *  pixel dimensions per form. Expected at {@code assets/newmeriacore/textures/entity/companion/<name>.png}. */
    public Identifier texture() {
        return texture;
    }
}
