package xyz.yfrostyf.toxony.client.events.subscribers;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.items.ToxScalpelItem;
import xyz.yfrostyf.toxony.items.WitchingBladeItem;
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
                    // The item to apply the property to.
                    ItemRegistry.LETHAL_DOSE.get(),
                    // The id of the property.
                    ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "active"),
                    // A reference to a method that calculates the override value.
                    // Parameters are the used item stack, the level context, the player using the item,
                    // and a random seed you can use.
                    (stack, level, player, seed) ->
                            ToxScalpelItem.isActive(stack) ? 1.0F : 0.0F
            );
        });
    }
}
