package fr.akkun.newmeriacore.mixin;

import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.pathfinder.Node;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Gives {@code Saphira} access to the dragon's flight path nodes, which vanilla always lays out
 *  around the world origin (0, 0) no matter where the dragon actually is. */
@Mixin(EnderDragon.class)
public interface EnderDragonAccessor {
    @Accessor("nodes")
    Node[] newmeriacore$getNodes();
}
