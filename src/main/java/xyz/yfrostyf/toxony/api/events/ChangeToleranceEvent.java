package xyz.yfrostyf.toxony.api.events;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import xyz.yfrostyf.toxony.api.tox.ToxData;

public class ChangeToleranceEvent extends PlayerEvent implements ICancellableEvent {
    private final ToxData toxData;
    private final float oldTolerance;
    private float newTolerance;

    public ChangeToleranceEvent(Player player, ToxData toxData, float oldTox, float newTox) {
        super(player);
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
    public Player getEntity() {
        return super.getEntity();
    }
}