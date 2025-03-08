package xyz.yfrostyf.toxony.jei;

import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
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
import xyz.yfrostyf.toxony.recipes.AlembicRecipe;
import xyz.yfrostyf.toxony.registries.ItemRegistry;

// thank you [Iron's Spells and Spellbooks] for the reference

public class AlembicRecipeCatagory implements IRecipeCategory<AlembicRecipe> {
    public static final RecipeType<AlembicRecipe> ALEMBIC_RECIPE = RecipeType.create(ToxonyMain.MOD_ID, "alembic", AlembicRecipe.class);

    private final int IMAGE_WIDTH = 176;
    private final int IMAGE_HEIGHT = 166;

    private final IDrawable icon;
    private final IDrawableStatic background;
    private final IDrawableAnimated bubbles;
    private final IDrawableAnimated progressBar;

    public AlembicRecipeCatagory(IGuiHelper guiHelper) {
        ResourceLocation alembic_menu = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/gui/jei/alembic_menu_jei.png");
        ResourceLocation alembic_bubbles = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/gui/sprites/container/bubbles.png");
        ResourceLocation progress_bar = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/gui/sprites/container/progress_bar.png");

        icon = guiHelper.createDrawableItemStack(new ItemStack(ItemRegistry.ALEMBIC.get()));
        background = guiHelper.createDrawable(alembic_menu, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        bubbles = guiHelper.drawableBuilder(alembic_bubbles, 0, 0, 10, 27).setTextureSize(10, 27)
                .buildAnimated(new AlembicBubblesTickTimer(guiHelper), IDrawableAnimated.StartDirection.BOTTOM);
        progressBar = guiHelper.drawableBuilder(progress_bar, 0, 0, 3, 25).setTextureSize(3, 25)
                .buildAnimated(guiHelper.createTickTimer(300, 25, true), IDrawableAnimated.StartDirection.BOTTOM);
    }

    @Override
    public RecipeType<AlembicRecipe>  getRecipeType() {
        return ALEMBIC_RECIPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.toxony.alembic");
    }

    @Override
    public void draw(AlembicRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics, 0, 1);
        bubbles.draw(guiGraphics, 52, 34);
        progressBar.draw(guiGraphics, 140, 42);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AlembicRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 77, 52)
                .addIngredients(recipe.getIngredient())
                .setSlotName("inputSlot");

        builder.addSlot(RecipeIngredientRole.INPUT, 113, 47)
                .addIngredients(recipe.getIngredientToConvert())
                .setSlotName("inputSlotConvert");

        builder.addSlot(RecipeIngredientRole.OUTPUT, 113, 12)
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

    private static class AlembicBubblesTickTimer implements ITickTimer {
        @SuppressWarnings("JavadocReference")
        private static final int[] BUBBLE_LENGTHS = new int[]{27, 24, 20, 16, 11, 6, 0};
        private final ITickTimer internalTimer;

        public AlembicBubblesTickTimer(IGuiHelper guiHelper) {
            this.internalTimer = guiHelper.createTickTimer(14, BUBBLE_LENGTHS.length - 1, false);
        }

        @Override
        public int getValue() {
            int timerValue = this.internalTimer.getValue();
            return BUBBLE_LENGTHS[timerValue];
        }

        @Override
        public int getMaxValue() {
            return BUBBLE_LENGTHS[0];
        }
    }
}
