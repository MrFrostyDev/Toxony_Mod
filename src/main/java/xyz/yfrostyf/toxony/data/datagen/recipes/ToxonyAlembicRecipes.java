package xyz.yfrostyf.toxony.data.datagen.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import xyz.yfrostyf.toxony.data.datagen.recipebuilders.AlembicRecipeBuilder;

import java.util.concurrent.CompletableFuture;

public class ToxonyAlembicRecipes extends RecipeProvider {

    public ToxonyAlembicRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    public static void get(RecipeOutput output){
        new AlembicRecipeBuilder(PotionContents.createItemStack(Items.POTION, Potions.WATER))
                .ingredient(Items.HONEY_BOTTLE)
                .boilTime(200)
                .unlockedByItems("has_honey", Items.HONEY_BOTTLE)
                .build(output);
    }
}
