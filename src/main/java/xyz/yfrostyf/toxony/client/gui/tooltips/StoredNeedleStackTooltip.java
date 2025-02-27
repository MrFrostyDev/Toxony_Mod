package xyz.yfrostyf.toxony.client.gui.tooltips;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.joml.Matrix4f;
import xyz.yfrostyf.toxony.registries.DataComponentsRegistry;

public class StoredNeedleStackTooltip implements ClientTooltipComponent {
    private ItemStack itemstack;

    public StoredNeedleStackTooltip(StoredNeedleStackTooltipComponent tooltipComponent){
        this.itemstack = tooltipComponent.itemstack();
    }

    @Override
    public int getHeight() {
        return 18;
    }

    @Override
    public int getWidth(Font font) {
        return 18;
    }

    @Override
    public void renderText(Font font, int mouseX, int mouseY, Matrix4f matrix, MultiBufferSource.BufferSource bufferSource) {
        ClientTooltipComponent.super.renderText(font, mouseX, mouseY, matrix, bufferSource);
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        ItemStack stack = new ItemStack(itemstack.getOrDefault(DataComponentsRegistry.AFFINITY_STORED_ITEM, BuiltInRegistries.ITEM.wrapAsHolder(Items.AIR)));
        if (stack.isEmpty()) return;

        guiGraphics.renderItem(stack, x+1, y+1, 0);
    }

    public record StoredNeedleStackTooltipComponent(ItemStack itemstack) implements TooltipComponent { }

}