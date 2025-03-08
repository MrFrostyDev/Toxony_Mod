package xyz.yfrostyf.toxony.api.tox;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForge;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.affinity.Affinity;
import xyz.yfrostyf.toxony.api.events.ChangeThresholdEvent;
import xyz.yfrostyf.toxony.api.events.ChangeToleranceEvent;
import xyz.yfrostyf.toxony.api.events.ChangeToxEvent;
import xyz.yfrostyf.toxony.api.mutagens.MutagenEffect;
import xyz.yfrostyf.toxony.api.registries.ToxonyRegistries;
import xyz.yfrostyf.toxony.api.util.ToxUtil;

import java.util.*;

public class ToxData {
    // Heavily based on Irons-Spells-n-Spellbooks MagicData class
    // Thank you very much for the reference!

    public static final int MAX_TOLERANCE = 999;
    public static final int MAX_MUTAGENS = 3; // Be aware of the max tolerance as if that changes, this must as well.
    public static final int MIN_TOLERANCE = 10;
    public static final int DEFAULT_TOLERANCE = 30;
    public static final int MINIMUM_KNOW = 20;
    public static final int THRESHOLD_MULTIPLIER = 100;

    private Player player;

    // Synced Data
    private float tox = 0;
    private float tolerance = DEFAULT_TOLERANCE;
    private int threshold = 0;
    private Map<Affinity, Integer> affinities = new HashMap<>(20);
    private List<Holder<MobEffect>> mutagens = new ArrayList<>(MAX_MUTAGENS);
    private Map<ResourceLocation, Integer> knownIngredients = new HashMap<>(30); // Used as a Set for searching capabilities.
    private boolean deathState = false;

    // Non-Synced Data
    private float thresholdTolGoal = 100;

    public ToxData(Player player){
        this.player = player;
    }

    public Player getPlayer(){
        return this.player;
    }

    /**
    *   Wrapper method for data sync methods when handling ToxData server packet information to client.
    **/
    @OnlyIn(Dist.CLIENT)
    public void handleSyncedToxData(float tox,
                              float tolerance,
                              int threshold,
                              Map<Affinity, Integer> affinities,
                              List<Holder<MobEffect>> mutagens,
                              Map<ResourceLocation, Integer> knownIngredients,
                              boolean deathState) {
        this.setToxSynced(tox);
        this.setToleranceSynced(tolerance);
        this.setThresholdSynced(threshold);
        this.setAffinities(affinities);
        this.setMutagens(mutagens);
        this.knownIngredients = knownIngredients;
        this.setDeathState(deathState);
    }

    // |========================= Tox Data =========================|

    public float getTox(){
        return tox;
    }

    public void setToxSynced(float inTox){
        if (inTox > tolerance) {
            this.tox = tolerance;
            this.setDeathState(true);
        }
        else if (inTox <= 0) {
            this.tox = 0;
            this.resetThreshold();
            this.clearMutagens();
        }
        else{
            this.tox = inTox;
        }

        while(this.tox > thresholdTolGoal){
            this.setThresholdSynced(this.threshold + 1);
        }
    }

    public void setTox(float inTox){
        ChangeToxEvent event = new ChangeToxEvent(player, this, this.tox, inTox);
        if (NeoForge.EVENT_BUS.post(event).isCanceled())return;

        if (event.getNewTox() > tolerance) {
            event.setNewTox(tolerance);
            this.setDeathState(true);
        }
        else if (event.getNewTox() <= 0) {
            event.setNewTox(0);
            this.resetThreshold();
            this.clearMutagens();
        }

        this.tox = event.getNewTox();

        // After setting tox, check if current threshold goal has been reached!
        // While loop to check if multiple thresholds were reached at once.
        while(this.tox > this.thresholdTolGoal){
            this.setThreshold(this.threshold + 1);
        }
    }

    public void addTox(float inTox){
        setTox(this.tox + inTox);
    }

    // |========================= Tolerance Data =========================|

    public float getTolerance(){
        return tolerance;
    }

    private void setToleranceSynced(float inTolerance){
        if (inTolerance > MAX_TOLERANCE) {
            this.tolerance = MAX_TOLERANCE;
        }
        else if (inTolerance < MIN_TOLERANCE) {
            this.tolerance = MIN_TOLERANCE;
        }
        else{
            this.tolerance = inTolerance;
        }
    }

