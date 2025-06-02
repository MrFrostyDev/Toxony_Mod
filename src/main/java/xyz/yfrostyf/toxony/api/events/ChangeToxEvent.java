package xyz.yfrostyf.toxony.api.events;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import xyz.yfrostyf.toxony.api.tox.ToxData;

public class ChangeToxEvent extends LivingEvent implements ICancellableEvent {
    private final ToxData toxData;
    private final float oldTox;
    private float newTox;

    public ChangeToxEvent(LivingEntity player, ToxData toxData, float oldTox, float newTox) {
        super(player);
        this.toxData = toxData;
        this.oldTox = oldTox;
        this.newTox = newTox;
    }

    public ToxData getToxData() {
        return toxData;
    }

    public float getOldTox() {
        return oldTox;
    }

    public float getNewTox() {
        return newTox;
    }

    public void setNewTox(float newTox) {
        this.newTox = newTox;
    }

    public boolean isAdding(){
        if((oldTox < newTox)){
            return true;
        }
        return false;
    }

    @Override
    public LivingEntity getEntity() {
        return super.getEntity();
    }
}