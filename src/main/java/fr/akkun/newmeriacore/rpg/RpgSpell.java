package fr.akkun.newmeriacore.rpg;

import fr.akkun.newmeriacore.NewmeriaCore;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.Locale;

/** Order matters: this is both the unlock order shown in the Magic menu (by required Magic level)
 *  and the slice order in the spell wheel (see {@code MagicWheelScreen}) - adding a spell here (with
 *  a matching 16x16 icon, see {@link #icon()}) automatically adds it as a wheel slice, no other
 *  wiring needed. Cooldown is not per-spell anymore - see {@link SpellCasting} for the general
 *  Magic-level cooldown. */
public enum RpgSpell {
    LIGHTNING(1, "rpg.newmeriacore.spell.lightning"),
    TELEPORT(2, "rpg.newmeriacore.spell.teleport"),
    FRIENDSHIP(3, "rpg.newmeriacore.spell.friendship"),
    FIREBALL(4, "rpg.newmeriacore.spell.fireball"),
    INK_FRIEND(5, "rpg.newmeriacore.spell.ink_friend");

    private final int requiredMagicLevel;
    private final String translationKey;
    private final Identifier icon;

    RpgSpell(int requiredMagicLevel, String translationKey) {
        this.requiredMagicLevel = requiredMagicLevel;
        this.translationKey = translationKey;
        this.icon = Identifier.fromNamespaceAndPath(NewmeriaCore.MOD_ID,
                "textures/gui/spell/" + name().toLowerCase(Locale.ROOT) + ".png");
    }

    public int requiredMagicLevel() {
        return requiredMagicLevel;
    }

    public boolean isUnlocked(int magicLevel) {
        return magicLevel >= requiredMagicLevel;
    }

    public Component displayName() {
        return Component.translatable(translationKey);
    }

    /** 16x16 icon, expected at {@code assets/newmeriacore/textures/gui/spell/<name>.png}. */
    public Identifier icon() {
        return icon;
    }
}
