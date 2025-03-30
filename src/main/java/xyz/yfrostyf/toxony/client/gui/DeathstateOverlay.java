package xyz.yfrostyf.toxony.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.client.ClientToxData;

// Thank you Iron's Spells n Spellbooks for the really nice overlay reference!

public class DeathstateOverlay implements LayeredDraw.Layer{
    private final static ResourceLocation RESOURCE =
            ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/gui/overlays/toxin_deathstate.png");
    private final int[] TEXTURE_ARRAY = {0, 256};


    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (Minecraft.getInstance().options.hideGui
                || Minecraft.getInstance().player == null
                || Minecraft.getInstance().player.isSpectator()
                || ClientToxData.getToxData() == null) return;

        var screenWidth = guiGraphics.guiWidth();
        var screenHeight = guiGraphics.guiHeight();

        boolean isDeathstate = ClientToxData.getToxData().getDeathState();

        if (isDeathstate) {
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ONE,
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ONE
            );
            guiGraphics.setColor( 0.0F, 0.25F, 0.0F, 0.75F);

            // Overlay | (ResourceLocation atlasLocation,
            // int x, int y,
            // int width, int height,
            // float uOffset, float vOffset,
            // int uWidth, int vHeight
            // int textureWidth, int textureHeight)
            guiGraphics.blit(RESOURCE,
                    0, 0,
                    screenWidth, screenHeight,
                    0, TEXTURE_ARRAY[Mth.floor((float)Minecraft.getInstance().player.tickCount % 60.0F / 30.0F) > 0 ? 1 : 0],
                    256, 256,
                    256, 512);


            guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
        }
    }


}
