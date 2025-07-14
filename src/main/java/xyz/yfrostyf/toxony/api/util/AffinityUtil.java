package xyz.yfrostyf.toxony.api.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import xyz.yfrostyf.toxony.api.affinity.Affinity;
import xyz.yfrostyf.toxony.api.tox.ToxData;
import xyz.yfrostyf.toxony.client.ClientIngredientAffinityMapData;
import xyz.yfrostyf.toxony.data.world.IngredientAffinityMapData;
import xyz.yfrostyf.toxony.registries.DataComponentsRegistry;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;

public class AffinityUtil {
    public static IngredientAffinityMapData computeIngredientAffinityMap(ServerLevel svlevel){
        SavedData savedData = svlevel.getServer().overworld().getDataStorage().computeIfAbsent(IngredientAffinityMapData.factory(), "toxony_affinity_map");
        if(savedData instanceof IngredientAffinityMapData affinityMapSavedData){
            return affinityMapSavedData;
        }
        throw new ClassCastException("Saved data was not an instance of IngredientAffinityMapData");
    }

    public static Map<ResourceLocation, Affinity> getIngredientAffinityMap(Level level) {
        if(level instanceof ServerLevel svlevel){
            SavedData savedData = svlevel.getServer().overworld().getDataStorage().get(IngredientAffinityMapData.factory(), "toxony_affinity_map");
            if(savedData instanceof IngredientAffinityMapData affinityMapSavedData){
                return affinityMapSavedData.getIngredientToAffinityMap();
            }
            throw new ClassCastException("Saved data was not an instance of IngredientAffinityMapData");
        }
        else{
            Optional<IngredientAffinityMapData> data = ClientIngredientAffinityMapData.getData();
            if(data.isPresent()){
                return ClientIngredientAffinityMapData.getData().get().getIngredientToAffinityMap();
            }
            throw new ClassCastException("Client does not have Ingredient-Affinity Map data");
        }
    }

    public static Affinity readAffinityFromIngredientMap(ItemStack item){
        MinecraftServer mcServer = ServerLifecycleHooks.getCurrentServer();
        return readAffinityFromIngredientMap(item, mcServer != null ? mcServer.getLevel(Level.OVERWORLD) : null);
    }

    public static Affinity readAffinityFromIngredientMap(ItemStack item, @Nullable Level level){
        if(!(item.has(DataComponentsRegistry.POSSIBLE_AFFINITIES)))return Affinity.EMPTY;
        Map<ResourceLocation, Affinity> map = getIngredientAffinityMap(level);
        Affinity affinity = map.get(item.getItemHolder().getKey().location());
        return affinity;
    }

    /**
     * Checks if the inputted affinity matches with affinity associated with the item in the affinity map.
     * Returns true if match was found. Returns false if values do not match or if the item is not in the map.
     */
    public static boolean matchesAffinityMap(ItemStack item, Affinity affinity, Level level){
        Affinity compareValue = readAffinityFromIngredientMap(item, level);
        return (affinity.equals(compareValue));
    }

    public static void addAffinityByItem(ToxData toxData, ItemStack item, Affinity affinity, int amount){
        toxData.addAffinity(affinity, amount);
        toxData.addKnownIngredients(item);
    }
}
