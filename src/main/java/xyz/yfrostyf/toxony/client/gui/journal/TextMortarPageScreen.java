package xyz.yfrostyf.toxony.client.gui.journal;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import xyz.yfrostyf.toxony.ToxonyMain;

import java.util.List;

// Thank you Malum mod for the reference for rendering ingredients
public class TextMortarPageScreen extends TextPageScreen{
    private static final ResourceLocation MORTAR_IMAGE = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/gui/journal/journal_mortar_pestle_menu.png");

    private final ItemStack outputItem;
    private final List<List<ItemStack>> inputItems;

    public TextMortarPageScreen(String translateID, ItemStack outputItem, List<List<ItemStack>> inputItems, int indexID, JournalPages journalPages) {
        super(translateID, indexID, journalPages);
        this.inputItems = inputItems;
        this.outputItem = outputItem;
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int backgroundPosX = (this.width - IMAGE_WIDTH) / 2;
        int backgroundPosY = (this.height - IMAGE_HEIGHT) / 2;

        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.blit(MORTAR_IMAGE, backgroundPosX, backgroundPosY,
                0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        int halfScreenPosX = this.width / 2;
        int halfScreenPosY = this.height / 2;

        int posX = halfScreenPosX - 33;
        int posY = halfScreenPosY + 28+17;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                int index = i * 2 + j;
                if (inputItems.size() > index) {
                    final List<ItemStack> ingredients = inputItems.get(index);

                    final ItemStack stack;
                    if(ingredients.size() == 1) stack = ingredients.getFirst();
                    else if(ingredients.size() > 1) stack = ingredients.get((int)(this.minecraft.level.getGameTime() % (20L * ingredients.size()) / 20));
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

        guiGraphics.renderItem(outputItem, posX + 56, posY + 9);
    }
}
