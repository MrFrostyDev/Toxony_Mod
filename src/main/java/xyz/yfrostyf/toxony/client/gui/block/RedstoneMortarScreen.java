package xyz.yfrostyf.toxony.client.gui.block;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.blocks.entities.RedstoneMortarBlockEntity;


public class RedstoneMortarScreen extends AbstractContainerScreen<RedstoneMortarMenu> {
    private static final ResourceLocation REDSTONE_MORTAR_MENU = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/gui/container/redstone_mortar_menu.png");
    private static final ResourceLocation PROGRESS_ARROW = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "container/progress_arrow");

    int PROGRESS_ARROW_WIDTH = 10;
    int PROGRESS_ARROW_HEIGHT = 7;

    private final RedstoneMortarMenu menu;

    protected RedstoneMortarBlockEntity blockEntity;
    protected Player player;

    public RedstoneMortarScreen(RedstoneMortarMenu menu, Inventory plyInventory, Component title) {
        super(menu, plyInventory, title);

        this.menu = menu;
        this.player = plyInventory.player;
        this.blockEntity = menu.blockEntity;
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int half_screen_x = (this.width - this.imageWidth) / 2;
        int half_screen_y = (this.height - this.imageHeight) / 2;

        // Menu Background
        guiGraphics.blit(REDSTONE_MORTAR_MENU,
                half_screen_x, half_screen_y,
                0, 0,
                this.imageWidth, this.imageHeight);

        // Progress Sprite
        // Check if its pestling
        if(this.menu.isPestling()) {
            float progress = this.menu.getPestleProgress();

            guiGraphics.blitSprite(PROGRESS_ARROW,
                    PROGRESS_ARROW_WIDTH, PROGRESS_ARROW_HEIGHT,
                    0, 0,
                    this.leftPos + 107, this.topPos + 38,
                    Mth.floor(PROGRESS_ARROW_WIDTH * progress), PROGRESS_ARROW_HEIGHT);
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
