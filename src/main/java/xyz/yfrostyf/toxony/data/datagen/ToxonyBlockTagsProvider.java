package xyz.yfrostyf.toxony.data.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.registries.BlockRegistry;
import xyz.yfrostyf.toxony.registries.TagRegistry;

import java.util.concurrent.CompletableFuture;

public class ToxonyBlockTagsProvider extends BlockTagsProvider {

    // Get parameters from one of the `GatherDataEvent`s.
    public ToxonyBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ToxonyMain.MOD_ID, existingFileHelper);
    }

    // Add your tag entries here.
    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        this.tag(TagRegistry.POISONOUS_PLANTS_BLOCK_TAG)
                .add(
                        BlockRegistry.WILD_OCELOT_MINT.get(),
                        BlockRegistry.OCELOT_MINT.get()
                );
    }
}
