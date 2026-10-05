package fr.akkun.newmeriacore.stat;

import fr.akkun.newmeriacore.NewmeriaCore;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModStats {
    public static final DeferredRegister<Identifier> CUSTOM_STATS =
            DeferredRegister.create(BuiltInRegistries.CUSTOM_STAT, NewmeriaCore.MOD_ID);

    private static Supplier<Identifier> makeCustomStat(String key) {
        Identifier statIdentifier = Identifier.fromNamespaceAndPath(NewmeriaCore.MOD_ID, key);
        return CUSTOM_STATS.register(key, () -> statIdentifier);
    }

    public static void register(IEventBus eventBus) {
        CUSTOM_STATS.register(eventBus);
    }
}
