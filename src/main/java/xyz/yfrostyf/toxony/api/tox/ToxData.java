package xyz.yfrostyf.toxony.api.tox;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import xyz.yfrostyf.toxony.api.affinity.Affinity;
import xyz.yfrostyf.toxony.api.events.ChangeThresholdEvent;
import xyz.yfrostyf.toxony.api.events.ChangeToleranceEvent;
import xyz.yfrostyf.toxony.api.events.ChangeToxEvent;
import xyz.yfrostyf.toxony.api.registries.ToxonyRegistries;
import xyz.yfrostyf.toxony.api.util.ToxUtil;

import java.util.*;

public class ToxData {

    // Heavily based on Irons-Spells-n-Spellbooks MagicData class
    // Thank you very much for the reference!

    public static final int MAX_TOLERANCE = 999;
    public static final int MIN_TOLERANCE = 10;
    public static final int DEFAULT_TOLERANCE = 30;
    public static final int MINIMUM_KNOW = 2;

    private Player player;

    // Synced Data
    private float tox = 0;
    private float tolerance = DEFAULT_TOLERANCE;
    private int threshold = 0;
    private Map<Affinity, Integer> affinities = new HashMap<>(20);
    private Map<ResourceLocation, Integer> knownIngredients = new HashMap<>(20); // Used as a Set for searching capabilities.
    private boolean deathState = false;

    // Non-Synced Data
    private float thresholdTolGoal = 100;

