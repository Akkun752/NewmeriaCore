package fr.akkun.newmeriacore.frying;

import fr.akkun.newmeriacore.item.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Map;

public class FryingRecipes {
    // Class is only ever loaded (and this map only ever built) well after mod registration finishes,
    // so eagerly calling .get() here on our own DeferredItems is safe.
    private static final Map<Item, Item> RAW_TO_FRIED = Map.of(
            Items.BEEF, ModItems.FRIED_BEEF.get(),
            Items.CHICKEN, ModItems.FRIED_CHICKEN.get(),
            Items.COD, ModItems.FRIED_COD.get(),
            Items.MUTTON, ModItems.FRIED_MUTTON.get(),
            Items.PORKCHOP, ModItems.FRIED_PORKCHOP.get(),
            Items.RABBIT, ModItems.FRIED_RABBIT.get(),
            Items.SALMON, ModItems.FRIED_SALMON.get(),
            ModItems.RAW_DUCK.get(), ModItems.FRIED_DUCK.get(),
            ModItems.RAW_SAUSAGE.get(), ModItems.FRIED_SAUSAGE.get()
    );

    /** @return the fried result for a raw food item, or null if it isn't fryable. */
    public static Item getFriedResult(Item raw) {
        return RAW_TO_FRIED.get(raw);
    }
}
