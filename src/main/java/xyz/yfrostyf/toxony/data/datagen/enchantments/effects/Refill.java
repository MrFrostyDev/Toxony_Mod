package xyz.yfrostyf.toxony.data.datagen.enchantments.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record Refill(float value) {
    public static final Codec<Refill> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("value").forGetter(Refill::value)
            ).apply(instance, Refill::new)
    );
}
