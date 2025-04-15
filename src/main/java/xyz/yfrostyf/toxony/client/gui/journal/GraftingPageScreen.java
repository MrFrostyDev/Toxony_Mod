package xyz.yfrostyf.toxony.client.gui.journal;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.affinity.Affinity;

import java.util.Map;

public class GraftingPageScreen extends PageScreen{
    protected static final ResourceLocation IMAGE = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/gui/journal/journal_background.png");
    private static final ResourceLocation GRAFTING_SPRITE = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "journal/grafting_menu");
    private static final ResourceLocation GRAFTING_BACKGROUND = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/gui/journal/journal_grafting_background.png");
    private static final ResourceLocation AFFINITY_IMAGE = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/gui/affinity_icons.png");

    protected static final int IMAGE_WIDTH = 203;
    protected static final int IMAGE_HEIGHT = 237;

    private static final int GRAFTING_IMAGE_WIDTH = 78;
    private static final int GRAFTING_IMAGE_HEIGHT = 18;

    private static final int AFFINITY_IMAGE_WIDTH = 52;
    private static final int AFFINITY_IMAGE_HEIGHT = 39;

    // |================= Icons UVs =================|

    private static final int ICON_WIDTH = 13;
    private static final int ICON_HEIGHT = 13;

    private final Map<Pair<ItemStack, ItemStack>, Affinity> inputs;

    public GraftingPageScreen(String translateID, Map<Pair<ItemStack, ItemStack>, Affinity> inputs, int indexID, JournalPages journalPages) {
        super(translateID, indexID, journalPages);
        this.inputs = inputs;
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int backgroundPosX = (this.width - IMAGE_WIDTH) / 2;
        int backgroundPosY = (this.height - IMAGE_HEIGHT) / 2;

        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.blit(IMAGE, backgroundPosX, backgroundPosY,
                0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        guiGraphics.blit(GRAFTING_BACKGROUND, backgroundPosX, backgroundPosY,
                0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        int halfScreenPosX = this.width / 2;
        int halfScreenPosY = this.height / 2;

        int posX = halfScreenPosX - 35;
        int posY = halfScreenPosY - 45;

        int posXOffsetAffinity = 31;
        int posXOffsetEvolved = 60;

        int i = 0;
        for(Map.Entry<Pair<ItemStack, ItemStack>, Affinity> entry : inputs.entrySet()){
            int index = entry.getValue().getIndex();
            ItemStack base = entry.getKey().getFirst();
            ItemStack evolved = entry.getKey().getSecond();

            // Grafting Menu
            guiGraphics.blitSprite(
                    GRAFTING_SPRITE, GRAFTING_IMAGE_WIDTH, GRAFTING_IMAGE_HEIGHT,
                    0, 0, posX-1, posY-1+i, GRAFTING_IMAGE_WIDTH, GRAFTING_IMAGE_HEIGHT);

            // Icon
            guiGraphics.blit(AFFINITY_IMAGE,
                    posX + posXOffsetAffinity, posY+i,
                    index * 13, (int)((double)(index / 4) * 13),
                    ICON_WIDTH, ICON_HEIGHT,
                    AFFINITY_IMAGE_WIDTH, AFFINITY_IMAGE_HEIGHT);

            guiGraphics.renderItem(base, posX, posY+i);
            guiGraphics.renderItemDecorations(this.minecraft.font, base, posX, posY+i, null);

            guiGraphics.renderItem(evolved, posX + posXOffsetEvolved, posY+i);
            guiGraphics.renderItemDecorations(this.minecraft.font, evolved, posX + posXOffsetEvolved, posY+i, null);

            if (mouseX > posX && mouseX < posX + 16 && mouseY > posY + i && mouseY < posY + i + 16) {
                guiGraphics.renderComponentTooltip(this.minecraft.font, Screen.getTooltipFromItem(this.minecraft, base), mouseX, mouseY);
            }
            if (mouseX > posX + posXOffsetEvolved && mouseX < posX + posXOffsetEvolved + 16 && mouseY > posY + i && mouseY < posY + i + 16) {
                guiGraphics.renderComponentTooltip(this.minecraft.font, Screen.getTooltipFromItem(this.minecraft, evolved), mouseX, mouseY);
            }

            i += 20;
        }
    }
}
