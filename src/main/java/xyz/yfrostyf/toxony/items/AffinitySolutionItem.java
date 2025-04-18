package xyz.yfrostyf.toxony.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import xyz.yfrostyf.toxony.registries.DataComponentsRegistry;

public class AffinitySolutionItem extends Item {
    public AffinitySolutionItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        if(stack.has(DataComponentsRegistry.AFFINITY_STORED_ITEM)) return true;
        return super.isFoil(stack);
    }
}
