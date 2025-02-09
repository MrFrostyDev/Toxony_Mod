package xyz.yfrostyf.toxony.data.datagen.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import xyz.yfrostyf.toxony.data.recipebuilders.AlembicRecipeBuilder;
import xyz.yfrostyf.toxony.registries.ItemRegistry;

import java.util.concurrent.CompletableFuture;

public class ToxonyCraftingTableRecipes extends RecipeProvider {

    public ToxonyCraftingTableRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    public static void get(RecipeOutput output){
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.IRON_PICKAXE)
                // Create the lines of your pattern. Each call to #pattern adds a new line.
                // Patterns will be validated, i.e. their shape will be checked.
                .pattern("XXX")
                .pattern(" # ")
                .pattern(" # ")
                // Create the keys for the pattern. All non-space characters used in the pattern must be defined.
                // This can either accept Ingredients, TagKey<Item>s or ItemLikes, i.e. items or blocks.
                .define('X', Items.IRON_INGOT)
                .define('#', Items.STICK)
                // Creates the recipe advancement. While not mandated by the consuming background systems,
                // the recipe builder will crash if you omit this. The first parameter is the advancement name,
                // and the second one is the condition. Normally, you want to use the has() shortcut for the condition.
                // Multiple advancement requirements can be added by calling #unlockedBy multiple times.
                .unlockedBy("has_iron_ingot", RecipeProvider.has(Items.IRON_INGOT))
                // Stores the recipe in the passed RecipeOutput, to be written to disk.
                // If you want to add conditions to the recipe, those can be set on the output.
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.CLAY_OIL_POT.get())
                .pattern("   ")
                .pattern("#X#")
                .pattern(" # ")
                .define('X', ItemRegistry.POISON_PASTE.get())
                .define('#', Items.CLAY_BALL)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(output);
    }
}
