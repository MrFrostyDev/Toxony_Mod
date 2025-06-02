package xyz.yfrostyf.toxony.api.oils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import xyz.yfrostyf.toxony.ToxonyConfig;
import xyz.yfrostyf.toxony.api.registries.ToxonyRegistries;
import xyz.yfrostyf.toxony.registries.OilsRegistry;

import java.util.Objects;
import java.util.function.Consumer;

public record ItemOil(Holder<Oil> oil, int duration, int amplifier, int maxUses, boolean showInTooltip) implements TooltipProvider {
    public static final ItemOil EMPTY = new ItemOil(null, 0, 0, 0, false);
    public static final Codec<ItemOil> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            ToxonyRegistries.OIL_REGISTRY.holderByNameCodec().fieldOf("oil").forGetter(ItemOil::oil),
                            Codec.INT.fieldOf("duration").forGetter(ItemOil::duration),
                            Codec.INT.fieldOf("amplifier").forGetter(ItemOil::amplifier),
                            Codec.INT.fieldOf("max_uses").forGetter(ItemOil::maxUses),
                            Codec.BOOL.optionalFieldOf("show_in_tooltip", Boolean.valueOf(true)).forGetter(ItemOil::showInTooltip)
                    ).apply(instance, ItemOil::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemOil> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.holder(OilsRegistry.OILS.getRegistryKey(), Oil.STREAM_CODEC),
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

    public static ItemOil createItemOil(Holder<Oil> oil, int duration, int amplifier, int maxUses, boolean showInTooltip){
        return new ItemOil(oil, duration, amplifier, maxUses, showInTooltip);
    }

    public static ItemOil createItemOil(Holder<Oil> oil, int duration, int amplifier, int maxUses){
        return new ItemOil(oil, duration, amplifier, maxUses, false);
    }

    public static ItemOil createItemOil(Holder<Oil> oil, int duration, int amplifier){
        return new ItemOil(oil, duration, amplifier, -1, false);
    }

    public int getMaxUses() {
        return Mth.floor(maxUses * ToxonyConfig.OIL_DURABILITY_MULT.get());
    }

    public Oil getOil() {
        return oil.value();
    }

    public boolean isEmpty() {
        return this.oil == null;
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
        }
        else {
            return obj instanceof ItemOil compare
                    && this.oil.equals(compare.oil);
        }
    }

    // Using RenderTooltip events instead.
    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag) {}
}
