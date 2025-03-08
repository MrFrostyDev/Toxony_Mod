package xyz.yfrostyf.toxony.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
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
import org.jetbrains.annotations.Nullable;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.recipes.CrucibleRecipe;
import xyz.yfrostyf.toxony.registries.ItemRegistry;

// thank you [Iron's Spells and Spellbooks] for the reference

public class CrucibleRecipeCatagory implements IRecipeCategory<CrucibleRecipe> {
    public static final RecipeType<CrucibleRecipe> CRUCIBLE_RECIPE = RecipeType.create(ToxonyMain.MOD_ID, "crucible", CrucibleRecipe.class);

    private final int IMAGE_WIDTH = 176;
    private final int IMAGE_HEIGHT = 166;

    private final IDrawable icon;
    private final IDrawableStatic background;
    protected final IDrawableAnimated flames;
    protected final IDrawableAnimated progressBar;

    public CrucibleRecipeCatagory(IGuiHelper guiHelper) {

        ResourceLocation crucible_menu = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/gui/jei/copper_crucible_menu_jei.png");
        ResourceLocation crucible_flames = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/gui/sprites/container/crucible_flames.png");
        ResourceLocation progress_bar = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/gui/sprites/container/progress_bar.png");

        icon = guiHelper.createDrawableItemStack(new ItemStack(ItemRegistry.COPPER_CRUCIBLE.get()));
        background = guiHelper.createDrawable(crucible_menu, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        flames = guiHelper.drawableBuilder(crucible_flames, 0, 0, 13, 11).setTextureSize(13, 11)
                .buildAnimated(400, IDrawableAnimated.StartDirection.TOP, true);
        progressBar = guiHelper.drawableBuilder(progress_bar, 0, 0, 3, 25).setTextureSize(3, 25)
                .buildAnimated(300, IDrawableAnimated.StartDirection.BOTTOM, false);
    }

    @Override
    public RecipeType<CrucibleRecipe> getRecipeType() {
        return CRUCIBLE_RECIPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.toxony.copper_crucible");
    }

    @Override
    public void draw(CrucibleRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics, 0, 1);
        progressBar.draw(guiGraphics, 115, 18);
        flames.draw(guiGraphics, 82, 45);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CrucibleRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 80, 22)
                .addIngredients(recipe.getIngredients().getFirst())
                .setSlotName("inputSlot");

        builder.addSlot(RecipeIngredientRole.OUTPUT, 128, 22)
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
