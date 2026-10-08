package fr.akkun.newmeriacore.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.akkun.newmeriacore.NewmeriaCore;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.feature.AbstractOreFeature;
import net.minecraft.world.level.levelgen.feature.BlockReplacement;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class ModFeatures {
    public static final DeferredRegister<MapCodec<? extends Feature>> FEATURE_TYPES =
            DeferredRegister.create(Registries.FEATURE_TYPE, NewmeriaCore.MOD_ID);

    /**
     * Highest size a {@link LargeOreFeature} accepts. A vein reaches up to {@code ceil(size / 8) +
     * ceil((size / 8 + 1) / 2)} blocks away from its origin, and a feature may only write up to 16
     * blocks outside its own chunk - with the usual {@code in_square} placement (origin anywhere in
     * the chunk) that caps the reach at 16 blocks, i.e. this size.
     */
    public static final int MAX_LARGE_ORE_SIZE = 80;

    /** The vanilla ore feature, unchanged, for veins bigger than vanilla's JSON format allows (64 is
     *  both the cap and what granite/diorite/andesite already use). Type: {@code newmeriacore:large_ore}. */
    public static class LargeOreFeature extends OreFeature {
        // Vanilla's own ore codec (AbstractOreFeature#makeCodec), minus its hard cap of 64 on the vein size.
        public static final MapCodec<LargeOreFeature> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.list(BlockReplacement.CODEC).fieldOf("targets").forGetter(AbstractOreFeature::targetStates),
                Codec.intRange(0, MAX_LARGE_ORE_SIZE).fieldOf("size").forGetter(AbstractOreFeature::size),
                Codec.floatRange(0.0F, 1.0F).fieldOf("discard_chance_on_air_exposure").forGetter(AbstractOreFeature::discardChanceOnAirExposure)
        ).apply(instance, LargeOreFeature::new));

        public LargeOreFeature(List<BlockReplacement> targetStates, int size, float discardChanceOnAirExposure) {
            super(targetStates, size, discardChanceOnAirExposure);
        }

        // OreFeature#codec is declared as returning exactly MapCodec<OreFeature>, which a subclass
        // cannot narrow to its own type: the unchecked cast is safe, this codec only ever sees LargeOreFeature.
        @Override
        @SuppressWarnings("unchecked")
        public MapCodec<OreFeature> codec() {
            return (MapCodec<OreFeature>) (MapCodec<?>) CODEC;
        }
    }

    public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<LargeOreFeature>> LARGE_ORE =
            FEATURE_TYPES.register("large_ore", () -> LargeOreFeature.CODEC);

    /**
     * Places one of the given structure templates on the ground, picked at random (list a template
     * several times to make it more likely) and randomly rotated. Unlike a real structure this is
     * plain chunk decoration, so it can be as dense as trees. The template's air blocks are not
     * placed; its lowest {@code sink} layers go below the feature's origin, into the ground.
     * Type: {@code newmeriacore:surface_template}.
     */
    public record SurfaceTemplateFeature(List<Identifier> structures, int sink) implements Feature {
        public static final MapCodec<SurfaceTemplateFeature> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ExtraCodecs.nonEmptyList(Identifier.CODEC.listOf()).fieldOf("structures").forGetter(SurfaceTemplateFeature::structures),
                Codec.intRange(0, 16).optionalFieldOf("sink", 0).forGetter(SurfaceTemplateFeature::sink)
        ).apply(instance, SurfaceTemplateFeature::new));

        @Override
        public MapCodec<SurfaceTemplateFeature> codec() {
            return CODEC;
        }

        @Override
        public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
            Rotation rotation = Rotation.getRandom(random);
            StructureTemplate template = level.getLevel().getServer().getStructureTemplateManager()
                    .getOrCreate(this.structures.get(random.nextInt(this.structures.size())));
            // A feature may only write to its own chunk and the 8 around it.
            ChunkPos chunkPos = ChunkPos.containing(origin);
            BoundingBox writable = new BoundingBox(
                    chunkPos.getMinBlockX() - 16, level.getMinY(), chunkPos.getMinBlockZ() - 16,
                    chunkPos.getMaxBlockX() + 16, level.getMaxY(), chunkPos.getMaxBlockZ() + 16);
            StructurePlaceSettings settings = new StructurePlaceSettings().setRotation(rotation).setBoundingBox(writable)
                    .setRandom(random).addProcessor(BlockIgnoreProcessor.AIR);
            Vec3i size = template.getSize(rotation);
            BlockPos lowCorner = origin.offset(-size.getX() / 2, -this.sink, -size.getZ() / 2);
            BlockPos start = template.getZeroPositionWithTransform(lowCorner, Mirror.NONE, rotation);
            return template.placeInWorld(level, start, start, settings, random, 260);
        }
    }

    public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<SurfaceTemplateFeature>> SURFACE_TEMPLATE =
            FEATURE_TYPES.register("surface_template", () -> SurfaceTemplateFeature.CODEC);

    public static void register(IEventBus eventBus) {
        FEATURE_TYPES.register(eventBus);
    }
}
