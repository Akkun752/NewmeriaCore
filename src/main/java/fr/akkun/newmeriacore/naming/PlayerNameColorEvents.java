package fr.akkun.newmeriacore.naming;

import fr.akkun.newmeriacore.NewmeriaCore;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

/** Colors a player's name in chat ({@code NameFormat}) and in the tab list ({@code
 *  TabListNameFormat}) according to {@link PlayerColors}, if they have a color set. */
@EventBusSubscriber(modid = NewmeriaCore.MOD_ID)
public class PlayerNameColorEvents {
    @SubscribeEvent
    public static void onNameFormat(PlayerEvent.NameFormat event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        ChatFormatting color = PlayerColors.get(player.level().getServer(), player.getGameProfile().name());
        if (color != null) {
            event.setDisplayname(event.getUsername().copy().withStyle(color));
        }
    }

    @SubscribeEvent
    public static void onTabListNameFormat(PlayerEvent.TabListNameFormat event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        ChatFormatting color = PlayerColors.get(player.level().getServer(), player.getGameProfile().name());
        if (color != null) {
            event.setDisplayName(Component.literal(player.getGameProfile().name()).withStyle(color));
        }
    }
}
