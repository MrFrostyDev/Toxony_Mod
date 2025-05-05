package xyz.yfrostyf.toxony.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.registries.BlockRegistry;

import java.util.List;

// thank you [Modding by Kaupenjoe]

public class ToxonyPlacedFeature {

    public static final ResourceKey<PlacedFeature> PATCH_FALSE_BERRY_BUSH_PLACED = registerKey("false_berry_bush_placed");
    public static final ResourceKey<PlacedFeature> WILD_OCELOT_MINT_PLACED = registerKey("wild_ocelot_mint_placed");
    public static final ResourceKey<PlacedFeature> WILD_NIGHTSHADE_PLACED = registerKey("wild_nightshade_placed");
    public static final ResourceKey<PlacedFeature> WILD_WATER_HEMLOCK_PLACED = registerKey("wild_water_hemlock_placed");
    public static final ResourceKey<PlacedFeature> WILD_COLDSNAP_PLACED = registerKey("wild_coldsnap_placed");
    public static final ResourceKey<PlacedFeature> PATCH_WILD_BLOODROOT_PLACED = registerKey("wild_bloodroot_placed");
    public static final ResourceKey<PlacedFeature> ORE_ANCIENT_SILVER_SMALL = registerKey("ore_ancient_silver_small");

    public static void bootstrap(BootstrapContext<PlacedFeature> context){
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, PATCH_FALSE_BERRY_BUSH_PLACED, configuredFeatures.getOrThrow(ToxonyConfiguredFeatures.PATCH_FALSE_BERRY_BUSH),
                List.of(RarityFilter.onAverageOnceEvery(48),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                        BiomeFilter.biome()));

        register(context, WILD_OCELOT_MINT_PLACED, configuredFeatures.getOrThrow(ToxonyConfiguredFeatures.WILD_OCELOT_MINT),
                List.of(RarityFilter.onAverageOnceEvery(48),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                        BiomeFilter.biome()));

        register(context, WILD_NIGHTSHADE_PLACED, configuredFeatures.getOrThrow(ToxonyConfiguredFeatures.WILD_NIGHTSHADE),
                List.of(RarityFilter.onAverageOnceEvery(48),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                        BiomeFilter.biome()));

        register(context, WILD_WATER_HEMLOCK_PLACED, configuredFeatures.getOrThrow(ToxonyConfiguredFeatures.WILD_WATER_HEMLOCK),
                List.of(RarityFilter.onAverageOnceEvery(48),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                        BiomeFilter.biome()));

        register(context, WILD_COLDSNAP_PLACED, configuredFeatures.getOrThrow(ToxonyConfiguredFeatures.WILD_COLDSNAP),
                List.of(RarityFilter.onAverageOnceEvery(48),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                        BiomeFilter.biome()));

        register(context, PATCH_WILD_BLOODROOT_PLACED, configuredFeatures.getOrThrow(ToxonyConfiguredFeatures.PATCH_WILD_BLOODROOT),
                List.of(RarityFilter.onAverageOnceEvery(4),
                        InSquarePlacement.spread(),
                        PlacementUtils.FULL_RANGE,
                        BiomeFilter.biome()));

       register(context, ORE_ANCIENT_SILVER_SMALL, configuredFeatures.getOrThrow(ToxonyConfiguredFeatures.ORE_ANCIENT_SILVER_SMALL),
               List.of(RarityFilter.onAverageOnceEvery(2),
                       InSquarePlacement.spread(),
                       HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(-32)),
                       BiomeFilter.biome()
               )
       );

    }

    public static ResourceKey<PlacedFeature> registerKey(String name){
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, name));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key,
                                 Holder<ConfiguredFeature<?,?>> configuration, List<PlacementModifier> modifiers){

        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
