package fr.akkun.newmerias2core.entity;

import fr.akkun.newmerias2core.NewmeriaS2Core;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, NewmeriaS2Core.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<SnowWalker>> SNOW_WALKER = ENTITY_TYPES.register("snow_walker",
            () -> EntityType.Builder.<SnowWalker>of(SnowWalker::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .eyeHeight(1.74F)
                    .clientTrackingRange(8)
                    .notInPeaceful()
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(NewmeriaS2Core.MOD_ID, "snow_walker"))));

    public static final DeferredHolder<EntityType<?>, EntityType<HellZombie>> HELL_ZOMBIE = ENTITY_TYPES.register("hell_zombie",
            () -> EntityType.Builder.<HellZombie>of(HellZombie::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .eyeHeight(1.74F)
                    .clientTrackingRange(8)
                    .notInPeaceful()
                    .fireImmune()
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(NewmeriaS2Core.MOD_ID, "hell_zombie"))));

    private static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(SNOW_WALKER.get(), SnowWalker.createAttributes().build());
        event.put(HELL_ZOMBIE.get(), HellZombie.createAttributes().build());
    }

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
        eventBus.addListener(ModEntityTypes::onEntityAttributeCreation);
    }
}
