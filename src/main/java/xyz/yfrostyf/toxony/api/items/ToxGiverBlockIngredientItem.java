package xyz.yfrostyf.toxony.api.items;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import xyz.yfrostyf.toxony.api.affinity.Affinity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ToxGiverBlockIngredientItem extends ToxGiverBlockItem implements AffinityIngredient{
    protected final Supplier<List<Affinity>> affinities;

    public ToxGiverBlockIngredientItem(Block block, Properties properties, float tox, float tolerance, int tier, Supplier<ItemStack> returnItem, List<MobEffectInstance> mobEffectInstances, Supplier<List<Affinity>> affinities) {
        super(block, properties, tox, tolerance, tier, returnItem, mobEffectInstances);
        this.affinities = affinities;
    }

    @Override
    public List<Affinity> getPossibleAffinities() {
        return affinities.get();
    }

    public static ToxGiverBlockIngredientItem.Builder builder(){
        return new ToxGiverBlockIngredientItem.Builder();
    }

    public static class Builder extends ToxGiverBlockItem.Builder {
        protected Supplier<List<Affinity>> affinities = ArrayList::new;

        public ToxGiverBlockIngredientItem.Builder affinity(Supplier<List<Affinity>> affinities){
            this.affinities = affinities;
            return this;
        }

        public ToxGiverBlockIngredientItem build() {
            return new ToxGiverBlockIngredientItem(this.block, properties, this.tox, this.tolerance, this.tier, this.returnItem, this.mobEffectInstances, this.affinities);
        }
    }
}
