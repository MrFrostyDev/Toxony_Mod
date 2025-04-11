package xyz.yfrostyf.toxony.client.events.subscribers;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.client.models.FlailBallEntityModel;
import xyz.yfrostyf.toxony.client.models.HunterArmorEntityModel;
import xyz.yfrostyf.toxony.client.models.PlagueDoctorArmorEntityModel;
import xyz.yfrostyf.toxony.client.renderers.MortarPestleRenderer;
import xyz.yfrostyf.toxony.client.renderers.entities.BoltRenderer;
import xyz.yfrostyf.toxony.client.renderers.entities.FlailBallRenderer;
import xyz.yfrostyf.toxony.registries.BlockRegistry;
import xyz.yfrostyf.toxony.registries.EntityRegistry;

@EventBusSubscriber(modid = ToxonyMain.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class RenderRegisterEvents {

    @SubscribeEvent
    public static void onRegisterEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(MortarPestleRenderer.LAYER_LOCATION, MortarPestleRenderer::newModelLayer);
        event.registerLayerDefinition(PlagueDoctorArmorEntityModel.LAYER_LOCATION, PlagueDoctorArmorEntityModel::createBodyLayer);
        event.registerLayerDefinition(HunterArmorEntityModel.LAYER_LOCATION, HunterArmorEntityModel::createBodyLayer);
        event.registerLayerDefinition(FlailBallEntityModel.LAYER_LOCATION, FlailBallEntityModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onEntityRendererRegister(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.OIL_POT.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegistry.BOLT.get(), BoltRenderer::new);
        event.registerEntityRenderer(EntityRegistry.FLAIL_BALL.get(), FlailBallRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterRenders(EntityRenderersEvent.RegisterRenderers event){
        event.registerBlockEntityRenderer(
                // The block entity type to register the renderer for.
                BlockRegistry.MORTAR_PESTLE_ENTITY.get(),
                // A function of BlockEntityRendererProvider.Context to BlockEntityRenderer.
                MortarPestleRenderer::new
        );

        event.registerEntityRenderer(
                EntityRegistry.FLAIL_BALL.get(),
                FlailBallRenderer::new
        );
    }
}
