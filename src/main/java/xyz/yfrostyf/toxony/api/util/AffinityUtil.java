package xyz.yfrostyf.toxony.api.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.SavedData;
import xyz.yfrostyf.toxony.api.affinity.Affinity;
import xyz.yfrostyf.toxony.api.items.AffinityIngredient;
import xyz.yfrostyf.toxony.api.registries.ToxonyRegistries;
import xyz.yfrostyf.toxony.data.world.IngredientAffinityMapSavedData;
import xyz.yfrostyf.toxony.registries.AffinityRegistry;

import java.util.Map;
import java.util.Objects;

public class AffinityUtil {
    public static IngredientAffinityMapSavedData computeIngredientAffinityMap(ServerLevel svlevel){
        SavedData savedData = svlevel.getDataStorage().computeIfAbsent(IngredientAffinityMapSavedData.factory(), "toxony_affinity_map");
        if(savedData instanceof IngredientAffinityMapSavedData affinityMapSavedData){
            return affinityMapSavedData;
        }
        throw new ClassCastException("Saved data was not an instance of IngredientAffinityMapSavedData");
    }

    public static Map<String, Affinity> getIngredientAffinityMap(ServerLevel svlevel){
        SavedData savedData = svlevel.getDataStorage().get(IngredientAffinityMapSavedData.factory(), "toxony_affinity_map");
        if(savedData instanceof IngredientAffinityMapSavedData affinityMapSavedData){
            return affinityMapSavedData.getIngredientToAffinityMap();
        }
        throw new ClassCastException("Saved data was not an instance of IngredientAffinityMapSavedData");
    }

    public static Affinity readAffinityFromIngredientMap(ItemStack item, ServerLevel svlevel){
        if(!(item.getItem() instanceof AffinityIngredient))return Affinity.EMPTY;
        Map<String, Affinity> map = getIngredientAffinityMap(svlevel);
        Affinity affinity = map.get(item.getItemHolder().getKey().location().toString());
        return affinity;
    }

    /**
     * Checks if the inputted affinity matches with affinity associated with the item in the affinity map.
     * Returns true if match was found. Returns false if values do not match or if the item is not in the map.
     */
    public static boolean matchesAffinityMap(ItemStack item, Affinity affinity, ServerLevel svlevel){
        Affinity compareValue = readAffinityFromIngredientMap(item, svlevel);
        return Objects.equals(affinity.getName(), compareValue.getName());
    }
}
