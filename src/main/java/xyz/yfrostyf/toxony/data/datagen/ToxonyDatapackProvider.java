package xyz.yfrostyf.toxony.data.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.data.datagen.enchantments.ToxonyEnchantments;
import xyz.yfrostyf.toxony.worldgen.ToxonyBiomeModifier;
import xyz.yfrostyf.toxony.worldgen.ToxonyConfiguredFeatures;
import xyz.yfrostyf.toxony.worldgen.ToxonyPlacedFeature;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

// thank you [Modding by Kaupenjoe]

public class ToxonyDatapackProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, ToxonyConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, ToxonyPlacedFeature::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ToxonyBiomeModifier::bootstrap)
            .add(Registries.ENCHANTMENT, ToxonyEnchantments::bootstrap);

    public ToxonyDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(ToxonyMain.MOD_ID));
    }
}
