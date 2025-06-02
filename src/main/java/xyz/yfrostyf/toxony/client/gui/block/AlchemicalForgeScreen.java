package xyz.yfrostyf.toxony.client.gui.block;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.network.ClientStartAlchemicalForgePacket;

public class AlchemicalForgeScreen extends AbstractContainerScreen<AlchemicalForgeMenu> {
    public static final ResourceLocation ALCHEMICAL_FORGE_MENU = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/gui/container/alchemical_forge_menu.png");

    static final ResourceLocation ALCHEMICAL_FORGE_FUEL = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "container/alchemical_forge_fuel");
    static final ResourceLocation ALCHEMICAL_FORGE_BUTTON_ACTIVE = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "container/alchemical_forge_button_active");
    static final ResourceLocation ALCHEMICAL_FORGE_BUTTON_INACTIVE = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "container/alchemical_forge_button_inactive");
    static final ResourceLocation ALCHEMICAL_FORGE_BUTTON_HOVER = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "container/alchemical_forge_button_hover");

    protected AlchemicalForgeConfirmButton cbutton;
    private final AlchemicalForgeMenu menu;
    protected Player player;

    public AlchemicalForgeScreen(AlchemicalForgeMenu menu, Inventory plyInventory, Component title) {
        super(menu, plyInventory, title);

        this.menu = menu;
        this.player = plyInventory.player;
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void init() {
        super.init();
        cbutton = new AlchemicalForgeConfirmButton(this.leftPos + 76, this.topPos + 65, this);
        this.addRenderableWidget(cbutton);
        cbutton.active = false;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {

        final int FUEL_WIDTH = 7;
        final int FUEL_HEIGHT = 23;
        final int FUEL_POSX = this.imageWidth - 32;
        final int FUEL_POSY = 69;

        int half_screen_x = (this.width - this.imageWidth) / 2;
        int half_screen_y = (this.height - this.imageHeight) / 2;

        int fuelHeight = Mth.ceil(FUEL_HEIGHT * this.menu.blockEntity.getFuelPercentage());
        guiGraphics.blit(ALCHEMICAL_FORGE_MENU, half_screen_x, half_screen_y, 0, 0, this.imageWidth, this.imageHeight);

        // Fuel Bar
        if(this.menu.blockEntity.hasFuel()) {
            guiGraphics.blitSprite(ALCHEMICAL_FORGE_FUEL,
                    FUEL_WIDTH, FUEL_HEIGHT,
                    0, FUEL_HEIGHT - fuelHeight,
                    this.leftPos + FUEL_POSX, this.topPos + FUEL_POSY - fuelHeight,
                    FUEL_WIDTH, fuelHeight);
        }
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
        cbutton.active = menu.blockEntity.canForge();
    }

    @OnlyIn(Dist.CLIENT)
    protected static class AlchemicalForgeConfirmButton extends AbstractButton {
        static final int BUTTON_WIDTH = 23;
        static final int BUTTON_HEIGHT = 13;

        private final AlchemicalForgeScreen screen;

        protected AlchemicalForgeConfirmButton(int x, int y, AlchemicalForgeScreen screen) {
            super(x, y, BUTTON_WIDTH, BUTTON_HEIGHT, CommonComponents.EMPTY);
            this.screen = screen;
            screen.cbutton = this;
        }

        @Override
        public void onPress() {
            if(!this.active)return;
            PacketDistributor.sendToServer(new ClientStartAlchemicalForgePacket());
            screen.player.closeContainer();
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            if (!this.active) {
                guiGraphics.blitSprite(
                        ALCHEMICAL_FORGE_BUTTON_INACTIVE, BUTTON_WIDTH, BUTTON_HEIGHT,
                        0, 0, this.getX(), this.getY(), BUTTON_WIDTH, BUTTON_HEIGHT);
            }
            else if (this.isHoveredOrFocused()) {
                guiGraphics.blitSprite(
                        ALCHEMICAL_FORGE_BUTTON_HOVER, BUTTON_WIDTH, BUTTON_HEIGHT,
                        0, 0, this.getX(), this.getY(), BUTTON_WIDTH, BUTTON_HEIGHT);
            }
            else { // For both active/pressed
                guiGraphics.blitSprite(
                        ALCHEMICAL_FORGE_BUTTON_ACTIVE, BUTTON_WIDTH, BUTTON_HEIGHT,
                        0, 0, this.getX(), this.getY(), BUTTON_WIDTH, BUTTON_HEIGHT);
            }
        }

        @Override
        public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            this.defaultButtonNarrationText(narrationElementOutput);
        }
    }

}
