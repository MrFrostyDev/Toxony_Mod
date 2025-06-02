package xyz.yfrostyf.toxony.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.registries.DataAttachmentRegistry;


public record SyncMobToxDataPacket(int entityID, float toxin) implements CustomPacketPayload {

    public static final Type<SyncMobToxDataPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "sync_mob_tox_data"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncMobToxDataPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SyncMobToxDataPacket::entityID,
            ByteBufCodecs.FLOAT, SyncMobToxDataPacket::toxin,
            SyncMobToxDataPacket::new
    );

    public static SyncMobToxDataPacket create(int entityID, float toxin){
        return new SyncMobToxDataPacket(entityID, toxin);
    }

    public static void handle(SyncMobToxDataPacket packet, IPayloadContext context){
        context.enqueueWork(() -> {
            Level level = context.player().level();

            Entity entity = level.getEntity(packet.entityID);
            if(entity instanceof LivingEntity livingEntity) {
                livingEntity.setData(DataAttachmentRegistry.MOB_TOXIN, packet.toxin);
            }

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
