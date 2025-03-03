package xyz.yfrostyf.toxony.data.datagen.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import xyz.yfrostyf.toxony.api.util.VialUtil;
import xyz.yfrostyf.toxony.data.datagen.recipebuilders.AlembicRecipeBuilder;
import xyz.yfrostyf.toxony.registries.ItemRegistry;
import xyz.yfrostyf.toxony.registries.TagRegistry;

import java.util.concurrent.CompletableFuture;

public class ToxonyAlembicRecipes extends RecipeProvider {

    public ToxonyAlembicRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    public static void get(RecipeOutput output){
        new AlembicRecipeBuilder(new ItemStack(ItemRegistry.TOXIC_FORMULA.get()))
                .ingredient(Items.NETHER_WART)
                .ingredientToConvert(DataComponentIngredient.of(false, VialUtil.createPotionItemStack(ItemRegistry.TOX_VIAL.get(), Potions.WATER)))
                .boilTime(1200)
                .unlockedByItems("has_toxic_paste", ItemRegistry.TOXIC_FORMULA.get())
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
