package fr.akkun.newmeriacore.item;

import fr.akkun.newmeriacore.NewmeriaCore;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

public class ModToolTiers {
    /** Tools that count as "made of sapphire" for gameplay checks (e.g. what the Snow Walker is weak to). */
    public static final TagKey<Item> SAPPHIRE_TOOLS =
            TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(NewmeriaCore.MOD_ID, "sapphire_tools"));

    /** What repairs a sapphire tool on an anvil / with a grindstone. */
    public static final TagKey<Item> SAPPHIRE_TOOL_MATERIALS =
            TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(NewmeriaCore.MOD_ID, "sapphire_tool_materials"));

    // Netherite's mining level/speed/attack damage bonus/enchantability, but diamond's durability.
    public static final ToolMaterial SAPPHIRE = new ToolMaterial(
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 1561, 9.0F, 4.0F, 15, SAPPHIRE_TOOL_MATERIALS);

    /** Tools that count as "made of Hell material" for gameplay checks (e.g. what the Snow Walker is weak to). */
    public static final TagKey<Item> HELL_TOOLS =
            TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(NewmeriaCore.MOD_ID, "hell_tools"));

    /** Same stats and repair item (netherite ingot) as netherite; the only difference is behavioral -
     *  Hell tools ignite whatever they hit, handled directly in the item classes. */
    public static final ToolMaterial HELL = ToolMaterial.NETHERITE;

    /** Same stats as the given vanilla tool material, but with half its durability - used by the Spatulas. */
    public static ToolMaterial halfDurability(ToolMaterial base) {
        return new ToolMaterial(base.incorrectBlocksForDrops(), Math.max(1, base.durability() / 2),
                base.speed(), base.attackDamageBonus(), base.enchantmentValue(), base.repairItems());
    }
}
