package xyz.yfrostyf.toxony.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.mutagens.MutagenData;
import xyz.yfrostyf.toxony.registries.DataAttachmentRegistry;

import java.util.HashMap;
import java.util.Map;

public record SyncMutagenDataPacket(Map<String, Boolean> booleanMap, Map<String, Integer> integerMap) implements CustomPacketPayload {

    public static final Type<SyncMutagenDataPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "sync_mutagen_data"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncMutagenDataPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(HashMap::new, ByteBufCodecs.STRING_UTF8, ByteBufCodecs.BOOL, 256),
            SyncMutagenDataPacket::booleanMap,
            ByteBufCodecs.map(HashMap::new, ByteBufCodecs.STRING_UTF8, ByteBufCodecs.INT, 256),
            SyncMutagenDataPacket::integerMap,
            SyncMutagenDataPacket::new
    );

    public static SyncMutagenDataPacket create(MutagenData mutagenData){
        return new SyncMutagenDataPacket(mutagenData.getBooleanMap(), mutagenData.getIntegerMap());
    }

    public static void handle(SyncMutagenDataPacket packet, IPayloadContext context){
        context.enqueueWork(() -> {
            context.player().setData(DataAttachmentRegistry.MUTAGEN_DATA, new MutagenData(packet.booleanMap, packet.integerMap));

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
