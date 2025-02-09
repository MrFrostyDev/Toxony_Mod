package xyz.yfrostyf.toxony.api.tox;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForge;
import xyz.yfrostyf.toxony.api.affinity.Affinity;
import xyz.yfrostyf.toxony.api.events.ChangeThresholdEvent;
import xyz.yfrostyf.toxony.api.events.ChangeToleranceEvent;
import xyz.yfrostyf.toxony.api.events.ChangeToxEvent;
import xyz.yfrostyf.toxony.api.util.ToxUtil;

import java.util.HashMap;
import java.util.Map;

public class ToxData {

    // Heavily based on Irons-Spells-n-Spellbooks MagicData class
    // Thank you very much for the reference!

    public static final int MAX_TOLERANCE = 999;
    public static final int MIN_TOLERANCE = 10;
    public static final int DEFAULT_TOLERANCE = 30;

    private Player player;

    // Synced Data
    private float tox = 0;
    private float tolerance = DEFAULT_TOLERANCE;
    private int threshold = 0;
    private Map<Integer, Integer> affinities = Affinity.initAffinities();
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
            deathState = true;
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

    // |========================= Affinity Data =========================|

    public Map<Integer, Integer> getAffinities(){
        return affinities;
    }

    public void setAffinities(Map<Integer, Integer> inAffinities){
        affinities = inAffinities;
    }

    public void addAffinity(int affinity, int value){
        int oldValue = affinities.get(affinity);
        affinities.put(affinity, oldValue + value);
    }

    // Utility function to save and load NBT data. This is used for the PlayerToxSerializer.
    public void saveNBTData(CompoundTag compound, HolderLookup.Provider provider) {
        compound.putInt("tox", (int)this.tox);
        compound.putInt("tolerance", (int)this.tolerance);
        compound.putInt("threshold", this.threshold);
        compound.putIntArray("affinityKeys", affinities.keySet().stream().toList());
        compound.putIntArray("affinityValues", affinities.values().stream().toList());
        compound.putBoolean("deathState", this.deathState);
    }

    public void loadNBTData(CompoundTag compound, HolderLookup.Provider provider) {
        tox = compound.getInt("tox");
        tolerance = compound.getInt("tolerance");
        threshold = compound.getInt("threshold");
        int[] affinityKeys = compound.getIntArray("affinityKeys");
        int[] affinityValues = compound.getIntArray("affinityValues");

        Map<Integer, Integer> readMap = new HashMap<>();
        for (int i = 0; i < affinityKeys.length; i++) {
            readMap.put(affinityKeys[i], affinityValues[i]);
        }

        affinities = readMap;
        deathState = compound.getBoolean("deathState");
    }

    // |========================= DeathState Data =========================|

    public void setDeathState(boolean setState){
        deathState = setState;
    }

    public boolean getDeathState(){
        return deathState;
    }
}