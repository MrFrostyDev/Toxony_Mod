package xyz.yfrostyf.toxony.jei;

import com.mojang.datafixers.util.Pair;
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
import xyz.yfrostyf.toxony.api.affinity.Affinity;
import xyz.yfrostyf.toxony.recipes.AlchemicalForgeRecipe;
import xyz.yfrostyf.toxony.registries.ItemRegistry;

import java.util.List;

// thank you [Iron's Spells and Spellbooks] for the reference

public class AlchemicalForgeRecipeCatagory implements IRecipeCategory<AlchemicalForgeRecipe> {
    public static final RecipeType<AlchemicalForgeRecipe> ALCHEMICAL_FORGE_RECIPE = RecipeType.create(ToxonyMain.MOD_ID, "alchemical_forge", AlchemicalForgeRecipe.class);
    public static final ResourceLocation AFFINITY_ICONS = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/gui/affinity_icons.png");

    private final int IMAGE_WIDTH = 176;
    private final int IMAGE_HEIGHT = 166;

    private final IDrawable icon;
    private final IDrawableStatic background;

    public AlchemicalForgeRecipeCatagory(IGuiHelper guiHelper) {
        ResourceLocation alchemical_forge_menu = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/gui/jei/alchemical_forge_menu_jei.png");

        icon = guiHelper.createDrawableItemStack(new ItemStack(ItemRegistry.ALCHEMICAL_FORGE_PART.get()));
        background = guiHelper.createDrawable(alchemical_forge_menu, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
    }

    @Override
    public RecipeType<AlchemicalForgeRecipe>  getRecipeType() {
        return ALCHEMICAL_FORGE_RECIPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.toxony.alchemical_forge");
    }

    @Override
    public void draw(AlchemicalForgeRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics, 0, 1);

        List<Affinity> affinities = recipe.getAffinities();

        int factorBasedOnSize = (affinities.size()-1) * 8;

        // Affinity Icons
        int i = 0;
        for(Affinity affinity : affinities){
            guiGraphics.blit(AFFINITY_ICONS,
                    82 - factorBasedOnSize + i * 15, 9,
                    affinity.getIndex() * 13, (int)((double)(affinity.getIndex() / 4) * 13),
                    13, 13,
                    52, 39);
            i++;
        }
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AlchemicalForgeRecipe recipe, IFocusGroup focuses) {
        Ingredient mainingredient = recipe.getMainIngredient();
        Pair<Ingredient, Ingredient> auxIngredients = recipe.getAuxIngredients();

        builder.addSlot(RecipeIngredientRole.INPUT, 80, 44)
                .addIngredients(mainingredient)
                .setSlotName("mainInputSlot");

        builder.addSlot(RecipeIngredientRole.INPUT, 54, 44)
                .addIngredients(auxIngredients.getFirst())
                .setSlotName("auxInputSlot1");

        builder.addSlot(RecipeIngredientRole.INPUT, 106, 44)
                .addIngredients(auxIngredients.getSecond())
                .setSlotName("auxInputSlot2");

        builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 94)
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
