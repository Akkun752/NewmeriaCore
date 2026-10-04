package fr.akkun.newmerias2core.entity;

import fr.akkun.newmerias2core.item.ModItems;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * A Nether-born Zombie: 30 HP, fire-immune (see the {@code fireImmune()} entity type flag in
 * {@link ModEntityTypes}), almost always wielding a Hell Sword (a 1% chance of a Hell Spatula
 * instead).
 */
public class HellZombie extends Zombie {
    public HellZombie(EntityType<? extends Zombie> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes().add(Attributes.MAX_HEALTH, 30.0);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(random, difficulty);
        boolean spatula = random.nextInt(100) == 0;
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(spatula ? ModItems.HELL_SPATULA.get() : ModItems.HELL_SWORD.get()));
        // Overrides whatever chance vanilla Zombie's own super call just rolled for this slot.
        this.setDropChance(EquipmentSlot.MAINHAND, 0.05F);
    }
}
