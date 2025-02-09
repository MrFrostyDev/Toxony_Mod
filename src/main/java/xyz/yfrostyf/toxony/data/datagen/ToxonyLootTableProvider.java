package xyz.yfrostyf.toxony.data.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import xyz.yfrostyf.toxony.data.loot.ToxonyBlockLoot;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ToxonyLootTableProvider extends LootTableProvider{
    public ToxonyLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(
                output,
                Collections.emptySet(),
                List.of(
                    new SubProviderEntry(ToxonyBlockLoot::new, LootContextParamSets.BLOCK)
                ),
                registries
        );
    }
}
