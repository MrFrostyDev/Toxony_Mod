package xyz.yfrostyf.toxony.network;


import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.bus.api.SubscribeEvent;
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
                SyncIngredientAffinityMapPacket.TYPE,
                SyncIngredientAffinityMapPacket.STREAM_CODEC,
                SyncIngredientAffinityMapPacket::handle
        );

        registrar.playToServer(
                ClientStartPestlingPacket.TYPE,
                ClientStartPestlingPacket.STREAM_CODEC,
                ClientStartPestlingPacket::handle
        );
    }
}
