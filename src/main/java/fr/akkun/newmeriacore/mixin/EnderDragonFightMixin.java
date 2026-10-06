package fr.akkun.newmeriacore.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fr.akkun.newmeriacore.entity.ModEntityTypes;
import fr.akkun.newmeriacore.entity.Saphira;
import fr.akkun.newmeriacore.entity.SaphiraSummoning;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.end.EnderDragonFight;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

/** The hooks into the End's dragon fight that {@link SaphiraSummoning} needs - see there. */
@Mixin(EnderDragonFight.class)
public abstract class EnderDragonFightMixin {
    @Shadow
    private ServerLevel level;
    @Shadow
    private BlockPos origin;
    @Shadow
    private @Nullable UUID dragonUUID;

    @Inject(method = "tryRespawn", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/dimension/end/EnderDragonFight;respawnDragon(Ljava/util/List;)V"))
    private void newmeriacore$takeSaphiraEgg(CallbackInfo ci) {
        SaphiraSummoning.onRitualStarted(this.level, this.origin);
    }

    @Inject(method = "abortRespawnSequence", at = @At("HEAD"))
    private void newmeriacore$giveSaphiraEggBack(CallbackInfo ci) {
        SaphiraSummoning.onRitualAborted(this.level, this.origin);
    }

    // Vanilla only leaves a Dragon Egg behind the first time the Ender Dragon dies. Saphira leaves
    // hers every time, so she can be summoned again.
    @Inject(method = "setDragonKilled", at = @At("TAIL"))
    private void newmeriacore$leaveSaphiraEgg(EnderDragon dragon, CallbackInfo ci) {
        if (dragon instanceof Saphira && dragon.getUUID().equals(this.dragonUUID)) {
            SaphiraSummoning.onSaphiraKilled(this.level, this.origin);
        }
    }

    // Everything vanilla then does to its new dragon (fight, origin, phase, position) applies to her.
    @WrapOperation(method = "createNewDragon", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/EntityType;create(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/EntitySpawnReason;)Lnet/minecraft/world/entity/Entity;"))
    private Entity newmeriacore$createSaphiraInstead(EntityType<?> type, Level level, EntitySpawnReason reason, Operation<Entity> original) {
        if (SaphiraSummoning.consumePending(this.level)) {
            return ModEntityTypes.SAPHIRA.get().create(level, reason);
        }
        return original.call(type, level, reason);
    }

    // Saphira carries her own boss bar: the fight's ("Ender Dragon", pink) stays hidden for her.
    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerBossEvent;setVisible(Z)V"))
    private boolean newmeriacore$hideFightBarForSaphira(boolean visible) {
        return visible && !(this.dragonUUID != null && this.level.getEntity(this.dragonUUID) instanceof Saphira);
    }
}
