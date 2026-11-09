package fr.akkun.newmeriacore.item;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.Event;

/**
 * Fired on the server ({@code NeoForge.EVENT_BUS}) for each extra block a Hammer mines around the one
 * the player broke (see {@link HammerEvents}), once its drops are given. Those extra blocks do not
 * fire the usual block break event, so this is how other mods (NewmeriaRPG, for its points) learn
 * about them.
 */
public class HammerExtraBlockMinedEvent extends Event {
    private final ServerLevel level;
    private final ServerPlayer player;
    private final BlockPos pos;
    private final BlockState state;

    public HammerExtraBlockMinedEvent(ServerLevel level, ServerPlayer player, BlockPos pos, BlockState state) {
        this.level = level;
        this.player = player;
        this.pos = pos;
        this.state = state;
    }

    public ServerLevel getLevel() {
        return this.level;
    }

    public ServerPlayer getPlayer() {
        return this.player;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    /** The block as it was before being mined. */
    public BlockState getState() {
        return this.state;
    }
}
