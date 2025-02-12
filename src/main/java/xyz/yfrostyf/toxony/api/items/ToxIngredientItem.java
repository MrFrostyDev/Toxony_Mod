package xyz.yfrostyf.toxony.api.items;

import net.minecraft.world.item.Item;
import xyz.yfrostyf.toxony.api.affinity.Affinity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ToxIngredientItem extends Item implements AffinityIngredient {

    protected final Supplier<List<Affinity>> affinities;

    public ToxIngredientItem(Properties properties, Supplier<List<Affinity>> affinities) {
        super(properties);
        this.affinities = affinities;
    }

    @Override
    public List<Affinity> getPossibleAffinities() {
        return affinities.get();
    }

    public static ToxIngredientItem.Builder builder(){
        return new ToxIngredientItem.Builder();
    }

    public static class Builder {
        protected Item.Properties properties = new Item.Properties();
        protected Supplier<List<Affinity>> affinities = ArrayList::new;

        public Builder properties(Item.Properties properties){
            this.properties = properties;
            return this;
        }

        public Builder affinity(Supplier<List<Affinity>> affinities){
            this.affinities = affinities;
            return this;
        }

        public ToxIngredientItem build(){
            return new ToxIngredientItem(this.properties, this.affinities);
        }
    }
}
