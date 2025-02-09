package xyz.yfrostyf.toxony.data.datagen.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import xyz.yfrostyf.toxony.data.recipebuilders.CrucibleRecipeBuilder;

import java.util.concurrent.CompletableFuture;

public class ToxonyCrucibleRecipes extends RecipeProvider {
    public ToxonyCrucibleRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    public static void get(RecipeOutput output) {
        //
        // Crucible Recipes
        //
        new CrucibleRecipeBuilder(new ItemStack(Items.POISONOUS_POTATO))
                .ingredient(Items.BONE)
                .cookTime(200)
                .unlockedByItems("has_bone", Items.BONE)
                .build(output);
    }
}
