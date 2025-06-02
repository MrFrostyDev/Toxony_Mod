package xyz.yfrostyf.toxony.client.gui.block;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import xyz.yfrostyf.toxony.ToxonyMain;

public class CopperCrucibleScreen extends AbstractContainerScreen<CopperCrucibleMenu> {
    private static final ResourceLocation COPPER_CRUCIBLE_MENU = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/gui/container/copper_crucible_menu.png");
    static final ResourceLocation COPPER_CRUCIBLE_FLAMES = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "container/crucible_flames");
    static final ResourceLocation PROGRESS_BAR = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "container/progress_bar");

    private final CopperCrucibleMenu menu;

    public CopperCrucibleScreen(CopperCrucibleMenu menu, Inventory plyInventory, Component title) {
        super(menu, plyInventory, title);

        this.menu = menu;
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        final int FIRE_WIDTH = 13;
        final int FIRE_HEIGHT = 11;
        final int FIRE_POSX = 81;
        final int FIRE_POSY = 42 + FIRE_HEIGHT;

        final int BAR_WIDTH = 3;
        final int BAR_HEIGHT = 25;
        final int BAR_POSX = 115;
        final int BAR_POSY = 17 + BAR_HEIGHT;

        int half_screen_x = (this.width - this.imageWidth) / 2;
        int half_screen_y = (this.height - this.imageHeight) / 2;

        int fireHeight = Mth.ceil(FIRE_HEIGHT * this.menu.getLitProgress());
        int barHeight = Mth.ceil(BAR_HEIGHT * this.menu.getCookProgress());

        // Main Menu
        guiGraphics.blit(COPPER_CRUCIBLE_MENU,
                half_screen_x, half_screen_y,
                0, 0,
                this.imageWidth, this.imageHeight);

        // Fire Sprite
        if(this.menu.isLit()) {
            guiGraphics.blitSprite(COPPER_CRUCIBLE_FLAMES,
                    FIRE_WIDTH, FIRE_HEIGHT,
                    0, FIRE_HEIGHT - fireHeight,
                    this.leftPos + FIRE_POSX, this.topPos + FIRE_POSY - fireHeight,
                    FIRE_WIDTH, fireHeight);
        }

        // Bar Sprite
        if(this.menu.isCooking()) {
            guiGraphics.blitSprite(PROGRESS_BAR,
                    BAR_WIDTH, BAR_HEIGHT,
                    0, BAR_HEIGHT - barHeight,
                    this.leftPos + BAR_POSX, this.topPos + BAR_POSY - barHeight,
                    BAR_WIDTH, barHeight);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {}

}
