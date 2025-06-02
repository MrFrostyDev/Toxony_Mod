package xyz.yfrostyf.toxony.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.client.gui.NightPredatorOverlay;

public record ServerNightPredatorPacket(boolean isStarting) implements CustomPacketPayload {
    public static final Type<ServerNightPredatorPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "night_predator"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerNightPredatorPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, ServerNightPredatorPacket::isStarting,
            ServerNightPredatorPacket::new
    );

    public static ServerNightPredatorPacket create(boolean isStarting){
        return new ServerNightPredatorPacket(isStarting);
    }

    public static void handle(ServerNightPredatorPacket packet, IPayloadContext context){
        context.enqueueWork(() -> {
            if(!context.player().level().isClientSide())return;
            if (packet.isStarting) {
                NightPredatorOverlay.startAnimation();
            } else {
                NightPredatorOverlay.endAnimation();
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type(){
        return TYPE;
    }
}
