package xyz.yfrostyf.toxony.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.affinity.Affinity;
import xyz.yfrostyf.toxony.client.ClientIngredientAffinityMapData;
import xyz.yfrostyf.toxony.data.world.IngredientAffinityMapData;

import java.util.HashMap;
import java.util.Map;

public record SyncIngredientAffinityMapPacket(Map<String, Affinity> map) implements CustomPacketPayload {

    public static final Type<SyncIngredientAffinityMapPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "sync_affinity_map"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncIngredientAffinityMapPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(HashMap::new, ByteBufCodecs.STRING_UTF8, Affinity.STREAM_CODEC),
            SyncIngredientAffinityMapPacket::map,
            SyncIngredientAffinityMapPacket::new
    );

    public static SyncIngredientAffinityMapPacket create(Map<ResourceLocation, Affinity> map){
        Map<String, Affinity> newMap = new HashMap<>();
        for(Map.Entry<ResourceLocation, Affinity> entry : map.entrySet()){
            newMap.put(entry.getKey().toString(), entry.getValue());
        }
        return new SyncIngredientAffinityMapPacket(newMap);
    }

    public static void handle(SyncIngredientAffinityMapPacket syncIngredientAffinityMapPacket, IPayloadContext context){
        context.enqueueWork(() -> {
            Map<ResourceLocation, Affinity> newMap = new HashMap<>();
            for(Map.Entry<String, Affinity> entry : syncIngredientAffinityMapPacket.map.entrySet()){
                newMap.put(ResourceLocation.parse(entry.getKey()), entry.getValue());
            }
            ClientIngredientAffinityMapData.setData(IngredientAffinityMapData.create(newMap));

        }).exceptionally(e -> {
            // Handle exception
            context.disconnect(Component.translatable("toxony.networking.sync_affinity_map.failed", e.getMessage()));
            return null;
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type(){
        return TYPE;
    }
}
