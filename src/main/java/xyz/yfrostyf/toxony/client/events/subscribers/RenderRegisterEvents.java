package xyz.yfrostyf.toxony.client.events.subscribers;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.client.models.HunterArmorEntityModel;
import xyz.yfrostyf.toxony.client.models.PlagueDoctorArmorEntityModel;
import xyz.yfrostyf.toxony.client.renderers.MortarPestleRenderer;
import xyz.yfrostyf.toxony.client.renderers.entities.BoltRenderer;
import xyz.yfrostyf.toxony.registries.BlockRegistry;
import xyz.yfrostyf.toxony.registries.EntityRegistry;

@EventBusSubscriber(modid = ToxonyMain.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class RenderRegisterEvents {


    public static final ModelLayerLocation MORTAR_PESTLE_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "mortar_pestle_pestle"), "main");

    @SubscribeEvent
    public static void onRegisterEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(MORTAR_PESTLE_LAYER, MortarPestleRenderer::newModelLayer);
        event.registerLayerDefinition(PlagueDoctorArmorEntityModel.LAYER_LOCATION, PlagueDoctorArmorEntityModel::createBodyLayer);
        event.registerLayerDefinition(HunterArmorEntityModel.LAYER_LOCATION, HunterArmorEntityModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onEntityRendererRegister(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.OIL_POT.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegistry.BOLT.get(), BoltRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterRenders(EntityRenderersEvent.RegisterRenderers event){
        event.registerBlockEntityRenderer(
                // The block entity type to register the renderer for.
                BlockRegistry.MORTAR_PESTLE_ENTITY.get(),
                // A function of BlockEntityRendererProvider.Context to BlockEntityRenderer.
                MortarPestleRenderer::new
        );
    }
}
