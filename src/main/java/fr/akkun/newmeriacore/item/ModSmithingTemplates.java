package fr.akkun.newmeriacore.item;

import fr.akkun.newmeriacore.NewmeriaCore;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SmithingTemplateItem;

import java.util.List;

public class ModSmithingTemplates {
    // The Enderite Upgrade: vanilla's Netherite Upgrade template with its own tooltip texts. The
    // empty-slot icons shown on the smithing table are the ones vanilla cycles through for netherite.
    public static SmithingTemplateItem createEnderiteUpgradeTemplate(Item.Properties properties) {
        return new SmithingTemplateItem(
                text("applies_to").withStyle(ChatFormatting.BLUE),
                text("ingredients").withStyle(ChatFormatting.BLUE),
                text("base_slot_description"),
                text("additions_slot_description"),
                List.of(slot("helmet"), slot("sword"), slot("chestplate"), slot("pickaxe"), slot("leggings"), slot("axe"),
                        slot("boots"), slot("hoe"), slot("shovel"), slot("nautilus_armor"), slot("spear")),
                List.of(slot("ingot")),
                properties);
    }

    private static MutableComponent text(String key) {
        return Component.translatable(Util.makeDescriptionId("item",
                Identifier.fromNamespaceAndPath(NewmeriaCore.MOD_ID, "smithing_template.enderite_upgrade." + key)));
    }

    private static Identifier slot(String name) {
        return Identifier.withDefaultNamespace("container/slot/" + name);
    }
}
