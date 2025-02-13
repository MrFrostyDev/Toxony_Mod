package xyz.yfrostyf.toxony.network;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.affinity.Affinity;
import xyz.yfrostyf.toxony.api.tox.ToxData;
import xyz.yfrostyf.toxony.api.client.ClientToxData;
import xyz.yfrostyf.toxony.registries.DataAttachmentRegistry;

import java.util.HashMap;
import java.util.Map;

public record SyncToxPacket(int tox, int tolerance, int threshold, Map<Affinity, Integer> affinities, Map<ResourceLocation, Integer> knownIngredients, boolean deathState) implements CustomPacketPayload {

    public static final Type<SyncToxPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "sync_tox"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncToxPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SyncToxPacket::tox,
            ByteBufCodecs.INT, SyncToxPacket::tolerance,
            ByteBufCodecs.INT, SyncToxPacket::threshold,
            ByteBufCodecs.map(HashMap::new, Affinity.STREAM_CODEC, ByteBufCodecs.INT), SyncToxPacket::affinities,
            ByteBufCodecs.map(HashMap::new, ResourceLocation.STREAM_CODEC, ByteBufCodecs.INT), SyncToxPacket::knownIngredients,
            ByteBufCodecs.BOOL, SyncToxPacket::deathState,
            SyncToxPacket::new
    );

    public static SyncToxPacket create(ToxData plyToxData){
        return new SyncToxPacket((int)plyToxData.getTox(),
                (int)plyToxData.getTolerance(),
                plyToxData.getThreshold(),
                plyToxData.getAffinities(),
                plyToxData.getKnownIngredients(),
                plyToxData.getDeathState());
    }
    public static void handle(SyncToxPacket syncToxPacket, IPayloadContext context){
        context.enqueueWork(() -> {
            if(!(context.player() instanceof LocalPlayer player)){return;}

            ClientToxData.setTox(syncToxPacket.tox);
            ClientToxData.setTolerance(syncToxPacket.tolerance);
            ClientToxData.setThreshold(syncToxPacket.threshold);
            ClientToxData.setAffinities(syncToxPacket.affinities);
            ClientToxData.setKnownIngredients(syncToxPacket.knownIngredients);
            ClientToxData.setDeathState(syncToxPacket.deathState);

            context.player().setData(DataAttachmentRegistry.TOX_DATA, ClientToxData.getToxData());

        }).exceptionally(e -> {
            // Handle exception
            context.disconnect(Component.translatable("toxony.networking.sync_tox.failed", e.getMessage()));
            return null;
        });

    }

//    public SyncToxPacket(FriendlyByteBuf buf){
//        tox = buf.readInt();
//        tolerance = buf.readInt();
//        threshold = buf.readInt();
//        affinities = buf.readMap(FriendlyByteBuf::read, FriendlyByteBuf::readInt);
//        deathState = buf.readBoolean();
//    }
//
//    public void write(FriendlyByteBuf buf){
//        buf.writeInt((int)plyToxData.getTox());
//        buf.writeInt((int)plyToxData.getTolerance());
//        buf.writeInt(plyToxData.getThreshold());
//
//        Collection<String> resourceLocations = new ArrayList<>();
//        List<Integer> values = new ArrayList<>();
//        for(Map.Entry<Affinity, Integer> entry : plyToxData.getKnownAffinities().entrySet()){
//            resourceLocations.add(ToxonyRegistries.AFFINITY_REGISTRY.getKey(entry.getKey()).toString());
//            values.add(entry.getValue());
//        }
//
//        buf.writeCollection(resourceLocations, FriendlyByteBuf::writeBytes);
//        buf.writeVarIntArray(values.stream().mapToInt(i->i).toArray());
//
//
//        buf.writeBoolean(plyToxData.getDeathState());
//    }

    @Override
    public Type<? extends CustomPacketPayload> type(){
        return TYPE;
    }
}
