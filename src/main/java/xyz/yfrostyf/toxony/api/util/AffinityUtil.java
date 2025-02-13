package xyz.yfrostyf.toxony.api.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.SavedData;
import xyz.yfrostyf.toxony.api.affinity.Affinity;
import xyz.yfrostyf.toxony.api.tox.ToxData;
import xyz.yfrostyf.toxony.data.world.IngredientAffinityMapData;
import xyz.yfrostyf.toxony.registries.DataComponentsRegistry;

import java.util.Map;

public class AffinityUtil {
    public static IngredientAffinityMapData computeIngredientAffinityMap(ServerLevel svlevel){
        SavedData savedData = svlevel.getDataStorage().computeIfAbsent(IngredientAffinityMapData.factory(), "toxony_affinity_map");
        if(savedData instanceof IngredientAffinityMapData affinityMapSavedData){
            return affinityMapSavedData;
        }
        throw new ClassCastException("Saved data was not an instance of IngredientAffinityMapData");
    }

    public static Map<ResourceLocation, Affinity> getIngredientAffinityMap(ServerLevel svlevel){
        SavedData savedData = svlevel.getDataStorage().get(IngredientAffinityMapData.factory(), "toxony_affinity_map");
        if(savedData instanceof IngredientAffinityMapData affinityMapSavedData){
            return affinityMapSavedData.getIngredientToAffinityMap();
        }
        throw new ClassCastException("Saved data was not an instance of IngredientAffinityMapData");
    }

    public static Affinity readAffinityFromIngredientMap(ItemStack item, ServerLevel svlevel){
        if(!(item.has(DataComponentsRegistry.POSSIBLE_AFFINITIES)))return Affinity.EMPTY;
        Map<ResourceLocation, Affinity> map = getIngredientAffinityMap(svlevel);
        Affinity affinity = map.get(item.getItemHolder().getKey().location());
        return affinity;
    }

    /**
     * Checks if the inputted affinity matches with affinity associated with the item in the affinity map.
     * Returns true if match was found. Returns false if values do not match or if the item is not in the map.
     */
    public static boolean matchesAffinityMap(ItemStack item, Affinity affinity, ServerLevel svlevel){
        Affinity compareValue = readAffinityFromIngredientMap(item, svlevel);
        return (compareValue == affinity);
    }

    public static void addAffinityByItem(ToxData toxData, ItemStack item, Affinity affinity, int amount){
        toxData.addAffinity(affinity, amount);
        toxData.addKnownIngredients(item);
    }
}
