package xyz.yfrostyf.toxony.client.gui.tooltips;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Holder;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import xyz.yfrostyf.toxony.registries.DataComponentsRegistry;

import java.util.List;

public class StoredAffinityStacksTooltip implements ClientTooltipComponent {
    private ItemStack itemstack;

    public StoredAffinityStacksTooltip(StoredAffinityStacksTooltipComponent tooltipComponent){
        this.itemstack = tooltipComponent.itemstack();
    }

    @Override
    public int getHeight() {
        return 18;
    }

    @Override
    public int getWidth(Font font) {
        if (itemstack.has(DataComponentsRegistry.AFFINITY_STORED_ITEMS)){
            return itemstack.get(DataComponentsRegistry.AFFINITY_STORED_ITEMS).size()*18;
        }
        return 0;
    }

    @Override
    public void renderText(Font font, int mouseX, int mouseY, Matrix4f matrix, MultiBufferSource.BufferSource bufferSource) {
        ClientTooltipComponent.super.renderText(font, mouseX, mouseY, matrix, bufferSource);
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        if(!itemstack.has(DataComponentsRegistry.AFFINITY_STORED_ITEMS)) return;

        List<Holder<Item>> stack = itemstack.get(DataComponentsRegistry.AFFINITY_STORED_ITEMS);
        int i = 0;
        for(Holder<Item> holder : stack){
            guiGraphics.renderItem(new ItemStack(holder), x+(16*i)+1, y+1, 0);
            i++;
        }
    }

    public record StoredAffinityStacksTooltipComponent(ItemStack itemstack) implements TooltipComponent { }

}