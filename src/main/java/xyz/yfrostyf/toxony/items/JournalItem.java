package xyz.yfrostyf.toxony.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import xyz.yfrostyf.toxony.client.gui.journal.JournalUtil;

public class JournalItem extends Item {
    public JournalItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack usedStack = player.getItemInHand(usedHand);
        if(level.isClientSide()){
            JournalUtil.startPage("journal.toxony.page.introduction.0");
            return InteractionResultHolder.consume(usedStack);
        }
        return super.use(level, player, usedHand);
    }
}
