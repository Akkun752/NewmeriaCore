package fr.akkun.newmeriacore.entity;

import fr.akkun.newmeriacore.mixin.EnderDragonAccessor;
import fr.akkun.newmeriacore.rpg.SpellCasting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.EnderDragonPart;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * A boss inheriting everything from the Ender Dragon (model, phases, attacks), with twice its
 * health. The vanilla dragon leans on the End in three ways this class has to make up for, so
 * Saphira can be fought anywhere:
 *
 * <ul>
 *   <li><b>Arena</b>: vanilla lays the flight path out around the world origin (0, 0) and lands on
 *       the End's exit portal. Saphira's "fight origin" is wherever she first appeared, and her
 *       flight path is laid out around it.</li>
 *   <li><b>Boss bar</b>: the vanilla one belongs to the End's dragon fight, not to the dragon.
 *       Saphira carries her own, like the Wither. Its id is her own UUID, which is how the client
 *       recognises it and draws it in a darker blue than vanilla's (see {@code SaphiraClientEvents}).</li>
 *   <li><b>Entity type</b>: see {@code EnderDragonMixin}.</li>
 * </ul>
 *
 * In the End she can also be summoned in place of the Ender Dragon - see {@link SaphiraSummoning}.
 * She is then the dragon of the End's fight like any other: the pillars' crystals heal her and her
 * death reopens the exit portal.
 */
