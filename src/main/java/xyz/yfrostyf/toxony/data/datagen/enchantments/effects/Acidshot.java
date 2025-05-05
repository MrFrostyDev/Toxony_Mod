package xyz.yfrostyf.toxony.data.datagen.enchantments.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record Acidshot(float value) {
    public static final Codec<Acidshot> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("value").forGetter(Acidshot::value)
            ).apply(instance, Acidshot::new)
    );
}
