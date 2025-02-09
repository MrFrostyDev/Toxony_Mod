package xyz.yfrostyf.toxony.data.datagen.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import xyz.yfrostyf.toxony.data.recipebuilders.MortarPestleRecipeBuilder;
import xyz.yfrostyf.toxony.registries.ItemRegistry;

import java.util.concurrent.CompletableFuture;

public class ToxonyMortarRecipes extends RecipeProvider {
    public ToxonyMortarRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    public static void get(RecipeOutput output) {
        //
        // Mortar Recipes
        //
        new MortarPestleRecipeBuilder(new ItemStack(ItemRegistry.CUB_BLEND.get()))
                .ingredient(Items.BONE)
                .ingredient(Items.FERN)
                .ingredient(Items.POISONOUS_POTATO)
                .use(Items.BOWL)
                .unlockedByItems("has_bone", Items.BONE)
                .build(output);
    }
}
