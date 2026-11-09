package fr.akkun.newmeriacore.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.neoforged.bus.api.Event;

/**
 * Fired on the server ({@code NeoForge.EVENT_BUS}) when a player right-clicks with a wand (see
 * {@link WandItem}) and nothing else used the click. NewmeriaCore gives wands no effect of its own:
 * a mod listening to this event (NewmeriaRPG) casts the player's spell and calls {@link #setCast()}.
 * The wand only loses durability when a spell was actually cast.
 */
public class WandCastEvent extends Event {
    private final ServerPlayer player;
    private final InteractionHand hand;
    private boolean cast;

    public WandCastEvent(ServerPlayer player, InteractionHand hand) {
        this.player = player;
        this.hand = hand;
    }

    public ServerPlayer getPlayer() {
        return this.player;
    }

    public InteractionHand getHand() {
        return this.hand;
    }

    /** To call once a spell has been cast: the wand then loses durability. */
    public void setCast() {
        this.cast = true;
    }

    public boolean wasCast() {
        return this.cast;
    }
}
