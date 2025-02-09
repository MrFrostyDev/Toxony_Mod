package xyz.yfrostyf.toxony.api.events;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import xyz.yfrostyf.toxony.api.tox.ToxData;

public class ChangeThresholdEvent extends PlayerEvent implements ICancellableEvent {
    private final ToxData toxData;
    private final int oldThreshold;
    private int newThreshold;

    public ChangeThresholdEvent(Player player, ToxData toxData, int oldThreshold, int newThreshold) {
        super(player);
        this.toxData = toxData;
        this.oldThreshold = oldThreshold;
        this.newThreshold = newThreshold;
    }

    public ToxData getToxData() {
        return toxData;
    }

    public int getOldThreshold() {
        return oldThreshold;
    }

    public int getNewThreshold() {
        return newThreshold;
    }

    public void setNewThreshold(int newThreshold) {
        this.newThreshold = newThreshold;
    }

    public boolean isAdding(){
        if((oldThreshold < newThreshold)){
            return true;
        }
        return false;
    }

    @Override
    public Player getEntity() {
        return super.getEntity();
    }
}