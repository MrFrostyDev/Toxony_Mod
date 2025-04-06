package xyz.yfrostyf.toxony.events.subscribers.entities.player;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.tox.ToxData;
import xyz.yfrostyf.toxony.api.util.AffinityUtil;
import xyz.yfrostyf.toxony.network.SyncIngredientAffinityMapPacket;
import xyz.yfrostyf.toxony.network.SyncToxDataPacket;
import xyz.yfrostyf.toxony.registries.DataAttachmentRegistry;

@EventBusSubscriber(modid = ToxonyMain.MOD_ID)
public class ServerPlayerEvents {

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer svplayer) {
            ToxData plyToxData = svplayer.getData(DataAttachmentRegistry.TOX_DATA);
            PacketDistributor.sendToPlayer(svplayer, SyncToxDataPacket.create(plyToxData));
            PacketDistributor.sendToPlayer(svplayer, SyncIngredientAffinityMapPacket.create(AffinityUtil.getIngredientAffinityMap(svplayer.level())));
        }
    }
}
