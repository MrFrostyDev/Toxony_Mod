package xyz.yfrostyf.toxony.client.gui.journal;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.client.utils.ClientUtil;

import java.util.List;

// Thank you Malum mod for the reference for rendering ingredients
public class TextCruciblePageScreen extends TextPageScreen{
    private static final ResourceLocation CRUCIBLE_IMAGE = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/gui/journal/journal_crucible_menu.png");

    private final ItemStack outputItem;
    private final List<ItemStack> inputItem;

    public TextCruciblePageScreen(String translateID, ItemStack outputItem, List<ItemStack> inputItem, int indexID, JournalPages journalPages) {
        super(translateID, indexID, journalPages);
        this.inputItem = inputItem;
        this.outputItem = outputItem;
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int backgroundPosX = (this.width - IMAGE_WIDTH) / 2;
        int backgroundPosY = (this.height - IMAGE_HEIGHT) / 2;

        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.blit(CRUCIBLE_IMAGE, backgroundPosX, backgroundPosY,
                0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        int halfScreenPosX = this.width / 2;
        int halfScreenPosY = this.height / 2;

        int posX = halfScreenPosX - 25;
        int posY = halfScreenPosY + 45+17;

        if (!inputItem.isEmpty()) {
            final ItemStack stack = inputItem.get((int)(ClientUtil.getClientTick() % (20L * inputItem.size()) / 20));

            if (!stack.isEmpty()) {
                guiGraphics.renderItem(stack, posX, posY);
                guiGraphics.renderItemDecorations(this.minecraft.font, stack, posX, posY, null);
                if (mouseX > posX && mouseX < posX + 16 && mouseY > posY && mouseY < posY + 16) {
                    guiGraphics.renderComponentTooltip(this.minecraft.font, Screen.getTooltipFromItem(this.minecraft, stack), mouseX, mouseY);
                }
            }
        }

        int outputPosX = posX + 47;
        guiGraphics.renderItem(outputItem, outputPosX, posY);
        if (mouseX > outputPosX && mouseX < outputPosX + 16 && mouseY > posY && mouseY < posY + 16) {
            guiGraphics.renderComponentTooltip(this.minecraft.font, Screen.getTooltipFromItem(this.minecraft, outputItem), mouseX, mouseY);
        }
    }
}
