package xyz.yfrostyf.toxony.api.events;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import xyz.yfrostyf.toxony.api.tox.ToxData;

public class ChangeToleranceEvent extends LivingEvent implements ICancellableEvent {
    private final ToxData toxData;
    private final float oldTolerance;
    private float newTolerance;

    public ChangeToleranceEvent(LivingEntity entity, ToxData toxData, float oldTox, float newTox) {
        super(entity);
        this.toxData = toxData;
        this.oldTolerance = oldTox;
        this.newTolerance = newTox;
    }

    public ToxData getToxData() {
        return toxData;
    }

    public float getOldTolerance() {
        return oldTolerance;
    }

    public float getNewTolerance() {
        return newTolerance;
    }

    public void setNewTolerance(float newTox) {
        this.newTolerance = newTox;
    }

    @Override
    public LivingEntity getEntity() {
        return super.getEntity();
    }
}