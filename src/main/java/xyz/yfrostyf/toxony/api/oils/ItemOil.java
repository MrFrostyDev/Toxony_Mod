package xyz.yfrostyf.toxony.api.oils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public record ItemOil(Oil oil, int duration, int amplifier, int maxUses, boolean showInTooltip) implements TooltipProvider {
    public static final ItemOil EMPTY = new ItemOil(Oil.EMPTY, 0, 0, 0, false);
    public static final Codec<ItemOil> CODEC = RecordCodecBuilder.create(
            p_337961_ -> p_337961_.group(
                            Oil.CODEC.fieldOf("oil").forGetter(ItemOil::oil),
                            Codec.INT.fieldOf("duration").forGetter(ItemOil::duration),
                            Codec.INT.fieldOf("amplifier").forGetter(ItemOil::amplifier),
                            Codec.INT.fieldOf("max_uses").forGetter(ItemOil::maxUses),
                            Codec.BOOL.optionalFieldOf("show_in_tooltip", Boolean.valueOf(true)).forGetter(ItemOil::showInTooltip)
                    )
                    .apply(p_337961_, ItemOil::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemOil> STREAM_CODEC = StreamCodec.composite(
            Oil.STREAM_CODEC,
            ItemOil::oil,
            ByteBufCodecs.INT,
            ItemOil::duration,
            ByteBufCodecs.VAR_INT,
            ItemOil::amplifier,
            ByteBufCodecs.INT,
            ItemOil::maxUses,
            ByteBufCodecs.BOOL,
            ItemOil::showInTooltip,
            ItemOil::new
    );

    public boolean isEmpty() {
        return this.oil == Oil.EMPTY;
    }

    public ItemOil copy(){
        return new ItemOil(this.oil, this.duration, this.amplifier, this.maxUses, this.showInTooltip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.oil, this.showInTooltip);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else {
            return obj instanceof ItemOil oil
                    && this.oil == oil.oil
                    && this.showInTooltip == oil.showInTooltip;
        }
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag) {

    }

    public static ItemOil create(Supplier<Oil> oil, int duration, int amplifier, int maxUses, boolean showInTooltip){
        return new ItemOil(oil.get(), duration, amplifier, maxUses, showInTooltip);
    }
}
