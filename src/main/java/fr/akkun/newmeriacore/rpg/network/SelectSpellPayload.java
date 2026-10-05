package fr.akkun.newmeriacore.rpg.network;

import fr.akkun.newmeriacore.NewmeriaCore;
import fr.akkun.newmeriacore.rpg.RpgData;
import fr.akkun.newmeriacore.rpg.RpgSpell;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/** {@code spell} is the ordinal of the {@link RpgSpell} to select, or {@link RpgData#NO_SPELL} to deselect. */
public record SelectSpellPayload(int spell) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SelectSpellPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(NewmeriaCore.MOD_ID, "select_spell"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SelectSpellPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, SelectSpellPayload::spell,
            SelectSpellPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
