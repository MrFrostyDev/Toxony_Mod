package xyz.yfrostyf.toxony.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.blocks.entities.MortarPestleBlockEntity;
import xyz.yfrostyf.toxony.network.ClientStartPestlingPacket;


public class MortarPestleScreen extends AbstractContainerScreen<MortarPestleMenu> {
    private static final ResourceLocation MORTAR_PESTLE_MENU = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/gui/container/mortar_pestle_menu.png");
    static final ResourceLocation MORTAR_PESTLE_BUTTON_ACTIVE = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "container/mortar_pestle_button_active");
    static final ResourceLocation MORTAR_PESTLE_BUTTON_INACTIVE = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "container/mortar_pestle_button_inactive");
    static final ResourceLocation MORTAR_PESTLE_BUTTON_HOVER = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "container/mortar_pestle_button_hover");


    private final MortarPestleMenu menu;

    protected MortarPestleConfirmButton cbutton;
    protected MortarPestleBlockEntity blockEntity;
    protected Player player;

    public MortarPestleScreen(MortarPestleMenu menu, Inventory plyInventory, Component title) {
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
        cbutton = new MortarPestleConfirmButton(this.leftPos + 108, this.topPos + 32, this);
        this.addRenderableWidget(cbutton);
        cbutton.active = false;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int half_screen_x = (this.width - this.imageWidth) / 2;
        int half_screen_y = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(MORTAR_PESTLE_MENU, half_screen_x, half_screen_y, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {}

    @Override
    protected void containerTick() {
        cbutton.active = (!blockEntity.getResultItem().isEmpty());
    }

    @OnlyIn(Dist.CLIENT)
    protected static class MortarPestleConfirmButton extends AbstractButton {
        static final int BUTTON_TEXTURE_WIDTH = 25;
        static final int BUTTON_TEXTURE_HEIGHT = 17;

        private final MortarPestleScreen screen;

        protected MortarPestleConfirmButton(int x, int y, MortarPestleScreen screen) {
            super(x, y, BUTTON_TEXTURE_WIDTH, BUTTON_TEXTURE_HEIGHT, CommonComponents.EMPTY);
            this.screen = screen;
            screen.cbutton = this;
        }

        @Override
        public void onPress() {
            if(!this.active)return;
            PacketDistributor.sendToServer(new ClientStartPestlingPacket());
            screen.player.closeContainer();
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            if (!this.active) {
                guiGraphics.blitSprite(
                        MORTAR_PESTLE_BUTTON_INACTIVE, BUTTON_TEXTURE_WIDTH, BUTTON_TEXTURE_HEIGHT,
                        0, 0, this.getX(), this.getY(), BUTTON_TEXTURE_WIDTH, BUTTON_TEXTURE_HEIGHT);
            }
            else if (this.isHoveredOrFocused()) {
                guiGraphics.blitSprite(
                        MORTAR_PESTLE_BUTTON_HOVER, BUTTON_TEXTURE_WIDTH, BUTTON_TEXTURE_HEIGHT,
                        0, 0, this.getX(), this.getY(), BUTTON_TEXTURE_WIDTH, BUTTON_TEXTURE_HEIGHT);
            }
            else { // For both active/pressed
                guiGraphics.blitSprite(
                        MORTAR_PESTLE_BUTTON_ACTIVE, BUTTON_TEXTURE_WIDTH, BUTTON_TEXTURE_HEIGHT,
                        0, 0, this.getX(), this.getY(), BUTTON_TEXTURE_WIDTH, BUTTON_TEXTURE_HEIGHT);
            }
        }

        @Override
        public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            this.defaultButtonNarrationText(narrationElementOutput);
        }
    }
}
