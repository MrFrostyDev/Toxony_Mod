package xyz.yfrostyf.toxony.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.mutagens.MutagenData;
import xyz.yfrostyf.toxony.effects.mutagens.BeastMutagenEffect;
import xyz.yfrostyf.toxony.registries.DataAttachmentRegistry;

public class NightPredatorOverlay implements LayeredDraw.Layer{
    private static final ResourceLocation RESOURCE =
            ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/gui/overlays/night_predator_overlay.png");

    private static final int[] TEXTURE_ARRAY_ACTIVATE = {0, 256, 512, 758, 1024, 1280, 1536, 1792, 2048, 2304};
    private static final int[] TEXTURE_ARRAY_DEACTIVATE = {2304, 2048, 1792, 1536, 1280, 1024, 758, 512, 512, 512};
    private static final int DEFAULT_PLAYTIME = 20;

    private static boolean isActivating = true;
    private static boolean active = false; // Since there is only one type of overlay at any time
    private static int nextTickEnd = 0;

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (Minecraft.getInstance().options.hideGui
                || Minecraft.getInstance().player == null
                || Minecraft.getInstance().player.isSpectator()) return;

        var screenWidth = guiGraphics.guiWidth();
        var screenHeight = guiGraphics.guiHeight();

        Player player = Minecraft.getInstance().player;
        MutagenData mutagenData = player.getData(DataAttachmentRegistry.MUTAGEN_DATA);

        if (active && player.tickCount >= nextTickEnd || player.tickCount < 40){
            active = false;
            nextTickEnd = player.tickCount;
        }

        if(active){
            float progress = (float)(DEFAULT_PLAYTIME - (nextTickEnd - player.tickCount)) / DEFAULT_PLAYTIME;
            int index = Mth.floor((progress % ((float)DEFAULT_PLAYTIME / TEXTURE_ARRAY_ACTIVATE.length)) * TEXTURE_ARRAY_ACTIVATE.length);
            if(index >= TEXTURE_ARRAY_ACTIVATE.length || index < 0) return;

            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ONE,
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ONE
            );
            guiGraphics.setColor( 0.35F, 0.35F, 0.40F, 0.95F);

            if (isActivating) {
                guiGraphics.blit(RESOURCE,
                        0, 0,
                        screenWidth, screenHeight,
                        0, TEXTURE_ARRAY_ACTIVATE[index],
                        256, 256,
                        256, 2560);
            }
            else{
                guiGraphics.blit(RESOURCE,
                        0, 0,
                        screenWidth, screenHeight,
                        0, TEXTURE_ARRAY_DEACTIVATE[index],
                        256, 256,
                        256, 2560);
            }

            guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
        }
        else{
            if(mutagenData.getBool(BeastMutagenEffect.NIGHT_PREDATOR_ACTIVE)){
                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(false);
                RenderSystem.enableBlend();
                RenderSystem.blendFuncSeparate(
                        GlStateManager.SourceFactor.ONE,
                        GlStateManager.DestFactor.ONE,
                        GlStateManager.SourceFactor.ONE,
                        GlStateManager.DestFactor.ONE
                );
                guiGraphics.setColor( 0.35F, 0.35F, 0.40F, 0.95F);

                guiGraphics.blit(RESOURCE,
                        0, 0,
                        screenWidth, screenHeight,
                        0, TEXTURE_ARRAY_DEACTIVATE[0],
                        256, 256,
                        256, 2560);

                guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.defaultBlendFunc();
                RenderSystem.disableBlend();
                RenderSystem.depthMask(true);
                RenderSystem.enableDepthTest();
            }
        }
    }

    public static void startAnimation(){
        if (Minecraft.getInstance().options.hideGui
                || Minecraft.getInstance().player == null
                || Minecraft.getInstance().player.isSpectator()
                || active) return;
        isActivating = true;
        nextTickEnd = Minecraft.getInstance().player.tickCount + DEFAULT_PLAYTIME;
        active = true;
    }

    public static void endAnimation(){
        if (Minecraft.getInstance().options.hideGui
                || Minecraft.getInstance().player == null
                || Minecraft.getInstance().player.isSpectator()
                || active) return;
        isActivating = false;
        nextTickEnd = Minecraft.getInstance().player.tickCount + DEFAULT_PLAYTIME;
        active = true;
    }

}
