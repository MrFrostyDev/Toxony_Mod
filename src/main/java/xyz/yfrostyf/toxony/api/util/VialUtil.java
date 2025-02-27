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
        ItemStack itemstack = new ItemStack(item);
        itemstack.set(DataComponentsRegistry.AFFINITY_STORED_ITEM, holder);
        return itemstack;
    }

    public static ItemStack createPotionItemStack(Item item, Holder<Potion> potion) {
        ItemStack itemstack = new ItemStack(item);
        itemstack.set(DataComponents.POTION_CONTENTS, new PotionContents(potion));
        return itemstack;
    }
}
