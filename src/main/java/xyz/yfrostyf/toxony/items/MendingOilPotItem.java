package xyz.yfrostyf.toxony.items;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import xyz.yfrostyf.toxony.api.oils.ItemOil;
import xyz.yfrostyf.toxony.registries.ItemRegistry;
import xyz.yfrostyf.toxony.registries.TagRegistry;

public class MendingOilPotItem extends OilPotItem {
    public static final int REPAIR_AMOUNT = 100;

    public MendingOilPotItem(Properties properties, Holder<Block> oilPotBlock) {
        super(properties.stacksTo(1), ItemOil.EMPTY, oilPotBlock);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack oilPot = player.getMainHandItem().is(this) ? player.getMainHandItem() : player.getOffhandItem();
        ItemStack otherStack = player.getMainHandItem().is(this) ? player.getOffhandItem() : player.getMainHandItem();

        if(otherStack.is(ItemRegistry.OIL_BASE)){
            otherStack.consume(1, player);
            oilPot.setDamageValue(0);
        }
        else if(otherStack.isDamaged() && otherStack.is(TagRegistry.OIL_REPAIRABLE) && oilPot.getDamageValue() < oilPot.getMaxDamage()){
            return ItemUtils.startUsingInstantly(level, player, hand);
        }
        else if(!otherStack.is(TagRegistry.OIL_REPAIRABLE)){
            if(level.isClientSide()){
                Minecraft.getInstance().gui.setOverlayMessage(Component.translatable("message.toxony.oilpot.mending_fail"), false);
            }
        }
        return InteractionResultHolder.pass(oilPot);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if(!(entity instanceof Player player))return stack;
        ItemStack applied = player.getMainHandItem().is(this) ? player.getOffhandItem() : player.getMainHandItem();
        int damage = stack.getDamageValue();

        if (!applied.isDamaged() || !applied.is(TagRegistry.OIL_REPAIRABLE)) return stack;

        if (damage < stack.getMaxDamage()) {
            applied.setDamageValue(Math.max(0, applied.getDamageValue() - REPAIR_AMOUNT));
            if(stack.isDamageableItem()){
                stack.setDamageValue(damage + 1);
            }
        }
        return stack;
    }
}
