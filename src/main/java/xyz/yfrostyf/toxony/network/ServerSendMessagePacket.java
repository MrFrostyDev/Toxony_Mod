package xyz.yfrostyf.toxony.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import xyz.yfrostyf.toxony.ToxonyMain;

public record ServerSendMessagePacket(String componentID) implements CustomPacketPayload {
    public static final Type<ServerSendMessagePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "send_message"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerSendMessagePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ServerSendMessagePacket::componentID,
            ServerSendMessagePacket::new
    );

    public static ServerSendMessagePacket create(String componentID){
        return new ServerSendMessagePacket(componentID);
    }

    public static void handle(ServerSendMessagePacket messagePacket, IPayloadContext context){
        context.enqueueWork(() -> {
            if(!context.player().level().isClientSide())return;
            Minecraft.getInstance().gui.setOverlayMessage(
                    Component.translatable(messagePacket.componentID),
                    false
            );
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type(){
        return TYPE;
    }
}
