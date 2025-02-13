package xyz.yfrostyf.toxony.client.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.affinity.Affinity;
import xyz.yfrostyf.toxony.api.client.ClientToxData;
import xyz.yfrostyf.toxony.api.tox.ToxData;
import xyz.yfrostyf.toxony.client.ClientIngredientAffinityMapData;

import java.util.Map;

public class ToxIngredientTooltip implements ClientTooltipComponent {
    public static final ResourceLocation RESOURCE = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "textures/gui/affinity_icons.png");
    private ItemStack itemstack;

    public ToxIngredientTooltip(ToxIngredientComponent tooltipComponent){
        this.itemstack = tooltipComponent.itemstack();
    }

    @Override
    public int getHeight() {
        return 15;
    }

    @Override
    public int getWidth(Font font) {
        return 15;
    }

    @Override
    public void renderText(Font font, int mouseX, int mouseY, Matrix4f matrix, MultiBufferSource.BufferSource bufferSource) {
        ClientTooltipComponent.super.renderText(font, mouseX, mouseY, matrix, bufferSource);
    }

    private static final int TEXTURE_WIDTH = 52;
    private static final int TEXTURE_HEIGHT = 39;

    // |================= Icons UVs =================|

    private static final int ICON_WIDTH = 13;
    private static final int ICON_HEIGHT = 13;

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        ToxData plyToxData = ClientToxData.getToxData();
        int index = 0;

        if(plyToxData.knowsIngredient(itemstack)){
            Map<ResourceLocation, Affinity> map = ClientIngredientAffinityMapData.getData().get().getIngredientToAffinityMap();
            Affinity affinity = map.get(itemstack.getItemHolder().getKey().location());
            index = affinity.getIndex();
        }

        // Max Bar | (ResourceLocation atlasLocation, int x, int y, float uOffset, float vOffset, int width, int height, int textureWidth, int textureHeight)
        // Icon
        guiGraphics.blit(RESOURCE,
                x+1, y+1,
                index * 13, (int)((double)(index / 4) * 13),
                ICON_WIDTH, ICON_HEIGHT,
                TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

        public record ToxIngredientComponent(ItemStack itemstack) implements TooltipComponent { }

}