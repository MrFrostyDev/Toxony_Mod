package xyz.yfrostyf.toxony.registries;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.affinity.Affinity;
import xyz.yfrostyf.toxony.api.oils.ItemOil;
import xyz.yfrostyf.toxony.api.registries.ToxonyRegistries;
import xyz.yfrostyf.toxony.data.datagen.enchantments.effects.Acidshot;
import xyz.yfrostyf.toxony.data.datagen.enchantments.effects.Impact;
import xyz.yfrostyf.toxony.data.datagen.enchantments.effects.Refill;

import java.util.List;

public class DataComponentsRegistry {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, ToxonyMain.MOD_ID);
    public static final DeferredRegister<DataComponentType<?>> ENCHANTMENT_COMPONENT_TYPES = DeferredRegister.create(BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, ToxonyMain.MOD_ID);


    public static void register(IEventBus eventBus) {
        DATA_COMPONENTS.register(eventBus);
        ENCHANTMENT_COMPONENT_TYPES.register(eventBus);
    }

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> ACTIVE = DATA_COMPONENTS.registerComponentType(
            "active",
            builder -> builder
                    // The codec to read/write the data to disk
                    .persistent(Codec.BOOL)
                    // The codec to read/write the data across the network
                    .networkSynchronized(ByteBufCodecs.BOOL)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemOil>> OIL = DATA_COMPONENTS.registerComponentType(
            "oil",
            builder -> builder
                    .persistent(ItemOil.CODEC)
                    .networkSynchronized(ItemOil.STREAM_CODEC)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> OIL_USES = DATA_COMPONENTS.registerComponentType(
            "oil_uses",
            builder -> builder
                    .persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.INT)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<Holder<Item>>>> AFFINITY_STORED_ITEMS = DATA_COMPONENTS.registerComponentType(
            "affinity_stored_items",
            builder -> builder
                    .persistent(BuiltInRegistries.ITEM.holderByNameCodec().listOf())
                    .networkSynchronized(ByteBufCodecs.holderRegistry(Registries.ITEM).apply(ByteBufCodecs.list()))
                    .cacheEncoding()
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ResourceKey<Affinity>>>> POSSIBLE_AFFINITIES = DATA_COMPONENTS.registerComponentType(
            "possible_affinities",
            builder -> builder
                    .persistent(ResourceKey.codec(ToxonyRegistries.AFFINITY_REGISTRY_KEY).listOf())
                    .networkSynchronized(StreamCodec.unit(List.of()))
                    .cacheEncoding()
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Holder<Item>>> AFFINITY_STORED_ITEM = DATA_COMPONENTS.registerComponentType(
            "affinity_stored_item",
            builder -> builder
                    .persistent(BuiltInRegistries.ITEM.holderByNameCodec())
                    .networkSynchronized(ByteBufCodecs.holderRegistry(Registries.ITEM))
                    .cacheEncoding()
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> LOADED_SHOTS = DATA_COMPONENTS.registerComponentType(
            "loaded_shots",
            builder -> builder
                    .persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.INT)
    );

    // Enchantments
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Impact>> IMPACT = ENCHANTMENT_COMPONENT_TYPES.register(
            "impact",
            () -> DataComponentType.<Impact>builder()
                    .persistent(Codec.unit(new Impact()))
                    .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Refill>> REFILL = ENCHANTMENT_COMPONENT_TYPES.register(
            "refill",
            () -> DataComponentType.<Refill>builder()
                    .persistent(Refill.CODEC)
                    .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Acidshot>> ACIDSHOT = ENCHANTMENT_COMPONENT_TYPES.register(
            "acidshot",
            () -> DataComponentType.<Acidshot>builder()
                    .persistent(Acidshot.CODEC)
                    .build());
}
