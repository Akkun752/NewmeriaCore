package fr.akkun.newmeriacore.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/** A plain sword, except it sets whatever it hits on fire for 12 seconds. */
public class HellSwordItem extends Item {
    public HellSwordItem(Properties properties) {
        super(properties);
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        super.hurtEnemy(stack, target, attacker);
        target.igniteForSeconds(12.0F);
    }
}
