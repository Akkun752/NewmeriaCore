package fr.akkun.newmeriacore.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A Chicken in everything but diet, toughness and family: same model, AI, sounds and egg laying, but
 * with 1.5x the health, its own meat drop (see the duck loot table), and it is tempted by, healed by
 * and bred with kelp instead of seeds. It only ever breeds with other Ducks - vanilla's
 * {@code Animal#canMate} already requires both partners to be the exact same class, so Ducks and
 * Chickens ignore each other.
 */
public class Duck extends Chicken {
    private static final float KELP_HEAL_AMOUNT = 2.0F;
    /** Entity tag (as in {@code /tag}) marking a Duck gilded with a gold ingot. The tag is the single
     *  source of truth and is saved with the entity; {@link #DATA_GOLDEN} only mirrors it to clients,
     *  which never receive entity tags, so the renderer can pick the golden texture. */
    public static final String GOLDEN_TAG = "golden_duck";
    private static final EntityDataAccessor<Boolean> DATA_GOLDEN = SynchedEntityData.defineId(Duck.class, EntityDataSerializers.BOOLEAN);

    public Duck(EntityType<? extends Duck> type, Level level) {
        super(type, level);
    }

    /** Natural spawn rule: like any farm animal (grass, enough light), plus sand of any colour so
     *  Ducks can spawn in the Black Desert, their only natural biome. */
    public static boolean checkDuckSpawnRules(EntityType<Duck> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        BlockState below = level.getBlockState(pos.below());
        boolean brightEnough = EntitySpawnReason.ignoresLightRequirements(spawnReason) || isBrightEnoughToSpawn(level, pos);
        return (below.is(BlockTags.ANIMALS_SPAWNABLE_ON) || below.is(BlockTags.SAND)) && brightEnough;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(DATA_GOLDEN, false);
    }

    public boolean isGolden() {
        return this.entityData.get(DATA_GOLDEN);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        // Covers every way the tag can change: the gold ingot, a reload, or a manual /tag command.
        if (!this.level().isClientSide()) {
            boolean golden = this.entityTags().contains(GOLDEN_TAG);
            if (golden != this.isGolden()) {
                this.entityData.set(DATA_GOLDEN, golden);
            }
        }
    }

    // Like the vanilla Chicken, which gets this from the fall_damage_immune entity type tag.
    @Override
    public boolean causeFallDamage(double fallDistance, float damageModifier, DamageSource damageSource) {
        return false;
    }

    // Chicken: 4 max health -> 6. (The Chicken has no armor or other resistance to scale.)
    public static AttributeSupplier.Builder createAttributes() {
        return Chicken.createAttributes().add(Attributes.MAX_HEALTH, 6.0);
    }

    // Same goals as Chicken#registerGoals (deliberately not calling super), only the tempt item differs.
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.4));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.0, this::isFood, false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(Items.KELP);
    }

    /** A gold ingot gilds an adult Duck (consumed, but not food: no healing, no breeding). A hurt
     *  Duck is healed by kelp first; only a Duck at full health uses it to breed / grow up. */
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(Items.GOLD_INGOT) && !this.isBaby() && !this.isGolden()) {
            if (!this.level().isClientSide()) {
                this.usePlayerItem(player, hand, itemStack);
                this.addTag(GOLDEN_TAG);
                this.entityData.set(DATA_GOLDEN, true);
                this.playSound(SoundEvents.ARMOR_EQUIP_GOLD.value(), 1.0F, 1.0F);
            }
            return InteractionResult.SUCCESS;
        }
        if (this.isFood(itemStack) && this.getHealth() < this.getMaxHealth()) {
            if (this.level() instanceof ServerLevel level) {
                this.usePlayerItem(player, hand, itemStack);
                this.heal(KELP_HEAL_AMOUNT);
                level.sendParticles(ParticleTypes.HEART, this.getX(), this.getY(0.75), this.getZ(), 3, 0.2, 0.2, 0.2, 0.0);
            }
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    // Always a fresh, plain Duck: being golden is never inherited.
    @Override
    public Duck getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return ModEntityTypes.DUCK.get().create(level, EntitySpawnReason.BREEDING);
    }
}
