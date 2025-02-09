package xyz.yfrostyf.toxony.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import xyz.yfrostyf.toxony.ToxonyMain;

public class CopperCrucibleScreen extends AbstractContainerScreen<CopperCrucibleMenu> {
    private static final ResourceLocation COPPER_CRUCIBLE_MENU = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/gui/container/copper_crucible_menu.png");
    private final ResourceLocation COPPER_CRUCIBLE_SPRITES = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/gui/container/copper_crucible_sprites.png");

    private final CopperCrucibleMenu menu;

    public CopperCrucibleScreen(CopperCrucibleMenu menu, Inventory plyInventory, Component title) {
        super(menu, plyInventory, title);

        this.menu = menu;
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        final int SPRITE_TEXTURE_WIDTH = 17;
        final int SPRITE_TEXTURE_HEIGHT = 25;

        final int FIRE_WIDTH = 14;
        final int FIRE_HEIGHT = 11;
        final int FIRE_UOFFSET = 3;
        final int FIRE_POSX = 81;
        final int FIRE_POSY = 42 + FIRE_HEIGHT;

        final int BAR_WIDTH = 3;
        final int BAR_UOFFSET = 0;
        final int BAR_POSX = 115;
        final int BAR_POSY = 17 + SPRITE_TEXTURE_HEIGHT;

        int half_screen_x = (this.width - this.imageWidth) / 2;
        int half_screen_y = (this.height - this.imageHeight) / 2;

        int fireHeight = Mth.ceil(FIRE_HEIGHT * this.menu.getLitProgress());
        int barHeight = Mth.ceil(SPRITE_TEXTURE_HEIGHT * this.menu.getCookProgress());

        // Main Menu
        guiGraphics.blit(COPPER_CRUCIBLE_MENU,
                half_screen_x, half_screen_y,
                0, 0,
                this.imageWidth, this.imageHeight);

        // Fire Sprite
        if(this.menu.isLit()) {
            guiGraphics.blit(COPPER_CRUCIBLE_SPRITES,
                    this.leftPos + FIRE_POSX, this.topPos + FIRE_POSY - fireHeight,
                    FIRE_UOFFSET, FIRE_HEIGHT - fireHeight,
                    FIRE_WIDTH, fireHeight,
                    SPRITE_TEXTURE_WIDTH, SPRITE_TEXTURE_HEIGHT);
        }

        // Bar Sprite
        if(this.menu.isCooking()) {
            guiGraphics.blit(COPPER_CRUCIBLE_SPRITES,
                    this.leftPos + BAR_POSX, this.topPos + BAR_POSY - barHeight,
                    BAR_UOFFSET, SPRITE_TEXTURE_HEIGHT - barHeight,
                    BAR_WIDTH, barHeight,
                    SPRITE_TEXTURE_WIDTH, SPRITE_TEXTURE_HEIGHT);
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
