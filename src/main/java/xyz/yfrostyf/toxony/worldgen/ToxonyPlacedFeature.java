package xyz.yfrostyf.toxony.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import xyz.yfrostyf.toxony.ToxonyMain;

import java.util.List;

// thank you [Modding by Kaupenjoe]

public class ToxonyPlacedFeature {

    public static final ResourceKey<PlacedFeature> PATCH_FALSE_BERRY_BUSH_PLACED = registerKey("false_berry_bush_placed");
    public static final ResourceKey<PlacedFeature> WILD_OCELOT_MINT_PLACED = registerKey("wild_ocelot_mint_placed");
    public static final ResourceKey<PlacedFeature> WILD_NIGHTSHADE_PLACED = registerKey("wild_nightshade_placed");
    public static final ResourceKey<PlacedFeature> WILD_WATER_HEMLOCK_PLACED = registerKey("wild_water_hemlock_placed");


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
    }

    public static ResourceKey<PlacedFeature> registerKey(String name){
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, name));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key,
                                 Holder<ConfiguredFeature<?,?>> configuration, List<PlacementModifier> modifiers){

        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
