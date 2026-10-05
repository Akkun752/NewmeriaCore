package fr.akkun.newmeriacore.rpg;

import fr.akkun.newmeriacore.NewmeriaCore;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class RpgAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, NewmeriaCore.MOD_ID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<RpgData>> RPG_DATA = ATTACHMENT_TYPES.register("rpg_data",
            () -> AttachmentType.builder(() -> RpgData.DEFAULT)
                    .serialize(RpgData.MAP_CODEC)
                    .copyOnDeath()
                    .sync(RpgData.STREAM_CODEC)
                    .build());

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
