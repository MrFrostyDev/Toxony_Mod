package xyz.yfrostyf.toxony.worldgen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.internal.NeoForgeBiomeTagsProvider;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import xyz.yfrostyf.toxony.ToxonyMain;

// thank you [Modding by Kaupenjoe]

public class ToxonyBiomeModifier {

    public static final ResourceKey<BiomeModifier> ADD_PATCH_FALSE_BERRY_BUSH = registerKey("add_patch_false_berry_bush");
    public static final ResourceKey<BiomeModifier> ADD_WILD_OCELOT_MINT = registerKey("add_wild_ocelot_mint");
    public static final ResourceKey<BiomeModifier> ADD_WILD_NIGHTSHADE = registerKey("add_wild_nightshade");
    public static final ResourceKey<BiomeModifier> ADD_WILD_WATER_HEMLOCK = registerKey("add_wild_water_hemlock");
    public static final ResourceKey<BiomeModifier> ADD_WILD_COLDSNAP = registerKey("add_wild_coldsnap");
    public static final ResourceKey<BiomeModifier> ADD_PATCH_WILD_BLOODROOT = registerKey("add_patch_wild_bloodroot");
    public static final ResourceKey<BiomeModifier> ADD_ORE_ANCIENT_SILVER_SMALL = registerKey("add_ore_ancient_silver_small");

    public static void bootstrap(BootstrapContext<BiomeModifier> context){
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);

        context.register(ADD_PATCH_FALSE_BERRY_BUSH, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(Tags.Biomes.IS_TAIGA),
                HolderSet.direct(placedFeatures.getOrThrow(ToxonyPlacedFeature.PATCH_FALSE_BERRY_BUSH_PLACED)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));

        context.register(ADD_WILD_OCELOT_MINT, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(Tags.Biomes.IS_JUNGLE),
                HolderSet.direct(placedFeatures.getOrThrow(ToxonyPlacedFeature.WILD_OCELOT_MINT_PLACED)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));

        context.register(ADD_WILD_NIGHTSHADE, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.HAS_WOODLAND_MANSION),
                HolderSet.direct(placedFeatures.getOrThrow(ToxonyPlacedFeature.WILD_NIGHTSHADE_PLACED)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));

        context.register(ADD_WILD_WATER_HEMLOCK, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(Tags.Biomes.IS_SWAMP),
                HolderSet.direct(placedFeatures.getOrThrow(ToxonyPlacedFeature.WILD_WATER_HEMLOCK_PLACED)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));

        context.register(ADD_WILD_COLDSNAP, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(Tags.Biomes.IS_COLD_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(ToxonyPlacedFeature.WILD_COLDSNAP_PLACED)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));

        context.register(ADD_PATCH_WILD_BLOODROOT, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.CRIMSON_FOREST)),
                HolderSet.direct(placedFeatures.getOrThrow(ToxonyPlacedFeature.PATCH_WILD_BLOODROOT_PLACED)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));

        context.register(ADD_ORE_ANCIENT_SILVER_SMALL, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(Tags.Biomes.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(ToxonyPlacedFeature.ORE_ANCIENT_SILVER_SMALL)),
                GenerationStep.Decoration.UNDERGROUND_ORES
        ));
    }

    private static ResourceKey<BiomeModifier> registerKey(String name){
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, name));
    }
}
