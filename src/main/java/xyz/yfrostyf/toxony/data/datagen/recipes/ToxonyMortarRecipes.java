package xyz.yfrostyf.toxony.data.datagen.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import xyz.yfrostyf.toxony.data.datagen.recipebuilders.MortarPestleRecipeBuilder;
import xyz.yfrostyf.toxony.registries.ItemRegistry;
import xyz.yfrostyf.toxony.registries.TagRegistry;

import java.util.concurrent.CompletableFuture;

public class ToxonyMortarRecipes extends RecipeProvider {
    public ToxonyMortarRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    public static void get(RecipeOutput output) {
        new MortarPestleRecipeBuilder(new ItemStack(ItemRegistry.POISON_PASTE.get()))
                .ingredient(Items.BONE_MEAL)
                .ingredient(TagRegistry.POISONOUS_INGREDIENTS_ITEM_TAG)
                .ingredient(TagRegistry.POISONOUS_PLANTS_ITEM_TAG)
                .use(Items.BOWL)
                .unlockedByTag("has_poisonous_ingredient", TagRegistry.POISONOUS_INGREDIENTS_ITEM_TAG)
                .unlockedByTag("has_poisonous_plant", TagRegistry.POISONOUS_PLANTS_ITEM_TAG)
                .build(output);

        // Blend Recipes
        new MortarPestleRecipeBuilder(new ItemStack(ItemRegistry.POISON_BLEND.get()))
                .ingredient(ItemRegistry.POISON_PASTE.get())
                .ingredient(TagRegistry.POISONOUS_INGREDIENTS_ITEM_TAG)
                .use(Items.BOWL)
                .unlockedByItems("has_poisonous_paste", ItemRegistry.POISON_PASTE.get())
                .build(output);

        new MortarPestleRecipeBuilder(new ItemStack(ItemRegistry.POISON_BLEND.get()))
                .ingredient(ItemRegistry.POISON_PASTE.get())
                .ingredient(TagRegistry.POISONOUS_PLANTS_ITEM_TAG)
                .use(Items.BOWL)
                .unlockedByItems("has_poisonous_paste", ItemRegistry.POISON_PASTE.get())
                .suffix("_alt")
                .build(output);

        new MortarPestleRecipeBuilder(new ItemStack(ItemRegistry.TOXIC_BLEND.get()))
                .ingredient(ItemRegistry.TOXIC_PASTE.get())
                .ingredient(TagRegistry.POISONOUS_INGREDIENTS_ITEM_TAG)
                .ingredient(TagRegistry.POISONOUS_PLANTS_ITEM_TAG)
                .use(Items.BOWL)
                .unlockedByItems("has_toxic_paste", ItemRegistry.TOXIC_PASTE.get())
                .build(output);

        new MortarPestleRecipeBuilder(new ItemStack(ItemRegistry.PURE_BLEND.get()))
                .ingredient(ItemRegistry.TOXIN.get())
                .ingredient(TagRegistry.POISONOUS_INGREDIENTS_ITEM_TAG)
                .ingredient(TagRegistry.POISONOUS_PLANTS_ITEM_TAG)
                .ingredient(TagRegistry.POISONOUS_PLANTS_ITEM_TAG)
                .use(Items.BOWL)
                .unlockedByItems("has_poisonous_paste", ItemRegistry.POISON_PASTE.get())

                .build(output);

        new MortarPestleRecipeBuilder(new ItemStack(ItemRegistry.PURE_BLEND.get()))
                .ingredient(ItemRegistry.TOXIN.get())
                .ingredient(TagRegistry.POISONOUS_INGREDIENTS_ITEM_TAG)
                .ingredient(TagRegistry.POISONOUS_INGREDIENTS_ITEM_TAG)
                .ingredient(TagRegistry.POISONOUS_PLANTS_ITEM_TAG)
                .use(Items.BOWL)
                .unlockedByItems("has_poisonous_paste", ItemRegistry.POISON_PASTE.get())
                .suffix("_alt")
                .build(output);
    }


}
