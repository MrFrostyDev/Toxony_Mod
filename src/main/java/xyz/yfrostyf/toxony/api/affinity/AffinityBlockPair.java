package xyz.yfrostyf.toxony.api.affinity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import xyz.yfrostyf.toxony.api.registries.ToxonyRegistries;

public record AffinityBlockPair(Holder<Affinity> affinity, Holder<Block> block) {
    public static final Codec<AffinityBlockPair> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ToxonyRegistries.AFFINITY_REGISTRY.holderByNameCodec().fieldOf("affinity").forGetter(AffinityBlockPair::affinity),
                    BuiltInRegistries.BLOCK.holderByNameCodec().fieldOf("block").forGetter(AffinityBlockPair::block)
            ).apply(instance, AffinityBlockPair::new)
    );

    public static AffinityBlockPair of(Holder<Affinity> affinity, Holder<Block> block){
        return new AffinityBlockPair(affinity, block);
    }
}
