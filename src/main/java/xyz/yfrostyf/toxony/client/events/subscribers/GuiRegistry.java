package xyz.yfrostyf.toxony.client.events.subscribers;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.client.gui.*;
import xyz.yfrostyf.toxony.client.gui.block.*;
import xyz.yfrostyf.toxony.registries.MenuRegistry;

@EventBusSubscriber(modid = ToxonyMain.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class GuiRegistry {

    @SubscribeEvent
    private static void onRegisterMenu(RegisterMenuScreensEvent event) {
        event.register(MenuRegistry.MORTAR_PESTLE_MENU.get(), MortarPestleScreen::new);
        event.register(MenuRegistry.REDSTONE_MORTAR_MENU.get(), RedstoneMortarScreen::new);
        event.register(MenuRegistry.COPPER_CRUCIBLE_MENU.get(), CopperCrucibleScreen::new);
        event.register(MenuRegistry.ALEMBIC_MENU.get(), AlembicScreen::new);
        event.register(MenuRegistry.ALCHEMICAL_FORGE_MENU.get(), AlchemicalForgeScreen::new);
    }

    @SubscribeEvent
    public static void onRegisterGui(RegisterGuiLayersEvent event){
        event.registerBelow(VanillaGuiLayers.PLAYER_HEALTH, ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "tox_bar_overlay"), new ToxBarOverlay());
        event.registerBelow(VanillaGuiLayers.PLAYER_HEALTH, ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "mob_tox_bar_overlay"), new MobToxBarOverlay());
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "toxin_deathstate_overlay"), new DeathstateOverlay());
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "mutagen_transform_overlay"), new MutagenTransformOverlay());
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "night_predator_overlay"), new NightPredatorOverlay());
    }
}
