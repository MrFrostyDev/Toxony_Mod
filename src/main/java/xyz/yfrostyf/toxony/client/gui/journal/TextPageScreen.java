package xyz.yfrostyf.toxony.client.gui.journal;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import xyz.yfrostyf.toxony.ToxonyMain;

import java.util.List;

// Thank you XFactHD on NeoForge Discord for the reference for text wrapping!
public class TextPageScreen extends PageScreen{
    protected static final ResourceLocation IMAGE = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/gui/journal/journal_background.png");
    protected final int MAX_LINE_WIDTH = 105;

    protected static final int IMAGE_WIDTH = 188;
    protected static final int IMAGE_HEIGHT = 210;

    protected final Font font;
    protected final List<FormattedCharSequence> charSeqLines;

    public TextPageScreen(String translateID, ChatFormatting[] formatting, int indexID, JournalPages journalPages) {
        super(translateID, indexID, journalPages);
        this.font = Minecraft.getInstance().font;

        charSeqLines = (font.split(Component.translatable(translateID).withStyle(formatting), MAX_LINE_WIDTH));
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

        super.render(guiGraphics, mouseX, mouseY, partialTick);

        int yOffset = 0;
        for(FormattedCharSequence line : charSeqLines){
            guiGraphics.drawString(Minecraft.getInstance().font, line,
                    backgroundPosX + 10, backgroundPosY - 20 + yOffset,
                    0, false);
            yOffset += font.lineHeight;
        }
    }
}
