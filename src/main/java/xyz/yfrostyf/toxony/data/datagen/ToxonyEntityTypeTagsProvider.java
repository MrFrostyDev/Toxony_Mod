package xyz.yfrostyf.toxony.data.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.util.CompatibilityUtil;
import xyz.yfrostyf.toxony.registries.TagRegistry;

import java.util.concurrent.CompletableFuture;

public class ToxonyEntityTypeTagsProvider extends EntityTypeTagsProvider {
    public ToxonyEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ToxonyMain.MOD_ID, existingFileHelper);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        this.tag(TagRegistry.SILVER_VULNERABLE)
                .addOptionalTags(
                        CompatibilityUtil.VAMPIRE
                );
    }
}
