package xyz.yfrostyf.toxony.data.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import xyz.yfrostyf.toxony.data.datagen.recipes.ToxonyAlembicRecipes;
import xyz.yfrostyf.toxony.data.datagen.recipes.ToxonyCraftingTableRecipes;
import xyz.yfrostyf.toxony.data.datagen.recipes.ToxonyCrucibleRecipes;
import xyz.yfrostyf.toxony.data.datagen.recipes.ToxonyMortarRecipes;

import java.util.concurrent.CompletableFuture;

public class ToxonyRecipeProvider extends RecipeProvider  {

    public ToxonyRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        ToxonyCraftingTableRecipes.get(output);
        ToxonyMortarRecipes.get(output);
        ToxonyCrucibleRecipes.get(output);
        ToxonyAlembicRecipes.get(output);
    }
}
