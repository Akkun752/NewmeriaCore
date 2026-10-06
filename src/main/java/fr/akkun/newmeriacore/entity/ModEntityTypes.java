package fr.akkun.newmeriacore.entity;

import fr.akkun.newmeriacore.NewmeriaCore;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, NewmeriaCore.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<SnowWalker>> SNOW_WALKER = ENTITY_TYPES.register("snow_walker",
            () -> EntityType.Builder.<SnowWalker>of(SnowWalker::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .eyeHeight(1.74F)
                    .clientTrackingRange(8)
                    .notInPeaceful()
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(NewmeriaCore.MOD_ID, "snow_walker"))));

    public static final DeferredHolder<EntityType<?>, EntityType<HellZombie>> HELL_ZOMBIE = ENTITY_TYPES.register("hell_zombie",
            () -> EntityType.Builder.<HellZombie>of(HellZombie::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .eyeHeight(1.74F)
                    .clientTrackingRange(8)
                    .notInPeaceful()
                    .fireImmune()
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(NewmeriaCore.MOD_ID, "hell_zombie"))));

    // Same dimensions as the vanilla Chicken.
    public static final DeferredHolder<EntityType<?>, EntityType<Duck>> DUCK = ENTITY_TYPES.register("duck",
            () -> EntityType.Builder.<Duck>of(Duck::new, MobCategory.CREATURE)
                    .sized(0.4F, 0.7F)
                    .eyeHeight(0.644F)
                    .passengerAttachments(new Vec3(0.0, 0.7, -0.1))
                    .clientTrackingRange(10)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(NewmeriaCore.MOD_ID, "duck"))));

    private static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(DUCK.get(), Duck.createAttributes().build());
        event.put(SNOW_WALKER.get(), SnowWalker.createAttributes().build());
        event.put(HELL_ZOMBIE.get(), HellZombie.createAttributes().build());
    }

    private static void onRegisterSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(DUCK.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Duck::checkDuckSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(SNOW_WALKER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                SnowWalker::checkSnowWalkerSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
        eventBus.addListener(ModEntityTypes::onEntityAttributeCreation);
        eventBus.addListener(ModEntityTypes::onRegisterSpawnPlacements);
    }
}