public class Saphira extends EnderDragon {
    // Empty until the server has picked the fight origin, on Saphira's first tick.
    private static final EntityDataAccessor<Optional<BlockPos>> DATA_FIGHT_ORIGIN =
            SynchedEntityData.defineId(Saphira.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
    // Vanilla's lowest flight altitude, and how far above the ground each ring of path nodes flies.
    private static final int MIN_NODE_Y = 73;

    // Created lazily: its id has to be Saphira's final UUID, which a load from disk only sets after construction.
    private @Nullable ServerBossEvent bossEvent;
    private boolean flightPathMoved;

    // ---- Special attacks (see #tickSpecialAttacks) ----
    private static final int PHASE_ONE_ATTACK_INTERVAL_TICKS = 15 * 20;
    private static final int PHASE_TWO_ATTACK_INTERVAL_TICKS = 20 * 20;
    /** Below this share of her max health Saphira is in phase 2. */
    private static final float PHASE_TWO_HEALTH_RATIO = 0.5F;
    // "30 blocks across", ignoring height.
    private static final double LIGHTNING_RADIUS = 15.0;
    private static final int FIREBALL_COUNT = 32;
    // "64 blocks across", ignoring height.
    private static final double FIREBALL_RADIUS = 32.0;
    private static final int FIREBALL_SPAWN_HEIGHT = 40;
    // The rain is spread over ~5 seconds rather than dropped in a single tick.
    private static final int TICKS_BETWEEN_FIREBALLS = 3;

    private int ticksUntilAttack = PHASE_ONE_ATTACK_INTERVAL_TICKS;
    private boolean inPhaseTwo;
    private boolean nextAttackIsFireballs;
    private int fireballsLeftToDrop;

    public Saphira(EntityType<? extends Saphira> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return EnderDragon.createAttributes().add(Attributes.MAX_HEALTH, 400.0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(DATA_FIGHT_ORIGIN, Optional.empty());
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        super.onSyncedDataUpdated(accessor);
        // The client only needs the origin for a few animation details (how close she is to her perch).
        if (DATA_FIGHT_ORIGIN.equals(accessor) && this.level().isClientSide()) {
            this.entityData.get(DATA_FIGHT_ORIGIN).ifPresent(this::setFightOrigin);
        }
    }

    private void chooseFightOrigin(BlockPos origin) {
        this.setFightOrigin(origin);
        this.entityData.set(DATA_FIGHT_ORIGIN, Optional.of(origin));
    }

    @Override
    public void aiStep() {
        if (!this.level().isClientSide()) {
            // Summoned through the End ritual (see SaphiraSummoning), she belongs to the End's dragon
            // fight, which has already given her its origin and sent her flying.
            boolean inEndFight = this.getDragonFight() != null;
            if (this.entityData.get(DATA_FIGHT_ORIGIN).isEmpty()) {
                // First tick of a freshly spawned Saphira (a loaded one already has both from her save).
                if (inEndFight) {
                    this.chooseFightOrigin(this.getFightOrigin());
                } else {
                    // A dragon starts out hovering in place forever: in vanilla it is the End's dragon
                    // fight that sends it flying, so we have to do it ourselves.
                    this.chooseFightOrigin(this.blockPosition());
                    this.getPhaseManager().setPhase(EnderDragonPhase.HOLDING_PATTERN);
                }
            }
            ServerBossEvent bar = this.bossEvent();
            bar.setProgress(this.getHealth() / this.getMaxHealth());
            // What the fight's own (hidden) bar would have brought: the dragon fight music and fog.
            bar.setPlayBossMusic(inEndFight);
            bar.setCreateWorldFog(inEndFight);
            this.tickSpecialAttacks((ServerLevel) this.level());
        }
        super.aiStep();
    }

    /** Same 24 nodes (three rings of 60, 40 and 20 blocks) and same links between them as vanilla,
     *  moved from around (0, 0) to around the fight origin. */
    @Override
    public int findClosestNode() {
        if (!this.flightPathMoved) {
            // Lets vanilla build its nodes and their adjacency table first.
            super.findClosestNode();
            BlockPos origin = this.getFightOrigin();
            Node[] nodes = ((EnderDragonAccessor) this).newmeriacore$getNodes();
            for (int i = 0; i < nodes.length; i++) {
                int x = nodes[i].x + origin.getX();
                int z = nodes[i].z + origin.getZ();
                int aboveGround = i >= 12 && i < 20 ? 15 : 5;
                int y = Math.max(MIN_NODE_Y, this.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new BlockPos(x, 0, z)).getY() + aboveGround);
                nodes[i] = new Node(x, y, z);
            }
            this.flightPathMoved = true;
        }
        return super.findClosestNode();
    }

    /**
     * One special attack at a time, on top of everything the Ender Dragon already does:
     * <ul>
     *   <li>Phase 1 (above half health): a lightning bolt every 15 seconds.</li>
     *   <li>Phase 2 (half health and below), every 20 seconds: a rain of fireballs and the lightning bolt, in turns -
     *       always opening the phase with the fireballs.</li>
     * </ul>
     * Healing back above half health (End Crystals) returns her to phase 1.
     */
    private void tickSpecialAttacks(ServerLevel level) {
        if (this.isDeadOrDying() || this.getPhaseManager().getCurrentPhase().getPhase() == EnderDragonPhase.DYING) {
            return;
        }
        if (this.fireballsLeftToDrop > 0 && this.tickCount % TICKS_BETWEEN_FIREBALLS == 0) {
            this.dropFireball(level);
            this.fireballsLeftToDrop--;
        }

        boolean phaseTwo = this.getHealth() <= this.getMaxHealth() * PHASE_TWO_HEALTH_RATIO;
        if (phaseTwo != this.inPhaseTwo) {
            this.inPhaseTwo = phaseTwo;
            this.nextAttackIsFireballs = phaseTwo;
        }

        if (--this.ticksUntilAttack > 0) {
            return;
        }
        this.ticksUntilAttack = this.inPhaseTwo ? PHASE_TWO_ATTACK_INTERVAL_TICKS : PHASE_ONE_ATTACK_INTERVAL_TICKS;
        if (this.inPhaseTwo && this.nextAttackIsFireballs) {
            this.fireballsLeftToDrop = FIREBALL_COUNT;
        } else {
            this.strikeLightning(level);
        }
        if (this.inPhaseTwo) {
            this.nextAttackIsFireballs = !this.nextAttackIsFireballs;
        }
    }

    /** Uniformly random point of the disc of that radius around Saphira, height ignored. */
    private Vec3 randomPointAround(double radius) {
        double distance = radius * Math.sqrt(this.random.nextDouble());
        double angle = this.random.nextDouble() * Math.PI * 2.0;
        return new Vec3(this.getX() + distance * Math.cos(angle), this.getY(), this.getZ() + distance * Math.sin(angle));
    }

    private int groundY(ServerLevel level, double x, double z) {
        return level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, BlockPos.containing(x, 0.0, z)).getY();
    }

