package xyz.yfrostyf.toxony.events.subscribers.entities.player;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.tox.ToxData;
import xyz.yfrostyf.toxony.network.SyncToxDataPacket;
import xyz.yfrostyf.toxony.registries.DataAttachmentRegistry;

@EventBusSubscriber(modid = ToxonyMain.MOD_ID)
public class PlayerEvents {

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        ToxData plyToxData = event.getEntity().getData(DataAttachmentRegistry.TOX_DATA.get());

        if(!player.level().isClientSide()){
            PacketDistributor.sendToPlayer((ServerPlayer) player, SyncToxDataPacket.create(plyToxData));
        }

        plyToxData.applyMutagens();
    }
}
