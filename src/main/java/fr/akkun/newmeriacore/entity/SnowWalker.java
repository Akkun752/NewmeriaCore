package fr.akkun.newmeriacore.entity;

import fr.akkun.newmeriacore.item.ModItems;
import fr.akkun.newmeriacore.item.ModToolTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

/**
 * Game of Thrones-inspired "White Walker": a zombie-shaped raider that is hostile to every other
 * living thing (including passive animals), permanently wields a {@link ModItems#SAPPHIRE_SWORD},
 * and is immune to everything except fire, explosions, and sapphire/Hell tools.
 */
public class SnowWalker extends Zombie {
    private static final EntityDataAccessor<Integer> DATA_TEXTURE_VARIANT =
            SynchedEntityData.defineId(SnowWalker.class, EntityDataSerializers.INT);

    public SnowWalker(EntityType<? extends Zombie> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.ARMOR, 2.0);
    }

    /**
     * Chance for a spawn attempt to go through, by night and by day. A regular monster spawn attempt
     * on the surface passes the vanilla light test about 27% of the time at night (the sky light roll
     * times the night brightness roll): the night value is twice that, so Snow Walkers are twice as
     * common, relative to other monsters, as their spawn weight alone says. By day they are three
     * times rarer than by night.
     */
    private static final float NIGHT_SPAWN_CHANCE = 0.54F;
    private static final float DAYLIGHT_SPAWN_CHANCE = 0.18F;
    /** Brightest block light (torches, lamps... not the sky) a Snow Walker still spawns in. */
    private static final int MAX_SPAWN_BLOCK_LIGHT = 7;

    /** Whether a mob standing at {@code feet} stands on snow or ice of any kind: a snow layer it
     *  stands in, or a snow block, powder snow or any ice right below. */
    public static boolean isOnSnowOrIce(BlockGetter level, BlockPos feet) {
        BlockState below = level.getBlockState(feet.below());
        return level.getBlockState(feet).is(BlockTags.SNOW) || below.is(BlockTags.SNOW) || below.is(BlockTags.ICE);
    }

    /**
     * Where a Snow Walker fits: the vanilla "on ground" test, except that snow and ice count as
     * ground whatever vanilla says about them (plain ice only lets polar bears spawn, powder snow
     * nothing at all).
     */
    public static final SpawnPlacementType ON_SNOW_OR_ICE = (level, pos, type) -> {
        if (type == null || !level.getWorldBorder().isWithinBounds(pos) || !isOnSnowOrIce(level, pos)) {
            return false;
        }
        BlockPos above = pos.above();
        BlockState feetState = level.getBlockState(pos);
        BlockState headState = level.getBlockState(above);
        return NaturalSpawner.isValidEmptySpawnBlock(level, pos, feetState, feetState.getFluidState(), type)
                && NaturalSpawner.isValidEmptySpawnBlock(level, above, headState, headState.getFluidState(), type);
    };

    /**
     * Natural spawn rule. Snow Walkers can appear in any biome (see {@code SnowWalkerEvents}), on
     * snow or ice only, under the open sky, by night and - less often - by day. Daylight never
     * stops them; strong light from blocks does.
     */
    public static boolean checkSnowWalkerSpawnRules(
            EntityType<SnowWalker> type, ServerLevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random
    ) {
        // A monster spawner works anywhere.
        if (EntitySpawnReason.isSpawner(spawnReason)) {
            return true;
        }
        if (!level.canSeeSky(pos) || !isOnSnowOrIce(level, pos)) {
            return false;
        }
        if (EntitySpawnReason.ignoresLightRequirements(spawnReason)) {
            return true;
        }
        if (level.getBrightness(LightLayer.BLOCK, pos) > MAX_SPAWN_BLOCK_LIGHT) {
            return false;
        }
        return random.nextFloat() < (level.getLevel().isBrightOutside() ? DAYLIGHT_SPAWN_CHANCE : NIGHT_SPAWN_CHANCE);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(DATA_TEXTURE_VARIANT, 0);
    }

    /** 0 or 1, picked once at spawn - which of the two Snow Walker textures this individual uses. */
    public int getTextureVariant() {
        return this.getEntityData().get(DATA_TEXTURE_VARIANT);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(
            ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData groupData
    ) {
        this.getEntityData().set(DATA_TEXTURE_VARIANT, this.random.nextInt(2));
        return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
    }

    @Override
    public void setBaby(boolean baby) {
        // Snow Walkers never spawn as babies - blocking this here (rather than just in finalizeSpawn)
        // also covers using a matching spawn egg on an existing Snow Walker, which spawns "offspring"
        // through a separate code path (SpawnEggItem.spawnOffspringFromSpawnEgg) that forces
        // setBaby(true) directly and never goes through finalizeSpawn at all.
        super.setBaby(false);
    }

    @Override
    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(2, new ZombieAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        // Hostile to every living thing - players, animals, villagers, other monsters - except our own kind.
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true,
                (target, level) -> !(target instanceof SnowWalker)));
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(random, difficulty);
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.SAPPHIRE_SWORD.get()));
    }

    @Override
    public boolean isInvulnerableTo(ServerLevel level, DamageSource source) {
        if (super.isInvulnerableTo(level, source)) {
            return true;
        }
        // Burning tick damage and explosions have no attacking entity but must still land.
        if (source.is(DamageTypeTags.IS_FIRE) || source.is(DamageTypeTags.IS_EXPLOSION)) {
            return false;
        }
        // Other environmental damage (fall, drown, starve...) has no attacking entity and no
        // on-hit side effects worth preserving, so it's simply blocked outright. Damage coming
        // from an attacking entity (melee/ranged) is instead let through so on-hit enchant
        // effects (Fire Aspect, Knockback...) still trigger normally; SnowWalkerEvents then
        // zeroes out the actual damage amount unless the source is a sapphire tool.
        return source.getEntity() == null;
    }

    public boolean acceptsDamage(DamageSource source) {
        if (source.is(DamageTypeTags.IS_FIRE) || source.is(DamageTypeTags.IS_EXPLOSION)) {
            return true;
        }
        if (!(source.getEntity() instanceof LivingEntity attacker)) {
            return false;
        }
        ItemStack weapon = attacker.getMainHandItem();
        return weapon.is(ModToolTiers.SAPPHIRE_TOOLS) || weapon.is(ModToolTiers.HELL_TOOLS);
    }

    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        return super.getBaseExperienceReward(level) * 2;
    }

    @Override
    public boolean killedEntity(ServerLevel level, LivingEntity entity, DamageSource source) {
        // Skip Zombie's own "convert killed villager into a zombie villager" behaviour: we want the
        // villager to actually die so SnowWalkerEvents can spawn a new Snow Walker in its place.
        return true;
    }
}
