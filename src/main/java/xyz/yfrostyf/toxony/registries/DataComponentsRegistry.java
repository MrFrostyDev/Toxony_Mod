package xyz.yfrostyf.toxony.registries;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.oils.ItemOil;

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
}
