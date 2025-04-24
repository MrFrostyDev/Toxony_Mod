package xyz.yfrostyf.toxony.data.datagen.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import xyz.yfrostyf.toxony.data.datagen.recipebuilders.AlembicRecipeBuilder;
import xyz.yfrostyf.toxony.registries.ItemRegistry;
import xyz.yfrostyf.toxony.registries.TagRegistry;

import java.util.concurrent.CompletableFuture;

public class ToxonyAlembicRecipes extends RecipeProvider {

    public ToxonyAlembicRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    public static void get(RecipeOutput output){
        new AlembicRecipeBuilder(new ItemStack(ItemRegistry.TOXIN.get()))
                .ingredient(ItemRegistry.TOXIC_FORMULA.get())
                .ingredientToConvert(ItemRegistry.GLASS_VIAL.get())
                .remainingItem(new ItemStack(ItemRegistry.GLASS_VIAL.get()))
                .boilTime(800)
                .unlockedByItems("has_toxic_formula", ItemRegistry.TOXIC_FORMULA.get())
                .build(output);

        new AlembicRecipeBuilder(new ItemStack(ItemRegistry.TOXIC_LEATHER.get(), 2))
                .ingredient(ItemRegistry.TOXIN.get())
                .ingredientToConvert(Items.LEATHER)
                .remainingItem(new ItemStack(ItemRegistry.GLASS_VIAL.get()))
                .boilTime(800)
                .unlockedByItems("has_toxin", ItemRegistry.TOXIN.get())
                .build(output);

        new AlembicRecipeBuilder(new ItemStack(ItemRegistry.EMPTY_TOX_POT.get()))
                .ingredient(ItemRegistry.TOXIN.get())
                .ingredientToConvert(ItemRegistry.EMPTY_OIL_POT.get())
                .remainingItem(new ItemStack(ItemRegistry.GLASS_VIAL.get()))
                .boilTime(800)
                .unlockedByItems("has_toxin", ItemRegistry.TOXIN.get())
                .build(output);

        new AlembicRecipeBuilder(new ItemStack(ItemRegistry.AFFINITY_SOLUTION.get()))
                .ingredient(TagRegistry.POISONOUS_PLANTS_ITEM)
                .ingredientToConvert(ItemRegistry.REDSTONE_SOLUTION.get())
                .boilTime(800)
                .unlockedByItems("has_redstone_solution", ItemRegistry.REDSTONE_SOLUTION.get())
                .build(output);

        new AlembicRecipeBuilder(new ItemStack(ItemRegistry.AFFINITY_SOLUTION.get()))
                .ingredient(TagRegistry.POISONOUS_INGREDIENTS_ITEM)
                .ingredientToConvert(ItemRegistry.REDSTONE_SOLUTION.get())
                .boilTime(800)
                .unlockedByItems("has_redstone_solution", ItemRegistry.REDSTONE_SOLUTION.get())
                .suffix("_alt")
                .build(output);
    }
}
