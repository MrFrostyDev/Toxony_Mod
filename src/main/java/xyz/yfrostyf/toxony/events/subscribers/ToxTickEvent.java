package xyz.yfrostyf.toxony.events.subscribers;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.tox.ToxData;
import xyz.yfrostyf.toxony.network.SyncToxPacket;
import xyz.yfrostyf.toxony.registries.DataAttachmentRegistry;

@EventBusSubscriber(modid = ToxonyMain.MOD_ID)
public class ToxTickEvent {
    public static final int TOX_TICK = 80;

    @SubscribeEvent
    public static void onWorldTick(LevelTickEvent.Pre event) {
        if(event.getLevel().isClientSide())return;
        if(event.getLevel().getServer().getTickCount() % TOX_TICK != 0)return;

        event.getLevel().players().stream().toList().forEach(player -> {
            if (!(player instanceof ServerPlayer svplayer)){return;}

            ToxData plyToxData = svplayer.getData(DataAttachmentRegistry.TOX_DATA);

            if ((plyToxData.getTox() > 0) && !plyToxData.getDeathState()){
                plyToxData.addTox(-1);
                PacketDistributor.sendToPlayer(svplayer, SyncToxPacket.create(plyToxData.getTox()));
            }
        });
    }
}
