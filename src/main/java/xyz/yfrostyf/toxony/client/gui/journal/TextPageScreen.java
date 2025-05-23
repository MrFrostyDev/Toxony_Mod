package xyz.yfrostyf.toxony.client.gui.journal;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import xyz.yfrostyf.toxony.ToxonyMain;

import java.util.List;

// Thank you XFactHD on NeoForge Discord for the reference for text wrapping!
public class TextPageScreen extends PageScreen{
    protected static final ResourceLocation IMAGE = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/gui/journal/journal_background.png");
    protected final int MAX_LINE_WIDTH = 130;

    protected static final int IMAGE_WIDTH = 203;
    protected static final int IMAGE_HEIGHT = 237;

    protected final Font font;
    protected final List<FormattedCharSequence> charSeqLines;

    protected int yOffset;

    public TextPageScreen(String translateID, int indexID, JournalPages journalPages) {
        super(translateID, indexID, journalPages);
        this.font = Minecraft.getInstance().font;

        charSeqLines = (font.split(Component.translatable(translateID).withStyle(Style.EMPTY.withColor(0xcebd81)), MAX_LINE_WIDTH));
        this.yOffset = 0;
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

        int yOffset1 = yOffset;
        for(FormattedCharSequence line : charSeqLines){
            guiGraphics.drawString(Minecraft.getInstance().font, line,
                    backgroundPosX + 8, backgroundPosY - 23 + yOffset1,
                    0, false);
            yOffset1 += font.lineHeight;
        }
    }
}
