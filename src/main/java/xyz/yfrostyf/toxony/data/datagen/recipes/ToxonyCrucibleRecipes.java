package xyz.yfrostyf.toxony.data.datagen.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.ItemStack;
import xyz.yfrostyf.toxony.data.datagen.recipebuilders.CrucibleRecipeBuilder;
import xyz.yfrostyf.toxony.registries.ItemRegistry;

import java.util.concurrent.CompletableFuture;

public class ToxonyCrucibleRecipes extends RecipeProvider {
    public ToxonyCrucibleRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    public static void get(RecipeOutput output) {
        //
        // Crucible Recipes
        //
        new CrucibleRecipeBuilder(new ItemStack(ItemRegistry.TOXIC_PASTE))
                .ingredient(ItemRegistry.POISON_PASTE.get())
                .cookTime(400)
                .unlockedByItems("has_poison_paste", ItemRegistry.POISON_PASTE.get())
                .build(output);

        new CrucibleRecipeBuilder(new ItemStack(ItemRegistry.REDSTONE_SOLUTION))
                .ingredient(ItemRegistry.REDSTONE_MIXTURE.get())
                .cookTime(600)
                .unlockedByItems("has_redstone_mixture", ItemRegistry.REDSTONE_MIXTURE.get())
                .build(output);
    }
}
