package xyz.yfrostyf.toxony.client.models.items;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import xyz.yfrostyf.toxony.ToxonyMain;

public class HunterArmorEntityModel<T extends LivingEntity> extends HumanoidModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "hunter_armor"), "main");

    public HunterArmorEntityModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = PlayerModel.createMesh(CubeDeformation.NONE, 0.0F);
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 49).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.55F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Hat7_r1 = Head.addOrReplaceChild("Hat7_r1", CubeListBuilder.create().texOffs(36, 16).addBox(4.7237F, -8.0F, -6.4204F, 0.0F, 6.0F, 18.0F, new CubeDeformation(0.001F))
                .texOffs(32, 56).addBox(3.7237F, -4.0F, -6.4204F, 2.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(44, 0).addBox(2.7237F, -2.0F, -6.4204F, 3.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.0F, 0.0F, 0.0886F, 0.1739F, 0.0154F));

        PartDefinition Hat6_r1 = Head.addOrReplaceChild("Hat6_r1", CubeListBuilder.create().texOffs(72, 16).addBox(-3.0F, -5.0F, -4.0F, 6.0F, 3.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.0F, -2.0F, -7.0F, 8.0F, 2.0F, 14.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, -6.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

        PartDefinition Hat2_r1 = Head.addOrReplaceChild("Hat2_r1", CubeListBuilder.create().texOffs(64, 56).addBox(-5.7237F, -4.0F, -6.4204F, 2.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(0, 16).addBox(-4.7237F, -8.0F, -6.4204F, 0.0F, 6.0F, 18.0F, new CubeDeformation(0.001F))
                .texOffs(44, 40).addBox(-5.7237F, -2.0F, -6.4204F, 3.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.0F, 0.0F, 0.0886F, -0.1739F, -0.0154F));

        PartDefinition Body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(34, 72).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.35F))
                .texOffs(0, 72).addBox(-5.0F, 10.0F, -3.5F, 10.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Rim1_r1 = Body.addOrReplaceChild("Rim1_r1", CubeListBuilder.create().texOffs(80, 72).addBox(-0.5F, -4.0F, -6.0F, 0.0F, 6.0F, 11.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(5.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

        PartDefinition Rim0_r1 = Body.addOrReplaceChild("Rim0_r1", CubeListBuilder.create().texOffs(58, 72).addBox(0.5F, -4.0F, -6.0F, 0.0F, 6.0F, 11.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(-5.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

        PartDefinition Shoulder0_r1 = Body.addOrReplaceChild("Shoulder0_r1", CubeListBuilder.create().texOffs(72, 28).addBox(2.0F, -3.0F, -5.0F, 7.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -1.0F, 1.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition Belt0_r1 = Body.addOrReplaceChild("Belt0_r1", CubeListBuilder.create().texOffs(0, 40).addBox(0.0F, 0.0F, -3.5F, 15.0F, 2.0F, 7.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-5.0F, 10.0F, 0.0F, 0.0F, 0.0F, -0.9163F));

        PartDefinition RightArm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(48, 89).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.35F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

        PartDefinition LeftArm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 88).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.35F)), PartPose.offset(5.0F, 2.0F, 0.0F));

        PartDefinition RightLeg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(16, 81).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.35F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

        PartDefinition RightRobe_r1 = RightLeg.addOrReplaceChild("RightRobe_r1", CubeListBuilder.create().texOffs(78, 0).addBox(-4.0F, 0.0F, -3.0F, 4.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

        PartDefinition LeftLeg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 81).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.35F)), PartPose.offset(1.9F, 12.0F, 0.0F));

        PartDefinition LeftRobe_r1 = LeftLeg.addOrReplaceChild("LeftRobe_r1", CubeListBuilder.create().texOffs(78, 40).addBox(0.0F, 0.0F, -3.0F, 4.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.8F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }
}
