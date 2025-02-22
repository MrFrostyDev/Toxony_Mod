package xyz.yfrostyf.toxony.client.events.subscribers;

import com.mojang.datafixers.util.Either;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.client.gui.OilTooltip;
import xyz.yfrostyf.toxony.client.gui.ToxIngredientTooltip;
import xyz.yfrostyf.toxony.registries.DataComponentsRegistry;
import xyz.yfrostyf.toxony.registries.ItemRegistry;

@EventBusSubscriber(modid = ToxonyMain.MOD_ID, value = Dist.CLIENT)
public class TooltipRenderEvents {

    @SubscribeEvent
    public static void onRenderToolTipForOil(RenderTooltipEvent.GatherComponents event){
        ItemStack itemstack = event.getItemStack();

        // Left for FormattedText, Right for TooltipComponents
        if(itemstack.has(DataComponentsRegistry.OIL)){
            event.getTooltipElements().add(Either.right(new OilTooltip.OilTooltipComponent(itemstack)));
        }
        if(itemstack.has(DataComponentsRegistry.POSSIBLE_AFFINITIES)
                && Minecraft.getInstance().player.isHolding(ItemRegistry.MAGNIFYING_GLASS.get())){
            event.getTooltipElements().add(Either.right(new ToxIngredientTooltip.ToxIngredientComponent(itemstack)));
        }
    }
}
