package xyz.yfrostyf.toxony.api.events;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import xyz.yfrostyf.toxony.api.tox.ToxData;

public class ChangeToxEvent extends PlayerEvent implements ICancellableEvent {
    private final ToxData toxData;
    private final float oldTox;
    private float newTox;

    public ChangeToxEvent(Player player, ToxData toxData, float oldTox, float newTox) {
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
    public Player getEntity() {
        return super.getEntity();
    }
}