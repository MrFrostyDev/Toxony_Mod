package xyz.yfrostyf.toxony.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.blocks.WildPoisonCropBlock;
import xyz.yfrostyf.toxony.blocks.plants.FalseBerryBushBlock;
import xyz.yfrostyf.toxony.registries.BlockRegistry;

import java.util.List;


// thank you [Modding by Kaupenjoe]

public class ToxonyConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?,?>> PATCH_FALSE_BERRY_BUSH = registerKey("patch_false_berry_bush");
    public static final ResourceKey<ConfiguredFeature<?,?>> WILD_OCELOT_MINT = registerKey("wild_ocelot_mint");
    public static final ResourceKey<ConfiguredFeature<?,?>> WILD_NIGHTSHADE = registerKey("wild_nightshade");
    public static final ResourceKey<ConfiguredFeature<?,?>> WILD_WATER_HEMLOCK = registerKey("wild_water_hemlock");
    public static final ResourceKey<ConfiguredFeature<?,?>> WILD_COLDSNAP = registerKey("wild_coldsnap");
    public static final ResourceKey<ConfiguredFeature<?,?>> PATCH_WILD_BLOODROOT = registerKey("patch_wild_bloodroot");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context){
        register(context, PATCH_FALSE_BERRY_BUSH, Feature.RANDOM_PATCH,
                FeatureUtils.simplePatchConfiguration(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(
                                BlockStateProvider.simple(BlockRegistry.FALSE_BERRY_BUSH.get().defaultBlockState()
                                        .setValue(FalseBerryBushBlock.AGE, Integer.valueOf(FalseBerryBushBlock.MAX_AGE)))
                        ),
                        List.of(Blocks.GRASS_BLOCK)
                ));

        register(context, WILD_OCELOT_MINT, Feature.FLOWER,
                FeatureUtils.simplePatchConfiguration(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(
                                BlockStateProvider.simple(BlockRegistry.WILD_OCELOT_MINT.get().defaultBlockState()
                                        .setValue(WildPoisonCropBlock.AGE, Integer.valueOf(WildPoisonCropBlock.MAX_AGE)))
                        ),
                        List.of(Blocks.GRASS_BLOCK)
                ));

        register(context, WILD_NIGHTSHADE, Feature.FLOWER,
                FeatureUtils.simplePatchConfiguration(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(
                                BlockStateProvider.simple(BlockRegistry.WILD_NIGHTSHADE.get().defaultBlockState()
                                        .setValue(WildPoisonCropBlock.AGE, Integer.valueOf(WildPoisonCropBlock.MAX_AGE)))
                        ),
                        List.of(Blocks.GRASS_BLOCK)
                ));

        register(context, WILD_WATER_HEMLOCK, Feature.FLOWER,
                FeatureUtils.simplePatchConfiguration(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(
                                BlockStateProvider.simple(BlockRegistry.WILD_WATER_HEMLOCK.get().defaultBlockState()
                                        .setValue(WildPoisonCropBlock.AGE, Integer.valueOf(WildPoisonCropBlock.MAX_AGE)))
                        ),
                        List.of(Blocks.GRASS_BLOCK)
                ));

        register(context, WILD_COLDSNAP, Feature.FLOWER,
                FeatureUtils.simplePatchConfiguration(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(
                                BlockStateProvider.simple(BlockRegistry.WILD_COLDSNAP.get().defaultBlockState()
                                        .setValue(WildPoisonCropBlock.AGE, Integer.valueOf(WildPoisonCropBlock.MAX_AGE)))
                        ),
                        List.of(Blocks.GRASS_BLOCK)
                ));

        register(context, PATCH_WILD_BLOODROOT, Feature.RANDOM_PATCH,
                FeatureUtils.simplePatchConfiguration(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(
                                BlockStateProvider.simple(BlockRegistry.WILD_BLOODROOT.get().defaultBlockState()
                                        .setValue(WildPoisonCropBlock.AGE, Integer.valueOf(WildPoisonCropBlock.MAX_AGE)))
                        ),
                        List.of(Blocks.CRIMSON_NYLIUM)
                ));
    }

    public static ResourceKey<ConfiguredFeature<?,?>> registerKey(String name){
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(
            BootstrapContext<ConfiguredFeature<?, ?>> context,
            ResourceKey<ConfiguredFeature<?,?>> key, F feature, FC configuration){

        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
