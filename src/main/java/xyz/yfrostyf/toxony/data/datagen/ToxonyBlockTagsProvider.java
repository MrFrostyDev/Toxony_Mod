package xyz.yfrostyf.toxony.data.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
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
        this.tag(TagRegistry.POISONOUS_PLANTS_BLOCK)
                .add(
                        BlockRegistry.WILD_OCELOT_MINT.get(),
                        BlockRegistry.WILD_NIGHTSHADE.get(),
                        BlockRegistry.WILD_WATER_HEMLOCK.get(),
                        BlockRegistry.WILD_COLDSNAP.get(),
                        BlockRegistry.WILD_BLOODROOT.get(),
                        BlockRegistry.OCELOT_MINT.get(),
                        BlockRegistry.SNOW_MINT.get(),
                        BlockRegistry.NIGHTSHADE.get(),
                        BlockRegistry.SUNSPOT.get(),
                        BlockRegistry.WATER_HEMLOCK.get(),
                        BlockRegistry.MOONLIGHT_HEMLOCK.get(),
                        BlockRegistry.COLDSNAP.get(),
                        BlockRegistry.WHIRLSNAP.get(),
                        BlockRegistry.BLOODROOT.get(),
                        BlockRegistry.WARPROOT.get()
                );

        this.tag(TagRegistry.OPEN_FLAME)
                .add(
                        Blocks.TORCH,
                        Blocks.TORCHFLOWER,
                        Blocks.CAMPFIRE,
                        Blocks.SOUL_CAMPFIRE,
                        Blocks.FIRE,
                        Blocks.WALL_TORCH,
                        Blocks.SOUL_WALL_TORCH
                );

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(
                        BlockRegistry.REDSTONE_MORTAR.get(),
                        BlockRegistry.ANCIENT_SILVER.get(),
                        BlockRegistry.ALCHEMICAL_FORGE.get(),
                        BlockRegistry.ALCHEMICAL_FORGE_PART.get()
                );

        this.tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(
                        BlockRegistry.ANCIENT_SILVER.get(),
                        BlockRegistry.ALCHEMICAL_FORGE.get(),
                        BlockRegistry.ALCHEMICAL_FORGE_PART.get()
                );

        this.tag(BlockTags.CANDLE_CAKES)
                .add(
                        BlockRegistry.TOXIC_CAKE.get(),
                        BlockRegistry.CANDLE_TOXIC_CAKE.get(),
                        BlockRegistry.WHITE_CANDLE_TOXIC_CAKE.get(),
                        BlockRegistry.ORANGE_CANDLE_TOXIC_CAKE.get(),
                        BlockRegistry.MAGENTA_CANDLE_TOXIC_CAKE.get(),
                        BlockRegistry.LIGHT_BLUE_CANDLE_TOXIC_CAKE.get(),
                        BlockRegistry.YELLOW_CANDLE_TOXIC_CAKE.get(),
                        BlockRegistry.LIME_CANDLE_TOXIC_CAKE.get(),
                        BlockRegistry.PINK_CANDLE_TOXIC_CAKE.get(),
                        BlockRegistry.GRAY_CANDLE_TOXIC_CAKE.get(),
                        BlockRegistry.LIGHT_GRAY_CANDLE_TOXIC_CAKE.get(),
                        BlockRegistry.CYAN_CANDLE_TOXIC_CAKE.get(),
                        BlockRegistry.PURPLE_CANDLE_TOXIC_CAKE.get(),
                        BlockRegistry.BLUE_CANDLE_TOXIC_CAKE.get(),
                        BlockRegistry.BROWN_CANDLE_TOXIC_CAKE.get(),
                        BlockRegistry.GREEN_CANDLE_TOXIC_CAKE.get(),
                        BlockRegistry.RED_CANDLE_TOXIC_CAKE.get(),
                        BlockRegistry.BLACK_CANDLE_TOXIC_CAKE.get()
                );
    }
}
