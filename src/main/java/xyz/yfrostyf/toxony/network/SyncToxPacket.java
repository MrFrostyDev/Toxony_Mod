package xyz.yfrostyf.toxony.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.client.ClientToxData;
import xyz.yfrostyf.toxony.registries.DataAttachmentRegistry;

public record SyncToxPacket(int tox) implements CustomPacketPayload {

    public static final Type<SyncToxPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "sync_tox"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncToxPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SyncToxPacket::tox,
            SyncToxPacket::new
    );

    public static SyncToxPacket create(float tox){
        return new SyncToxPacket((int)tox);
    }

    public static void handle(SyncToxPacket syncToxPacket, IPayloadContext context){
        context.enqueueWork(() -> {
            context.player().getData(DataAttachmentRegistry.TOX_DATA);
            ClientToxData.changeTox(syncToxPacket.tox);

        }).exceptionally(e -> {
            // Handle exception
            context.disconnect(Component.translatable("toxony.networking.sync_tox.failed", e.getMessage()));
            return null;
        });

    }

    @Override
    public Type<? extends CustomPacketPayload> type(){
        return TYPE;
    }
}
