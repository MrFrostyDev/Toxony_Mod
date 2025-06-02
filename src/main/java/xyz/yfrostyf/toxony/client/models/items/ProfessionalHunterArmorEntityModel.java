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

public class ProfessionalHunterArmorEntityModel<T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "professional_hunter_armor"), "main");
    public final ModelPart Head;
    public final ModelPart Body;
    public final ModelPart RightArm;
    public final ModelPart LeftArm;
    public final ModelPart RightLeg;
    public final ModelPart LeftLeg;

    public ProfessionalHunterArmorEntityModel(ModelPart root) {
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

        PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 49).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.55F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Hat8_r1 = Head.addOrReplaceChild("Hat8_r1", CubeListBuilder.create().texOffs(0, 51).addBox(0.0F, -7.0F, -2.0F, 0.0F, 7.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -9.0F, 1.0F, 0.0873F, 0.0F, 0.0F));

        PartDefinition Hat7_r1 = Head.addOrReplaceChild("Hat7_r1", CubeListBuilder.create().texOffs(0, 16).addBox(4.7237F, -8.0F, -6.4204F, 0.0F, 6.0F, 18.0F, new CubeDeformation(0.001F))
                .texOffs(60, 56).addBox(3.7237F, -4.0F, -6.4204F, 2.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(44, 0).addBox(2.7237F, -2.0F, -6.4204F, 3.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.0F, 0.0F, 0.0886F, 0.1739F, 0.0154F));

        PartDefinition Hat6_r1 = Head.addOrReplaceChild("Hat6_r1", CubeListBuilder.create().texOffs(72, 16).addBox(-3.0F, -5.0F, -4.0F, 6.0F, 3.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.0F, -2.0F, -7.0F, 8.0F, 2.0F, 14.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, -6.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

        PartDefinition Hat2_r1 = Head.addOrReplaceChild("Hat2_r1", CubeListBuilder.create().texOffs(28, 56).addBox(-5.7237F, -4.0F, -6.4204F, 2.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(36, 16).addBox(-4.7237F, -8.0F, -6.4204F, 0.0F, 6.0F, 18.0F, new CubeDeformation(0.001F))
                .texOffs(40, 40).addBox(-5.7237F, -2.0F, -6.4204F, 3.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.0F, 0.0F, 0.0886F, -0.1739F, -0.0154F));

        PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(64, 72).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.35F))
                .texOffs(0, 72).addBox(-5.0F, 10.0F, -3.5F, 10.0F, 2.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(92, 61).addBox(-5.0F, 12.0F, -3.5F, 10.0F, 5.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(92, 56).addBox(-5.0F, 12.0F, 3.5F, 10.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Rim3_r1 = Body.addOrReplaceChild("Rim3_r1", CubeListBuilder.create().texOffs(34, 72).addBox(-2.409F, -2.5758F, -6.0F, 4.0F, 0.0F, 11.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(-5.5F, -1.0F, 0.0F, 0.0737F, 0.0468F, -0.5655F));

        PartDefinition Rim2_r1 = Body.addOrReplaceChild("Rim2_r1", CubeListBuilder.create().texOffs(42, 72).addBox(-1.591F, -2.5758F, -6.0F, 4.0F, 0.0F, 11.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(5.5F, -1.0F, 0.0F, 0.0737F, -0.0468F, 0.5655F));

        PartDefinition Rim1_r1 = Body.addOrReplaceChild("Rim1_r1", CubeListBuilder.create().texOffs(78, 39).addBox(-0.5F, -3.0F, -6.0F, 0.0F, 6.0F, 11.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(5.5F, -1.0F, 0.0F, 0.0859F, -0.0151F, 0.1739F));

        PartDefinition Rim0_r1 = Body.addOrReplaceChild("Rim0_r1", CubeListBuilder.create().texOffs(78, -1).addBox(0.5F, -3.0F, -6.0F, 0.0F, 6.0F, 11.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(-5.5F, -1.0F, 0.0F, 0.0859F, 0.0151F, -0.1739F));

        PartDefinition Belt0_r1 = Body.addOrReplaceChild("Belt0_r1", CubeListBuilder.create().texOffs(0, 40).addBox(0.0F, 0.0F, -3.5F, 15.0F, 2.0F, 7.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-5.0F, 10.0F, 0.0F, 0.0F, 0.0F, -0.9163F));

        PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(88, 72).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.35F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

        PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(72, 88).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.35F)), PartPose.offset(5.0F, 2.0F, 0.0F));

        PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(56, 88).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.35F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

        PartDefinition RightRobe_r1 = RightLeg.addOrReplaceChild("RightRobe_r1", CubeListBuilder.create().texOffs(0, 81).addBox(-4.0F, 0.0F, -3.0F, 4.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

        PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(40, 83).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.35F)), PartPose.offset(1.9F, 12.0F, 0.0F));

        PartDefinition LeftRobe_r1 = LeftLeg.addOrReplaceChild("LeftRobe_r1", CubeListBuilder.create().texOffs(20, 81).addBox(0.0F, 0.0F, -3.0F, 4.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.8F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

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
