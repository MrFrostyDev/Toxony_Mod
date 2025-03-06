package xyz.yfrostyf.toxony.api.util;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import xyz.yfrostyf.toxony.registries.DataComponentsRegistry;

public class VialUtil {
    public static ItemStack createAffinityStoredItemStack(Item item, Holder<Item> holder) {
        ItemStack itemStack = new ItemStack(item);
        itemStack.set(DataComponentsRegistry.AFFINITY_STORED_ITEM, holder);
        return itemStack;
    }

    public static ItemStack createPotionItemStack(Item item, Holder<Potion> potion) {
        ItemStack itemStack = new ItemStack(item);
        itemStack.set(DataComponents.POTION_CONTENTS, new PotionContents(potion));
        return itemStack;
    }

    public static ItemStack createAffinitySolutionItemStack(Item item, Item stored){
        ItemStack itemStack = new ItemStack(item);
        itemStack.set(DataComponentsRegistry.AFFINITY_STORED_ITEM, new ItemStack(stored).getItemHolder());
        return itemStack;
    }
}
