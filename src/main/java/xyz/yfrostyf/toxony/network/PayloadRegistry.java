package xyz.yfrostyf.toxony.network;


import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import xyz.yfrostyf.toxony.ToxonyMain;

@EventBusSubscriber(modid = ToxonyMain.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class PayloadRegistry {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(ToxonyMain.MOD_ID)
                .versioned("1.0")
                .optional();

        registrar.playToClient(
                SyncToxPacket.TYPE,
                SyncToxPacket.STREAM_CODEC,
                SyncToxPacket::handle
        );

        registrar.playToClient(
                SyncToxDataPacket.TYPE,
                SyncToxDataPacket.STREAM_CODEC,
                SyncToxDataPacket::handle
        );

        registrar.playToClient(
                SyncMobToxDataPacket.TYPE,
                SyncMobToxDataPacket.STREAM_CODEC,
                SyncMobToxDataPacket::handle
        );

        registrar.playToClient(
                SyncIngredientAffinityMapPacket.TYPE,
                SyncIngredientAffinityMapPacket.STREAM_CODEC,
                SyncIngredientAffinityMapPacket::handle
        );

        registrar.playToClient(
                SyncMutagenDataPacket.TYPE,
                SyncMutagenDataPacket.STREAM_CODEC,
                SyncMutagenDataPacket::handle
        );

        registrar.playToClient(
                ServerSendMessagePacket.TYPE,
                ServerSendMessagePacket.STREAM_CODEC,
                ServerSendMessagePacket::handle
        );

        registrar.playToClient(
                ServerNightPredatorPacket.TYPE,
                ServerNightPredatorPacket.STREAM_CODEC,
                ServerNightPredatorPacket::handle
        );

        registrar.playToClient(
                ServerStartLostJournal.TYPE,
                ServerStartLostJournal.STREAM_CODEC,
                ServerStartLostJournal::handle
        );

        registrar.playToServer(
                ClientStartPestlingPacket.TYPE,
                ClientStartPestlingPacket.STREAM_CODEC,
                ClientStartPestlingPacket::handle
        );

        registrar.playToServer(
                ClientStartAlchemicalForgePacket.TYPE,
                ClientStartAlchemicalForgePacket.STREAM_CODEC,
                ClientStartAlchemicalForgePacket::handle
        );
    }
}
