package xyz.yfrostyf.toxony.client.gui.tooltips;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.oils.ItemOil;
import xyz.yfrostyf.toxony.registries.DataComponentsRegistry;

public class OilTooltip implements ClientTooltipComponent {
    public static final ResourceLocation RESOURCE = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/gui/oil_bars.png");
    private ItemStack itemstack;

    public OilTooltip(OilTooltipComponent tooltipComponent){
        this.itemstack = tooltipComponent.itemstack();
    }

    @Override
    public int getHeight() {
        return 20;
    }

    @Override
    public int getWidth(Font font) {
        return 20;
    }

    @Override
    public void renderText(Font font, int mouseX, int mouseY, Matrix4f matrix, MultiBufferSource.BufferSource bufferSource) {
        ClientTooltipComponent.super.renderText(font, mouseX, mouseY, matrix, bufferSource);
    }

    private static final int TEXTURE_WIDTH = 58;
    private static final int TEXTURE_HEIGHT = 43;

    // |================= Bar UVs =================|

    private static final int BAR_MAX_WIDTH = 58;
    private static final int BAR_OIL_WIDTH = 56;

    private static final int BAR_HEIGHT = 9;

    private static final int BAR_UOFFSET = 0;
    private static final int BAR_MAX_VOFFSET = 25;
    private static final int BAR_OIL_VOFFSET = 34;

    // |================= Icon UVs =================|

    private static final int ICON_WIDTH = 20;
    private static final int ICON_HEIGHT = 20;

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        ItemOil itemoil = itemstack.getOrDefault(DataComponentsRegistry.OIL, ItemOil.EMPTY);
        if (itemoil.isEmpty()) return;
        int uses = itemstack.getOrDefault(DataComponentsRegistry.OIL_USES, 0);

        Holder<MobEffect> holder = itemoil.oil().effects().getFirst();
        MobEffectTextureManager mobEffectTextureManager = Minecraft.getInstance().getMobEffectTextures();
        TextureAtlasSprite textureatlassprite = mobEffectTextureManager.get(holder);

        int oilBarWidth = Mth.ceil(BAR_OIL_WIDTH * Math.min((float)(itemoil.maxUses()-uses)/(float)itemoil.maxUses(), 1));

        // Max Bar | (ResourceLocation atlasLocation, int x, int y, float uOffset, float vOffset, int width, int height, int textureWidth, int textureHeight)
        guiGraphics.blit(RESOURCE,
                x+ICON_WIDTH-1, y+6,
                BAR_UOFFSET, BAR_MAX_VOFFSET,
                BAR_MAX_WIDTH, BAR_HEIGHT,
                TEXTURE_WIDTH, TEXTURE_HEIGHT);

        // Oil Bar
        guiGraphics.blit(RESOURCE,
                x+ICON_WIDTH-1, y+6,
                BAR_UOFFSET, BAR_OIL_VOFFSET,
                oilBarWidth, BAR_HEIGHT,
                TEXTURE_WIDTH, TEXTURE_HEIGHT);

        // Icon
        guiGraphics.blit(RESOURCE,
                x, y,
                0, 0,
                ICON_WIDTH, ICON_HEIGHT,
                TEXTURE_WIDTH, TEXTURE_HEIGHT);

        // Mob Effect
        guiGraphics.blit(
                x+1, y+1,
                0,
                18, 18,
                textureatlassprite);
    }

    public record OilTooltipComponent(ItemStack itemstack) implements TooltipComponent { }

}