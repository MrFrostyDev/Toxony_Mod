package xyz.yfrostyf.toxony.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.client.ClientToxData;
import xyz.yfrostyf.toxony.api.tox.ToxData;
import xyz.yfrostyf.toxony.registries.DataAttachmentRegistry;

public record SyncToxDataPacket(ToxData toxData) implements CustomPacketPayload {

    public static final Type<SyncToxDataPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "sync_tox_data"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncToxDataPacket> STREAM_CODEC = StreamCodec.composite(
            ToxData.STREAM_CODEC, SyncToxDataPacket::toxData,
            SyncToxDataPacket::new
    );

    public static SyncToxDataPacket create(ToxData plyToxData){
        return new SyncToxDataPacket(plyToxData);
    }

    public static void handle(SyncToxDataPacket packet, IPayloadContext context){
        context.enqueueWork(() -> {
            ToxData toxData = packet.toxData;

            ClientToxData.changeToxData(
                    toxData.getTox(),
                    toxData.getTolerance(),
                    toxData.getThreshold(),
                    toxData.getAffinities(),
                    toxData.getMutagens(),
                    toxData.getKnownIngredients(),
                    toxData.getDeathState()
            );

            ToxData clientToxData = ClientToxData.getToxData();
            context.player().setData(DataAttachmentRegistry.TOX_DATA, clientToxData);

            ToxonyMain.LOGGER.debug("[New Client ToxData]: \nTox: {}, \nTol: {}, \nThreshold: {}, \nAffinities: {}, \nMutagens: {}, \nKnownIngreds: {}, \nDeathstate: {}",
                    clientToxData.getTox(),
                    clientToxData.getTolerance(),
                    clientToxData.getThreshold(),
                    clientToxData.getAffinities(),
                    clientToxData.getMutagens(),
                    clientToxData.getKnownIngredients(),
                    clientToxData.getDeathState());

        }).exceptionally(e -> {
            // Handle exception
            context.disconnect(Component.translatable("toxony.networking.sync_tox_data.failed", e.getMessage()));
            return null;
        });

    }

    @Override
    public Type<? extends CustomPacketPayload> type(){
        return TYPE;
    }
}
