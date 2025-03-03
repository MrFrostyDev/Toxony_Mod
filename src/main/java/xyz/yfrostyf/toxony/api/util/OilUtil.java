package xyz.yfrostyf.toxony.api.util;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.oils.ItemOil;
import xyz.yfrostyf.toxony.registries.DataComponentsRegistry;

public class OilUtil {

    public static void useOil(Level level, ItemStack stack, int amount) {
        if(!OilUtil.hasOil(stack) || level == null) return;

        int curOilUses = stack.get(DataComponentsRegistry.OIL_USES);
        int maxOilUses = stack.get(DataComponentsRegistry.OIL).maxUses();

        if(stack.set(DataComponentsRegistry.OIL_USES, curOilUses+amount) >= maxOilUses){
            if(!level.isClientSide()){
                OilUtil.removeOil(stack);
            }
            else{
                Minecraft.getInstance().gui.setOverlayMessage(Component.translatable("message.toxony.oil.empty"), false);
            }
        }
    }

    public static ItemOil updateOil(ItemStack stack, ItemOil oil) {
        ItemOil itemoil = stack.getOrDefault(DataComponentsRegistry.OIL, ItemOil.EMPTY);
        if (itemoil.isEmpty()) {
            return setOil(stack, oil);
        } else {
            ItemOil newItemOil = oil.copy();
            stack.set(DataComponentsRegistry.OIL, newItemOil);
            stack.set(DataComponentsRegistry.OIL_USES, 0);
            return newItemOil;
        }
    }

    public static ItemOil setOil(ItemStack stack, ItemOil oil) {
        stack.set(DataComponentsRegistry.OIL, oil);
        stack.set(DataComponentsRegistry.OIL_USES, 0);
        return oil;
    }

    public static ItemOil removeOil(ItemStack stack) {
        if(!stack.has(DataComponentsRegistry.OIL)) return ItemOil.EMPTY;
        ItemOil itemOil = stack.get(DataComponentsRegistry.OIL);
        stack.remove(DataComponentsRegistry.OIL);
        stack.remove(DataComponentsRegistry.OIL_USES);
        return itemOil;
    }

    public static boolean hasOil(ItemStack stack) {
        return !stack.getOrDefault(DataComponentsRegistry.OIL, ItemOil.EMPTY).isEmpty();
    }
}
