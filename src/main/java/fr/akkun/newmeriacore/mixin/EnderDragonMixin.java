package fr.akkun.newmeriacore.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * Makes the Ender Dragon subclassable (see {@code Saphira}): vanilla's constructor takes an entity
 * type but then ignores it and always passes {@code ENDER_DRAGON} up, so a subclass would be saved,
 * synced and rendered as a plain Ender Dragon. For the vanilla dragon itself nothing changes.
 */
@Mixin(EnderDragon.class)
public abstract class EnderDragonMixin {
    @ModifyArg(method = "<init>", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Mob;<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V"), index = 0)
    private static EntityType<?> newmeriacore$useGivenType(EntityType<?> hardcoded, @Local(argsOnly = true) EntityType<? extends EnderDragon> type) {
        return type;
    }
}
