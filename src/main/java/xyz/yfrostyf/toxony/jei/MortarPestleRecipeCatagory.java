package xyz.yfrostyf.toxony.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.recipes.MortarPestleRecipe;
import xyz.yfrostyf.toxony.registries.ItemRegistry;

import java.util.List;

public class MortarPestleRecipeCatagory implements IRecipeCategory<MortarPestleRecipe> {
    public static final RecipeType<MortarPestleRecipe> MORTAR_PESTLE_RECIPE = RecipeType.create(ToxonyMain.MOD_ID, "mortar_pestle", MortarPestleRecipe.class);

    private final int IMAGE_WIDTH = 176;
    private final int IMAGE_HEIGHT = 166;

    private final IDrawable icon;
    private final IDrawableStatic background;

    public MortarPestleRecipeCatagory(IGuiHelper guiHelper) {

        ResourceLocation mortar_menu = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/gui/jei/mortar_pestle_menu_jei.png");

        icon = guiHelper.createDrawableItemStack(new ItemStack(ItemRegistry.MORTAR_PESTLE.get()));
        background = guiHelper.createDrawable(mortar_menu, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
    }

    @Override
    public RecipeType<MortarPestleRecipe> getRecipeType() {
        return MORTAR_PESTLE_RECIPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.toxony.mortar_pestle");
    }

    @Override
    public void draw(MortarPestleRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics, 0, 1);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MortarPestleRecipe recipe, IFocusGroup focuses) {
        List<Ingredient> ingredients = recipe.getIngredients();

        for (int i=0; i < ingredients.size(); i++) {
            builder.addSlot(RecipeIngredientRole.INPUT, 33 + i % 2 * 18, i > 1 ? 26 + 18 : 26)
                    .addIngredients(ingredients.get(i))
                    .setSlotName("inputSlot");
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 132, 35)
                .addItemStack(recipe.getResultItem(null))
                .setSlotName("outputSlot");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public int getWidth() {
        return IMAGE_WIDTH;
    }

    @Override
    public int getHeight() {
        return IMAGE_HEIGHT;
    }
}
