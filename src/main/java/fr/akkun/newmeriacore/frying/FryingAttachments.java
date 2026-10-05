package fr.akkun.newmeriacore.frying;

import fr.akkun.newmeriacore.NewmeriaCore;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class FryingAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, NewmeriaCore.MOD_ID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<OilFryProgress>> OIL_FRY_PROGRESS =
            ATTACHMENT_TYPES.register("oil_fry_progress",
                    () -> AttachmentType.builder(() -> OilFryProgress.NONE)
                            .serialize(OilFryProgress.CODEC)
                            .build());

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
