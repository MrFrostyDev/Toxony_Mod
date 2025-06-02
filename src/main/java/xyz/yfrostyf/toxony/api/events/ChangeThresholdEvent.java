package xyz.yfrostyf.toxony.api.events;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import xyz.yfrostyf.toxony.api.tox.ToxData;

public class ChangeThresholdEvent extends LivingEvent implements ICancellableEvent {
    private final ToxData toxData;
    private final int oldThreshold;
    private int newThreshold;

    public ChangeThresholdEvent(LivingEntity entity, ToxData toxData, int oldThreshold, int newThreshold) {
        super(entity);
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
    public LivingEntity getEntity() {
        return super.getEntity();
    }

    public boolean isPlayer(){
        return this.getEntity() instanceof Player;
    }
}