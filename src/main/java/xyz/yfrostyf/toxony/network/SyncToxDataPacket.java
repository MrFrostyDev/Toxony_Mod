package xyz.yfrostyf.toxony.network;

import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.affinity.Affinity;
import xyz.yfrostyf.toxony.api.client.ClientToxData;
import xyz.yfrostyf.toxony.api.tox.ToxData;
import xyz.yfrostyf.toxony.registries.DataAttachmentRegistry;

import java.util.*;

public record SyncToxDataPacket(int tox, int tolerance, int threshold, Map<Affinity, Integer> affinities, List<Holder<MobEffect>> mutagens, Map<ResourceLocation, Integer> knownIngredients, boolean deathState) implements CustomPacketPayload {

    public static final Type<SyncToxDataPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "sync_tox_data"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncToxDataPacket> STREAM_CODEC = NeoForgeStreamCodecs.composite(
            ByteBufCodecs.INT, SyncToxDataPacket::tox,
            ByteBufCodecs.INT, SyncToxDataPacket::tolerance,
            ByteBufCodecs.INT, SyncToxDataPacket::threshold,
            ByteBufCodecs.map(HashMap::new, Affinity.STREAM_CODEC, ByteBufCodecs.INT), SyncToxDataPacket::affinities,
            MobEffect.STREAM_CODEC.apply(ByteBufCodecs.list()), SyncToxDataPacket::mutagens,
            ByteBufCodecs.map(HashMap::new, ResourceLocation.STREAM_CODEC, ByteBufCodecs.INT), SyncToxDataPacket::knownIngredients,
            ByteBufCodecs.BOOL, SyncToxDataPacket::deathState,
            SyncToxDataPacket::new
    );

    public static SyncToxDataPacket create(ToxData plyToxData){
        return new SyncToxDataPacket((int)plyToxData.getTox(),
                (int)plyToxData.getTolerance(),
                plyToxData.getThreshold(),
                plyToxData.getAffinities(),
                plyToxData.getMutagens(),
                plyToxData.getKnownIngredients(),
                plyToxData.getDeathState());
    }

    public static void handle(SyncToxDataPacket syncToxPacket, IPayloadContext context){
        context.enqueueWork(() -> {
            context.player().getData(DataAttachmentRegistry.TOX_DATA);

            ClientToxData.changeToxData(
                    syncToxPacket.tox,
                    syncToxPacket.tolerance,
                    syncToxPacket.threshold,
                    syncToxPacket.affinities,
                    syncToxPacket.mutagens,
                    syncToxPacket.knownIngredients,
                    syncToxPacket.deathState
            );
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
