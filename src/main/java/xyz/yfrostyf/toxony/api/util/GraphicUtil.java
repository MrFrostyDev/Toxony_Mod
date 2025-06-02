package xyz.yfrostyf.toxony.api.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FormattedCharSequence;

public class GraphicUtil {

    public static void drawStringWithScale(GuiGraphics guiGraphics, Font font, FormattedCharSequence formattedCharSeq, float x, float y, float scale, int color, boolean dropShadow) {
        float posScale = (1.0F / scale);

        PoseStack pose = guiGraphics.pose();
        pose.pushPose();
        pose.scale(scale, scale, scale);
        guiGraphics.drawString(
                font, formattedCharSeq,
                x * posScale,
                y * posScale,
                color, dropShadow);
        pose.popPose();
    }

    public static void drawStringWithScale(GuiGraphics guiGraphics, Font font, String string, float x, float y, float scale, int color, boolean dropShadow) {
        float posScale = (1.0F / scale);

        PoseStack pose = guiGraphics.pose();
        pose.pushPose();
        pose.scale(scale, scale, scale);
        guiGraphics.drawString(
                font, string,
                x * posScale,
                y * posScale,
                color, dropShadow);
        pose.popPose();
    }

}
