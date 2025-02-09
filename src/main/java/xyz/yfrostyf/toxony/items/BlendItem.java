package xyz.yfrostyf.toxony.items;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import xyz.yfrostyf.toxony.api.items.ToxGiverItem;

import java.util.List;
import java.util.Map;

public class BlendItem extends ToxGiverItem {


    public BlendItem(Properties properties, float tox, float tolerance, int tier, Map<Integer, Integer> affinities, List<MobEffectInstance> mobEffectInstances) {
        super(properties, tox, tolerance, tier, affinities, mobEffectInstances);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof ServerPlayer svplayer) {
            return ItemUtils.createFilledResult(super.finishUsingItem(stack, level, entity), svplayer, new ItemStack(Items.BOWL), false);
        }
        else{
            return super.finishUsingItem(stack, level, entity);
        }
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder extends ToxGiverItem.Builder {
        public BlendItem build(){
            return new BlendItem(properties, this.tox, this.tolerance, this.tier, this.affinities, this.mobEffectInstances);
        }
    }
}
