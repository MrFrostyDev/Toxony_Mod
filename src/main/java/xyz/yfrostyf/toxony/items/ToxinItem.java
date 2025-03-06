package xyz.yfrostyf.toxony.items;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import xyz.yfrostyf.toxony.api.items.ToxGiverItem;

import java.util.List;
import java.util.function.Supplier;

public class ToxinItem extends ToxGiverItem {
    protected static final int DRINK_DURATION = 16;

    public ToxinItem(Properties properties, float tox, float tolerance, int tier, Supplier<ItemStack> returnItem, List<MobEffectInstance> mobEffectInstances) {
        super(properties, tox, tolerance, tier, returnItem, mobEffectInstances);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return DRINK_DURATION;
    }

    public static ToxinItem.Builder builder(){
        return new ToxinItem.Builder();
    }

    public static class Builder extends ToxGiverItem.Builder{
        public ToxinItem build(){
            return new ToxinItem(properties, this.tox, this.tolerance, this.tier, this.returnItem, this.mobEffectInstances);
        }
    }
}
