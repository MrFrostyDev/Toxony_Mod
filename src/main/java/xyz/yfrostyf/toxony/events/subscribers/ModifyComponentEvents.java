package xyz.yfrostyf.toxony.events.subscribers;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import xyz.yfrostyf.toxony.ToxonyConfig;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.affinity.Affinity;
import xyz.yfrostyf.toxony.api.registries.ToxonyRegistries;
import xyz.yfrostyf.toxony.items.weapons.WitchingBladeItem;
import xyz.yfrostyf.toxony.registries.AffinityRegistry;
import xyz.yfrostyf.toxony.registries.DataComponentsRegistry;
import xyz.yfrostyf.toxony.registries.ItemRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@EventBusSubscriber(modid = ToxonyMain.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModifyComponentEvents {

    @SubscribeEvent
    public static void modifyComponents(ModifyDefaultComponentsEvent event) {
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

        event.modify(Items.LILY_OF_THE_VALLEY, builder ->
                builder.set(DataComponentsRegistry.POSSIBLE_AFFINITIES.get(), List.of(
                        AffinityRegistry.FOREST.getKey(), AffinityRegistry.HEAT.getKey()
                ))
        );

        event.modify(Items.RED_MUSHROOM, builder ->
                builder.set(DataComponentsRegistry.POSSIBLE_AFFINITIES.get(), List.of(
                        AffinityRegistry.FOREST.getKey()
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

        // Modify Default Components of mod items for configs
        if(!ToxonyConfig.SPEC.isLoaded()) return;

        event.modify(ItemRegistry.WITCHING_BLADE.get(), builder ->
                builder.set(DataComponents.ATTRIBUTE_MODIFIERS, WitchingBladeItem.createAttributes(
                        ToxonyConfig.WITCHINGBLADE_DAMAGE.get().floatValue()-1, -2.4F
                ))
        );

        event.modify(ItemRegistry.FLAIL.get(), builder ->
                builder.set(DataComponents.ATTRIBUTE_MODIFIERS, WitchingBladeItem.createAttributes(
                        ToxonyConfig.FLAIL_DAMAGE.get().floatValue()-1, -3.4F + (3.4F * (ToxonyConfig.FLAIL_SPIN_SPEED_MULT.get().floatValue() - 1))
                ))
        );

        setCustomItemAffinity(event);
    }

    /**
     * Modify and add affinities to items listed in the {@link ToxonyConfig}
     */
    private static void setCustomItemAffinity(ModifyDefaultComponentsEvent event){
        List<? extends String> configList = ToxonyConfig.CUSTOM_ITEM_AFFINITIES.get();

        Pair<Item, List<ResourceKey<Affinity>>> itemAffinityPair = new Pair<>(Items.AIR, new ArrayList<>());
        for (String s : configList) {
            ResourceLocation resourceLocation = ResourceLocation.tryParse(s);
            if (resourceLocation == null) continue;

            Item item = BuiltInRegistries.ITEM.get(resourceLocation);
            Optional<Holder.Reference<Affinity>> optionalAffinity = ToxonyRegistries.AFFINITY_REGISTRY.getHolder(resourceLocation);

            if (item != Items.AIR) {
                if (itemAffinityPair.getFirst() != Items.AIR) {
                    List<ResourceKey<Affinity>> affinityList = itemAffinityPair.getSecond().stream().toList();
                    event.modify(itemAffinityPair.getFirst(), builder ->
                            builder.set(DataComponentsRegistry.POSSIBLE_AFFINITIES.get(), affinityList)
                    );
                }
                itemAffinityPair = new Pair<>(item, new ArrayList<>());
            } else if (optionalAffinity.isPresent()) {
                itemAffinityPair.getSecond().add(optionalAffinity.get().key());
            }
        }

        if(itemAffinityPair.getFirst() != Items.AIR){
            List<ResourceKey<Affinity>> affinityList = itemAffinityPair.getSecond().stream().toList();
            event.modify(itemAffinityPair.getFirst(), builder ->
                    builder.set(DataComponentsRegistry.POSSIBLE_AFFINITIES.get(), affinityList)
            );
        }

    }
}
