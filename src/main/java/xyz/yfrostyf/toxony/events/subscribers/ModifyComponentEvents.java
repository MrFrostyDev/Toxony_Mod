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
                        AffinityRegistry.HEAT.getKey(), AffinityRegistry.FOREST.getKey()
                ))
        );

        event.modify(Items.SPIDER_EYE, builder ->
                builder.set(DataComponentsRegistry.POSSIBLE_AFFINITIES.get(), List.of(
                        AffinityRegistry.COLD.getKey(), AffinityRegistry.FOREST.getKey()
                ))
        );

        event.modify(Items.WITHER_ROSE, builder ->
                builder.set(DataComponentsRegistry.POSSIBLE_AFFINITIES.get(), List.of(
                        AffinityRegistry.DECAY.getKey()
                ))
        );

        event.modify(Items.PUFFERFISH, builder ->
                builder.set(DataComponentsRegistry.POSSIBLE_AFFINITIES.get(), List.of(
                        AffinityRegistry.OCEAN.getKey()
                ))
        );

        event.modify(Items.ROTTEN_FLESH, builder ->
                builder.set(DataComponentsRegistry.POSSIBLE_AFFINITIES.get(), List.of(
                        AffinityRegistry.DECAY.getKey()
                ))
        );
    }
}
