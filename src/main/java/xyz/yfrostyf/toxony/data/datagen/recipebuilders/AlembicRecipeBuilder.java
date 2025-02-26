package xyz.yfrostyf.toxony.data.datagen.recipebuilders;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.recipes.AlembicRecipe;

import java.util.LinkedHashMap;
import java.util.Map;


public class AlembicRecipeBuilder implements RecipeBuilder {
    protected String suffix = "";
    protected ItemStack result;
    protected Ingredient ingredient;
    protected Ingredient ingredientToConvert;
    protected int boiltime;
    protected Map<String, Criterion<?>> criteria = new LinkedHashMap<>();


    public AlembicRecipeBuilder(ItemStack result) {
        this.result = result;
        this.boiltime = 200;
    }

    public AlembicRecipeBuilder suffix(String suffix){
        this.suffix = suffix;
        return this;
    }

    public AlembicRecipeBuilder ingredient(ItemLike item) {
        this.ingredient = Ingredient.of(item);
        return this;
    }

    public AlembicRecipeBuilder ingredient(ItemStack item) {
        this.ingredient = Ingredient.of(item);
        return this;
    }

    public AlembicRecipeBuilder ingredient(TagKey<Item> item) {
        this.ingredient = Ingredient.of(item);
        return this;
    }

    public AlembicRecipeBuilder ingredientToConvert(ItemLike item) {
        this.ingredientToConvert = Ingredient.of(item);
        return this;
    }

    public AlembicRecipeBuilder ingredientToConvert(ItemStack item) {
        this.ingredientToConvert = Ingredient.of(item);
        return this;
    }

    public AlembicRecipeBuilder ingredientToConvert(TagKey<Item> item) {
        this.ingredientToConvert = Ingredient.of(item);
        return this;
    }

    public AlembicRecipeBuilder boilTime(int boiltime) {
        this.boiltime = boiltime;
        return this;
    }

    public AlembicRecipeBuilder unlockedByItems(String criterionName, ItemLike... items) {
        return unlockedBy(criterionName, InventoryChangeTrigger.TriggerInstance.hasItems(items));
    }

    @Override
    public AlembicRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public AlembicRecipeBuilder group(String group) {
        return this;
    }

    @Override
    public Item getResult() {
        return this.result.getItem();
    }

    public void build(RecipeOutput output){
        String pathName = BuiltInRegistries.ITEM.getKey(result.getItem()).getPath() + suffix;
        save(output, ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, pathName).withPrefix("alembic/"));
    }

    // Saves a recipe using the given RecipeOutput and ResourceLocation. This method is defined in the RecipeBuilder interface.
    @Override
    public void save(RecipeOutput output, @NotNull ResourceLocation id) {
        // Build the advancement.
        Advancement.Builder advancement = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement::addCriterion);
        // Our factory parameters.
        AlembicRecipe recipe = new AlembicRecipe(this.result, this.ingredient, this.ingredientToConvert, this.boiltime);
        // Pass the id, the recipe, and the recipe advancement into the RecipeOutput.
        output.accept(id, recipe, advancement.build(id.withPrefix("recipes/")));
    }

}
