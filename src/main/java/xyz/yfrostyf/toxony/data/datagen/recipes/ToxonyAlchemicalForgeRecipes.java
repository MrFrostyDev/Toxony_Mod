package xyz.yfrostyf.toxony.data.datagen.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import xyz.yfrostyf.toxony.data.datagen.recipebuilders.AlchemicalForgeRecipeBuilder;
import xyz.yfrostyf.toxony.registries.AffinityRegistry;
import xyz.yfrostyf.toxony.registries.ItemRegistry;

import java.util.concurrent.CompletableFuture;

public class ToxonyAlchemicalForgeRecipes extends RecipeProvider {

    public ToxonyAlchemicalForgeRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    public static void get(RecipeOutput output){
        new AlchemicalForgeRecipeBuilder(new ItemStack(ItemRegistry.WITCHING_BLADE))
                .mainIngredient(Items.IRON_SWORD)
                .auxIngredient(Items.REDSTONE)
                .auxIngredient(Items.COPPER_INGOT)
                .affinity(AffinityRegistry.OCEAN.get())
                .affinity(AffinityRegistry.SUN.get())
                .affinity(AffinityRegistry.FOREST.get())
                .unlockedByItems("has_alchemical_forge_part", ItemRegistry.ALCHEMICAL_FORGE_PART.get())
                .build(output);

        new AlchemicalForgeRecipeBuilder(new ItemStack(ItemRegistry.LETHAL_DOSE))
                .mainIngredient(ItemRegistry.NETHERITE_SCALPEL.get())
                .auxIngredient(Items.REDSTONE)
                .auxIngredient(ItemRegistry.TOXIN_CANISTER.get())
                .affinity(AffinityRegistry.DECAY.get())
                .affinity(AffinityRegistry.FOREST.get())
                .affinity(AffinityRegistry.MOON.get())
                .unlockedByItems("has_alchemical_forge_part", ItemRegistry.ALCHEMICAL_FORGE_PART.get())
                .build(output);

        new AlchemicalForgeRecipeBuilder(new ItemStack(ItemRegistry.ETERNAL_PLAGUE))
                .mainIngredient(ItemRegistry.OIL_POT_BANDOLIER.get())
                .auxIngredient(ItemRegistry.TOXIN_CANISTER.get())
                .auxIngredient(ItemRegistry.TOXIC_LEATHER.get())
                .affinity(AffinityRegistry.COLD.get())
                .affinity(AffinityRegistry.SOUL.get())
                .affinity(AffinityRegistry.WIND.get())
                .unlockedByItems("has_alchemical_forge_part", ItemRegistry.ALCHEMICAL_FORGE_PART.get())
                .build(output);

        new AlchemicalForgeRecipeBuilder(new ItemStack(ItemRegistry.PLAGUEBRINGER_MASK))
                .mainIngredient(ItemRegistry.PLAGUE_DOCTOR_HOOD.get())
                .auxIngredient(Items.NETHERITE_SCRAP)
                .auxIngredient(Items.NETHERITE_SCRAP)
                .affinity(AffinityRegistry.DECAY.get())
                .affinity(AffinityRegistry.SOUL.get())
                .affinity(AffinityRegistry.SUN.get())
                .unlockedByItems("has_alchemical_forge_part", ItemRegistry.ALCHEMICAL_FORGE_PART.get())
                .build(output);

        new AlchemicalForgeRecipeBuilder(new ItemStack(ItemRegistry.PLAGUEBRINGER_COAT))
                .mainIngredient(ItemRegistry.PLAGUE_DOCTOR_COAT.get())
                .auxIngredient(Items.NETHERITE_SCRAP)
                .auxIngredient(Items.NETHERITE_SCRAP)
                .affinity(AffinityRegistry.DECAY.get())
                .affinity(AffinityRegistry.SOUL.get())
                .affinity(AffinityRegistry.SUN.get())
                .unlockedByItems("has_alchemical_forge_part", ItemRegistry.ALCHEMICAL_FORGE_PART.get())
                .build(output);

        new AlchemicalForgeRecipeBuilder(new ItemStack(ItemRegistry.PLAGUEBRINGER_LEGGINGS))
                .mainIngredient(ItemRegistry.PLAGUE_DOCTOR_LEGGINGS.get())
                .auxIngredient(Items.NETHERITE_SCRAP)
                .auxIngredient(Items.NETHERITE_SCRAP)
                .affinity(AffinityRegistry.DECAY.get())
                .affinity(AffinityRegistry.WIND.get())
                .affinity(AffinityRegistry.MOON.get())
                .unlockedByItems("has_alchemical_forge_part", ItemRegistry.ALCHEMICAL_FORGE_PART.get())
                .build(output);

        new AlchemicalForgeRecipeBuilder(new ItemStack(ItemRegistry.PLAGUEBRINGER_BOOTS))
                .mainIngredient(ItemRegistry.PLAGUE_DOCTOR_BOOTS.get())
                .auxIngredient(Items.NETHERITE_SCRAP)
                .auxIngredient(Items.NETHERITE_SCRAP)
                .affinity(AffinityRegistry.DECAY.get())
                .affinity(AffinityRegistry.WIND.get())
                .affinity(AffinityRegistry.MOON.get())
                .unlockedByItems("has_alchemical_forge_part", ItemRegistry.ALCHEMICAL_FORGE_PART.get())
                .build(output);
    }
}
