package xyz.yfrostyf.toxony.worldgen;

import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class ToxonyFeatureUtils {
    private static BlockPredicate simpleTagPatchPredicate(TagKey<Block> blocks) {
        return BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, BlockPredicate.matchesTag(Direction.DOWN.getNormal(), blocks));
    }

    public static RandomPatchConfiguration simpleRandomPatchConfiguration(int tries, Holder<PlacedFeature> feature) {
        return new RandomPatchConfiguration(tries, 7, 3, feature);
    }
    public static <FC extends FeatureConfiguration, F extends Feature<FC>> RandomPatchConfiguration simplePatchConfiguration(F feature, FC config, TagKey<Block> blocks, int tries) {
        return simpleRandomPatchConfiguration(tries, PlacementUtils.filtered(feature, config, simpleTagPatchPredicate(blocks)));
    }

    public static <FC extends FeatureConfiguration, F extends Feature<FC>> RandomPatchConfiguration simplePatchConfiguration(F feature, FC config, TagKey<Block> blocks) {
        return simplePatchConfiguration(feature, config, blocks, 96);
    }
}
