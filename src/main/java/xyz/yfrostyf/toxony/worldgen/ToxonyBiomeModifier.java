package xyz.yfrostyf.toxony.worldgen;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
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

    public static void bootstrap(BootstrapContext<BiomeModifier> context){
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        context.register(ADD_PATCH_FALSE_BERRY_BUSH, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.TAIGA)),
                HolderSet.direct(placedFeatures.getOrThrow(ToxonyPlacedFeature.PATCH_FALSE_BERRY_BUSH_PLACED)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));

        context.register(ADD_WILD_OCELOT_MINT, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.JUNGLE), biomes.getOrThrow(Biomes.SPARSE_JUNGLE)),
                HolderSet.direct(placedFeatures.getOrThrow(ToxonyPlacedFeature.WILD_OCELOT_MINT_PLACED)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));

        context.register(ADD_WILD_NIGHTSHADE, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.DARK_FOREST)),
                HolderSet.direct(placedFeatures.getOrThrow(ToxonyPlacedFeature.WILD_NIGHTSHADE_PLACED)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));

        context.register(ADD_WILD_WATER_HEMLOCK, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.SWAMP), biomes.getOrThrow(Biomes.MANGROVE_SWAMP)),
                HolderSet.direct(placedFeatures.getOrThrow(ToxonyPlacedFeature.WILD_WATER_HEMLOCK_PLACED)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));

        context.register(ADD_WILD_COLDSNAP, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.TAIGA), biomes.getOrThrow(Biomes.SNOWY_TAIGA),
                        biomes.getOrThrow(Biomes.SNOWY_PLAINS), biomes.getOrThrow(Biomes.SNOWY_SLOPES)),
                HolderSet.direct(placedFeatures.getOrThrow(ToxonyPlacedFeature.WILD_COLDSNAP_PLACED)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));

        context.register(ADD_PATCH_WILD_BLOODROOT, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.CRIMSON_FOREST)),
                HolderSet.direct(placedFeatures.getOrThrow(ToxonyPlacedFeature.PATCH_WILD_BLOODROOT_PLACED)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));
    }

    private static ResourceKey<BiomeModifier> registerKey(String name){
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, name));
    }
}
