package xyz.yfrostyf.toxony.data.datagen.recipebuilders;

import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
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
import xyz.yfrostyf.toxony.recipes.MortarPestleRecipe;

import java.util.*;


public class MortarPestleRecipeBuilder implements RecipeBuilder {
    protected String suffix = "";
    protected ItemStack result;
    protected ItemStack use;
    protected NonNullList<Ingredient> ingredients = NonNullList.create();
    protected Map<String, Criterion<?>> criteria = new LinkedHashMap<>();


    public MortarPestleRecipeBuilder(ItemStack result) {
        this.result = result;
    }

    public MortarPestleRecipeBuilder suffix(String suffix){
        this.suffix = suffix;
        return this;
    }

    public MortarPestleRecipeBuilder ingredient(ItemLike item) {
        ingredients.add(Ingredient.of(item));
        return this;
    }

    public MortarPestleRecipeBuilder ingredient(ItemStack item) {
        ingredients.add(Ingredient.of(item));
        return this;
    }

    public MortarPestleRecipeBuilder ingredient(TagKey<Item> item) {
        ingredients.add(Ingredient.of(item));
        return this;
    }

    public MortarPestleRecipeBuilder use(ItemLike item) {
        this.use = new ItemStack(item);
        return this;
    }
    public MortarPestleRecipeBuilder unlockedByItems(String criterionName, ItemLike... items) {
        return unlockedBy(criterionName, InventoryChangeTrigger.TriggerInstance.hasItems(items));
    }

    public MortarPestleRecipeBuilder unlockedByTag(String criterionName, TagKey<Item> tag) {
        return unlockedBy(criterionName, InventoryChangeTrigger.TriggerInstance.hasItems(
                Arrays.stream(new ItemPredicate.Builder[]{ItemPredicate.Builder.item().of(tag)})
                        .map(ItemPredicate.Builder::build)
                        .toArray(ItemPredicate[]::new)
                )
        );
    }

    @Override
    public MortarPestleRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public MortarPestleRecipeBuilder group(String group) {
        return this;
    }

    @Override
    public Item getResult() {
        return this.result.getItem();
    }

    public void build(RecipeOutput output){
        String pathName = BuiltInRegistries.ITEM.getKey(result.getItem()).getPath() + suffix;
        save(output, ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, pathName).withPrefix("mortar/"));
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
        MortarPestleRecipe recipe = new MortarPestleRecipe(this.result, this.use, this.ingredients);
        // Pass the id, the recipe, and the recipe advancement into the RecipeOutput.
        output.accept(id, recipe, advancement.build(id.withPrefix("recipes/")));
    }

}
