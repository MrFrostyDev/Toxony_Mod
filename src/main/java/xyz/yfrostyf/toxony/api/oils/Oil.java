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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class Oil {
    public static final Oil EMPTY = new Oil(Component.empty(), HolderSet.empty(), List.of());
    public static final Codec<Oil> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            ComponentSerialization.CODEC.fieldOf("description").forGetter(Oil::getDescription),
                            RegistryCodecs.homogeneousList(Registries.ITEM).fieldOf("supported_items").forGetter(Oil::getSupportedItems),
                            MobEffect.CODEC.listOf().fieldOf("effects").forGetter(Oil::getEffects)
                    ).apply(instance, Oil::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, Oil> STREAM_CODEC = StreamCodec.composite(
            ComponentSerialization.STREAM_CODEC,
            Oil::getDescription,
            ByteBufCodecs.holderSet(Registries.ITEM),
            Oil::getSupportedItems,
            MobEffect.STREAM_CODEC.apply(ByteBufCodecs.list()),
            Oil::getEffects,
            Oil::new
    );

    protected Component description;
    protected HolderSet<Item> supportedItems;
    protected List<Holder<MobEffect>> effects;

    public Oil(Component description, HolderSet<Item> supportedItems, List<Holder<MobEffect>> effects){
        this.description = description;
        this.supportedItems = supportedItems;
        this.effects = effects;
    }

    public Component getDescription() {
        return description;
    }

    public HolderSet<Item> getSupportedItems() {
        return supportedItems;
    }

    public List<Holder<MobEffect>> getEffects() {
        return effects;
    }

    public boolean canOil(ItemStack stack) {
        return this.supportedItems.contains(stack.getItemHolder());
    }

    public void applyOil(ItemOil itemOil, LivingEntity attacker, LivingEntity victim, Level level){
        for (Holder<MobEffect> effect : this.effects) {
            if (effect.value().isInstantenous()) {
                effect.value().applyInstantenousEffect(attacker, attacker, victim, itemOil.amplifier(), 1);
            } else {
                if(!victim.hasEffect(effect)){
                    MobEffectInstance effectInstance = new MobEffectInstance(effect, itemOil.duration(), itemOil.amplifier());
                    victim.addEffect(effectInstance, attacker);
                }
            }
        }
    }
}