    public void setTolerance(float inTolerance){
        ChangeToleranceEvent event = new ChangeToleranceEvent(player, this, this.tox, inTolerance);
        if (NeoForge.EVENT_BUS.post(event).isCanceled())return;

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

    private void setThresholdSynced(int inThreshold){
        this.threshold = inThreshold;
        this.thresholdTolGoal = ToxUtil.TriangularNumbersMult(this.threshold, THRESHOLD_MULTIPLIER);
    }

    public void setThreshold(int inThreshold){
        ChangeThresholdEvent event = new ChangeThresholdEvent(player, this, this.threshold, inThreshold);
        if (NeoForge.EVENT_BUS.post(event).isCanceled())return;

        this.threshold = event.getNewThreshold();
        this.thresholdTolGoal = ToxUtil.TriangularNumbersMult(this.threshold, THRESHOLD_MULTIPLIER);
    }

    public void resetThreshold(){
        this.threshold = 0;
        this.thresholdTolGoal = 100;
    }

    // |========================= DeathState Data =========================|

    public void setDeathState(boolean setState){
        this.deathState = setState;
    }

    public boolean getDeathState(){
        return deathState;
    }

    // |========================= Mutagens Data =========================|

    public List<Holder<MobEffect>> getMutagens() {
        return mutagens;
    }

    public void addMutagen(Holder<MobEffect> effect) {
        if(this.mutagens.size() == MAX_MUTAGENS){
            this.mutagens.remove(0);
        }
        this.mutagens.add(effect);
    }

    public void setMutagens(List<Holder<MobEffect>> mutagens) {
        this.mutagens = mutagens;
    }

    // Remove active mutagens and add new mutagens from this data
    public void applyMutagens(){
        List<MobEffectInstance> activeEffects = new ArrayList<>(player.getActiveEffects());
        for(MobEffectInstance effectInstance : activeEffects){
            if (effectInstance.getEffect().value() instanceof MutagenEffect){
                player.removeEffectNoUpdate(effectInstance.getEffect());
            }
        }
        for(Holder<MobEffect> effect : mutagens){
            ToxUtil.applyMutagenEffect(player, BuiltInRegistries.MOB_EFFECT.wrapAsHolder(effect.value()));
        }
        ToxonyMain.LOGGER.info("[applyMutagens]: mutagens: {}", mutagens);
    }

    public void addAndApplyMutagens(Iterable<Holder<MobEffect>> effects){
        for(Holder<MobEffect> effect : effects){
            this.addMutagen(effect);
        }
        this.applyMutagens();
    }

    public void clearMutagens(){
        this.mutagens.clear();
        this.applyMutagens();
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
        addKnownIngredients(itemstack, 1);
    }

    public void addKnownIngredients(ItemStack itemstack, int amount){
        ResourceLocation resourceLocation = itemstack.getItemHolder().getKey().location();
        this.knownIngredients.merge(resourceLocation, amount, Integer::sum);
    }

    public Map<ResourceLocation, Integer> getKnownIngredients(){
        return knownIngredients;
    }

    public int getIngredientProgress(ItemStack itemstack){
        ResourceLocation location = itemstack.getItemHolder().getKey().location();
        return knownIngredients.getOrDefault(location, 0);
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

        ListTag mutagenStringList = new ListTag();
        for(Holder<MobEffect> effect : mutagens){
            mutagenStringList.add(StringTag.valueOf(
                    BuiltInRegistries.MOB_EFFECT
                            .holders()
                            .filter(ref -> Objects.equals(ref.value(), effect.value()))
                            .findFirst()
                            .get().getKey()
                            .location()
                            .toString()
            ));
        }

        ListTag ingredientsStringsList = new ListTag();
        List<Integer> ingredientsValuesList = new ArrayList<>();
        for(ResourceLocation resourceLocation : knownIngredients.keySet()){
            ingredientsStringsList.add(StringTag.valueOf(resourceLocation.toString()));
            ingredientsValuesList.add(knownIngredients.get(resourceLocation));
        }

        compound.put("affinities", affinityStringsList);
        compound.putIntArray("affinity_values", affinityValuesList);
        compound.put("mutagens", mutagenStringList);
        compound.put("known_ingredients", ingredientsStringsList);
        compound.putIntArray("known_ingredients_values", ingredientsValuesList);

        compound.putBoolean("deathState", this.deathState);
    }

    public void loadNBTData(CompoundTag compound, HolderLookup.Provider provider) {
        tox = compound.getInt("tox");
        tolerance = compound.getInt("tolerance");
        threshold = compound.getInt("threshold");
        thresholdTolGoal = ToxUtil.TriangularNumbersMult(threshold, THRESHOLD_MULTIPLIER);

        ListTag affinityStringsList = compound.getList("affinities", Tag.TAG_STRING);
        int[] affinityValues = compound.getIntArray("affinity_values");
        ListTag mutagenStringList = compound.getList("mutagens", Tag.TAG_STRING);
        ListTag ingredientsStringsList = compound.getList("known_ingredients", Tag.TAG_STRING);
        int[] ingredientsValuesList = compound.getIntArray("known_ingredients_values");

        Map<Affinity, Integer> readAffinityMap = new HashMap<>();
        for (int i=0; i<affinityStringsList.size(); i++) {
            Affinity in = ToxonyRegistries.AFFINITY_REGISTRY.get(ResourceLocation.parse(affinityStringsList.getString(i)));
            assert in != null;
            readAffinityMap.put(in, affinityValues[i]);
        }

        List<Holder<MobEffect>> mutagensList = new ArrayList<>();
        for (int i=0; i<mutagenStringList.size(); i++) {
            Holder<MobEffect> in = BuiltInRegistries.MOB_EFFECT.getHolder(ResourceLocation.parse(mutagenStringList.getString(i))).get();
            mutagensList.add(in);
        }
        Map<ResourceLocation, Integer> readIngredientsMap = new HashMap<>();
        for (int i=0; i<ingredientsStringsList.size(); i++) {
            readIngredientsMap.put(ResourceLocation.parse(ingredientsStringsList.getString(i)), ingredientsValuesList[i]);
        }

        affinities = readAffinityMap;
        mutagens = mutagensList;
        knownIngredients = readIngredientsMap;
        deathState = compound.getBoolean("deathState");
    }

    @Override
    public String toString() {
        String strply = "Player: " + player.toString() + ",\n";
        String message = strply + "tox: "+tox+"\n"
                + "tolerance: "+tolerance+"\n"
                + "threshold: "+threshold+"\n"
                + "affinities: "+affinities+"\n"
                + "knownIngredients: "+knownIngredients+"\n"
                + "deathstate: "+deathState+"\n";
        return message;
    }
}