package fr.akkun.newmeriacore.potion;

import fr.akkun.newmeriacore.NewmeriaCore;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.Optional;

public class ModPotions {
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(BuiltInRegistries.POTION, NewmeriaCore.MOD_ID);

    // Blanc
    public static final int LIMONADE_COLOR = 0xFFFFFF;
    // Rouge
    public static final int DIABOLO_SWEET_BERRIES_COLOR = 0xC81414;
    // Jaune/orange
    public static final int DIABOLO_GLOW_BERRIES_COLOR = 0xFFA500;

    public static final DeferredHolder<Potion, Potion> LIMONADE = POTIONS.register("limonade",
            () -> new Potion("limonade",
                    new MobEffectInstance(MobEffects.HASTE, 40 * 20),
                    new MobEffectInstance(MobEffects.SPEED, 30 * 20)));

    public static final DeferredHolder<Potion, Potion> DIABOLO_SWEET_BERRIES = POTIONS.register("diabolo_sweet_berries",
            () -> new Potion("diabolo_sweet_berries",
                    new MobEffectInstance(MobEffects.HASTE, 80 * 20),
                    new MobEffectInstance(MobEffects.SPEED, 60 * 20)));

    public static final DeferredHolder<Potion, Potion> DIABOLO_GLOW_BERRIES = POTIONS.register("diabolo_glow_berries",
            () -> new Potion("diabolo_glow_berries",
                    new MobEffectInstance(MobEffects.HASTE, 80 * 20),
                    new MobEffectInstance(MobEffects.SPEED, 60 * 20),
                    new MobEffectInstance(MobEffects.NIGHT_VISION, 80 * 20)));

    public static ItemStack createStack(Item container, Holder<Potion> potion, int color) {
        ItemStack stack = new ItemStack(container);
        stack.set(DataComponents.POTION_CONTENTS, new PotionContents(Optional.of(potion), Optional.of(color), List.of(), Optional.empty()));
        return stack;
    }

    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }
}
