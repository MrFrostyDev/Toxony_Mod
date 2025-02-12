package xyz.yfrostyf.toxony.data.world;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.registries.DeferredHolder;
import xyz.yfrostyf.toxony.api.affinity.Affinity;
import xyz.yfrostyf.toxony.api.items.AffinityIngredient;
import xyz.yfrostyf.toxony.api.registries.ToxonyRegistries;
import xyz.yfrostyf.toxony.registries.ItemRegistry;

import java.util.*;

public class IngredientAffinityMapSavedData extends SavedData {
    // Mapped ResourceLocation's to Affinities
    Map<String, Affinity> ingredientToAffinityMap;

    private IngredientAffinityMapSavedData(Map<String, Affinity> ingredientToAffinityMap){
        this.ingredientToAffinityMap = ingredientToAffinityMap;
    }

    public Map<String, Affinity> getIngredientToAffinityMap(){
        return ingredientToAffinityMap;
    }

    // Create new instance of saved data
    public static IngredientAffinityMapSavedData create() {
        Map<String, Affinity> createdIngredientToAffinityMap = new HashMap<>();
        List<DeferredHolder<Item, ? extends Item>> items = ItemRegistry.ITEMS.getEntries().stream()
                .filter(item -> item.value().asItem() instanceof AffinityIngredient)
                .toList();

        for(DeferredHolder<Item, ? extends Item> holder : items){
            Pair<String, Affinity> pair = randIngredientToAffinity(holder, createdIngredientToAffinityMap);
            if(pair == null) continue;

            createdIngredientToAffinityMap.put(pair.getFirst(), pair.getSecond());
        }

        return new IngredientAffinityMapSavedData(createdIngredientToAffinityMap);
    }

    private static Pair<String, Affinity> randIngredientToAffinity(Holder<Item> holder, Map<String, Affinity> map){
        AffinityIngredient ingredient = (AffinityIngredient)holder.value();
        List<Affinity> possibleAffinities = new ArrayList<>(ingredient.getPossibleAffinities());
        if(possibleAffinities.isEmpty()) return null;

        Affinity selectedAffinity = possibleAffinities.remove(new Random().nextInt(possibleAffinities.size()));
        int i = 0;
        while(i < map.size()){
            if(!map.containsValue(selectedAffinity))break;
            selectedAffinity = possibleAffinities.remove(new Random().nextInt(possibleAffinities.size()));
            i++;
        }
        return new Pair<>(holder.getKey().location().toString(), selectedAffinity);
    }


    public static IngredientAffinityMapSavedData load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        ListTag stringListTag = tag.getList("affinitymap_items", Tag.TAG_STRING);
        ListTag affinityListTag = tag.getList("affinitymap_affinity", Tag.TAG_STRING);

        Map<String, Affinity> map = new HashMap<>();
        for(int i=0; i<stringListTag.size(); i++){
            map.put(stringListTag.getString(i), ToxonyRegistries.AFFINITY_REGISTRY.get(ResourceLocation.parse(affinityListTag.getString(i))));
        }

        return new IngredientAffinityMapSavedData(map);
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag stringListTag = new ListTag();
        ListTag affinityListTag = new ListTag();
        for(String entry : ingredientToAffinityMap.keySet()){
            stringListTag.add(StringTag.valueOf(entry));
            affinityListTag.add(StringTag.valueOf(
                    ToxonyRegistries.AFFINITY_REGISTRY.getKey(ingredientToAffinityMap.get(entry)).toString()
            ));
        }

        tag.put("affinitymap_items", stringListTag);
        tag.put("affinitymap_affinity", affinityListTag);

        return tag;
    }

    public void setDirty() {
        this.setDirty(true);
    }

    public static SavedData.Factory<SavedData> factory(){
        return new SavedData.Factory<>(IngredientAffinityMapSavedData::create, IngredientAffinityMapSavedData::load);
    }
}
