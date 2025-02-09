package xyz.yfrostyf.toxony.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import xyz.yfrostyf.toxony.ToxonyMain;

public class AlembicScreen extends AbstractContainerScreen<AlembicMenu> {
    private static final ResourceLocation ALEMBIC_MENU = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/gui/container/alembic_menu.png");
    private static final ResourceLocation ALEMBIC_SPRITES = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/gui/container/alembic_sprites.png");
    private static final int[] BUBBLE_LENGTHS = new int[]{27, 24, 20, 16, 11, 6, 0};

    private final AlembicMenu menu;


    public AlembicScreen(AlembicMenu menu, Inventory plyInventory, Component title) {
        super(menu, plyInventory, title);
        this.menu = menu;
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        final int SPRITE_TEXTURE_WIDTH = 18;
        final int SPRITE_TEXTURE_HEIGHT = 32;

        final int FUEL_WIDTH = 18;
        final int FUEL_HEIGHT = 4;
        final int FUEL_VOFFSET = 28;
        final int FUEL_POSX = 51;
        final int FUEL_POSY = 63;

        final int BAR_WIDTH = 3;
        final int BAR_HEIGHT = 25;
        final int BAR_UOFFSET = 0;
        final int BAR_POSX = 143;
        final int BAR_POSY = 41 + BAR_HEIGHT;

        final int BUBBLE_WIDTH = 10;
        final int BUBBLE_HEIGHT = 27;
        final int BUBBLE_UOFFSET = 4;
        final int BUBBLE_POSX = 55;
        final int BUBBLE_POSY = 60;

        int half_screen_x = (this.width - this.imageWidth) / 2;
        int half_screen_y = (this.height - this.imageHeight) / 2;

        int fuelWidth = (int)Mth.clamp(FUEL_WIDTH * menu.getFuelProgress(), 0, FUEL_WIDTH);;
        int barHeight = Mth.ceil(BAR_HEIGHT * this.menu.getBoilProgress());
        int bubbleHeight = BUBBLE_LENGTHS[(int)menu.getBoilTick() / 2 % 7];

        // Main Menu
        guiGraphics.blit(ALEMBIC_MENU,
                half_screen_x, half_screen_y,
                0, 0,
                this.imageWidth, this.imageHeight);

        // Fuel Sprite
        if(this.menu.hasFuel()) {
            guiGraphics.blit(ALEMBIC_SPRITES,
                    this.leftPos + FUEL_POSX, this.topPos + FUEL_POSY,
                    0, FUEL_VOFFSET,
                    fuelWidth, FUEL_HEIGHT,
                    SPRITE_TEXTURE_WIDTH, SPRITE_TEXTURE_HEIGHT);
        }

        if(this.menu.isBoiling()) {
            // Bar Sprite
            guiGraphics.blit(ALEMBIC_SPRITES,
                    this.leftPos + BAR_POSX, this.topPos + BAR_POSY - barHeight,
                    BAR_UOFFSET, BAR_HEIGHT - barHeight,
                    BAR_WIDTH, barHeight,
                    SPRITE_TEXTURE_WIDTH, SPRITE_TEXTURE_HEIGHT);

            // Bubble Sprite
            guiGraphics.blit(ALEMBIC_SPRITES,
                    this.leftPos + BUBBLE_POSX, this.topPos + BUBBLE_POSY - bubbleHeight,
                    BUBBLE_UOFFSET, BUBBLE_HEIGHT - bubbleHeight,
                    BUBBLE_WIDTH, bubbleHeight,
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
