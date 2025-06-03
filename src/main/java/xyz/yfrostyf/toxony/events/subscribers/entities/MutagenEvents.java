package xyz.yfrostyf.toxony.events.subscribers.entities;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.mutagens.MutagenEffect;

@EventBusSubscriber(modid = ToxonyMain.MOD_ID)
public class MutagenEvents {

    @SubscribeEvent
    public static void onMutagenRemoval(MobEffectEvent.Remove event){
        if(!(event.getEffect().value() instanceof MutagenEffect))return;
        if(event.getCure() != null){
            event.setCanceled(true);
        }
    }
}
