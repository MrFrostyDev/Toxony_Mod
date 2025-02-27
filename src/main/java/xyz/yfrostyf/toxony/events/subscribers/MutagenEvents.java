package xyz.yfrostyf.toxony.events.subscribers;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.mutagens.MutagenEffect;
import xyz.yfrostyf.toxony.api.tox.ToxData;
import xyz.yfrostyf.toxony.registries.DataAttachmentRegistry;

@EventBusSubscriber(modid = ToxonyMain.MOD_ID)
public class MutagenEvents {

    @SubscribeEvent
    public static void onMutagenRemoval(MobEffectEvent.Remove event){
        if(!(event.getEffect().value() instanceof MutagenEffect))return;

        if(event.getEntity() instanceof Player player){
            ToxData plyToxData = player.getData(DataAttachmentRegistry.TOX_DATA.get());
            if(plyToxData.getThreshold() <= 0) return;
        }
        event.setCanceled(true);
    }
}
