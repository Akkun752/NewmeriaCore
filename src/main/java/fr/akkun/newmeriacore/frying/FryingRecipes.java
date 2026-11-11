package fr.akkun.newmeriacore.frying;

import fr.akkun.newmeriacore.item.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Map;

public class FryingRecipes {
    /** What frying one raw item gives: {@code count} of {@code item}. */
    public record FriedResult(Item item, int count) {
    }

    // Class is only ever loaded (and this map only ever built) well after mod registration finishes,
    // so eagerly calling .get() here on our own DeferredItems is safe.
    private static final Map<Item, FriedResult> RAW_TO_FRIED = Map.of(
            Items.BEEF, new FriedResult(ModItems.FRIED_BEEF.get(), 1),
            Items.CHICKEN, new FriedResult(ModItems.FRIED_CHICKEN.get(), 1),
            Items.COD, new FriedResult(ModItems.FRIED_COD.get(), 1),
            Items.MUTTON, new FriedResult(ModItems.FRIED_MUTTON.get(), 1),
            Items.PORKCHOP, new FriedResult(ModItems.FRIED_PORKCHOP.get(), 1),
            Items.RABBIT, new FriedResult(ModItems.FRIED_RABBIT.get(), 1),
            Items.SALMON, new FriedResult(ModItems.FRIED_SALMON.get(), 1),
            ModItems.RAW_DUCK.get(), new FriedResult(ModItems.FRIED_DUCK.get(), 1),
            ModItems.RAW_SAUSAGE.get(), new FriedResult(ModItems.FRIED_SAUSAGE.get(), 1),
            // One onion is cut into 4 rings.
            ModItems.ONION.get(), new FriedResult(ModItems.ONION_RING.get(), 4)
    );

    /** @return what frying one of that raw item gives, or null if it isn't fryable. */
    public static FriedResult getFriedResult(Item raw) {
        return RAW_TO_FRIED.get(raw);
    }
}
