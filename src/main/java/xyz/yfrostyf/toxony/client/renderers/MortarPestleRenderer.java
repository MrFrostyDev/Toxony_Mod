package xyz.yfrostyf.toxony.client.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.blocks.entities.MortarPestleBlockEntity;
import xyz.yfrostyf.toxony.client.events.subscribers.RenderRegisterEvents;

public class MortarPestleRenderer implements BlockEntityRenderer<MortarPestleBlockEntity> {
    private final ModelPart pestleModel;
    public static final ResourceLocation PESTLE_TEXTURE = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/block/mortar_pestle_pestle.png");


    // Thank you Eidolon/Eidolon:Repraised for the code and
    // references to understanding model rendering.
    public static LayerDefinition newModelLayer(){
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition pestlePart = root.addOrReplaceChild("pestle", CubeListBuilder.create()
                .texOffs(0, 8).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.0F, 3.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 16, 16);
    }

    public MortarPestleRenderer(BlockEntityRendererProvider.Context context){
        this.pestleModel = Minecraft.getInstance().getEntityModels().bakeLayer(RenderRegisterEvents.MORTAR_PESTLE_LAYER).getChild("pestle");
    }

    @Override
    public void render(MortarPestleBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buf, int packedLight, int packedOverlay) {
        RenderSystem.setShaderTexture(0, PESTLE_TEXTURE);
        float coEff = blockEntity.pestleTick == 0 ? 0 : (blockEntity.pestleTick - partialTick) / MortarPestleBlockEntity.DEFAULT_PESTLE_TICK;
        poseStack.pushPose();
        poseStack.translate(0.5, 0.2, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(45 + coEff * 360));
        poseStack.translate(0, -0.125 * Math.sin(coEff * Math.PI), 0.125);
        pestleModel.xRot = (float) Math.PI / 8 * (1.0f - (float) Math.sin(coEff * Math.PI));
        pestleModel.render(poseStack, buf.getBuffer(RenderType.entitySolid(PESTLE_TEXTURE)), packedLight, packedOverlay);
        poseStack.popPose();
    }
}
