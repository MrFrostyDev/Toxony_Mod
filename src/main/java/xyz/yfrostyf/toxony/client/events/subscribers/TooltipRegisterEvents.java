package xyz.yfrostyf.toxony.client.events.subscribers;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.client.gui.OilTooltip;
import xyz.yfrostyf.toxony.client.gui.ToxIngredientTooltip;

//thank you [FarmersDelight Mod]

@EventBusSubscriber(modid = ToxonyMain.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class TooltipRegisterEvents {

    // Initialize a tooltip class(as both a class to supply methods for tool tip rendering and
    // a key for a map of factories) and its factory method.
    @SubscribeEvent
    public static void registerCustomTooltipRenderers(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(OilTooltip.OilTooltipComponent.class, OilTooltip::new);
        event.register(ToxIngredientTooltip.ToxIngredientComponent.class, ToxIngredientTooltip::new);
    }
}
