package fr.akkun.newmeriacore.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import fr.akkun.newmeriacore.rpg.companion.CompanionAttachments;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.sensing.VillagerHostilesSensor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Villagers decide what to flee from by entity type alone (zombies within 8 blocks, etc.), so a
 * Zombie-shaped Ink Friend companion would scare them like any zombie. Companions never do.
 */
@Mixin(VillagerHostilesSensor.class)
public abstract class VillagerHostilesSensorMixin {
    @ModifyReturnValue(method = "isHostile", at = @At("RETURN"))
    private boolean newmeriacore$companionsAreNotHostile(boolean original, LivingEntity entity) {
        return original && entity.getExistingData(CompanionAttachments.COMPANION_DATA).isEmpty();
    }
}
