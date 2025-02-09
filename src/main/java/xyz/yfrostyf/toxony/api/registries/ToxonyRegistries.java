package xyz.yfrostyf.toxony.api.registries;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.oils.Oil;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class ToxonyRegistries {
    public static final ResourceKey<Registry<Oil>> OIL_REGISTRY_KEY = ResourceKey.createRegistryKey(
            ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "oils"));

    public static final Registry<Oil> OIL_REGISTRY = new RegistryBuilder<>(OIL_REGISTRY_KEY)
            // If you want to enable integer id syncing, for networking.
            // These should only be used in networking contexts, for example in packets or purely networking-related NBT data.
            .sync(true)
            // Build the registry.
            .create();


    @SubscribeEvent
    static void registerRegistries(NewRegistryEvent event) {
        event.register(OIL_REGISTRY);
    }
}
