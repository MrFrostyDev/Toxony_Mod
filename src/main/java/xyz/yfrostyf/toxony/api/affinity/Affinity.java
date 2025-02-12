package xyz.yfrostyf.toxony.api.affinity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffect;
import xyz.yfrostyf.toxony.api.mutagens.MutagenEffect;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class Affinity {
    public static Affinity EMPTY = new Affinity("", 0, List.of());

    public static final Codec<Affinity> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codec.STRING.fieldOf("name").forGetter(Affinity::getName),
                            Codec.INT.fieldOf("index").forGetter(Affinity::getIndex),
                            MutagenEffect.CODEC.listOf().fieldOf("mutagens").forGetter(Affinity::getMutagens)
                    ).apply(instance, Affinity::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, Affinity> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            Affinity::getName,
            ByteBufCodecs.VAR_INT,
            Affinity::getIndex,
            MobEffect.STREAM_CODEC.apply(ByteBufCodecs.list()),
            Affinity::getMutagens,
            Affinity::new
    );

    private final String name;
    private final List<Holder<MobEffect>> mutagens;
    private final int index;

    public Affinity(String name, int index, List<Holder<MobEffect>> mutagens){
        this.name = name;
        this.index = index;
        this.mutagens = mutagens;
    }

    public static Affinity create(String name, int index, Supplier<List<Holder<MobEffect>>> mutagens){
        return new Affinity(name, index, mutagens.get());
    }


    public String getName() {
        return name;
    }

    public List<Holder<MobEffect>> getMutagens() {
        return mutagens;
    }

    public int getIndex(){
        return index;
    }

    public boolean isEmpty(){
        return this == EMPTY;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Affinity affinity){
            return Objects.equals(this.getName(), affinity.getName());
        }
        return false;
    }
}

