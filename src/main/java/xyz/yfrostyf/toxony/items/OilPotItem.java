package xyz.yfrostyf.toxony.items;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import xyz.yfrostyf.toxony.api.oils.ItemOil;
import xyz.yfrostyf.toxony.api.util.OilUtil;

import java.util.function.Supplier;

public class OilPotItem extends Item {
    private static final int USE_DURATION = 60;
    private final Supplier<ItemOil> itemOil;

    public OilPotItem(Properties properties, Supplier<ItemOil> itemOil) {
        super(properties.stacksTo(1));
        this.itemOil = itemOil;
    }

    public ItemOil getItemOil(){
        return itemOil.get();
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if(!(entity instanceof Player player))return stack;
        ItemStack applied = player.getMainHandItem().is(this) ? player.getOffhandItem() : player.getMainHandItem();

        if (!applied.is(getItemOil().oil().supportedItems())) return stack;
        if (getItemOil().isEmpty()) return stack;

        if (entity instanceof ServerPlayer svplayer && level instanceof ServerLevel svlevel) {

            OilUtil.updateOil(applied, getItemOil());
            if(stack.isDamageableItem()){
                stack.hurtAndBreak(1, svlevel, svplayer,
                        (item) -> ItemUtils.createFilledResult(stack, svplayer, this.resultingItem())
                );
            }

            return stack;
        }
        else {
            return stack;
        }
    }

    /**
     * The item returned after the oil has been used up and this item breaks.
     */
    private ItemStack resultingItem(){
        return new ItemStack(Items.BOWL);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack oilPot = player.getMainHandItem().is(this) ? player.getMainHandItem() : player.getOffhandItem();
        ItemStack applied = player.getMainHandItem().is(this) ? player.getOffhandItem() : player.getMainHandItem();

        if(applied.is(getItemOil().oil().supportedItems()) && !getItemOil().isEmpty()){
            return ItemUtils.startUsingInstantly(level, player, hand);
        }
        return InteractionResultHolder.fail(oilPot);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return USE_DURATION;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public SoundEvent getEatingSound() {
        return SoundEvents.HONEYCOMB_WAX_ON;
    }
}