    /** The Lightning spell's bolt: on one random player of those within range if there is any,
     *  otherwise on the ground somewhere within range. */
    private void strikeLightning(ServerLevel level) {
        List<ServerPlayer> inRange = level.getPlayers(player -> player.isAlive() && !player.isSpectator()
                && player.distanceToSqr(this.getX(), player.getY(), this.getZ()) <= LIGHTNING_RADIUS * LIGHTNING_RADIUS);
        Vec3 target;
        if (inRange.isEmpty()) {
            Vec3 point = this.randomPointAround(LIGHTNING_RADIUS);
            target = new Vec3(point.x, this.groundY(level, point.x, point.z), point.z);
        } else {
            target = inRange.get(this.random.nextInt(inRange.size())).position();
        }
        SpellCasting.strikeLightning(level, target, null);
    }

    /** One Fireball-spell fireball falling straight down from high above a random spot within range. */
    private void dropFireball(ServerLevel level) {
        Vec3 point = this.randomPointAround(FIREBALL_RADIUS);
        Vec3 from = new Vec3(point.x, this.groundY(level, point.x, point.z) + FIREBALL_SPAWN_HEIGHT, point.z);
        SpellCasting.launchFireball(level, this, from, new Vec3(0.0, -1.0, 0.0));
    }

    /** Her own attacks never hurt her - the fireballs' explosions would otherwise, like any explosion
     *  hurts an Ender Dragon. */
    @Override
    public boolean hurt(ServerLevel level, EnderDragonPart part, DamageSource source, float damage) {
        return source.getEntity() != this && super.hurt(level, part, source, damage);
    }

    private ServerBossEvent bossEvent() {
        if (this.bossEvent == null) {
            this.bossEvent = new ServerBossEvent(this.getUUID(), this.getDisplayName(), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.PROGRESS);
        }
        return this.bossEvent;
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent().addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent().removePlayer(player);
    }

    @Override
    public void setCustomName(@Nullable Component name) {
        super.setCustomName(name);
        if (this.bossEvent != null) {
            this.bossEvent.setName(this.getDisplayName());
        }
    }

    @Override
    protected void tickDeath() {
        if (this.bossEvent != null) {
            this.bossEvent.setProgress(0.0F);
        }
        super.tickDeath();
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.entityData.get(DATA_FIGHT_ORIGIN).ifPresent(origin -> output.store("fight_origin", BlockPos.CODEC, origin));
        output.putInt("ticks_until_attack", this.ticksUntilAttack);
        output.putBoolean("in_phase_two", this.inPhaseTwo);
        output.putBoolean("next_attack_is_fireballs", this.nextAttackIsFireballs);
        output.putInt("fireballs_left_to_drop", this.fireballsLeftToDrop);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        input.read("fight_origin", BlockPos.CODEC).ifPresent(this::chooseFightOrigin);
        this.ticksUntilAttack = input.getIntOr("ticks_until_attack", PHASE_ONE_ATTACK_INTERVAL_TICKS);
        this.inPhaseTwo = input.getBooleanOr("in_phase_two", false);
        this.nextAttackIsFireballs = input.getBooleanOr("next_attack_is_fireballs", false);
        this.fireballsLeftToDrop = input.getIntOr("fireballs_left_to_drop", 0);
    }
}
