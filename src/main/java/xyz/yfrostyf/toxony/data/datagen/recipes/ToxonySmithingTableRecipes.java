package xyz.yfrostyf.toxony.data.datagen.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import xyz.yfrostyf.toxony.registries.ItemRegistry;

import java.util.concurrent.CompletableFuture;

public class ToxonySmithingTableRecipes extends RecipeProvider {

    public ToxonySmithingTableRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    public static void get(RecipeOutput output) {
        netheriteSmithing(output,
                ItemRegistry.COPPER_SCALPEL.get(),
                RecipeCategory.TOOLS,
                ItemRegistry.NETHERITE_SCALPEL.get());

    }
}
