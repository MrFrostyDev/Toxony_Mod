package xyz.yfrostyf.toxony.data.datagen.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import xyz.yfrostyf.toxony.data.datagen.recipebuilders.AlembicRecipeBuilder;
import xyz.yfrostyf.toxony.registries.ItemRegistry;
import xyz.yfrostyf.toxony.registries.TagRegistry;

import java.util.concurrent.CompletableFuture;

public class ToxonyAlembicRecipes extends RecipeProvider {

    public ToxonyAlembicRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    public static void get(RecipeOutput output){
        new AlembicRecipeBuilder(PotionContents.createItemStack(Items.POTION, Potions.WATER))
                .ingredient(Items.HONEY_BOTTLE)
                .ingredientToConvert(Items.GLASS_BOTTLE)
                .boilTime(200)
                .unlockedByItems("has_honey", Items.HONEY_BOTTLE)
                .build(output);

        new AlembicRecipeBuilder(new ItemStack(ItemRegistry.AFFINITY_SOLUTION.get()))
                .ingredient(TagRegistry.POISONOUS_PLANTS_ITEM_TAG)
                .ingredientToConvert(ItemRegistry.REDSTONE_SOLUTION.get())
                .boilTime(800)
                .unlockedByItems("has_redstone_solution", ItemRegistry.REDSTONE_SOLUTION.get())
                .build(output);

        new AlembicRecipeBuilder(new ItemStack(ItemRegistry.AFFINITY_SOLUTION.get()))
                .ingredient(TagRegistry.POISONOUS_INGREDIENTS_ITEM_TAG)
                .ingredientToConvert(ItemRegistry.REDSTONE_SOLUTION.get())
                .boilTime(800)
                .unlockedByItems("has_redstone_solution", ItemRegistry.REDSTONE_SOLUTION.get())
                .suffix("_alt")
                .build(output);
    }
}
