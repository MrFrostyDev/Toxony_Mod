package xyz.yfrostyf.toxony.api.oils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record Oil(Component description, HolderSet<Item> supportedItems, List<Holder<MobEffect>> effects) {
    public static final Oil EMPTY = new Oil(Component.empty(), HolderSet.empty(), List.of());
    public static final Codec<Oil> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            ComponentSerialization.CODEC.fieldOf("description").forGetter(Oil::description),
                            RegistryCodecs.homogeneousList(Registries.ITEM).fieldOf("supported_items").forGetter(Oil::supportedItems),
                            MobEffect.CODEC.listOf().fieldOf("effects").forGetter(Oil::effects)
                    ).apply(instance, Oil::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, Oil> STREAM_CODEC = StreamCodec.composite(
            ComponentSerialization.STREAM_CODEC,
            Oil::description,
            ByteBufCodecs.holderSet(Registries.ITEM),
            Oil::supportedItems,
            MobEffect.STREAM_CODEC.apply(ByteBufCodecs.list()),
            Oil::effects,
            Oil::new
    );

    public boolean canOil(ItemStack stack) {
        return this.supportedItems().contains(stack.getItemHolder());
    }
}