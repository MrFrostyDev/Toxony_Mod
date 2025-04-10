package xyz.yfrostyf.toxony.client.gui.journal;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import xyz.yfrostyf.toxony.ToxonyMain;

import java.util.List;

// Thank you Malum mod for the reference for rendering ingredients
public class TextAlembicPageScreen extends TextPageScreen{
    private static final ResourceLocation ALEMBIC_IMAGE = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/gui/journal/journal_alembic_menu.png");

    private final ItemStack outputItem;
    private final List<List<ItemStack>> inputItems;

    public TextAlembicPageScreen(String translateID, ItemStack outputItem, List<List<ItemStack>> inputItems, int indexID, JournalPages journalPages) {
        super(translateID, indexID, journalPages);
        this.inputItems = inputItems;
        this.outputItem = outputItem;
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int backgroundPosX = (this.width - IMAGE_WIDTH) / 2;
        int backgroundPosY = (this.height - IMAGE_HEIGHT) / 2;

        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.blit(ALEMBIC_IMAGE, backgroundPosX, backgroundPosY,
                0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        int halfScreenPosX = this.width / 2;
        int halfScreenPosY = this.height / 2;

        int posX = halfScreenPosX - 30;
        int posY = halfScreenPosY + 48+17;

        for (int i = 0; i < 2; i++) {
            if (inputItems.size() > i) {
                boolean isMainSlot = i == 0;
                final List<ItemStack> ingredients = inputItems.get(i);

                final ItemStack stack;
                if(ingredients.size() == 1) stack = ingredients.getFirst();
                else if(ingredients.size() > 1) stack = ingredients.get((int)(this.minecraft.level.getGameTime() % (20L * ingredients.size()) / 20));
                else continue;

                if (!stack.isEmpty()) {
                    int itemPosX = isMainSlot ? posX : posX + 36;
                    int itemPosY = isMainSlot ? posY : posY - 5;


                    guiGraphics.renderItem(stack, itemPosX, itemPosY);
                    guiGraphics.renderItemDecorations(this.minecraft.font, stack, itemPosX, itemPosY, null);
                    if (mouseX > itemPosX && mouseX < itemPosX + 16 && mouseY > itemPosY && mouseY < itemPosY + 16) {
                        guiGraphics.renderComponentTooltip(this.minecraft.font, Screen.getTooltipFromItem(this.minecraft, stack), mouseX, mouseY);
                    }
                }
            }
        }

        int outputPosX = posX + 62;
        int outputPosY = posY - 31;
        guiGraphics.renderItem(outputItem, outputPosX, outputPosY);
        if (mouseX > outputPosX && mouseX < outputPosX + 16 && mouseY > outputPosY && mouseY < outputPosY + 16) {
            guiGraphics.renderComponentTooltip(this.minecraft.font, Screen.getTooltipFromItem(this.minecraft, outputItem), mouseX, mouseY);
        }
    }
}
