package xyz.yfrostyf.toxony.client.gui;

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
import xyz.yfrostyf.toxony.blocks.entities.AlchemicalForgeBlockEntity;
import xyz.yfrostyf.toxony.network.ClientStartAlchemicalForgePacket;
import xyz.yfrostyf.toxony.network.ClientStartPestlingPacket;

public class AlchemicalForgeScreen extends AbstractContainerScreen<AlchemicalForgeMenu> {
    public static final ResourceLocation ALCHEMICAL_FORGE_MENU = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/gui/container/alchemical_forge_menu.png");
    public static final ResourceLocation ALCHEMICAL_FORGE_SPRITES = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/gui/container/alchemical_forge_sprites.png");
    final int SPRITE_TEXTURE_WIDTH = 53;
    final int SPRITE_TEXTURE_HEIGHT = 23;

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
        final int FUEL_HEIGHT = SPRITE_TEXTURE_HEIGHT;
        final int FUEL_UOFFSET = 46;
        int half_screen_x = (this.width - this.imageWidth) / 2;
        int half_screen_y = (this.height - this.imageHeight) / 2;

        int fuelHeight = Mth.ceil(FUEL_HEIGHT * this.menu.blockEntity.getFuelPercentage());
        guiGraphics.blit(ALCHEMICAL_FORGE_MENU, half_screen_x, half_screen_y, 0, 0, this.imageWidth, this.imageHeight);

        // Fuel Bar
        if(this.menu.blockEntity.hasFuel()) {
            guiGraphics.blit(ALCHEMICAL_FORGE_SPRITES,
                    this.leftPos + this.imageWidth - 32, this.topPos + 69 - fuelHeight,
                    FUEL_UOFFSET, FUEL_HEIGHT - fuelHeight,
                    FUEL_WIDTH, fuelHeight,
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

    @Override
    protected void containerTick() {
        cbutton.active = menu.blockEntity.canForge();
    }

    @OnlyIn(Dist.CLIENT)
    protected static class AlchemicalForgeConfirmButton extends AbstractButton {
        static final int BUTTON_TEXTURE_WIDTH = 53;
        static final int BUTTON_TEXTURE_HEIGHT = 23;

        static final int BUTTON_WIDTH = 23;
        static final int BUTTON_HEIGHT = 13;

        static final int BUTTON_UPOS_INACTIVE = 0;
        static final int BUTTON_UPOS_ACTIVE = 23;

        private final AlchemicalForgeScreen screen;

        protected AlchemicalForgeConfirmButton(int x, int y, AlchemicalForgeScreen screen) {
            super(x, y, BUTTON_TEXTURE_WIDTH, BUTTON_TEXTURE_HEIGHT, CommonComponents.EMPTY);
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
                guiGraphics.blit(
                        AlchemicalForgeScreen.ALCHEMICAL_FORGE_SPRITES, this.getX(), this.getY(),
                        BUTTON_UPOS_INACTIVE, 0, BUTTON_WIDTH, BUTTON_HEIGHT, BUTTON_TEXTURE_WIDTH, BUTTON_TEXTURE_HEIGHT);
            }
            else {
                guiGraphics.blit(
                        AlchemicalForgeScreen.ALCHEMICAL_FORGE_SPRITES, this.getX(), this.getY(),
                        BUTTON_UPOS_ACTIVE, 0, BUTTON_WIDTH, BUTTON_HEIGHT, BUTTON_TEXTURE_WIDTH, BUTTON_TEXTURE_HEIGHT);
            }
        }

        @Override
        public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            this.defaultButtonNarrationText(narrationElementOutput);
        }
    }

}
