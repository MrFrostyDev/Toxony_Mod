package xyz.yfrostyf.toxony.client.models;

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

public class FlailBallEntityModel<T extends Entity> extends EntityModel<T> {

    // |---------------------------------------------------------------------------------------------|
    // | REMINDER TO CHANGE THE POSESTACK OFFSET SO IT DOESN'T OFFSET ITSELF FROM,
    // | THE CENTER BECAUSE BLOCKBENCH ADDS THAT
    // |---------------------------------------------------------------------------------------------|

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "flail_ball"), "main");
    private final ModelPart root;

    public FlailBallEntityModel(ModelPart root) {
        this.root = root.getChild("root");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(0.0F, -8.0F, -4.0F, 0.0F, 8.0F, 8.0F, new CubeDeformation(0.01F))
                .texOffs(0, 8).addBox(-4.0F, -8.0F, 0.0F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.ZERO);

        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {}

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }
}
