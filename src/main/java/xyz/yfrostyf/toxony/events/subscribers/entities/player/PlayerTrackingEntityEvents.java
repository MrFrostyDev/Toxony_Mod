package xyz.yfrostyf.toxony.events.subscribers.entities.player;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.network.SyncMobToxDataPacket;
import xyz.yfrostyf.toxony.registries.DataAttachmentRegistry;


@EventBusSubscriber(modid = ToxonyMain.MOD_ID)
public class PlayerTrackingEntityEvents {
    @SubscribeEvent
    public static void onEntityStartTracking(PlayerEvent.StartTracking event) {
        if(!event.getEntity().isAlive()) return;
        if(!event.getEntity().level().isClientSide()){
            if(event.getTarget() instanceof LivingEntity livingEntity && livingEntity.hasData(DataAttachmentRegistry.MOB_TOXIN)){
                float toxin = livingEntity.getData(DataAttachmentRegistry.MOB_TOXIN);
                PacketDistributor.sendToPlayersTrackingEntity(event.getTarget(), SyncMobToxDataPacket.create(livingEntity.getId(), toxin));
            }
        }
    }
}
