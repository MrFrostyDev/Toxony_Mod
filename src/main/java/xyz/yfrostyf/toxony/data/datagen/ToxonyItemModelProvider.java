package xyz.yfrostyf.toxony.data.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.registries.ItemRegistry;

import java.util.concurrent.CompletableFuture;

public class ToxonyItemModelProvider extends ItemModelProvider {

    public ToxonyItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ToxonyMain.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent(ItemRegistry.GUIDED_SPIRIT_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
    }
}
