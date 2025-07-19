package xyz.yfrostyf.toxony.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.client.gui.journal.JournalUtil;

public class ServerStartLostJournal implements CustomPacketPayload {
    public static final Type<ServerStartLostJournal> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "server_start_lost_journal"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerStartLostJournal> STREAM_CODEC = CustomPacketPayload.codec(
            ServerStartLostJournal::write,
            ServerStartLostJournal::new
    );

    public ServerStartLostJournal(){}

    public ServerStartLostJournal(FriendlyByteBuf buf){}

    public void write(FriendlyByteBuf buf){}

    public static void handle(ServerStartLostJournal messagePacket, IPayloadContext context){
        context.enqueueWork(() -> {
            if(!context.player().level().isClientSide())return;
            JournalUtil.startPage();
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type(){
        return TYPE;
    }
}
