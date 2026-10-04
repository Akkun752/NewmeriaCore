package fr.akkun.newmerias2core.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import fr.akkun.newmerias2core.rpg.LapisBypassSlot;
import fr.akkun.newmerias2core.rpg.RpgAttachments;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Magic 2+ players get a permanently simulated, always-full stack of Lapis Lazuli: the real slot
 * stops accepting new items entirely ({@link LapisBypassSlot} - meant to be paired with a custom GUI
 * texture that no longer draws a slot there), while both the enchant-click currency check and the
 * displayed Lapis count behave exactly as if a permanent stack of 64 were sitting in it - the real
 * slot's actual contents are never read for either purpose once bypassed.
 */
@Mixin(EnchantmentMenu.class)
public abstract class EnchantmentMenuMixin {
    @Shadow
    @Final
    private Container enchantSlots;

    @Unique
    private Player newmerias2core$player;

    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At("TAIL"))
    private void newmerias2core$capturePlayer(int containerId, Inventory inventory, ContainerLevelAccess access, CallbackInfo ci) {
        this.newmerias2core$player = inventory.player;
    }

    @ModifyArg(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/inventory/EnchantmentMenu;addSlot(Lnet/minecraft/world/inventory/Slot;)Lnet/minecraft/world/inventory/Slot;", ordinal = 1))
    private Slot newmerias2core$replaceLapisSlot(Slot original, @Local Inventory inventory) {
        return new LapisBypassSlot(this.enchantSlots, 1, 35, 47, inventory.player);
    }

    @Redirect(method = "clickMenuButton", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/Container;getItem(I)Lnet/minecraft/world/item/ItemStack;", ordinal = 1))
    private ItemStack newmerias2core$simulatedLapisForClick(Container enchantSlots, int slot) {
        if (this.newmerias2core$player != null && this.newmerias2core$player.getData(RpgAttachments.RPG_DATA).magicLevel() >= 2) {
            return new ItemStack(Items.LAPIS_LAZULI, 64);
        }
        return enchantSlots.getItem(slot);
    }

    @Inject(method = "getGoldCount", at = @At("HEAD"), cancellable = true)
    private void newmerias2core$simulatedLapisForDisplay(CallbackInfoReturnable<Integer> cir) {
        if (this.newmerias2core$player != null && this.newmerias2core$player.getData(RpgAttachments.RPG_DATA).magicLevel() >= 2) {
            cir.setReturnValue(64);
        }
    }
}
