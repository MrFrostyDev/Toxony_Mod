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

public class PlaguebringerArmorEntityModel<T extends LivingEntity> extends HumanoidModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "plaguebringer_armor"), "main");

    public PlaguebringerArmorEntityModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = PlayerModel.createMesh(CubeDeformation.NONE, 0.0F);
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(68, 48).addBox(-9.0F, -10.0F, -8.0F, 18.0F, 6.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(68, 54).addBox(-9.0F, -10.0F, 8.0F, 18.0F, 6.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(36, 48).addBox(8.5F, -10.0F, -8.0F, 0.0F, 6.0F, 16.0F, new CubeDeformation(0.01F))
                .texOffs(50, 0).addBox(-9.0F, -10.0F, -8.0F, 0.0F, 6.0F, 16.0F, new CubeDeformation(0.01F))
                .texOffs(0, 62).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.55F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Beak2_r1 = Head.addOrReplaceChild("Beak2_r1", CubeListBuilder.create().texOffs(32, 84).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -13.0F, 0.4363F, 0.0F, 0.0F));

        PartDefinition Beak1_r1 = Head.addOrReplaceChild("Beak1_r1", CubeListBuilder.create().texOffs(84, 24).addBox(-1.0F, -1.0F, -2.0F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -1.342F, -9.0603F, 0.3491F, 0.0F, 0.0F));

        PartDefinition Beak0_r1 = Head.addOrReplaceChild("Beak0_r1", CubeListBuilder.create().texOffs(82, 7).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.8695F, -5.9914F, 0.1309F, 0.0F, 0.0F));

        PartDefinition Hat2_r1 = Head.addOrReplaceChild("Hat2_r1", CubeListBuilder.create().texOffs(36, 34).addBox(-5.0066F, -4.6958F, -5.0F, 10.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0463F, -7.8007F, 0.0F, 0.0F, 0.0F, -0.0873F));

        PartDefinition Hat1_r1 = Head.addOrReplaceChild("Hat1_r1", CubeListBuilder.create().texOffs(0, 17).addBox(-0.0463F, -0.6993F, -8.0F, 9.0F, 1.0F, 16.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(-0.0463F, -7.8007F, 0.0F, 0.0F, 0.0F, -0.2182F));

        PartDefinition Hat0_r1 = Head.addOrReplaceChild("Hat0_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-8.9801F, -0.6964F, -8.0F, 9.0F, 1.0F, 16.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(-0.0463F, -7.8007F, 0.0F, 0.0F, 0.0F, 0.0F));

        PartDefinition Body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(68, 60).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.35F))
                .texOffs(50, 22).addBox(-5.0F, 10.0F, -3.5F, 10.0F, 2.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(82, 0).addBox(-4.5F, 8.5F, 2.0F, 9.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(84, 16).addBox(-3.5F, 9.0F, -6.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(50, 31).addBox(-2.5F, 8.0F, -5.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Rim0_r1 = Body.addOrReplaceChild("Rim0_r1", CubeListBuilder.create().texOffs(68, 87).addBox(0.5F, -4.0F, -6.0F, 0.0F, 6.0F, 11.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(-5.5F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

        PartDefinition Rim1_r1 = Body.addOrReplaceChild("Rim1_r1", CubeListBuilder.create().texOffs(68, 81).addBox(-0.5F, -4.0F, -6.0F, 0.0F, 6.0F, 11.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(5.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

        PartDefinition Cane0_r1 = Body.addOrReplaceChild("Cane0_r1", CubeListBuilder.create().texOffs(0, 34).addBox(0.0F, -5.0F, -9.0F, 0.0F, 10.0F, 18.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(5.0F, 12.0F, 0.0F, -0.4363F, 0.0F, 0.0F));

        PartDefinition RightArm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(16, 78).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.35F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

        PartDefinition LeftArm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 78).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.35F)), PartPose.offset(5.0F, 2.0F, 0.0F));

        PartDefinition RightLeg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(68, 76).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.35F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

        PartDefinition RightRobe_r1 = RightLeg.addOrReplaceChild("RightRobe_r1", CubeListBuilder.create().texOffs(32, 70).addBox(-4.0F, 0.0F, -3.0F, 4.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

        PartDefinition LeftLeg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(52, 70).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.35F)), PartPose.offset(1.9F, 12.0F, 0.0F));

        PartDefinition LeftRobe_r1 = LeftLeg.addOrReplaceChild("LeftRobe_r1", CubeListBuilder.create().texOffs(76, 31).addBox(0.0F, 0.0F, -3.0F, 4.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.8F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }
}
