package xyz.yfrostyf.toxony.client.gui.journal;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import xyz.yfrostyf.toxony.ToxonyMain;

public class ImagePageScreen extends PageScreen{
    private final ResourceLocation IMAGE;

    private final int offsetX;
    private final int offsetY;

    public ImagePageScreen(String translateID, String locationId, int indexID, JournalPages journalPages) {
        super(translateID, indexID, journalPages);
        this.IMAGE = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, locationId);
        this.offsetX = 0;
        this.offsetY = 0;
    }

    public ImagePageScreen(String translateID, String locationId, int offsetX, int offsetY, int indexID, JournalPages journalPages) {
        super(translateID, indexID, journalPages);
        this.IMAGE = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, locationId);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int imageWidth = 188;
        int imageHeight = 210;

        int backgroundPosX = (this.width - imageWidth) / 2;
        int backgroundPosY = (this.height - imageHeight) / 2;

        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.blit(IMAGE, backgroundPosX + offsetX, backgroundPosY + offsetY,
                0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }
}
