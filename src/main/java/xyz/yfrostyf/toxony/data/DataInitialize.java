package xyz.yfrostyf.toxony.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import xyz.yfrostyf.toxony.data.datagen.*;


import java.util.concurrent.CompletableFuture;

public class DataInitialize {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        ToxonyBlockTagsProvider blockTagsProvider =  new ToxonyBlockTagsProvider(output, lookupProvider, existingFileHelper);

        generator.addProvider(event.includeServer(), new ToxonyDatapackProvider(output, lookupProvider));
        generator.addProvider(event.includeServer(), new ToxonyLootTableProvider(output, lookupProvider));
        generator.addProvider(event.includeServer(), new ToxonyGlobalLootModifierProvider(output, lookupProvider));
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new ToxonyItemTagsProvider(output, lookupProvider, blockTagsProvider.contentsGetter()));
        generator.addProvider(event.includeServer(), new ToxonyRecipeProvider(output, lookupProvider));
        generator.addProvider(event.includeClient(), new ToxonySoundDefinitionsProvider(output, existingFileHelper));
    }
}
