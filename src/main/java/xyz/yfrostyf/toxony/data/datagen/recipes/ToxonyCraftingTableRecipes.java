package xyz.yfrostyf.toxony.data.datagen.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import xyz.yfrostyf.toxony.registries.ItemRegistry;
import xyz.yfrostyf.toxony.registries.TagRegistry;

import java.util.concurrent.CompletableFuture;

public class    ToxonyCraftingTableRecipes extends RecipeProvider {

    public ToxonyCraftingTableRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    public static void get(RecipeOutput output){
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.CLAY_OIL_POT.get())
                // Create the lines of your pattern. Each call to #pattern adds a new line.
                // Patterns will be validated, i.e. their shape will be checked.
                .pattern("   ")
                .pattern("#X#")
                .pattern(" # ")
                // Create the keys for the pattern. All non-space characters used in the pattern must be defined.
                // This can either accept Ingredients, TagKey<Item>s or ItemLikes, i.e. items or blocks.
                .define('X', ItemRegistry.POISON_PASTE.get())
                .define('#', Items.CLAY_BALL)
                // Creates the recipe advancement. While not mandated by the consuming background systems,
                // the recipe builder will crash if you omit this. The first parameter is the advancement name,
                // and the second one is the condition. Normally, you want to use the has() shortcut for the condition.
                // Multiple advancement requirements can be added by calling #unlockedBy multiple times.
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                // Stores the recipe in the passed RecipeOutput, to be written to disk.
                // If you want to add conditions to the recipe, those can be set on the output.
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.ALEMBIC.get())
                .pattern("C  ")
                .pattern("CI ")
                .pattern("B C")
                .define('B', ItemRegistry.ALEMBIC_BASE.get())
                .define('C', Items.COPPER_INGOT)
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.ALEMBIC_BASE.get())
                .pattern(" Q ")
                .pattern("CGC")
                .pattern("CCC")
                .define('G', Items.GOLD_INGOT)
                .define('C', Items.COPPER_INGOT)
                .define('Q', Items.QUARTZ)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemRegistry.REDSTONE_MIXTURE.get())
                .requires(ItemRegistry.TOXIC_PASTE.get())
                .requires(DataComponentIngredient.of(false, PotionContents.createItemStack(ItemRegistry.TOX_VIAL.get(), Potions.WATER)))
                .requires(Items.REDSTONE)
                .unlockedBy("has_toxic_paste", has(ItemRegistry.TOXIC_PASTE.get()))
                .save(output);

    }
}
