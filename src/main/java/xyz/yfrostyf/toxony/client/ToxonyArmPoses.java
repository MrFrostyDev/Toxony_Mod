package xyz.yfrostyf.toxony.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.client.IArmPoseTransformer;
import xyz.yfrostyf.toxony.items.CycleBow;

@OnlyIn(Dist.CLIENT)
public class ToxonyArmPoses {
    public static final EnumProxy<HumanoidModel.ArmPose> ONE_HAND_CROSSBOW_ENUM_PROXY = new EnumProxy<>(
            HumanoidModel.ArmPose.class,
            false,
            OneHandCrossbowArmPose.INSTANCE
    );

    public static class OneHandCrossbowArmPose implements IArmPoseTransformer{
        public static final IArmPoseTransformer INSTANCE = new OneHandCrossbowArmPose();

        // Same as the crossbow animation except without moving the other arm.
        @Override
        public void applyTransform(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm) {
            boolean isRightHanded = arm == HumanoidArm.RIGHT;
            ItemStack stack = entity.getItemInHand(isRightHanded ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
            if(CycleBow.isLoaded(stack)){
                ModelPart focusHandPart = isRightHanded ? model.rightArm : model.leftArm;
                focusHandPart.yRot = (isRightHanded ? -0.3F : 0.3F) + model.head.yRot;
                focusHandPart.xRot = (float) (-Math.PI / 2) + model.head.xRot + 0.1F;
            }
        }
    }
}
