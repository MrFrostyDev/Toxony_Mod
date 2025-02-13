package xyz.yfrostyf.toxony.events.subscribers;

import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.registries.AffinityRegistry;
import xyz.yfrostyf.toxony.registries.DataComponentsRegistry;

import java.util.List;

@EventBusSubscriber(modid = ToxonyMain.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModifyComponentEvents {

    @SubscribeEvent
    public static void modifyComponents(ModifyDefaultComponentsEvent event) {
        // Sets the component on melon seeds
        event.modify(Items.POISONOUS_POTATO, builder ->
                builder.set(DataComponentsRegistry.POSSIBLE_AFFINITIES.get(), List.of(
                        AffinityRegistry.SUN.getKey()
                ))
        );

        event.modify(Items.SPIDER_EYE, builder ->
                builder.set(DataComponentsRegistry.POSSIBLE_AFFINITIES.get(), List.of(
                        AffinityRegistry.MOON.getKey()
                ))
        );

        event.modify(Items.INK_SAC, builder ->
                builder.set(DataComponentsRegistry.POSSIBLE_AFFINITIES.get(), List.of(
                        AffinityRegistry.OCEAN.getKey()
                ))
        );
    }
}
