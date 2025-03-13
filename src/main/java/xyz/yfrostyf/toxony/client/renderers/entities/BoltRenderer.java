package xyz.yfrostyf.toxony.client.renderers.entities;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.entities.item.Bolt;

@OnlyIn(Dist.CLIENT)
public class BoltRenderer extends ArrowRenderer<Bolt> {
    public static final ResourceLocation BOLT_LOCATION = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/entity/projectiles/bolt.png");
    public BoltRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(Bolt entity) {
        return BOLT_LOCATION;
    }
}

