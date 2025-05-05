package xyz.yfrostyf.toxony.data.datagen.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import xyz.yfrostyf.toxony.registries.ItemRegistry;

import java.util.concurrent.CompletableFuture;

public class ToxonySmithingTableRecipes extends RecipeProvider {

    public ToxonySmithingTableRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    public static void get(RecipeOutput output) {
        netheriteSmithing(output,
                ItemRegistry.COPPER_SCALPEL.get(),
                RecipeCategory.TOOLS,
                ItemRegistry.NETHERITE_SCALPEL.get());

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(ItemRegistry.SILVER_STEEL_UPGRADE_SMITHING_TEMPLATE.get()),
                        Ingredient.of(ItemRegistry.HUNTER_HAT.get()),
                        Ingredient.of(ItemRegistry.SILVER_STEEL_INGOT.get()),
                        RecipeCategory.COMBAT,
                        ItemRegistry.PROFESSIONAL_HUNTER_HAT.get()
                )
                .unlocks("has_silver_steel_ingot", has(ItemRegistry.SILVER_STEEL_INGOT.get()))
                .save(output, getItemName(ItemRegistry.PROFESSIONAL_HUNTER_HAT.get()) + "_smithing");

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(ItemRegistry.SILVER_STEEL_UPGRADE_SMITHING_TEMPLATE.get()),
                        Ingredient.of(ItemRegistry.HUNTER_COAT.get()),
                        Ingredient.of(ItemRegistry.SILVER_STEEL_INGOT.get()),
                        RecipeCategory.COMBAT,
                        ItemRegistry.PROFESSIONAL_HUNTER_COAT.get()
                )
                .unlocks("has_silver_steel_ingot", has(ItemRegistry.SILVER_STEEL_INGOT.get()))
                .save(output, getItemName(ItemRegistry.PROFESSIONAL_HUNTER_COAT.get()) + "_smithing");

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(ItemRegistry.SILVER_STEEL_UPGRADE_SMITHING_TEMPLATE.get()),
                        Ingredient.of(ItemRegistry.HUNTER_LEGGINGS.get()),
                        Ingredient.of(ItemRegistry.SILVER_STEEL_INGOT.get()),
                        RecipeCategory.COMBAT,
                        ItemRegistry.PROFESSIONAL_HUNTER_LEGGINGS.get()
                )
                .unlocks("has_silver_steel_ingot", has(ItemRegistry.SILVER_STEEL_INGOT.get()))
                .save(output, getItemName(ItemRegistry.PROFESSIONAL_HUNTER_LEGGINGS.get()) + "_smithing");

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(ItemRegistry.SILVER_STEEL_UPGRADE_SMITHING_TEMPLATE.get()),
                        Ingredient.of(ItemRegistry.HUNTER_BOOTS.get()),
                        Ingredient.of(ItemRegistry.SILVER_STEEL_INGOT.get()),
                        RecipeCategory.COMBAT,
                        ItemRegistry.PROFESSIONAL_HUNTER_BOOTS.get()
                )
                .unlocks("has_silver_steel_ingot", has(ItemRegistry.SILVER_STEEL_INGOT.get()))
                .save(output, getItemName(ItemRegistry.PROFESSIONAL_HUNTER_BOOTS.get()) + "_smithing");

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(ItemRegistry.SILVER_STEEL_UPGRADE_SMITHING_TEMPLATE.get()),
                        Ingredient.of(Items.FISHING_ROD),
                        Ingredient.of(ItemRegistry.SILVER_STEEL_INGOT.get()),
                        RecipeCategory.COMBAT,
                        ItemRegistry.FLAIL.get()
                )
                .unlocks("has_silver_steel_ingot", has(ItemRegistry.SILVER_STEEL_INGOT.get()))
                .save(output, getItemName(ItemRegistry.FLAIL.get()) + "_smithing");

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(ItemRegistry.SILVER_STEEL_UPGRADE_SMITHING_TEMPLATE.get()),
                        Ingredient.of(ItemRegistry.FLINTLOCK_COMPONENTS.get()),
                        Ingredient.of(ItemRegistry.SILVER_STEEL_INGOT.get()),
                        RecipeCategory.COMBAT,
                        ItemRegistry.FLINTLOCK.get()
                )
                .unlocks("has_silver_steel_ingot", has(ItemRegistry.SILVER_STEEL_INGOT.get()))
                .save(output, getItemName(ItemRegistry.FLINTLOCK.get()) + "_smithing");
    }
}
