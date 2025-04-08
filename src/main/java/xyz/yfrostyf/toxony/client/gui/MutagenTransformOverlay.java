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

public class MutagenTransformOverlay implements LayeredDraw.Layer{
    private static final ResourceLocation RESOURCE =
            ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/gui/overlays/mutagen_transform.png");
    private static final int[] TEXTURE_ARRAY = {0, 256, 512, 758, 1024, 1280, 1536};
    private static final int DEFAULT_PLAYTIME = 100;
    private static boolean active = false; // Since there is only one type of overlay at any time
    private static int nextTickEnd = 0;

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (Minecraft.getInstance().options.hideGui
                || Minecraft.getInstance().player == null
                || Minecraft.getInstance().player.isSpectator()
                || ClientToxData.getToxData() == null) return;
        if (Minecraft.getInstance().player.tickCount >= nextTickEnd){
            active = false;
            return;
        }

        var screenWidth = guiGraphics.guiWidth();
        var screenHeight = guiGraphics.guiHeight();

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ONE,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ONE
        );
        guiGraphics.setColor( 0.15F, 0.35F, 0.15F, 0.9F);
        float progress = (float)(DEFAULT_PLAYTIME - (nextTickEnd - Minecraft.getInstance().player.tickCount)) / DEFAULT_PLAYTIME;
        int index = Mth.floor((progress % ((float)DEFAULT_PLAYTIME / TEXTURE_ARRAY.length)) * 7.0F);

        // Overlay | (ResourceLocation atlasLocation,
        // int x, int y,
        // int width, int height,
        // float uOffset, float vOffset,
        // int uWidth, int vHeight
        // int textureWidth, int textureHeight)
        guiGraphics.blit(RESOURCE,
                0, 0,
                screenWidth, screenHeight,
                0, TEXTURE_ARRAY[index],
                256, 256,
                256, 1792);


        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
    }

    public static void startAnimation(){
        if (Minecraft.getInstance().options.hideGui
                || Minecraft.getInstance().player == null
                || Minecraft.getInstance().player.isSpectator()
                || active) return;
        nextTickEnd = Minecraft.getInstance().player.tickCount + DEFAULT_PLAYTIME;
        active = true;
    }

}
