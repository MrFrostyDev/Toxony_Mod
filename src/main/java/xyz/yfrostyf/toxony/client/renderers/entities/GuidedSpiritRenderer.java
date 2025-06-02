package xyz.yfrostyf.toxony.client.renderers.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.client.models.entities.GuidedSpiritEntityModel;
import xyz.yfrostyf.toxony.entities.GuidedSpiritEntity;

public class GuidedSpiritRenderer extends MobRenderer<GuidedSpiritEntity, GuidedSpiritEntityModel<GuidedSpiritEntity>> {
    public GuidedSpiritRenderer(EntityRendererProvider.Context context) {
        super(context, new GuidedSpiritEntityModel<>(context.bakeLayer(GuidedSpiritEntityModel.LAYER_LOCATION)), 0.25F);
    }

    @Override
    public ResourceLocation getTextureLocation(GuidedSpiritEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/entity/guided_spirit/guided_spirit.png");
    }

    @Override
    public void render(GuidedSpiritEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.scale(1F, 1F, 1F);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    protected int getBlockLightLevel(GuidedSpiritEntity entity, BlockPos pos) {
        return 15;
    }
}
