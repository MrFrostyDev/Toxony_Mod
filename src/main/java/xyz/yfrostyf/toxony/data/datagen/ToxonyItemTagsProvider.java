package xyz.yfrostyf.toxony.data.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import xyz.yfrostyf.toxony.registries.ItemRegistry;
import xyz.yfrostyf.toxony.registries.TagRegistry;

import java.util.concurrent.CompletableFuture;

public class ToxonyItemTagsProvider extends ItemTagsProvider {


    public ToxonyItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookupProvider, blockTags);
    }

    // Add your tag entries here.
    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        this.tag(ItemTags.WEAPON_ENCHANTABLE)
                .add(
                        ItemRegistry.COPPER_SCALPEL.get(),
                        ItemRegistry.NETHERITE_SCALPEL.get(),
                        ItemRegistry.LETHAL_DOSE.get()
                );

        this.tag(TagRegistry.SCALPEL_ITEM)
                .add(
                        ItemRegistry.COPPER_SCALPEL.get(),
                        ItemRegistry.NETHERITE_SCALPEL.get(),
                        ItemRegistry.LETHAL_DOSE.get()
                );

        this.tag(TagRegistry.POISONOUS_INGREDIENTS_ITEM)
                .add(
                        Items.SPIDER_EYE,
                        Items.PUFFERFISH,
                        ItemRegistry.BRITTLE_SCUTE.get(),
                        ItemRegistry.WOLF_TOOTH.get()

                );

        this.tag(TagRegistry.POISONOUS_PLANTS_ITEM)
                .add(
                        Items.POISONOUS_POTATO,
                        ItemRegistry.FALSE_BERRIES.get(),
                        ItemRegistry.OCELOT_MINT.get(),
                        ItemRegistry.NIGHTSHADE.get(),
                        ItemRegistry.WATER_HEMLOCK.get()
                );
    }
}
