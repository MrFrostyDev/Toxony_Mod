package xyz.yfrostyf.toxony.registries;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.affinity.Affinity;
import xyz.yfrostyf.toxony.api.oils.ItemOil;
import xyz.yfrostyf.toxony.api.registries.ToxonyRegistries;

import java.util.List;

public class DataComponentsRegistry {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, ToxonyMain.MOD_ID);

    public static void register(IEventBus eventBus) {
        DATA_COMPONENTS.register(eventBus);
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

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<Affinity>>> AFFINITIES = DATA_COMPONENTS.registerComponentType(
            "affinities",
            builder -> builder
                    .persistent(Affinity.CODEC.listOf())
                    .networkSynchronized(Affinity.STREAM_CODEC.apply(ByteBufCodecs.list()))
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ResourceKey<Affinity>>>> POSSIBLE_AFFINITIES = DATA_COMPONENTS.registerComponentType(
            "affinity",
            builder -> builder
                    .persistent(ResourceKey.codec(ToxonyRegistries.AFFINITY_REGISTRY_KEY).listOf())
                    .networkSynchronized(ResourceKey.streamCodec(ToxonyRegistries.AFFINITY_REGISTRY_KEY).apply(ByteBufCodecs.list()))
                    .cacheEncoding()
    );
}
