package xyz.yfrostyf.toxony.network;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.affinity.Affinity;
import xyz.yfrostyf.toxony.api.tox.ToxData;
import xyz.yfrostyf.toxony.api.client.ClientToxData;
import xyz.yfrostyf.toxony.registries.DataAttachmentRegistry;

import java.util.Map;

public class SyncToxPacket implements CustomPacketPayload {
    private ToxData plyToxData = null;

    int tox = 0;
    int tolerance = 10;
    int threshold = 0;
    Map<Integer, Integer> affinities = Affinity.initAffinities();
    boolean deathState = false;

    public static final Type<SyncToxPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "sync_tox"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncToxPacket> STREAM_CODEC = CustomPacketPayload.codec(
            SyncToxPacket::write,
            SyncToxPacket::new
    );


    public SyncToxPacket(ToxData plyToxData){
        this.plyToxData = plyToxData;
    }

    public SyncToxPacket(FriendlyByteBuf buf){
        tox = buf.readInt();
        tolerance = buf.readInt();
        threshold = buf.readInt();
        affinities = buf.readMap(FriendlyByteBuf::readInt, FriendlyByteBuf::readInt);
        deathState = buf.readBoolean();
    }

    public void write(FriendlyByteBuf buf){
        buf.writeInt((int)plyToxData.getTox());
        buf.writeInt((int)plyToxData.getTolerance());
        buf.writeInt(plyToxData.getThreshold());
        buf.writeMap(plyToxData.getAffinities(), FriendlyByteBuf::writeInt, FriendlyByteBuf::writeInt);
        buf.writeBoolean(plyToxData.getDeathState());
    }

    public static void handle(SyncToxPacket syncToxPacket, IPayloadContext context){
        context.enqueueWork(() -> {
            if(!(context.player() instanceof LocalPlayer player)){return;}

            ClientToxData.setTox(syncToxPacket.tox);
            ClientToxData.setTolerance(syncToxPacket.tolerance);
            ClientToxData.setThreshold(syncToxPacket.threshold);
            ClientToxData.setAffinities(syncToxPacket.affinities);
            ClientToxData.setDeathState(syncToxPacket.deathState);

            context.player().setData(DataAttachmentRegistry.TOX_DATA, ClientToxData.getToxData());

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
