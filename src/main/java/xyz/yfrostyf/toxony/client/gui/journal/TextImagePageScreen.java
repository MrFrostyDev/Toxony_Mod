package xyz.yfrostyf.toxony.client.gui.journal;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import xyz.yfrostyf.toxony.ToxonyMain;

public class TextImagePageScreen extends TextPageScreen{
    private final ResourceLocation IMAGE;

    protected static final int IMAGE_WIDTH = 203;
    protected static final int IMAGE_HEIGHT = 237;

    private final int offsetX;
    private final int offsetY;

    public TextImagePageScreen(String translateID, String locationId, int indexID, JournalPages journalPages) {
        super(translateID, indexID, journalPages);
        this.IMAGE = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, locationId);
        this.offsetX = 0;
        this.offsetY = 0;
    }

    public TextImagePageScreen(String translateID, String locationId, int offsetX, int offsetY, int indexID, JournalPages journalPages) {
        super(translateID, indexID, journalPages);
        this.IMAGE = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, locationId);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int backgroundPosX = (this.width - IMAGE_WIDTH) / 2;
        int backgroundPosY = (this.height - IMAGE_HEIGHT) / 2;

        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.blit(IMAGE, backgroundPosX + offsetX, backgroundPosY + offsetY,
                0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }
}
