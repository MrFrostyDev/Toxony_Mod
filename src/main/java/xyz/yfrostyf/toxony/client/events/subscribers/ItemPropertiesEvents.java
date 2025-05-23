package xyz.yfrostyf.toxony.client.events.subscribers;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.items.*;
import xyz.yfrostyf.toxony.items.weapons.CycleBowItem;
import xyz.yfrostyf.toxony.items.weapons.FlailItem;
import xyz.yfrostyf.toxony.items.weapons.FlintlockItem;
import xyz.yfrostyf.toxony.items.weapons.WitchingBladeItem;
import xyz.yfrostyf.toxony.registries.ItemRegistry;

@EventBusSubscriber(modid = ToxonyMain.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ItemPropertiesEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(
                    // The item to apply the property to.
                    ItemRegistry.WITCHING_BLADE.get(),
                    // The id of the property.
                    ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "active"),
                    // A reference to a method that calculates the override value.
                    // Parameters are the used item stack, the level context, the player using the item,
                    // and a random seed you can use.
                    (stack, level, player, seed) ->
                            WitchingBladeItem.isActive(stack) ? 1.0F : 0.0F
            );
            ItemProperties.register(
                    ItemRegistry.LETHAL_DOSE.get(),
                    ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "active"),
                    (stack, level, player, seed) ->
                            ToxScalpelItem.isActive(stack) ? 1.0F : 0.0F
            );

            ItemProperties.register(
                    ItemRegistry.CYCLEBOW.get(),
                    ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "loaded"),
                    (stack, level, player, seed) ->
                            (float) CycleBowItem.getLoadedShots(stack)
            );

            ItemProperties.register(
                    ItemRegistry.FLINTLOCK.get(),
                    ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "loaded"),
                    (stack, level, player, seed) ->
                            FlintlockItem.isLoaded(stack) ? 1.0F : 0.0F
            );

            ItemProperties.register(
                    ItemRegistry.BOLT_CARTRIDGE.get(),
                    ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "loaded"),
                    (stack, level, player, seed) ->
                            BoltCartridgeItem.isLoaded(stack) ? 1.0F : 0.0F
            );

            ItemProperties.register(
                    ItemRegistry.FLAIL.get(),
                    ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "spin"),
                    (stack, level, player, seed) -> {
                        if (player == null) {
                            return 0.0F;
                        }
                        else{
                            return  player.getUseItem() == stack && FlailItem.isUsingFlail(player) ? 1.0F : 0.0F;
                        }
                    }
            );

            ItemProperties.register(
                    ItemRegistry.FLAIL.get(),
                    ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "thrown"),
                    (stack, level, player, seed) -> {
                        if (player == null) {
                            return 0.0F;
                        }
                        else{
                            return player.getUseItem() == stack && FlailItem.isFlailThrown(player) ? 1.0F : 0.0F;
                        }
                    }
            );

            ItemProperties.register(
                    ItemRegistry.POTION_FLASK.get(),
                    ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "uses"),
                    (stack, level, player, seed) ->
                            PotionFlaskItem.isFull(stack)
            );

            ItemProperties.register(
                    ItemRegistry.TOXIN_FLASK.get(),
                    ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "uses"),
                    (stack, level, player, seed) ->
                            PotionFlaskItem.isFull(stack)
            );
        });
    }
}
