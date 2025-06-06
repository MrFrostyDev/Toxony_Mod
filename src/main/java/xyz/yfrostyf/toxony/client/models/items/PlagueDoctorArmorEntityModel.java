package xyz.yfrostyf.toxony.client.models.items;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import xyz.yfrostyf.toxony.ToxonyMain;

public class PlagueDoctorArmorEntityModel<T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "plague_doctor_armor"), "main");
    public final ModelPart Head;
    public final ModelPart Body;
    public final ModelPart RightArm;
    public final ModelPart LeftArm;
    public final ModelPart RightLeg;
    public final ModelPart LeftLeg;

    public PlagueDoctorArmorEntityModel(ModelPart root) {
        this.Head = root.getChild("Head");
        this.Body = root.getChild("Body");
        this.RightArm = root.getChild("RightArm");
        this.LeftArm = root.getChild("LeftArm");
        this.RightLeg = root.getChild("RightLeg");
        this.LeftLeg = root.getChild("LeftLeg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 24).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.55F))
                .texOffs(56, 47).addBox(-3.5F, -0.25F, -3.5F, 7.0F, 3.0F, 0.0F, new CubeDeformation(0.001F))
                .texOffs(68, 23).addBox(-3.5F, -0.25F, 3.5F, 7.0F, 3.0F, 0.0F, new CubeDeformation(0.001F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Beak2_r1 = Head.addOrReplaceChild("Beak2_r1", CubeListBuilder.create().texOffs(68, 26).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -13.0F, 0.4363F, 0.0F, 0.0F));

        PartDefinition Beak1_r1 = Head.addOrReplaceChild("Beak1_r1", CubeListBuilder.create().texOffs(54, 16).addBox(-1.0F, -1.0F, -2.0F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -1.342F, -9.0603F, 0.3491F, 0.0F, 0.0F));

        PartDefinition Beak0_r1 = Head.addOrReplaceChild("Beak0_r1", CubeListBuilder.create().texOffs(48, 65).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.8695F, -5.9914F, 0.1309F, 0.0F, 0.0F));

        PartDefinition Hood5_r1 = Head.addOrReplaceChild("Hood5_r1", CubeListBuilder.create().texOffs(0, 12).addBox(-1.0F, 0.0F, 0.0F, 9.0F, 2.0F, 10.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(2.2071F, -10.7929F, -5.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition Hood3_r1 = Head.addOrReplaceChild("Hood3_r1", CubeListBuilder.create().texOffs(24, 51).addBox(-3.0F, -5.0F, -4.0F, 8.0F, 8.0F, 5.0F, new CubeDeformation(0.6F)), PartPose.offsetAndRotation(0.0F, -2.5147F, 7.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition Hood2_r1 = Head.addOrReplaceChild("Hood2_r1", CubeListBuilder.create().texOffs(0, 40).addBox(-0.9374F, -0.0838F, 0.0F, 2.0F, 8.0F, 10.0F, new CubeDeformation(0.11F)), PartPose.offsetAndRotation(8.4675F, -4.4139F, -5.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition Hood1_r1 = Head.addOrReplaceChild("Hood1_r1", CubeListBuilder.create().texOffs(32, 33).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 8.0F, 10.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(-8.4853F, -4.5147F, -5.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition Hood0_r1 = Head.addOrReplaceChild("Hood0_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-11.0F, 0.0F, 0.0F, 11.0F, 2.0F, 10.0F, new CubeDeformation(0.11F)), PartPose.offsetAndRotation(0.0F, -13.0F, -5.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(42, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.35F))
                .texOffs(34, 24).addBox(-5.0F, 10.0F, -3.5F, 10.0F, 2.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(68, 16).addBox(-4.5F, 8.5F, -4.0F, 5.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(38, 16).addBox(-3.5F, 9.0F, 2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(24, 40).addBox(-2.5F, 8.0F, 3.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(32, 64).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.35F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

        PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(16, 64).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.35F)), PartPose.offset(5.0F, 2.0F, 0.0F));

        PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(66, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.35F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

        PartDefinition RightRobe_r1 = RightLeg.addOrReplaceChild("RightRobe_r1", CubeListBuilder.create().texOffs(50, 51).addBox(-4.0F, 0.0F, -3.0F, 4.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

        PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(0, 58).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.35F)), PartPose.offset(1.9F, 12.0F, 0.0F));

        PartDefinition LeftRobe_r1 = LeftLeg.addOrReplaceChild("LeftRobe_r1", CubeListBuilder.create().texOffs(56, 33).addBox(0.0F, 0.0F, -3.0F, 4.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.8F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        Head.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        RightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        LeftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        RightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        LeftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

}
