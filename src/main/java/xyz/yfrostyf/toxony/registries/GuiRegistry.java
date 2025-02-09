package xyz.yfrostyf.toxony.registries;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.client.gui.AlembicScreen;
import xyz.yfrostyf.toxony.client.gui.CopperCrucibleScreen;
import xyz.yfrostyf.toxony.client.gui.MortarPestleScreen;
import xyz.yfrostyf.toxony.client.gui.ToxBar;

import static xyz.yfrostyf.toxony.registries.MenuRegistry.*;

@EventBusSubscriber(modid = ToxonyMain.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class GuiRegistry {

    @SubscribeEvent
    private static void onRegisterMenu(RegisterMenuScreensEvent event) {
        event.register(MORTAR_PESTLE_MENU.get(), MortarPestleScreen::new);
        event.register(COPPER_CRUCIBLE_MENU.get(), CopperCrucibleScreen::new);
        event.register(ALEMBIC_MENU.get(), AlembicScreen::new);
    }

    @SubscribeEvent
    public static void onRegisterGui(RegisterGuiLayersEvent event){
        event.registerBelow(VanillaGuiLayers.PLAYER_HEALTH, ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "tox_bar"), new ToxBar());
    }
}
