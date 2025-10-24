package xyz.yfrostyf.toxony.client.gui.journal;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.client.utils.ClientUtil;

import java.util.List;

// Thank you Malum mod for the reference for rendering ingredients
public class TextCraftingPageScreen extends TextPageScreen{
    private static final ResourceLocation CRAFTING_IMAGE = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/gui/journal/journal_crafting_menu.png");

    private final ItemStack outputItem;
    private final List<List<ItemStack>> inputItems;

    public TextCraftingPageScreen(String translateID, ItemStack outputItem, List<List<ItemStack>> inputItems, int indexID, JournalPages journalPages) {
        super(translateID, indexID, journalPages);
        this.inputItems = inputItems;
        this.outputItem = outputItem;
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int backgroundPosX = (this.width - IMAGE_WIDTH) / 2;
        int backgroundPosY = (this.height - IMAGE_HEIGHT) / 2;

        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.blit(CRAFTING_IMAGE, backgroundPosX, backgroundPosY,
                0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        int halfScreenPosX = this.width / 2;
        int halfScreenPosY = this.height / 2;

        int posX = halfScreenPosX - 33;
        int posY = halfScreenPosY + 34;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int index = i * 3 + j;
                if (inputItems.size() > index) {
                    final List<ItemStack> ingredients = inputItems.get(index);

                    final ItemStack stack;
                    if(ingredients.size() == 1) stack = ingredients.getFirst();
                    else if(ingredients.size() > 1) stack = ingredients.get((int)(ClientUtil.getClientTick() % (20L * ingredients.size()) / 20));
                    else continue;

                    if (!stack.isEmpty()) {
                        int itemPosX = posX + j * 17;
                        int itemPosY = posY + i * 17;


                        guiGraphics.renderItem(stack, itemPosX, itemPosY);
                        guiGraphics.renderItemDecorations(this.minecraft.font, stack, itemPosX, itemPosY, null);
                        if (mouseX > itemPosX && mouseX < itemPosX + 16 && mouseY > itemPosY && mouseY < itemPosY + 16) {
                            guiGraphics.renderComponentTooltip(this.minecraft.font, Screen.getTooltipFromItem(this.minecraft, stack), mouseX, mouseY);
                        }
                    }
                }
            }
        }

        int outputPosX = posX + 66;
        int outputPosY = posY + 16;
        guiGraphics.renderItem(outputItem, outputPosX, outputPosY);
        if (mouseX > outputPosX && mouseX < outputPosX + 16 && mouseY > outputPosY && mouseY < outputPosY + 16) {
            guiGraphics.renderComponentTooltip(this.minecraft.font, Screen.getTooltipFromItem(this.minecraft, outputItem), mouseX, mouseY);
        }
    }
}
