package xyz.yfrostyf.toxony.client.gui;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;
import top.theillusivec4.curios.api.CuriosApi;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.tox.ToxData;
import xyz.yfrostyf.toxony.api.util.GraphicUtil;
import xyz.yfrostyf.toxony.registries.DataAttachmentRegistry;
import xyz.yfrostyf.toxony.registries.ItemRegistry;

import java.util.List;

public class MobToxBarOverlay implements LayeredDraw.Layer {
    private static final ResourceLocation BACKGROUND_BAR = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "toxbar/background_bar");
    private static final ResourceLocation TOLERANCE_BAR = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "toxbar/tolerance_bar");
    private static final ResourceLocation TOXIN_BAR = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "toxbar/toxin_bar");
    private static final ResourceLocation SKULL_POINTER = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "toxbar/skull_pointer");
    private static final ResourceLocation SKULL_POINTER_RED = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "toxbar/skull_pointer_red");
    private static final ResourceLocation COMPLETED_MUTAGEN = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "toxbar/completed_mutagen");


    public static final int BAR_WIDTH = 62;
    public static final int BAR_HEIGHT = 5;

    public static final int SKULL_WIDTH = 9;
    public static final int SKULL_HEIGHT = 12;

    public static final int COMPLETED_WIDTH = 62;
    public static final int COMPLETED_HEIGHT = 119;

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Player player = Minecraft.getInstance().player;
        Entity pickedEntity = Minecraft.getInstance().crosshairPickEntity;

        if(player == null)return;
        if(Minecraft.getInstance().options.hideGui)return;
        if(player.isSpectator())return;
        if(!isToxGaugeOnPlayer(player) || !player.isHolding(ItemRegistry.MAGNIFYING_GLASS.get()))return;
        if(pickedEntity == null)return;
        if(!(pickedEntity instanceof LivingEntity livingEntity))return;
        if(!livingEntity.hasData(DataAttachmentRegistry.TOX_DATA) && !livingEntity.hasData(DataAttachmentRegistry.MOB_TOXIN))return;

        float tox;
        float tolerance;
        float thresholdGoal;
        boolean deathState;
        if(livingEntity.hasData(DataAttachmentRegistry.TOX_DATA)){
            ToxData toxData = livingEntity.getData(DataAttachmentRegistry.TOX_DATA);
            tox = toxData.getTox();
            tolerance = toxData.getTolerance();
            thresholdGoal = toxData.getThresholdTolGoal();
            deathState = toxData.getDeathState();
        }
        else{
            tox = livingEntity.getData(DataAttachmentRegistry.MOB_TOXIN);
            tolerance = livingEntity.getMaxHealth();
            thresholdGoal = livingEntity.getMaxHealth();
            deathState = false;
        }

        int toxBarWidth = Mth.floor((BAR_WIDTH - 2) * Math.min(tox / thresholdGoal, 1));
        int tolBarWidth = Mth.floor((BAR_WIDTH - 2) * Math.min(tolerance / thresholdGoal, 1));

        int posX = (guiGraphics.guiWidth() / 2) - (BAR_WIDTH / 2) - 1;
        int posY = (guiGraphics.guiHeight() / 2) + 14;

        if(livingEntity.hasData(DataAttachmentRegistry.MOB_TOXIN) && tox > livingEntity.getAttributeBaseValue(Attributes.MAX_HEALTH)){
            float scaleText = 0.67F;

            Font font = Minecraft.getInstance().font;
            int yOffsetAmount = Mth.ceil(font.lineHeight * scaleText);

            // Completed Mutagen
            guiGraphics.blitSprite(COMPLETED_MUTAGEN,
                    COMPLETED_WIDTH, COMPLETED_HEIGHT,
                    0, 0,
                    posX, posY - 6,
                    COMPLETED_WIDTH, COMPLETED_HEIGHT);

            // Text
            Component textComponent = Component
                    .translatable("mutageninfo.toxony.mob_mutagen")
                    .withStyle(Style.EMPTY.withColor(0xcebd81));

            List<FormattedCharSequence> formatted = font.split(textComponent, 78);
            int yOffset = 0;
            for(FormattedCharSequence line : formatted){
                GraphicUtil.drawStringWithScale(guiGraphics, font, line,
                        posX + 5, posY + ((float)font.lineHeight / 2) + yOffset + 34,
                        scaleText,
                        0, false);

                yOffset += yOffsetAmount;
            }
        }
        else{
            // Back Bar
            guiGraphics.blitSprite(BACKGROUND_BAR,
                    BAR_WIDTH, BAR_HEIGHT,
                    0, 0,
                    posX, posY,
                    BAR_WIDTH, BAR_HEIGHT);

            // Tol Bar
            guiGraphics.blitSprite(TOLERANCE_BAR,
                    tolBarWidth, BAR_HEIGHT,
                    0, 0,
                    posX + 1, posY,
                    tolBarWidth, BAR_HEIGHT);

            // Tox Bar
            guiGraphics.blitSprite(TOXIN_BAR,
                    toxBarWidth, BAR_HEIGHT,
                    0, 0,
                    posX + 1, posY,
                    toxBarWidth, BAR_HEIGHT);

            // Skull
            guiGraphics.blitSprite(deathState ? SKULL_POINTER_RED : SKULL_POINTER,
                    SKULL_WIDTH, SKULL_HEIGHT,
                    0, 0,
                    Math.clamp(posX + toxBarWidth - 4, posX - 2, posX + BAR_WIDTH - 2), posY + 1,
                    SKULL_WIDTH, SKULL_HEIGHT);
        }

    }

    private static boolean isToxGaugeOnPlayer(Player player){
        boolean isHolding = player.isHolding(ItemRegistry.TOX_GAUGE.get());
        boolean isInCurios = false;

        if(ModList.get().isLoaded("curios") && CuriosApi.getCuriosInventory(Minecraft.getInstance().player).isPresent()){
            isInCurios = CuriosApi.getCuriosInventory(Minecraft.getInstance().player).get().isEquipped(ItemRegistry.TOX_GAUGE.get());
        }

        return isHolding || isInCurios;
    }
}
