package xyz.yfrostyf.toxony.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import xyz.yfrostyf.toxony.client.models.items.HunterArmorEntityModel;
import xyz.yfrostyf.toxony.client.models.items.PlagueDoctorArmorEntityModel;
import xyz.yfrostyf.toxony.client.models.items.PlaguebringerArmorEntityModel;
import xyz.yfrostyf.toxony.client.models.items.ProfessionalHunterArmorEntityModel;

import java.util.Collections;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class ModelUtils {
    public static ModelPart EMPTY = new ModelPart(Collections.emptyList(), Collections.emptyMap());
    public static EntityModelSet MINECRAFT_MODEL_SET = Minecraft.getInstance().getEntityModels();

    public class PlagueDoctorArmor {
        public static HumanoidModel<?> getHead(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original){
            HumanoidModel model = new HumanoidModel(
                    new ModelPart(Collections.emptyList(), Map.of(
                            "left_leg", EMPTY,
                            "right_leg", EMPTY,
                            "head", new PlagueDoctorArmorEntityModel(MINECRAFT_MODEL_SET.bakeLayer(PlagueDoctorArmorEntityModel.LAYER_LOCATION)).head,
                            "hat", EMPTY,
                            "body", EMPTY,
                            "right_arm", EMPTY,
                            "left_arm", EMPTY
                    ))
            );
            model.crouching = livingEntity.isShiftKeyDown();
            model.riding = original.riding;
            model.young = livingEntity.isBaby();
            return model;
        }

        public static HumanoidModel<?> getBody(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original){
            HumanoidModel model = new HumanoidModel(
                    new ModelPart(Collections.emptyList(), Map.of(
                            "left_leg", EMPTY,
                            "right_leg", EMPTY,
                            "head", EMPTY,
                            "hat", EMPTY,
                            "body", new PlagueDoctorArmorEntityModel(MINECRAFT_MODEL_SET.bakeLayer(PlagueDoctorArmorEntityModel.LAYER_LOCATION)).body,
                            "right_arm", new PlagueDoctorArmorEntityModel(MINECRAFT_MODEL_SET.bakeLayer(PlagueDoctorArmorEntityModel.LAYER_LOCATION)).rightArm,
                            "left_arm", new PlagueDoctorArmorEntityModel(MINECRAFT_MODEL_SET.bakeLayer(PlagueDoctorArmorEntityModel.LAYER_LOCATION)).leftArm
                    ))
            );
            model.crouching = livingEntity.isShiftKeyDown();
            model.riding = original.riding;
            model.young = livingEntity.isBaby();
            return model;
        }

        public static HumanoidModel<?> getLegs(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original){
            HumanoidModel model = new HumanoidModel(
                    new ModelPart(Collections.emptyList(), Map.of(
                            "left_leg", new PlagueDoctorArmorEntityModel(MINECRAFT_MODEL_SET.bakeLayer(PlagueDoctorArmorEntityModel.LAYER_LOCATION)).leftLeg,
                            "right_leg", new PlagueDoctorArmorEntityModel(MINECRAFT_MODEL_SET.bakeLayer(PlagueDoctorArmorEntityModel.LAYER_LOCATION)).rightLeg,
                            "head", EMPTY,
                            "hat", EMPTY,
                            "body", EMPTY,
                            "right_arm", EMPTY,
                            "left_arm", EMPTY
                    ))
            );
            model.crouching = livingEntity.isShiftKeyDown();
            model.riding = original.riding;
            model.young = livingEntity.isBaby();
            return model;
        }
    }

    public class PlaguebringerArmor {
        public static HumanoidModel<?> getHead(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original){
            HumanoidModel model = new HumanoidModel(
                    new ModelPart(Collections.emptyList(), Map.of(
                            "left_leg", EMPTY,
                            "right_leg", EMPTY,
                            "head", new PlaguebringerArmorEntityModel<>(MINECRAFT_MODEL_SET.bakeLayer(PlaguebringerArmorEntityModel.LAYER_LOCATION)).head,
                            "hat", EMPTY,
                            "body", EMPTY,
                            "right_arm", EMPTY,
                            "left_arm", EMPTY
                    ))
            );
            model.crouching = livingEntity.isShiftKeyDown();
            model.riding = original.riding;
            model.young = livingEntity.isBaby();
            return model;
        }

        public static HumanoidModel<?> getBody(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original){
            HumanoidModel model = new HumanoidModel(
                    new ModelPart(Collections.emptyList(), Map.of(
                            "left_leg", EMPTY,
                            "right_leg", EMPTY,
                            "head", EMPTY,
                            "hat", EMPTY,
                            "body", new PlaguebringerArmorEntityModel(MINECRAFT_MODEL_SET.bakeLayer(PlaguebringerArmorEntityModel.LAYER_LOCATION)).body,
                            "right_arm", new PlaguebringerArmorEntityModel(MINECRAFT_MODEL_SET.bakeLayer(PlaguebringerArmorEntityModel.LAYER_LOCATION)).rightArm,
                            "left_arm", new PlaguebringerArmorEntityModel(MINECRAFT_MODEL_SET.bakeLayer(PlaguebringerArmorEntityModel.LAYER_LOCATION)).leftArm
                    ))
            );
            model.crouching = livingEntity.isShiftKeyDown();
            model.riding = original.riding;
            model.young = livingEntity.isBaby();
            return model;
        }

        public static HumanoidModel<?> getLegs(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original){
            HumanoidModel model = new HumanoidModel(
                    new ModelPart(Collections.emptyList(), Map.of(
                            "left_leg", new PlaguebringerArmorEntityModel(MINECRAFT_MODEL_SET.bakeLayer(PlaguebringerArmorEntityModel.LAYER_LOCATION)).leftLeg,
                            "right_leg", new PlaguebringerArmorEntityModel(MINECRAFT_MODEL_SET.bakeLayer(PlaguebringerArmorEntityModel.LAYER_LOCATION)).rightLeg,
                            "head", EMPTY,
                            "hat", EMPTY,
                            "body", EMPTY,
                            "right_arm", EMPTY,
                            "left_arm", EMPTY
                    ))
            );
            model.crouching = livingEntity.isShiftKeyDown();
            model.riding = original.riding;
            model.young = livingEntity.isBaby();
            return model;
        }
    }

    public class HunterArmor {
        public static HumanoidModel<?> getHead(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original){
            HumanoidModel model = new HumanoidModel(
                    new ModelPart(Collections.emptyList(), Map.of(
                            "left_leg", EMPTY,
                            "right_leg", EMPTY,
                            "head", new HunterArmorEntityModel(MINECRAFT_MODEL_SET.bakeLayer(HunterArmorEntityModel.LAYER_LOCATION)).head,
                            "hat", EMPTY,
                            "body", EMPTY,
                            "right_arm", EMPTY,
                            "left_arm", EMPTY
                    ))
            );
            model.crouching = livingEntity.isShiftKeyDown();
            model.riding = original.riding;
            model.young = livingEntity.isBaby();
            return model;
        }

        public static HumanoidModel<?> getBody(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original){
            HumanoidModel model = new HumanoidModel(
                    new ModelPart(Collections.emptyList(), Map.of(
                            "left_leg", EMPTY,
                            "right_leg", EMPTY,
                            "head", EMPTY,
                            "hat", EMPTY,
                            "body", new HunterArmorEntityModel<>(MINECRAFT_MODEL_SET.bakeLayer(HunterArmorEntityModel.LAYER_LOCATION)).body,
                            "right_arm", new HunterArmorEntityModel(MINECRAFT_MODEL_SET.bakeLayer(HunterArmorEntityModel.LAYER_LOCATION)).rightArm,
                            "left_arm", new HunterArmorEntityModel(MINECRAFT_MODEL_SET.bakeLayer(HunterArmorEntityModel.LAYER_LOCATION)).leftArm
                    ))
            );
            model.crouching = livingEntity.isShiftKeyDown();
            model.riding = original.riding;
            model.young = livingEntity.isBaby();
            return model;
        }

        public static HumanoidModel<?> getLegs(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original){
            HumanoidModel model = new HumanoidModel(
                    new ModelPart(Collections.emptyList(), Map.of(
                            "left_leg", new HunterArmorEntityModel<>(MINECRAFT_MODEL_SET.bakeLayer(HunterArmorEntityModel.LAYER_LOCATION)).leftLeg,
                            "right_leg", new HunterArmorEntityModel(MINECRAFT_MODEL_SET.bakeLayer(HunterArmorEntityModel.LAYER_LOCATION)).rightLeg,
                            "head", EMPTY,
                            "hat", EMPTY,
                            "body", EMPTY,
                            "right_arm", EMPTY,
                            "left_arm", EMPTY
                    ))
            );
            model.crouching = livingEntity.isShiftKeyDown();
            model.riding = original.riding;
            model.young = livingEntity.isBaby();
            return model;
        }
    }

    public class ProfessionalHunterArmor {
        public static HumanoidModel<?> getHead(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original){
            HumanoidModel model = new HumanoidModel(
                    new ModelPart(Collections.emptyList(), Map.of(
                            "left_leg", EMPTY,
                            "right_leg", EMPTY,
                            "head", new ProfessionalHunterArmorEntityModel(MINECRAFT_MODEL_SET.bakeLayer(ProfessionalHunterArmorEntityModel.LAYER_LOCATION)).head,
                            "hat", EMPTY,
                            "body", EMPTY,
                            "right_arm", EMPTY,
                            "left_arm", EMPTY
                    ))
            );
            model.crouching = livingEntity.isShiftKeyDown();
            model.riding = original.riding;
            model.young = livingEntity.isBaby();
            return model;
        }

        public static HumanoidModel<?> getBody(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original){
            HumanoidModel model = new HumanoidModel(
                    new ModelPart(Collections.emptyList(), Map.of(
                            "left_leg", EMPTY,
                            "right_leg", EMPTY,
                            "head", EMPTY,
                            "hat", EMPTY,
                            "body", new ProfessionalHunterArmorEntityModel(MINECRAFT_MODEL_SET.bakeLayer(ProfessionalHunterArmorEntityModel.LAYER_LOCATION)).body,
                            "right_arm", new ProfessionalHunterArmorEntityModel(MINECRAFT_MODEL_SET.bakeLayer(ProfessionalHunterArmorEntityModel.LAYER_LOCATION)).rightArm,
                            "left_arm", new ProfessionalHunterArmorEntityModel(MINECRAFT_MODEL_SET.bakeLayer(ProfessionalHunterArmorEntityModel.LAYER_LOCATION)).leftArm
                    ))
            );
            model.crouching = livingEntity.isShiftKeyDown();
            model.riding = original.riding;
            model.young = livingEntity.isBaby();
            return model;
        }

        public static HumanoidModel<?> getLegs(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original){
            HumanoidModel model = new HumanoidModel(
                    new ModelPart(Collections.emptyList(), Map.of(
                            "left_leg", new ProfessionalHunterArmorEntityModel(MINECRAFT_MODEL_SET.bakeLayer(ProfessionalHunterArmorEntityModel.LAYER_LOCATION)).leftLeg,
                            "right_leg", new ProfessionalHunterArmorEntityModel(MINECRAFT_MODEL_SET.bakeLayer(ProfessionalHunterArmorEntityModel.LAYER_LOCATION)).rightLeg,
                            "head", EMPTY,
                            "hat", EMPTY,
                            "body", EMPTY,
                            "right_arm", EMPTY,
                            "left_arm", EMPTY
                    ))
            );
            model.crouching = livingEntity.isShiftKeyDown();
            model.riding = original.riding;
            model.young = livingEntity.isBaby();
            return model;
        }
    }

}
