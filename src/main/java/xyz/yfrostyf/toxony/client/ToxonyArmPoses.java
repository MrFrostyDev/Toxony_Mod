package xyz.yfrostyf.toxony.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.client.IArmPoseTransformer;
import xyz.yfrostyf.toxony.items.weapons.CycleBowItem;
import xyz.yfrostyf.toxony.items.weapons.FlailItem;
import xyz.yfrostyf.toxony.items.weapons.FlintlockItem;
import xyz.yfrostyf.toxony.registries.ItemRegistry;

@OnlyIn(Dist.CLIENT)
public class ToxonyArmPoses {
    public static final EnumProxy<HumanoidModel.ArmPose> ONE_HAND_CROSSBOW_ENUM_PROXY = new EnumProxy<>(
            HumanoidModel.ArmPose.class,
            false,
            OneHandCrossbowArmPose.INSTANCE
    );

    public static final EnumProxy<HumanoidModel.ArmPose> ONE_HAND_PISTOL_ENUM_PROXY = new EnumProxy<>(
            HumanoidModel.ArmPose.class,
            false,
            OneHandedPistolArmPose.INSTANCE
    );

    public static final EnumProxy<HumanoidModel.ArmPose> FLAIL_ENUM_PROXY = new EnumProxy<>(
            HumanoidModel.ArmPose.class,
            false,
            FlailArmPose.INSTANCE
    );

    public static class OneHandCrossbowArmPose implements IArmPoseTransformer{
        public static final IArmPoseTransformer INSTANCE = new OneHandCrossbowArmPose();

        // Same as the crossbow animation except without moving the other arm.
        @Override
        public void applyTransform(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm) {
            boolean isRightHanded = arm == HumanoidArm.RIGHT;
            ItemStack stack = entity.getItemInHand(isRightHanded ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
            if(CycleBowItem.isLoaded(stack)){
                ModelPart focusHandPart = isRightHanded ? model.rightArm : model.leftArm;
                focusHandPart.yRot = (isRightHanded ? -0.3F : 0.3F) + model.head.yRot;
                focusHandPart.xRot = (float) (-Math.PI / 2) + model.head.xRot + 0.1F;
            }
        }
    }

    public static class OneHandedPistolArmPose implements IArmPoseTransformer{
        public static final IArmPoseTransformer INSTANCE = new OneHandedPistolArmPose();

        @Override
        public void applyTransform(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm) {
            boolean isRightHanded = arm == HumanoidArm.RIGHT;

            ModelPart usingHandPart = isRightHanded ? model.rightArm : model.leftArm;
            ModelPart otherHandPart = isRightHanded ? model.leftArm : model.rightArm;
            if(entity.isUsingItem()){
                usingHandPart.yRot = (isRightHanded ? -0.1F : 0.1F) + model.head.yRot;
                usingHandPart.xRot = (float) (-Math.PI) + (float)(Math.PI / 3) + model.head.xRot + 0.1F;
                if(FlintlockItem.isDuelWielding(entity)){
                    otherHandPart.yRot = (isRightHanded ? 0.1F : -0.1F) + model.head.yRot;
                    otherHandPart.xRot = (float) (-Math.PI / 2) + model.head.xRot + 0.1F;
                }
                usingHandPart.z = usingHandPart.z + 2;
            }
            else{
                usingHandPart.yRot = (isRightHanded ? -0.1F : 0.1F) + model.head.yRot;
                usingHandPart.xRot = (float) (-Math.PI / 2) + model.head.xRot + 0.1F;
                if(FlintlockItem.isDuelWielding(entity)){
                    otherHandPart.yRot = (isRightHanded ? 0.1F : -0.1F) + model.head.yRot;
                    otherHandPart.xRot = (float) (-Math.PI / 2) + model.head.xRot + 0.1F;
                }
            }
        }
    }

    public static class FlailArmPose implements IArmPoseTransformer{
        public static final IArmPoseTransformer INSTANCE = new FlailArmPose();

        // Based on the trident throwing arm pose
        @Override
        public void applyTransform(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm) {
            boolean isRightHanded = arm == HumanoidArm.RIGHT;
            if(entity instanceof Player player){
                if(FlailItem.isUsingFlail(player)){
                    ModelPart focusHandPart = isRightHanded ? model.rightArm : model.leftArm;
                    focusHandPart.xRot = focusHandPart.xRot * 0.5F - (float) Math.PI * 0.85F;
                    focusHandPart.yRot = focusHandPart.yRot * 0.5F + (float) Math.PI * 0.10F;
                }
            }
        }
    }
}
