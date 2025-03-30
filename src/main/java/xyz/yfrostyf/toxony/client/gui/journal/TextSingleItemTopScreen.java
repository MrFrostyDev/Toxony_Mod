package xyz.yfrostyf.toxony.client.gui.journal;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import xyz.yfrostyf.toxony.ToxonyMain;

import java.util.List;

// Thank you XFactHD on NeoForge Discord for the reference for text wrapping!
public class TextSingleItemTopScreen extends TextPageScreen{
    protected static final ResourceLocation IMAGE = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/gui/journal/journal_single_item_menu_top.png");
    protected final int MAX_LINE_WIDTH = 125;

    protected static final int IMAGE_WIDTH = 203;
    protected static final int IMAGE_HEIGHT = 237;

    protected final Font font;
    protected final List<FormattedCharSequence> charSeqLines;
    protected final ItemStack stack;

    public TextSingleItemTopScreen(String translateID, int indexID, ItemStack stack, JournalPages journalPages) {
        super(translateID, indexID, journalPages);
        this.font = Minecraft.getInstance().font;
        this.stack = stack;

        charSeqLines = (font.split(Component.translatable(translateID).withStyle(Style.EMPTY.withColor(0xcebd81)), MAX_LINE_WIDTH));
        this.yOffset = 40;
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int backgroundPosX = (this.width - IMAGE_WIDTH) / 2;
        int backgroundPosY = (this.height - IMAGE_HEIGHT) / 2;

        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.blit(IMAGE, backgroundPosX, backgroundPosY,
                0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int backgroundPosX = (this.width - MAX_LINE_WIDTH) / 2;
        int backgroundPosY = (this.height - MAX_LINE_WIDTH) / 2;

        int itemPosX = backgroundPosX + 58;
        int itemPosY = backgroundPosY - 23;

        super.render(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.renderItem(stack, itemPosX, itemPosY);
        guiGraphics.renderItemDecorations(this.minecraft.font, stack, itemPosX, itemPosY, null);
        if (mouseX > itemPosX && mouseX < itemPosX + 16 && mouseY > itemPosY && mouseY < itemPosY + 16) {
            guiGraphics.renderComponentTooltip(this.minecraft.font, Screen.getTooltipFromItem(this.minecraft, stack), mouseX, mouseY);
        }
    }
}
