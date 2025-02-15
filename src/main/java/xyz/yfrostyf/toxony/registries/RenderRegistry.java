package xyz.yfrostyf.toxony.registries;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.client.renderers.MortarPestleRenderer;

@EventBusSubscriber(modid = ToxonyMain.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class RenderRegistry {


    public static final ModelLayerLocation MORTAR_PESTLE_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "mortar_pestle_pestle"), "main");

    @SubscribeEvent
    public static void onRegisterEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(MORTAR_PESTLE_LAYER, MortarPestleRenderer::newModelLayer);
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
