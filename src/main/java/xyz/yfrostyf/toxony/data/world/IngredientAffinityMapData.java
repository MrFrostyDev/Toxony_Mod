package xyz.yfrostyf.toxony.data.world;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.saveddata.SavedData;
import xyz.yfrostyf.toxony.api.affinity.Affinity;
import xyz.yfrostyf.toxony.api.registries.ToxonyRegistries;
import xyz.yfrostyf.toxony.registries.DataComponentsRegistry;

import java.util.*;

public class IngredientAffinityMapData extends SavedData {
    private static final Random RANDOM = new Random();

    // Mapped ResourceLocation's to Affinities
    Map<ResourceLocation, Affinity> ingredientToAffinityMap;

    private IngredientAffinityMapData(Map<ResourceLocation, Affinity> ingredientToAffinityMap){
        this.ingredientToAffinityMap = ingredientToAffinityMap;
    }

    public Map<ResourceLocation, Affinity> getIngredientToAffinityMap(){
        return ingredientToAffinityMap;
    }

    public static IngredientAffinityMapData create(Map<ResourceLocation, Affinity> ingredientToAffinityMap) {
        return new IngredientAffinityMapData(ingredientToAffinityMap);
    }

    // Create new instance of saved data
    public static IngredientAffinityMapData create() {
        Map<ResourceLocation, Affinity> createdIngredientToAffinityMap = new HashMap<>();

        for(Holder<Item> holder : BuiltInRegistries.ITEM.asHolderIdMap()) {
            if(!holder.value().components().has(DataComponentsRegistry.POSSIBLE_AFFINITIES.get()))continue;
            Pair<ResourceLocation, Affinity> pair = randIngredientToAffinity(holder, createdIngredientToAffinityMap);
            if (pair == null) continue;

            createdIngredientToAffinityMap.put(pair.getFirst(), pair.getSecond());
        }

        IngredientAffinityMapData data = new IngredientAffinityMapData(createdIngredientToAffinityMap);
        data.setDirty(true);
        return data;
    }

    private static Pair<ResourceLocation, Affinity> randIngredientToAffinity(Holder<Item> holder, Map<ResourceLocation, Affinity> map){
        List<ResourceKey<Affinity>> possibleAffinities = new ArrayList<>(holder.value().components().get(DataComponentsRegistry.POSSIBLE_AFFINITIES.get()));
        if(possibleAffinities.isEmpty()) return null;

        ResourceKey<Affinity> randKey = possibleAffinities.remove(RANDOM.nextInt(possibleAffinities.size()));
        Affinity selectedAffinity = ToxonyRegistries.AFFINITY_REGISTRY.get(randKey);
        int i = 0;
        while(i < possibleAffinities.size()){
            if(!map.containsValue(selectedAffinity))break;
            randKey = possibleAffinities.remove(RANDOM.nextInt(possibleAffinities.size()));
            selectedAffinity = ToxonyRegistries.AFFINITY_REGISTRY.get(randKey);
            i++;
        }
        return new Pair<>(holder.getKey().location(), selectedAffinity);
    }


    public static IngredientAffinityMapData load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        ListTag stringListTag = tag.getList("affinitymap_items", Tag.TAG_STRING);
        ListTag affinityListTag = tag.getList("affinitymap_affinity", Tag.TAG_STRING);

        Map<ResourceLocation, Affinity> map = new HashMap<>();
        for(int i=0; i<stringListTag.size(); i++){
            map.put(ResourceLocation.parse(stringListTag.getString(i)), ToxonyRegistries.AFFINITY_REGISTRY.get(ResourceLocation.parse(affinityListTag.getString(i))));
        }

        return new IngredientAffinityMapData(map);
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag stringListTag = new ListTag();
        ListTag affinityListTag = new ListTag();
        for(Map.Entry<ResourceLocation, Affinity> entry : ingredientToAffinityMap.entrySet()){
            stringListTag.add(StringTag.valueOf(entry.getKey().toString()));
            affinityListTag.add(StringTag.valueOf(
                    ToxonyRegistries.AFFINITY_REGISTRY
                            .holders()
                            .filter(ref -> ref.value().equals(entry.getValue()))
                            .findFirst()
                            .get().getKey().location().toString()
            ));
        }

        tag.put("affinitymap_items", stringListTag);
        tag.put("affinitymap_affinity", affinityListTag);

        return tag;
    }

    public static SavedData.Factory<IngredientAffinityMapData> factory(){
        return new SavedData.Factory<>(IngredientAffinityMapData::create, IngredientAffinityMapData::load);
    }
}
