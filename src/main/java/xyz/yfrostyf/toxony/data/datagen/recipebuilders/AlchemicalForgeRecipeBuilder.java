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
import xyz.yfrostyf.toxony.api.affinity.Affinity;
import xyz.yfrostyf.toxony.recipes.AlchemicalForgeRecipe;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class AlchemicalForgeRecipeBuilder implements RecipeBuilder {
    protected String suffix = "";
    protected ItemStack result;
    protected Ingredient mainIngredient;
    protected List<Affinity> affinities = new ArrayList<>(3);
    protected List<Ingredient> auxIngredient = new ArrayList<>(2);
    protected Map<String, Criterion<?>> criteria = new LinkedHashMap<>();


    public AlchemicalForgeRecipeBuilder(ItemStack result) {
        this.result = result;
    }

    public AlchemicalForgeRecipeBuilder suffix(String suffix){
        this.suffix = suffix;
        return this;
    }

    public AlchemicalForgeRecipeBuilder mainIngredient(ItemLike item) {
        this.mainIngredient = Ingredient.of(item);
        return this;
    }

    public AlchemicalForgeRecipeBuilder mainIngredient(ItemStack stack) {
        this.mainIngredient = Ingredient.of(stack);
        return this;
    }

    public AlchemicalForgeRecipeBuilder affinity(Affinity affinity) {
        this.affinities.add(affinity);
        return this;
    }

    public AlchemicalForgeRecipeBuilder auxIngredient(ItemLike item) {
        this.auxIngredient.add(Ingredient.of(item));
        return this;
    }

    public AlchemicalForgeRecipeBuilder auxIngredient(ItemStack stack) {
        this.auxIngredient.add(Ingredient.of(stack));
        return this;
    }

    public AlchemicalForgeRecipeBuilder auxIngredient(Ingredient ingredient) {
        this.auxIngredient.add(ingredient);
        return this;
    }

    public AlchemicalForgeRecipeBuilder auxIngredient(TagKey<Item> item) {
        this.auxIngredient.add(Ingredient.of(item));
        return this;
    }

    public AlchemicalForgeRecipeBuilder unlockedByItems(String criterionName, ItemLike... items) {
        return unlockedBy(criterionName, InventoryChangeTrigger.TriggerInstance.hasItems(items));
    }

    @Override
    public AlchemicalForgeRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public AlchemicalForgeRecipeBuilder group(String group) {
        return this;
    }

    @Override
    public Item getResult() {
        return this.result.getItem();
    }

    public void build(RecipeOutput output){
        String pathName = BuiltInRegistries.ITEM.getKey(result.getItem()).getPath() + suffix;
        save(output, ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, pathName).withPrefix("alchemical_forge/"));
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
        AlchemicalForgeRecipe recipe = new AlchemicalForgeRecipe(this.result, this.mainIngredient, this.affinities, this.auxIngredient);
        // Pass the id, the recipe, and the recipe advancement into the RecipeOutput.
        output.accept(id, recipe, advancement.build(id.withPrefix("recipes/")));
    }

}
