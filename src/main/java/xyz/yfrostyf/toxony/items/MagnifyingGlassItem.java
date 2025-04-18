package xyz.yfrostyf.toxony.items;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class MagnifyingGlassItem extends Item {
    public MagnifyingGlassItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if(level.isClientSide()){
            BlockState state = level.getBlockState(context.getClickedPos());
            Minecraft.getInstance().gui.setOverlayMessage(
                    Component.translatable(state.getBlock().getDescriptionId()),
                    false
            );
        }
        return super.useOn(context);
    }
}
