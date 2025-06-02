package xyz.yfrostyf.toxony.api.mutagens;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.*;

import java.util.HashMap;
import java.util.Map;

public class MutagenData {
    Map<String, Boolean> booleanMap = new HashMap<>();
    Map<String, Integer> integerMap = new HashMap<>();

    public MutagenData(){};

    public MutagenData(Map<String, Boolean> booleanMap, Map<String, Integer> integerMap){
        this.booleanMap = booleanMap;
        this.integerMap = integerMap;
    }

    public Map<String, Boolean> getBooleanMap() {
        return booleanMap;
    }

    public Map<String, Integer> getIntegerMap() {
        return integerMap;
    }

    /**
     * Adds new boolean information into MutagenData while checking if the value already existed.
     * @param name name of data and the key for the boolean.
     * @param value the boolean to store.
     * @return False if a new entry was added, True if key already exists and was replaced.
     */
    public boolean addBool(String name, boolean value){
        if(booleanMap.containsKey(name)){
            booleanMap.replace(name, value);
            return false;
        }
        else{
            booleanMap.put(name, value);
            return true;
        }
    }

    public boolean addInt(String name, int value){
        if(integerMap.containsKey(name)){
            integerMap.replace(name, value);
            return false;
        }
        else{
            integerMap.put(name, value);
            return true;
        }
    }

    /**
     * Removes the value from the map associated with the inputted key.
     * @param name key associated with the boolean data to be removed.
     * @return True if the key exists and was removed, False if key with that name does not exist.
     */
    public boolean remove(String name){
        if(booleanMap.containsKey(name)){
            booleanMap.remove(name);
            return true;
        }
        else if(integerMap.containsKey(name)){
            integerMap.remove(name);
            return true;
        }
        return false;
    }

    /**
     * Gets the value from the map associated with the inputted key.
     * @param name key associated with the boolean data to return.
     * @return The value associated with the key or False if the key with that name does not exist.
     */
    public boolean getBool(String name){
        if(booleanMap.containsKey(name)){
            return booleanMap.get(name);
        }
        return false;
    }

    public int getInt(String name){
        if(integerMap.containsKey(name)){
            return integerMap.get(name);
        }
        return -1;
    }

    public void saveNBTData(CompoundTag compound, HolderLookup.Provider provider) {
        ListTag boolKeyList = new ListTag();
        ListTag boolValueList = new ListTag();

        ListTag intKeyList = new ListTag();
        ListTag intValueList = new ListTag();

        for(Map.Entry<String, Boolean> entry : booleanMap.entrySet()){
            boolKeyList.add(StringTag.valueOf(entry.getKey()));
            boolValueList.add(ByteTag.valueOf(entry.getValue()));
        }

        for(Map.Entry<String, Integer> entry : integerMap.entrySet()){
            intKeyList.add(StringTag.valueOf(entry.getKey()));
            intValueList.add(IntTag.valueOf(entry.getValue()));
        }

        compound.put("mutagen_bool_key_list", boolKeyList);
        compound.put("mutagen_bool_value_list", boolValueList);

        compound.put("mutagen_int_key_list", intKeyList);
        compound.put("mutagen_int_value_list", intValueList);
    }

    public void loadNBTData(CompoundTag compound, HolderLookup.Provider provider) {
        ListTag boolKeyList = compound.getList("mutagen_bool_key_list", Tag.TAG_STRING);
        ListTag boolValueList = compound.getList("mutagen_bool_value_list", Tag.TAG_BYTE);

        ListTag intKeyList = compound.getList("mutagen_int_key_list", Tag.TAG_STRING);
        ListTag intValueList = compound.getList("mutagen_int_value_list", Tag.TAG_INT);

        this.booleanMap = new HashMap<>();
        for(int i=0; i<boolKeyList.size();i++){
            this.booleanMap.put(boolKeyList.getString(i), boolValueList.getInt(i) != 0);
        }

        this.integerMap = new HashMap<>();
        for(int i=0; i<intKeyList.size();i++){
            this.integerMap.put(intKeyList.getString(i), intValueList.getInt(i));
        }
    }
}
