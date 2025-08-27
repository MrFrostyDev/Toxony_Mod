package xyz.yfrostyf.toxony.client.events.subscribers;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.client.models.entities.GuidedSpiritEntityModel;
import xyz.yfrostyf.toxony.client.models.items.*;
import xyz.yfrostyf.toxony.client.renderers.MortarPestleRenderer;
import xyz.yfrostyf.toxony.client.renderers.entities.BoltRenderer;
import xyz.yfrostyf.toxony.client.renderers.entities.FlailBallRenderer;
import xyz.yfrostyf.toxony.client.renderers.entities.GuidedSpiritRenderer;
import xyz.yfrostyf.toxony.registries.BlockRegistry;
import xyz.yfrostyf.toxony.registries.EntityRegistry;

@EventBusSubscriber(modid = ToxonyMain.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class RenderRegisterEvents {

    @SubscribeEvent
    public static void onRegisterEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(MortarPestleRenderer.LAYER_LOCATION, MortarPestleRenderer::newModelLayer);
        event.registerLayerDefinition(PlagueDoctorArmorEntityModel.LAYER_LOCATION, PlagueDoctorArmorEntityModel::createBodyLayer);
        event.registerLayerDefinition(PlaguebringerArmorEntityModel.LAYER_LOCATION, PlaguebringerArmorEntityModel::createBodyLayer);
        event.registerLayerDefinition(HunterArmorEntityModel.LAYER_LOCATION, HunterArmorEntityModel::createBodyLayer);
        event.registerLayerDefinition(ProfessionalHunterArmorEntityModel.LAYER_LOCATION, ProfessionalHunterArmorEntityModel::createBodyLayer);
        event.registerLayerDefinition(FlailBallEntityModel.LAYER_LOCATION, FlailBallEntityModel::createBodyLayer);

        event.registerLayerDefinition(GuidedSpiritEntityModel.LAYER_LOCATION, GuidedSpiritEntityModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onEntityRendererRegister(EntityRenderersEvent.RegisterRenderers event) {

        // Item Entities
        event.registerEntityRenderer(EntityRegistry.OIL_POT.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegistry.BOLT.get(), BoltRenderer::new);
        event.registerEntityRenderer(EntityRegistry.FLAIL_BALL.get(), FlailBallRenderer::new);
        event.registerEntityRenderer(EntityRegistry.FLINTLOCK_BALL.get(), context -> new ThrownItemRenderer<>(context, 0.4F, true));
        event.registerEntityRenderer(EntityRegistry.TOXIC_CAKE_PROJECTILE.get(), context -> new ThrownItemRenderer<>(context, 0.4F, false));

        // Entities
        event.registerEntityRenderer(EntityRegistry.GUIDED_SPIRIT.get(), GuidedSpiritRenderer::new);

        // Block Entities
        event.registerBlockEntityRenderer(BlockRegistry.MORTAR_PESTLE_ENTITY.get(), MortarPestleRenderer::new);
    }
}
