package fr.akkun.newmeriacore.entity;

import com.mojang.serialization.Codec;
import fr.akkun.newmeriacore.NewmeriaCore;
import fr.akkun.newmeriacore.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.dimension.end.EnderDragonFight;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

/**
 * Summoning Saphira in the End: the vanilla dragon respawn ritual (four End Crystals around the exit
 * portal, once the Ender Dragon is dead) with a Saphira Egg sitting on top of the portal's bedrock
 * pillar. Everything about the ritual stays vanilla (see {@code EnderDragonFightMixin} for the
 * hooks) except that the egg is consumed and the dragon that comes out of it is Saphira.
 *
 * <p>The egg has to be taken when the ritual starts rather than when the dragon appears: the first
 * thing vanilla does is rebuild the portal, which wipes whatever stands on the pillar. Whether the
 * ongoing ritual is Saphira's is therefore remembered on the level itself, and saved with it, so a
 * server restart in the middle of the ~30 second animation doesn't turn her into an Ender Dragon.
 */
public class SaphiraSummoning {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, NewmeriaCore.MOD_ID);

    private static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> SUMMON_PENDING =
            ATTACHMENT_TYPES.register("saphira_summon_pending", () -> AttachmentType.builder(() -> false)
                    .serialize(Codec.BOOL.fieldOf("pending"))
                    .build());

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }

    /** The block on top of the exit portal's pillar - where vanilla puts the Dragon Egg. */
    private static BlockPos topOfPillar(ServerLevel level, BlockPos fightOrigin) {
        return level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, EnderDragonFight.getPodiumLocation(fightOrigin)).below();
    }

    /** The four crystals are in place and the vanilla respawn ritual is about to start. */
    public static void onRitualStarted(ServerLevel level, BlockPos fightOrigin) {
        BlockPos top = topOfPillar(level, fightOrigin);
        if (level.getBlockState(top).is(ModBlocks.SAPHIRA_EGG.get())) {
            level.setBlockAndUpdate(top, Blocks.AIR.defaultBlockState());
            level.setData(SUMMON_PENDING, true);
        }
    }

    /** The ritual was interrupted (one of the four crystals got destroyed): the egg comes back. */
    public static void onRitualAborted(ServerLevel level, BlockPos fightOrigin) {
        if (level.getData(SUMMON_PENDING)) {
            level.setData(SUMMON_PENDING, false);
            level.setBlockAndUpdate(topOfPillar(level, fightOrigin).above(), ModBlocks.SAPHIRA_EGG.get().defaultBlockState());
        }
    }

    /** Saphira, as the dragon of the End's fight, just died: a new egg appears on the pillar of the
     *  exit portal (which vanilla has rebuilt by now), where the Dragon Egg appears in vanilla. */
    public static void onSaphiraKilled(ServerLevel level, BlockPos fightOrigin) {
        BlockPos top = topOfPillar(level, fightOrigin);
        // Vanilla reports a dragon's death more than once in some cases (e.g. /kill): one egg only.
        if (!level.getBlockState(top).is(ModBlocks.SAPHIRA_EGG.get())) {
            level.setBlockAndUpdate(top.above(), ModBlocks.SAPHIRA_EGG.get().defaultBlockState());
        }
    }

    /** @return whether the dragon the fight is creating right now should be Saphira (asked once per ritual). */
    public static boolean consumePending(ServerLevel level) {
        boolean pending = level.getData(SUMMON_PENDING);
        if (pending) {
            level.setData(SUMMON_PENDING, false);
        }
        return pending;
    }
}
