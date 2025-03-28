package xyz.yfrostyf.toxony.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import xyz.yfrostyf.toxony.client.gui.journal.JournalUtil;
import xyz.yfrostyf.toxony.registries.BlockRegistry;

public class JournalItem extends ItemNameBlockItem {
    public JournalItem(Properties properties) {
        super(BlockRegistry.LOST_JOURNAL.get(), properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack usedStack = player.getItemInHand(usedHand);
        if(!player.isCrouching()){
            if(level.isClientSide()){
                JournalUtil.startPage("journal.toxony.page.cover");
                return InteractionResultHolder.consume(usedStack);
            }
        }
        return super.use(level, player, usedHand);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        InteractionResult result = InteractionResult.PASS;
        if(context.getPlayer().isCrouching()){
            result = this.place(new BlockPlaceContext(context));
        }
        if (!result.consumesAction()) {
            result = this.use(context.getLevel(), context.getPlayer(), context.getHand()).getResult();
            return result == InteractionResult.CONSUME ? InteractionResult.CONSUME_PARTIAL : result;
        }
        return result;
    }
}
