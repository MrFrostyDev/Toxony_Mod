package xyz.yfrostyf.toxony.client.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;

public class AnimationUtils {

    // Minecraft's crossbow item animation logic with minor modifications
    public static void handleCycleAnimation(PoseStack poseStack, LocalPlayer player,
                                            HumanoidArm arm, ItemStack itemInHand,
                                            float partialTick, float equipProcess,
                                            float swingProcess){
        boolean isHumanoidArmRight = arm == HumanoidArm.RIGHT;

        int i = isHumanoidArmRight ? 1 : -1;
        if (player.isUsingItem() && player.getUseItemRemainingTicks() > 0) {
            applyItemArmTransform(poseStack, arm, equipProcess);
            poseStack.translate((float)i * -0.4785682F, -0.094387F, 0.05731531F);
            poseStack.mulPose(Axis.XP.rotationDegrees(-11.935F));
            poseStack.mulPose(Axis.YP.rotationDegrees((float)i * 65.3F));
            poseStack.mulPose(Axis.ZP.rotationDegrees((float)i * -9.785F));
            float f9 = (float)itemInHand.getUseDuration(player) - ((float)player.getUseItemRemainingTicks() - partialTick + 1.0F);
            float f13 = f9 / (float)itemInHand.getUseDuration(player);
            if (f13 > 1.0F) {
                f13 = 1.0F;
            }

            if (f13 > 0.1F) {
                float f16 = Mth.sin((f9 - 0.1F) * 1.3F);
                float f3 = f13 - 0.1F;
                float f4 = f16 * f3;
                poseStack.translate(f4 * 0.0F, f4 * 0.004F, f4 * 0.0F);
            }

            poseStack.translate(f13 * 0.0F, f13 * 0.0F, f13 * 0.04F);
            poseStack.scale(1.0F, 1.0F, 1.0F + f13 * 0.2F);
            poseStack.mulPose(Axis.YN.rotationDegrees((float)i * 45.0F));
        } else {
            float f = -0.4F * Mth.sin(Mth.sqrt(swingProcess) * (float) Math.PI);
            float f1 = 0.2F * Mth.sin(Mth.sqrt(swingProcess) * (float) (Math.PI * 2));
            float f2 = -0.2F * Mth.sin(swingProcess * (float) Math.PI);
            poseStack.translate((float)i * f, f1, f2);
            applyItemArmTransform(poseStack, arm, swingProcess);
            applyItemArmAttackTransform(poseStack, arm, swingProcess);
        }
    }

    private static void applyItemArmTransform(PoseStack poseStack, HumanoidArm hand, float equippedProg) {
        int i = hand == HumanoidArm.RIGHT ? 1 : -1;
        poseStack.translate((float)i * 0.56F, -0.52F + equippedProg * -0.6F, -0.72F);
    }

    private static void applyItemArmAttackTransform(PoseStack poseStack, HumanoidArm hand, float swingProgress) {
        int i = hand == HumanoidArm.RIGHT ? 1 : -1;
        float f = Mth.sin(swingProgress * swingProgress * (float) Math.PI);
        poseStack.mulPose(Axis.YP.rotationDegrees((float)i * (45.0F + f * -20.0F)));
        float f1 = Mth.sin(Mth.sqrt(swingProgress) * (float) Math.PI);
        poseStack.mulPose(Axis.ZP.rotationDegrees((float)i * f1 * -20.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(f1 * -80.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees((float)i * -45.0F));
    }
}
