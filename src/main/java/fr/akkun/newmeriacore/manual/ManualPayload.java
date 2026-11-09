package fr.akkun.newmeriacore.manual;

import fr.akkun.newmeriacore.NewmeriaCore;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Server -> client: the chapters of the Newmeria Manual, for each language (see {@link ManualChapters#load}). */
public record ManualPayload(Map<String, List<String>> chapters) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ManualPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(NewmeriaCore.MOD_ID, "manual"));

    // A chapter can be longer than the 32767 characters of the default string codec.
    private static final int MAX_CHAPTER_LENGTH = 1 << 18;

    public static final StreamCodec<ByteBuf, ManualPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(HashMap::new, ByteBufCodecs.STRING_UTF8,
                    ByteBufCodecs.stringUtf8(MAX_CHAPTER_LENGTH).apply(ByteBufCodecs.list())),
            ManualPayload::chapters,
            ManualPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
