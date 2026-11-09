package fr.akkun.newmeriacore.manual;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

/**
 * Sends the manual to the players: when they join, and to everyone after a {@code /reload} (both fire
 * {@link OnDatapackSyncEvent}), so that it always matches the mods and datapacks of the server.
 */
public final class ManualNetworking {
    private ManualNetworking() {
    }

    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(ManualNetworking::onRegisterPayloadHandlers);
        NeoForge.EVENT_BUS.addListener(ManualNetworking::onDatapackSync);
    }

    private static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToClient(ManualPayload.TYPE, ManualPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> ReceivedManual.set(payload.chapters())));
    }

    private static void onDatapackSync(OnDatapackSyncEvent event) {
        ManualPayload payload = new ManualPayload(ManualChapters.load(event.getPlayerList().getServer().getResourceManager()));
        event.getRelevantPlayers().forEach(player -> PacketDistributor.sendToPlayer(player, payload));
    }
}
