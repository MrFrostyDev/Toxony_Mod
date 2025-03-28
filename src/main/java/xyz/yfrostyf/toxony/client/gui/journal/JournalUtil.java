package xyz.yfrostyf.toxony.client.gui.journal;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.registries.ItemRegistry;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JournalUtil {
    private static final Item EMPTY = Items.AIR;
    private static final Level level = Minecraft.getInstance().level;
    private static final RecipeManager manager = level != null ? level.getRecipeManager() : null;

    public static void startPage(String translateID){
        JournalPages pages = JournalUtil.createPages().build(translateID);
        pages.updatePage();
    }

    public static JournalPagesBuilder createPages(){
        Recipe<?> recipe_poison_paste = getRecipe(manager, "mortar/poison_paste").value();
        Recipe<?> recipe_toxin = getRecipe(manager, "alembic/toxin").value();
        Recipe<?> recipe_toxic_paste = getRecipe(manager, "crucible/toxic_paste").value();

        return new JournalPagesBuilder()
                .ImagePageScreen("journal.toxony.page.cover", "textures/gui/journal/journal_cover.png")
                .TextPageScreen("journal.toxony.page.introduction.0")
                .TextPageScreen("journal.toxony.page.introduction.1")
                .IndexPageScreen("journal.toxony.page.index.0",
                        Map.of("Poison Basics", "journal.toxony.page.poison_base.0",
                                "Alembic Basics", "journal.toxony.page.alembic.0",
                                "Crucible Basics", "journal.toxony.page.crucible.0"
                        )
                )
                .TextPageScreen("journal.toxony.page.poison_base.0")
                .TextCraftingPageScreenItem("journal.toxony.page.poison_base.1", ItemRegistry.MORTAR_PESTLE.get(),
                        List.of(EMPTY, EMPTY, EMPTY,
                                Items.STONE, Items.STICK, Items.STONE,
                                Items.STONE, Items.STONE, Items.STONE)
                )
                .TextPageScreen("journal.toxony.page.poison_base.2")
                .TextPageScreen("journal.toxony.page.poison_base.3")
                .TextMortarPageScreenIngredients("journal.toxony.page.poison_base.4", ItemRegistry.POISON_PASTE.get(),
                        List.of(recipe_poison_paste.getIngredients().get(0), recipe_poison_paste.getIngredients().get(1),
                                recipe_poison_paste.getIngredients().get(2), Ingredient.EMPTY)
                )
                .TextCraftingPageScreenItem("journal.toxony.page.crafting.0", ItemRegistry.ALEMBIC.get(),
                        List.of(Items.COPPER_INGOT, EMPTY, EMPTY,
                                Items.COPPER_INGOT, Items.IRON_INGOT, EMPTY,
                                ItemRegistry.ALEMBIC_BASE.get(), EMPTY, Items.COPPER_INGOT)
                )
                .TextAlembicPageScreenIngredients("journal.toxony.page.alembic.0", ItemRegistry.TOXIN.get(),
                        List.of(recipe_toxin.getIngredients().get(0), recipe_toxin.getIngredients().get(1))
                )
                .TextCruciblePageScreenIngredient("journal.toxony.page.crucible.0", ItemRegistry.TOXIC_PASTE.get(),
                        recipe_toxic_paste.getIngredients().getFirst()
                );

    }

    public static RecipeHolder<?> getRecipe(RecipeManager manager, String location){
        Optional<RecipeHolder<?>> optional = Optional.empty();
        if(level != null){
            optional = manager.byKey(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, location));
        }
        return optional.orElseThrow();
    }

}