    public ToxData(Player player){
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    // |========================= Tox Data =========================|

    public float getTox(){
        return tox;
    }

    public void setTox(float inTox){
        boolean isServerPlayer = this.player instanceof ServerPlayer;

        ChangeToxEvent event = new ChangeToxEvent(isServerPlayer ? player : null, this, this.tox, inTox);
        if (NeoForge.EVENT_BUS.post(event).isCanceled() && isServerPlayer)return;

        if (event.getNewTox() > tolerance) {
            event.setNewTox(tolerance);
            setDeathState(true);
        }
        if (event.getNewTox() < 0) {
            event.setNewTox(0);
        }

        this.tox = event.getNewTox();

        // After setting tox, check if current threshold goal has been reached!
        // While loop to check if multiple thresholds were reached at once.
        while(this.tox > thresholdTolGoal){
            setThreshold(this.threshold + 1);
        }
    }

    public void addTox(float inTox){
        setTox(this.tox + inTox);
    }

    // |========================= Tolerance Data =========================|

    public float getTolerance(){
        return tolerance;
    }

    public void setTolerance(float inTolerance){
        boolean isServerPlayer = this.player instanceof ServerPlayer;

        ChangeToleranceEvent event = new ChangeToleranceEvent(isServerPlayer ? player : null, this, this.tox, inTolerance);
        if (NeoForge.EVENT_BUS.post(event).isCanceled() && isServerPlayer)return;

        if (event.getNewTolerance() > MAX_TOLERANCE) {
            event.setNewTolerance(MAX_TOLERANCE);
        }
        if (event.getNewTolerance() < MIN_TOLERANCE) {
            event.setNewTolerance(MIN_TOLERANCE);
        }
        this.tolerance = event.getNewTolerance();
    }

    public void addTolerance(float inTolerance){
        setTolerance(tolerance + inTolerance);
    }

    // |========================= Threshold Data =========================|

    public int getThreshold(){
        return threshold;
    }

    public float getThresholdTolGoal(){
        return thresholdTolGoal;
    }

    public void setThreshold(int inThreshold){
        boolean isServerPlayer = this.player instanceof ServerPlayer;

        ChangeThresholdEvent event = new ChangeThresholdEvent(isServerPlayer ? player : null, this, this.threshold, inThreshold);
        if (NeoForge.EVENT_BUS.post(event).isCanceled() && isServerPlayer)return;

        this.threshold = event.getNewThreshold();
        this.thresholdTolGoal = ToxUtil.TriangularNumbersMult(this.threshold, 100);
    }

    public void resetThreshold(){
        setThreshold(0);
    }

    // |========================= DeathState Data =========================|

    public void setDeathState(boolean setState){
        this.deathState = setState;
    }

    public boolean getDeathState(){
        return deathState;
    }

    // |========================= Affinity Data =========================|

    public Map<Affinity, Integer> getAffinities(){
        return affinities;
    }

    public void setAffinities(Map<Affinity, Integer> affinities){
        this.affinities = affinities;
    }

    public Integer getAffinityAmount(Affinity affinity){
        if(!this.haveAffinity(affinity)) return -1;
        return affinities.get(affinity);
    }

    public void setAffinity(Affinity affinity, int value){
        if(!this.haveAffinity(affinity)){
            affinities.put(affinity, value);
            return;
        }
        affinities.replace(affinity, value);
    }

    public void addAffinity(Affinity affinity, int value){
        if(!this.haveAffinity(affinity)){
            affinities.put(affinity, value);
            return;
        }
        this.setAffinity(affinity, affinities.get(affinity) + value);
    }

    public boolean haveAffinity(Affinity affinity){
        return affinities.containsKey(affinity);
    }

    // |========================= Known Ingredients Data =========================|

    public void addKnownIngredients(ItemStack itemstack){
        ResourceLocation resourceLocation = itemstack.getItemHolder().getKey().location();
        if(!this.hasKnownIngredient(itemstack)){
            this.knownIngredients.put(resourceLocation, 1);
            return;
        }
        this.knownIngredients.replace(resourceLocation, knownIngredients.get(resourceLocation) + 1);
    }

    public void setKnownIngredients(Map<ResourceLocation, Integer> knownIngredients){
        this.knownIngredients = knownIngredients;
    }

    public Map<ResourceLocation, Integer> getKnownIngredients(){
        return knownIngredients;
    }

    public int getIngredientProgress(ItemStack itemstack){
        ResourceLocation location = itemstack.getItemHolder().getKey().location();
        if(!this.hasKnownIngredient(itemstack))return 0;
        return knownIngredients.get(location);
    }

    private boolean hasKnownIngredient(ItemStack itemstack){
        return knownIngredients.containsKey(itemstack.getItemHolder().getKey().location());
    }

    /**
     * Check if the player knows the random affinity chosen for this item stack.
     */
    public boolean knowsIngredient(ItemStack itemstack){
        if(!this.hasKnownIngredient(itemstack))return false;
        return (knownIngredients.get(itemstack.getItemHolder().getKey().location()) >= MINIMUM_KNOW);
    }

    public boolean knowsIngredient(Holder<Item> holder){
        if(!this.hasKnownIngredient(new ItemStack(holder)))return false;
        return (knownIngredients.get(holder.getKey().location()) >= MINIMUM_KNOW);
    }

    // Utility function to save and load NBT data. This is used for the PlayerToxSerializer.
    public void saveNBTData(CompoundTag compound, HolderLookup.Provider provider) {
        compound.putInt("tox", (int)this.tox);
        compound.putInt("tolerance", (int)this.tolerance);
        compound.putInt("threshold", this.threshold);


        ListTag affinityStringsList = new ListTag();
        List<Integer> affinityValuesList = new ArrayList<>();
        for(Affinity affinity : affinities.keySet()){
            affinityStringsList.add(StringTag.valueOf(
                    ToxonyRegistries.AFFINITY_REGISTRY
                            .holders()
                            .filter(ref -> Objects.equals(ref.value().getName(), affinity.getName()))
                            .findFirst()
                            .get().getKey()
                            .location()
                            .toString()
            ));
            affinityValuesList.add(affinities.get(affinity));
        }

        ListTag ingredientsStringsList = new ListTag();
        List<Integer> ingredientsValuesList = new ArrayList<>();
        for(ResourceLocation resourceLocation : knownIngredients.keySet()){
            ingredientsStringsList.add(StringTag.valueOf(resourceLocation.toString()));
            ingredientsValuesList.add(knownIngredients.get(resourceLocation));
        }

        compound.put("affinities", affinityStringsList);
        compound.putIntArray("affinity_values", affinityValuesList);
        compound.put("known_ingredients", ingredientsStringsList);
        compound.putIntArray("known_ingredients_values", ingredientsValuesList);

        compound.putBoolean("deathState", this.deathState);
    }

    public void loadNBTData(CompoundTag compound, HolderLookup.Provider provider) {
        tox = compound.getInt("tox");
        tolerance = compound.getInt("tolerance");
        threshold = compound.getInt("threshold");

        ListTag affinityStringsList = compound.getList("affinities", Tag.TAG_STRING);
        int[] affinityValues = compound.getIntArray("affinity_values");
        ListTag ingredientsStringsList = compound.getList("known_ingredients", Tag.TAG_STRING);
        int[] ingredientsValuesList = compound.getIntArray("known_ingredients_values");

        Map<Affinity, Integer> readAffinityMap = new HashMap<>();
        for (int i=0; i<affinityStringsList.size(); i++) {
            Affinity in = ToxonyRegistries.AFFINITY_REGISTRY.get(ResourceLocation.parse(affinityStringsList.getString(i)));
            assert in != null;
            readAffinityMap.put(in, affinityValues[i]);
        }
        Map<ResourceLocation, Integer> readIngredientsMap = new HashMap<>();
        for (int i=0; i<ingredientsStringsList.size(); i++) {
            readIngredientsMap.put(ResourceLocation.parse(ingredientsStringsList.getString(i)), ingredientsValuesList[i]);
        }

        affinities = readAffinityMap;
        knownIngredients = readIngredientsMap;
        deathState = compound.getBoolean("deathState");
    }
}