package xyz.yfrostyf.toxony.data.datagen.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
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
                .ingredient(TagRegistry.POISONOUS_INGREDIENTS_ITEM)
                .ingredient(TagRegistry.POISONOUS_PLANTS_ITEM)
                .unlockedByTag("has_poisonous_ingredient", TagRegistry.POISONOUS_INGREDIENTS_ITEM)
                .unlockedByTag("has_poisonous_plant", TagRegistry.POISONOUS_PLANTS_ITEM)
                .build(output);

        new MortarPestleRecipeBuilder(new ItemStack(ItemRegistry.AFFINITY_FUSION_MIX.get()))
                .possibleIngredient()
                .possibleIngredient()
                .possibleIngredient()
                .ingredient(Items.NETHER_WART)
                .unlockedByTag("has_poisonous_ingredient", TagRegistry.POISONOUS_INGREDIENTS_ITEM)
                .unlockedByTag("has_poisonous_plant", TagRegistry.POISONOUS_PLANTS_ITEM)
                .build(output);

        // Oil Recipes
        // Oils Tier 0 Recipes
        new MortarPestleRecipeBuilder(new ItemStack(ItemRegistry.POISON_OIL_POT.get()))
                .ingredient(Items.HONEYCOMB)
                .ingredient(ItemRegistry.POISON_PASTE.get())
                .use(ItemRegistry.EMPTY_OIL_POT.get())
                .unlockedByItems("has_empty_oil_pot", ItemRegistry.EMPTY_OIL_POT.get())
                .build(output);

        new MortarPestleRecipeBuilder(new ItemStack(ItemRegistry.FIRE_RESISTANCE_OIL_POT.get()))
                .ingredient(Items.HONEYCOMB)
                .ingredient(Items.MAGMA_CREAM)
                .use(ItemRegistry.EMPTY_OIL_POT.get())
                .unlockedByItems("has_empty_oil_pot", ItemRegistry.EMPTY_OIL_POT.get())
                .build(output);

        new MortarPestleRecipeBuilder(new ItemStack(ItemRegistry.GLOWING_OIL_POT.get()))
                .ingredient(Items.HONEYCOMB)
                .ingredient(Items.GLOW_INK_SAC)
                .use(ItemRegistry.EMPTY_OIL_POT.get())
                .unlockedByItems("has_empty_oil_pot", ItemRegistry.EMPTY_OIL_POT.get())
                .build(output);

        new MortarPestleRecipeBuilder(new ItemStack(ItemRegistry.FATIGUE_OIL_POT.get()))
                .ingredient(Items.HONEYCOMB)
                .ingredient(Items.FERMENTED_SPIDER_EYE)
                .ingredient(ItemRegistry.WATER_HEMLOCK.get())
                .use(ItemRegistry.EMPTY_OIL_POT.get())
                .unlockedByItems("has_empty_oil_pot", ItemRegistry.EMPTY_OIL_POT.get())
                .build(output);


        // Oil Tier 1 Recipes
        new MortarPestleRecipeBuilder(new ItemStack(ItemRegistry.ACID_OIL_POT.get()))
                .ingredient(Items.HONEYCOMB)
                .ingredient(ItemRegistry.TOXIC_PASTE.get())
                .ingredient(ItemRegistry.ACID_SLIMEBALL.get())
                .ingredient(ItemRegistry.ACID_SLIMEBALL.get())
                .use(ItemRegistry.EMPTY_OIL_POT.get())
                .unlockedByItems("has_empty_oil_pot", ItemRegistry.EMPTY_OIL_POT.get())
                .build(output);

        new MortarPestleRecipeBuilder(new ItemStack(ItemRegistry.MENDING_OIL_POT.get()))
                .ingredient(Items.HONEYCOMB)
                .ingredient(ItemRegistry.TOXIC_PASTE.get())
                .ingredient(ItemRegistry.TOXIC_SPIT.get())
                .ingredient(ItemRegistry.OCELOT_MINT.get())
                .use(ItemRegistry.EMPTY_OIL_POT.get())
                .unlockedByItems("has_empty_oil_pot", ItemRegistry.EMPTY_TOX_POT.get())
                .build(output);


        // Oil Tier 2 Recipes
        new MortarPestleRecipeBuilder(new ItemStack(ItemRegistry.TOXIN_TOX_POT.get()))
                .ingredient(Items.HONEYCOMB)
                .ingredient(ItemRegistry.TOXIN.get())
                .ingredient(ItemRegistry.TOXIC_PASTE.get())
                .use(ItemRegistry.EMPTY_TOX_POT.get())
                .unlockedByItems("has_empty_oil_pot", ItemRegistry.EMPTY_TOX_POT.get())
                .build(output);

        new MortarPestleRecipeBuilder(new ItemStack(ItemRegistry.REGENERATION_TOX_POT.get()))
                .ingredient(Items.HONEYCOMB)
                .ingredient(Items.GHAST_TEAR)
                .ingredient(ItemRegistry.SUNSPOT.get())
                .use(ItemRegistry.EMPTY_TOX_POT.get())
                .unlockedByItems("has_empty_oil_pot", ItemRegistry.EMPTY_TOX_POT.get())
                .build(output);

        new MortarPestleRecipeBuilder(new ItemStack(ItemRegistry.SMOKE_TOX_POT.get()))
                .ingredient(Items.HONEYCOMB)
                .ingredient(Items.FERMENTED_SPIDER_EYE)
                .ingredient(ItemRegistry.MOONLIGHT_HEMLOCK.get())
                .use(ItemRegistry.EMPTY_TOX_POT.get())
                .unlockedByItems("has_empty_oil_pot", ItemRegistry.EMPTY_TOX_POT.get())
                .build(output);

        new MortarPestleRecipeBuilder(new ItemStack(ItemRegistry.ACID_TOX_POT.get()))
                .ingredient(Items.HONEYCOMB)
                .ingredient(ItemRegistry.WARPROOT.get())
                .ingredient(ItemRegistry.ACID_SLIMEBALL.get())
                .ingredient(ItemRegistry.BOG_BONE.get())
                .use(ItemRegistry.EMPTY_TOX_POT.get())
                .unlockedByItems("has_empty_oil_pot", ItemRegistry.EMPTY_TOX_POT.get())
                .build(output);

        new MortarPestleRecipeBuilder(new ItemStack(ItemRegistry.WITCHFIRE_TOX_POT.get()))
                .ingredient(Items.HONEYCOMB)
                .ingredient(Items.BLAZE_POWDER)
                .ingredient(ItemRegistry.WARPROOT.get())
                .use(ItemRegistry.EMPTY_TOX_POT.get())
                .unlockedByItems("has_empty_oil_pot", ItemRegistry.EMPTY_TOX_POT.get())
                .build(output);

        new MortarPestleRecipeBuilder(new ItemStack(ItemRegistry.OIL_BASE.get(), 2))
                .ingredient(Items.HONEYCOMB)
                .ingredient(ItemRegistry.TOXIC_PASTE.get())
                .unlockedByItems("has_empty_oil_pot", ItemRegistry.EMPTY_TOX_POT.get())
                .build(output);

        // Blend Recipes
        new MortarPestleRecipeBuilder(new ItemStack(ItemRegistry.POISON_BLEND.get()))
                .ingredient(ItemRegistry.POISON_PASTE.get())
                .possibleIngredient()
                .use(Items.BOWL)
                .unlockedByItems("has_poisonous_paste", ItemRegistry.POISON_PASTE.get())
                .build(output);

        new MortarPestleRecipeBuilder(new ItemStack(ItemRegistry.TOXIC_BLEND.get()))
                .ingredient(ItemRegistry.TOXIC_PASTE.get())
                .possibleIngredient()
                .possibleIngredient()
                .use(Items.BOWL)
                .unlockedByItems("has_toxic_paste", ItemRegistry.TOXIC_PASTE.get())
                .build(output);

        new MortarPestleRecipeBuilder(new ItemStack(ItemRegistry.PURE_BLEND.get()))
                .ingredient(ItemRegistry.TOXIN.get())
                .possibleIngredient()
                .possibleIngredient()
                .possibleIngredient()
                .use(Items.BOWL)
                .unlockedByItems("has_poisonous_paste", ItemRegistry.POISON_PASTE.get())
                .build(output);

        // Misc  Recipes
        new MortarPestleRecipeBuilder(new ItemStack(Items.GREEN_DYE, 4))
                .ingredient(ItemRegistry.POISON_PASTE.get())
                .unlockedByItems("has_poisonous_paste", ItemRegistry.POISON_PASTE.get())
                .build(output);

        new MortarPestleRecipeBuilder(new ItemStack(Items.GREEN_DYE, 8))
                .ingredient(ItemRegistry.TOXIC_PASTE.get())
                .unlockedByItems("has_toxic_paste", ItemRegistry.TOXIC_PASTE.get())
                .build(output, "_alt");
    }


}
