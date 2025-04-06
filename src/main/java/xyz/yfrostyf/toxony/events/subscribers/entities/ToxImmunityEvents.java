package xyz.yfrostyf.toxony.events.subscribers.entities;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.tox.ToxData;
import xyz.yfrostyf.toxony.registries.DataAttachmentRegistry;
import xyz.yfrostyf.toxony.registries.MobEffectRegistry;

@EventBusSubscriber(modid = ToxonyMain.MOD_ID)
public class ToxImmunityEvents {

    @SubscribeEvent
    public static void onEffectAddedWhileTox(MobEffectEvent.Added event){
        MobEffectInstance effectInst = event.getEffectInstance();

        if(effectInst.is(MobEffects.POISON) || effectInst.is(MobEffectRegistry.TOXIN)){
            if(event.getEntity().hasData(DataAttachmentRegistry.TOX_DATA)){
                ToxData data = event.getEntity().getData(DataAttachmentRegistry.TOX_DATA);
                if(data.getThreshold() < 1 || effectInst.isInfiniteDuration()) return;

                int duration = event.getEffectInstance().getDuration();
                duration = switch (data.getThreshold()){
                    case 2 -> duration / 2;
                    case 3 -> 0;
                    default -> duration;
                };

                effectInst.setDetailsFrom(new MobEffectInstance(effectInst.getEffect(), duration, effectInst.getAmplifier()));
            }
        }
    }
}
