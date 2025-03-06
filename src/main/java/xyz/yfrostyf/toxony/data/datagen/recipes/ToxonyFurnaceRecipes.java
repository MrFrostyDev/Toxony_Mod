package xyz.yfrostyf.toxony.data.datagen.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import xyz.yfrostyf.toxony.registries.ItemRegistry;

import java.util.concurrent.CompletableFuture;

public class ToxonyFurnaceRecipes extends RecipeProvider {

    public ToxonyFurnaceRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    public static void get(RecipeOutput output){
        // Use #smoking for smoking recipes, #blasting for blasting recipes, and #campfireCooking for campfire recipes.
        // All of these builders work the same otherwise.
        SimpleCookingRecipeBuilder.smelting(
                // Our input ingredient.
                Ingredient.of(ItemRegistry.CLAY_OIL_POT.get()),
                // Our recipe category.
                RecipeCategory.MISC,
                // Our result item. May also be an ItemStack.
                ItemRegistry.EMPTY_OIL_POT.get(),
                // Our experience reward
                0.5f,
                // Our cooking time.
                400
                )
                // The recipe advancement, like with the crafting recipes above.
                .unlockedBy("has_clay_pot", has(ItemRegistry.CLAY_OIL_POT.get()))
                // This overload of #save allows us to specify a name.
                .save(output);

        SimpleCookingRecipeBuilder.campfireCooking(
                Ingredient.of(ItemRegistry.CLAY_OIL_POT.get()),
                RecipeCategory.MISC,
                ItemRegistry.EMPTY_OIL_POT.get(),
                0.5f,
                1000
                )
                .unlockedBy("has_clay_pot", has(ItemRegistry.CLAY_OIL_POT.get()))
                .save(output, "clay_oil_pot_campfire");
    }
}
