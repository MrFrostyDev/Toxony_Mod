package xyz.yfrostyf.toxony.data.datagen.enchantments.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record Impact(int value) {
    public static final Codec<Impact> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("value").forGetter(Impact::value)
            ).apply(instance, Impact::new)
    );
}
