package xyz.yfrostyf.toxony.client.gui.journal;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import xyz.yfrostyf.toxony.ToxonyMain;

public abstract class PageScreen extends Screen {
    protected final String translateID;
    protected JournalPages journalPages;
    protected int indexID;

    public PageScreen(String translateID, int indexID, JournalPages journalPages) {
        super(Component.empty());
        this.minecraft = Minecraft.getInstance();
        this.translateID = translateID;
        this.indexID = indexID;
        this.journalPages = journalPages;
    }

    public int getIndexID() {
        return indexID;
    }

    @Override
    protected void init() {
        super.init();
        if(journalPages.hasNext()){
            this.addRenderableWidget(new PageSwitchButton(this, true));
        }
        if(journalPages.getIndex() > 0){
            this.addRenderableWidget(new PageSwitchButton(this, false));
        }
        if(!(this instanceof IndexPageScreen)){
            this.addRenderableWidget(new IndexTabButton(this));
        }
    }

    public void nextPage(){
        if(journalPages.hasNext()){
            journalPages.setIndex(journalPages.getIndex()+1);
            journalPages.updatePage();
        }
    }

    public void previousPage(){
        if(journalPages.getIndex() > 0){
            journalPages.setIndex(journalPages.getIndex()-1);
            journalPages.updatePage();
        }
    }

    @Override
    public boolean handleComponentClicked(Style style) {
        ClickEvent clickevent = style.getClickEvent();
        if (clickevent == null) {
            return false;
        } else if (clickevent.getAction() != ClickEvent.Action.CHANGE_PAGE) {
            boolean flag = super.handleComponentClicked(style);
            if (flag && clickevent.getAction() == ClickEvent.Action.RUN_COMMAND) {
                this.closeScreen();
            }

            return flag;
        }
        return false;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    protected void openScreen() {
        this.minecraft.setScreen(this);
    }

    protected void closeScreen() {
        JournalUtil.setLastPageID(this.translateID);
        this.minecraft.setScreen(null);
    }

    @Override
    public void onClose() {
        JournalUtil.setLastPageID(this.translateID);
        super.onClose();
    }


    @OnlyIn(Dist.CLIENT)
    protected static class PageSwitchButton extends AbstractButton {
        static final ResourceLocation BUTTON_LEFT_RESOURCE = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "journal/left_button");
        static final ResourceLocation BUTTONS_RIGHT_RESOURCE = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "journal/right_button");

        private final PageScreen screen;
        private final boolean isForward;

        private static final int BUTTON_OFFSETX = 75;
        private static final int BUTTON_OFFSETY = -6;

        private static final int BUTTON_TEXTURE_WIDTH = 18;
        private static final int BUTTON_TEXTURE_HEIGHT = 109;

        protected PageSwitchButton(PageScreen screen, boolean isForward) {
            super(isForward ? (screen.width - BUTTON_TEXTURE_WIDTH) / 2 + BUTTON_OFFSETX : (screen.width - BUTTON_TEXTURE_WIDTH) / 2 - BUTTON_OFFSETX,
                    (screen.height - 109) / 2 + BUTTON_OFFSETY,
                    BUTTON_TEXTURE_WIDTH, BUTTON_TEXTURE_HEIGHT,
                    CommonComponents.EMPTY);

            this.screen = screen;
            this.isForward = isForward;
        }

        @Override
        public void onPress() {
            if (!this.active) return;
            if(isForward){
                screen.nextPage();
                return;
            }
            screen.previousPage();
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

            int halfScreenWidth = (guiGraphics.guiWidth() - BUTTON_TEXTURE_WIDTH) / 2;
            int halfScreenHeight = (guiGraphics.guiHeight() - BUTTON_TEXTURE_HEIGHT) / 2;

            int posX = isForward ? halfScreenWidth + BUTTON_OFFSETX : halfScreenWidth - BUTTON_OFFSETX;
            int posY = halfScreenHeight + BUTTON_OFFSETY;

            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            if (this.isForward) {
                if (this.isHoveredOrFocused()) {
                    RenderSystem.setShaderTexture(0, BUTTONS_RIGHT_RESOURCE);
                        guiGraphics.blitSprite(
                                BUTTONS_RIGHT_RESOURCE, BUTTON_TEXTURE_WIDTH, BUTTON_TEXTURE_HEIGHT,
                            0, 0, posX, posY, BUTTON_TEXTURE_WIDTH, BUTTON_TEXTURE_HEIGHT);
                }
            }
            else{
                if (this.isHoveredOrFocused()) {
                    RenderSystem.setShaderTexture(0, BUTTON_LEFT_RESOURCE);
                    guiGraphics.blitSprite(
                            BUTTON_LEFT_RESOURCE, BUTTON_TEXTURE_WIDTH, BUTTON_TEXTURE_HEIGHT,
                            0, 0, posX, posY, BUTTON_TEXTURE_WIDTH, BUTTON_TEXTURE_HEIGHT);
                }
            }
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
        }

        @Override
        public void playDownSound(SoundManager handler) {
            handler.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 0.8F));
        }

        @Override
        public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            this.defaultButtonNarrationText(narrationElementOutput);
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected static class IndexTabButton extends AbstractButton {
        static final ResourceLocation INDEX_BUTTON = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "journal/index_tab_button");

        private final PageScreen screen;
        private float posY1;


        private static final int BUTTON_OFFSETX = -60;
        private static final int BUTTON_OFFSETY = -120;
        private static final int BUTTON_HOVER_OFFSETY = 20;

        private static final int BUTTON_TEXTURE_WIDTH = 25;
        private static final int BUTTON_TEXTURE_HEIGHT = 25;

        protected IndexTabButton(PageScreen screen) {
            super((screen.width - BUTTON_TEXTURE_WIDTH) / 2 + BUTTON_OFFSETX,
                    (screen.height - BUTTON_TEXTURE_HEIGHT) / 2 + BUTTON_OFFSETY,
                    BUTTON_TEXTURE_WIDTH, BUTTON_TEXTURE_HEIGHT,
                    CommonComponents.EMPTY);

            this.screen = screen;
            this.posY1 = this.getY() + BUTTON_HOVER_OFFSETY;

        }

        @Override
        public void onPress() {
            if (!this.active) return;
            screen.journalPages.setPage("journal.toxony.page.index.0");
            screen.journalPages.updatePage();
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            int posX = this.getX();
            int posY = this.getY();

            if (this.isHoveredOrFocused()) posY1 -= (BUTTON_HOVER_OFFSETY) * 0.03F;
            else posY1 += ((BUTTON_HOVER_OFFSETY) * 0.03F);


            // Hover = 0 / 0 pixels offset
            // Non-hover = 1.0 / 25 pixels offset
            int buttonHeight = 25 - Mth.floor(((float)BUTTON_HOVER_OFFSETY * Mth.clamp((posY1 - posY) / (float)BUTTON_HOVER_OFFSETY, 0.0F, 1.0F)));

            if(posY1 < posY) posY1 = posY;
            if(posY1 > posY + BUTTON_HOVER_OFFSETY) posY1 = posY + BUTTON_HOVER_OFFSETY;
            guiGraphics.blitSprite(INDEX_BUTTON,
                    BUTTON_TEXTURE_WIDTH, BUTTON_TEXTURE_HEIGHT,
                    0, 0,
                    posX, Mth.floor(posY1),
                    BUTTON_TEXTURE_WIDTH, buttonHeight);
        }

        @Override
        public void playDownSound(SoundManager handler) {
            handler.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 0.8F));
        }

        @Override
        public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            this.defaultButtonNarrationText(narrationElementOutput);
        }
    }
}
