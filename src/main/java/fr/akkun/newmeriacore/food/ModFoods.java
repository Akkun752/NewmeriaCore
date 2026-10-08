package fr.akkun.newmeriacore.food;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

import java.util.List;

public class ModFoods {
    // Apple: nutrition 4, saturationModifier 0.3 (2.4 saturation restored)
    public static final FoodProperties PEER = new FoodProperties.Builder().nutrition(5).saturationModifier(0.34f).build();

    // Light snack, like a small bag of rice
    public static final FoodProperties RICE = new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).build();

    public static final FoodProperties CHILI_PEPPER = new FoodProperties.Builder().nutrition(4).saturationModifier(0.32f).build();

    // Same as Golden Carrot, always edible regardless of hunger
    public static final FoodProperties CHILI_RICE = new FoodProperties.Builder().nutrition(6).saturationModifier(1.2f).alwaysEdible().build();

    public static final Consumable CHILI_RICE_CONSUMABLE = Consumables.defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(List.of(
                    new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1200),
                    new MobEffectInstance(MobEffects.REGENERATION, 100)
            ))).build();

    // Bread (nutrition 5, saturationModifier 0.6) + Cooked Porkchop (nutrition 8, saturationModifier 0.8)
    // combined: nutrition 13, saturation restored 6.0+12.8=18.8 -> saturationModifier = 18.8/(13*2)
    public static final FoodProperties SANDWICH = new FoodProperties.Builder().nutrition(13).saturationModifier(0.7230769f).build();

    // Fried meats: 1.5x their COOKED_X vanilla nutrition/saturationModifier.
    public static final FoodProperties FRIED_BEEF = new FoodProperties.Builder().nutrition(12).saturationModifier(1.2f).build();
    public static final FoodProperties FRIED_CHICKEN = new FoodProperties.Builder().nutrition(9).saturationModifier(0.9f).build();
    public static final FoodProperties FRIED_COD = new FoodProperties.Builder().nutrition(8).saturationModifier(0.9f).build();
    public static final FoodProperties FRIED_MUTTON = new FoodProperties.Builder().nutrition(9).saturationModifier(1.2f).build();
    public static final FoodProperties FRIED_PORKCHOP = new FoodProperties.Builder().nutrition(12).saturationModifier(1.2f).build();
    public static final FoodProperties FRIED_RABBIT = new FoodProperties.Builder().nutrition(8).saturationModifier(0.9f).build();
    public static final FoodProperties FRIED_SALMON = new FoodProperties.Builder().nutrition(9).saturationModifier(1.2f).build();

    // Duck meats: 1.5x their chicken counterpart (raw / cooked / fried). 13.5 nutrition rounded up to 14.
    public static final FoodProperties RAW_DUCK = new FoodProperties.Builder().nutrition(3).saturationModifier(0.45f).build();
    public static final FoodProperties COOKED_DUCK = new FoodProperties.Builder().nutrition(9).saturationModifier(0.9f).build();
    public static final FoodProperties FRIED_DUCK = new FoodProperties.Builder().nutrition(14).saturationModifier(1.35f).build();

    // Sausages: 0.3x their porkchop counterpart (raw 3/0.3, cooked 8/0.8, fried 12/1.2) in both hunger and
    // actual saturation restored (= nutrition * modifier * 2), so 4 sausages are worth 1.2 porkchops.
    // Hunger has to be a whole number (0.9 -> 1, 2.4 -> 2, 3.6 -> 4); the modifiers are then derived so
    // the saturation is exact: 0.54, 3.84 and 8.64 points.
    public static final FoodProperties RAW_SAUSAGE = new FoodProperties.Builder().nutrition(1).saturationModifier(0.27f).build();
    public static final FoodProperties SAUSAGE = new FoodProperties.Builder().nutrition(2).saturationModifier(0.96f).build();
    public static final FoodProperties FRIED_SAUSAGE = new FoodProperties.Builder().nutrition(4).saturationModifier(1.08f).build();

    // A light raw vegetable: a bit less filling than an Apple (4 / 0.3), same saturation ratio.
    public static final FoodProperties TOMATO = new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).build();

    // A small raw vegetable, between a Potato (1 / 0.3) and a Carrot (3 / 0.6).
    // Bread (5 / 0.6) + Steak (8 / 0.8) + Tomato (3 / 0.3) add up to 16 food and 20.6 saturation;
    // the assembled meal gives a little more: 18 food and 23.4 saturation.
    public static final FoodProperties HAMBURGER = new FoodProperties.Builder().nutrition(18).saturationModifier(0.65f).build();

    // Apple (4 / 0.3) + Pear (5 / 0.34) + Melon Slice (2 / 0.3) add up to 11 food and 7 saturation;
    // the salad gives 12 food and 9.6 saturation.
    public static final FoodProperties FRUIT_SALAD = new FoodProperties.Builder().nutrition(12).saturationModifier(0.4f).build();

    // Sausage (2 / 0.96) + Rice (3 / 0.3) + Onion (2 / 0.4) + Tomato (3 / 0.3) add up to 10 food
    // and 9 saturation; the cooked dish gives 11 food and 12.1 saturation.
    public static final FoodProperties SAUSAGE_ROUGAIL = new FoodProperties.Builder().nutrition(11).saturationModifier(0.55f).build();

    // Bread (5 / 0.6) + Sausage (2 / 0.96) add up to 7 food and 9.8 saturation;
    // the hot dog gives 8 food and 11.2 saturation.
    public static final FoodProperties HOT_DOG = new FoodProperties.Builder().nutrition(8).saturationModifier(0.7f).build();

    public static final FoodProperties ONION = new FoodProperties.Builder().nutrition(2).saturationModifier(0.4f).build();
}
