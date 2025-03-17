package xyz.yfrostyf.toxony.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.client.gui.AlembicScreen;
import xyz.yfrostyf.toxony.client.gui.CopperCrucibleScreen;
import xyz.yfrostyf.toxony.recipes.AlchemicalForgeRecipe;
import xyz.yfrostyf.toxony.recipes.AlembicRecipe;
import xyz.yfrostyf.toxony.recipes.CrucibleRecipe;
import xyz.yfrostyf.toxony.recipes.MortarPestleRecipe;
import xyz.yfrostyf.toxony.registries.BlockRegistry;
import xyz.yfrostyf.toxony.registries.RecipeRegistry;

import java.util.List;

// thank you [Iron's Spells and Spellbooks] and [Modding by Kaupenjoe] for the reference

@JeiPlugin
public class ToxonyJeiPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new MortarPestleRecipeCatagory(guiHelper));
        registration.addRecipeCategories(new CrucibleRecipeCatagory(guiHelper));
        registration.addRecipeCategories(new AlembicRecipeCatagory(guiHelper));
        registration.addRecipeCategories(new AlchemicalForgeRecipeCatagory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();

        List<RecipeHolder<MortarPestleRecipe>> mortarPestleRecipeHolders = manager.getAllRecipesFor(RecipeRegistry.MORTAR_PESTLE_RECIPE.get());
        List<MortarPestleRecipe> mortarPestleRecipes = mortarPestleRecipeHolders.stream().map(holder -> holder.value()).toList();
        registration.addRecipes(MortarPestleRecipeCatagory.MORTAR_PESTLE_RECIPE, mortarPestleRecipes);

        List<RecipeHolder<CrucibleRecipe>> crucibleRecipeHolders = manager.getAllRecipesFor(RecipeRegistry.CRUCIBLE_RECIPE.get());
        List<CrucibleRecipe> crucibleRecipes = crucibleRecipeHolders.stream().map(holder -> holder.value()).toList();
        registration.addRecipes(CrucibleRecipeCatagory.CRUCIBLE_RECIPE, crucibleRecipes);

        List<RecipeHolder<AlembicRecipe>> alembicRecipeHolders = manager.getAllRecipesFor(RecipeRegistry.ALEMBIC_RECIPE.get());
        List<AlembicRecipe> alembicRecipes = alembicRecipeHolders.stream().map(holder -> holder.value()).toList();
        registration.addRecipes(AlembicRecipeCatagory.ALEMBIC_RECIPE, alembicRecipes);

        List<RecipeHolder<AlchemicalForgeRecipe>> alchemicalForgeRecipeHolders = manager.getAllRecipesFor(RecipeRegistry.ALCHEMICAL_FORGE_RECIPE.get());
        List<AlchemicalForgeRecipe> alchemicalForgeRecipes = alchemicalForgeRecipeHolders.stream().map(holder -> holder.value()).toList();
        registration.addRecipes(AlchemicalForgeRecipeCatagory.ALCHEMICAL_FORGE_RECIPE, alchemicalForgeRecipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(CopperCrucibleScreen.class, 115, 27, 2, 2, CrucibleRecipeCatagory.CRUCIBLE_RECIPE);
        registration.addRecipeClickArea(AlembicScreen.class, 122, 31, 2, 2, AlembicRecipeCatagory.ALEMBIC_RECIPE);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.MORTAR_PESTLE.get()), MortarPestleRecipeCatagory.MORTAR_PESTLE_RECIPE);
        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.COPPER_CRUCIBLE.get()), CrucibleRecipeCatagory.CRUCIBLE_RECIPE);
        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.ALEMBIC.get()), AlembicRecipeCatagory.ALEMBIC_RECIPE);
        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.ALCHEMICAL_FORGE_PART.get()), AlchemicalForgeRecipeCatagory.ALCHEMICAL_FORGE_RECIPE);
    }

}
