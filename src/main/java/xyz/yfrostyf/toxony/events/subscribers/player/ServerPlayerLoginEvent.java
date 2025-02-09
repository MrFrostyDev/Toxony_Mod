package xyz.yfrostyf.toxony.events.subscribers.player;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.tox.ToxData;
import xyz.yfrostyf.toxony.network.SyncToxPacket;
import xyz.yfrostyf.toxony.registries.DataAttachmentRegistry;

@EventBusSubscriber(modid = ToxonyMain.MOD_ID, value = Dist.DEDICATED_SERVER)
public class ServerPlayerLoginEvent {

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer svplayer) {
            ToxData toxData = svplayer.getData(DataAttachmentRegistry.TOX_DATA);
            PacketDistributor.sendToPlayer(svplayer, new SyncToxPacket(toxData));
        }
    }
}
