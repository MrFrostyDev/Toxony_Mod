package xyz.yfrostyf.toxony.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.client.ClientToxData;
import xyz.yfrostyf.toxony.api.tox.ToxData;
import xyz.yfrostyf.toxony.api.util.GraphicUtil;

import java.util.ArrayList;
import java.util.List;


public class MutagenInfoScreen extends Screen {
    private static final ResourceLocation IMAGE = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/gui/mutagen_info.png");
    private static final ResourceLocation IMAGE_PANEL = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "mutageninfo/mutagen_panel");
    private static final ResourceLocation IMAGE_TOXIN = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "mutageninfo/toxin");

    private static final int IMAGE_WIDTH = 256;
    private static final int IMAGE_HEIGHT = 256;

    private static final int IMAGE_PANEL_WIDTH = 62;
    private static final int IMAGE_PANEL_HEIGHT = 149;

    private static final int IMAGE_TOXIN_WIDTH = 249;
    private static final int IMAGE_TOXIN_HEIGHT = 31;

    MobEffectTextureManager effectTextureManager;

    private Font font;
    private ToxData toxData;

    public MutagenInfoScreen() {
        super(Component.empty());
        this.minecraft = Minecraft.getInstance();
        this.font = Minecraft.getInstance().font;
        this.toxData = ClientToxData.getToxData();
        this.effectTextureManager = Minecraft.getInstance().getMobEffectTextures();
    }

    public void openScreen(){
        if(this.minecraft == null) return;
        this.minecraft.setScreen(this);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(keyCode == Minecraft.getInstance().options.keyInventory.getKey().getValue()){
            this.onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int backgroundPosX = (this.width - IMAGE_WIDTH) / 2;
        int backgroundPosY = (this.height - IMAGE_HEIGHT) / 2;

        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.blit(IMAGE, backgroundPosX, backgroundPosY,
                0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        int backgroundPosX = (this.width - IMAGE_WIDTH) / 2;
        int backgroundPosY = (this.height - IMAGE_HEIGHT) / 2;

        if(toxData.getMutagens().isEmpty()){
            this.renderNoMutagens(guiGraphics, mouseX, mouseY, partialTick);
        }
        else{
            this.renderMutagens(guiGraphics, mouseX, mouseY, partialTick);
        }

        float progress = Math.clamp(this.toxData.getTox() / this.toxData.getThresholdTolGoal(), 0, 1.0F) ;
        guiGraphics.blitSprite(IMAGE_TOXIN,
                IMAGE_TOXIN_WIDTH, IMAGE_TOXIN_HEIGHT,
                0, 0,
                backgroundPosX + 4, backgroundPosY + 201,
                Mth.floor(IMAGE_TOXIN_WIDTH * progress), IMAGE_TOXIN_HEIGHT);

        guiGraphics.drawString(this.font,
                (int)this.toxData.getTox() + " | " + (int)this.toxData.getThresholdTolGoal(),
                    backgroundPosX - 10 + (IMAGE_TOXIN_WIDTH / 2), backgroundPosY + 198 + (IMAGE_TOXIN_HEIGHT / 2), 0xa4ff6f, false);
    }

    public void renderNoMutagens(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick){
        int maxLineWidth = 100;
        int backgroundPosX = (this.width - IMAGE_WIDTH) / 2;
        int backgroundPosY = (this.height - IMAGE_HEIGHT) / 2;

        List<FormattedCharSequence> charSeqLines = font.split(
                Component.translatable("mutageninfo.toxony.empty").withStyle(Style.EMPTY.withColor(0xce5227)), maxLineWidth);

        int yOffset = 0;
        for(FormattedCharSequence line : charSeqLines){
            guiGraphics.drawString(Minecraft.getInstance().font, line,
                    backgroundPosX + 65, backgroundPosY + 55 + yOffset,
                    0, false);
            yOffset += font.lineHeight;
        }
    }

    public void renderMutagens(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick){
        float scaleText = 0.67F;
        int maxLineWidth = 78;
        int backgroundPosX = (this.width - IMAGE_WIDTH) / 2;
        int backgroundPosY = (this.height - IMAGE_HEIGHT) / 2;
        int textPosX = ((this.width - maxLineWidth) / 2) - 30;
        int textPosY = ((this.height - maxLineWidth) / 2) - 12;

        List<Holder<MobEffect>> effectCache = new ArrayList<>(3);
        int xOffset = 0;
        int yOffsetAmount = Mth.ceil(font.lineHeight * scaleText);

        for(Holder<MobEffect> holder : toxData.getMutagens()){
            if(holder.getKey() == null) continue;

            // Check how many of the same mutagen we have
            int dupeAmount = getDuplicateMutagens(effectCache, holder);

            // Mutagen Effect Icon
            guiGraphics.blit(
                    backgroundPosX + 61 + xOffset, backgroundPosY + 48, 0,
                    18, 18,
                    this.effectTextureManager.get(holder));

            // Mutagen Effect Level
            String level = switch (dupeAmount) {
                case 1 -> "||";
                case 2 -> "|||";
                default -> "|";
            };
            GraphicUtil.drawStringWithScale(guiGraphics, this.font, level,
                    backgroundPosX + 95 + xOffset, backgroundPosY + 51,
                    1.5F,
                    0xfaf6e3, true);

            // Image Panel
            guiGraphics.blitSprite(IMAGE_PANEL,
                    IMAGE_PANEL_WIDTH, IMAGE_PANEL_HEIGHT,
                    0, 0,
                    backgroundPosX + IMAGE_PANEL_WIDTH - 9 + xOffset, backgroundPosY + 45,
                    IMAGE_PANEL_WIDTH, IMAGE_PANEL_HEIGHT);

            // Text
            Component textComponent = Component
                    .translatable("mutageninfo.toxony." + holder.getKey().location().getPath() + "." + dupeAmount)
                    .withStyle(Style.EMPTY.withColor(0xcebd81));

            List<FormattedCharSequence> formatted = font.split(textComponent, maxLineWidth);
            int yOffset = 0;
            for(FormattedCharSequence line : formatted){
                GraphicUtil.drawStringWithScale(guiGraphics, this.font, line,
                        textPosX + xOffset, textPosY + yOffset + ((float)font.lineHeight / 2),
                        scaleText,
                        0, false);

                yOffset += yOffsetAmount;
            }

            effectCache.add(holder);
            xOffset += IMAGE_PANEL_WIDTH;
        }

    }

    private static int getDuplicateMutagens(List<Holder<MobEffect>> list, Holder<MobEffect> effect){
        int count = 0;
        for(Holder<MobEffect> effect1 : list){
            if(effect1.value() == effect.value()){
                count++;
            }
        }
        return count;
    }

}
