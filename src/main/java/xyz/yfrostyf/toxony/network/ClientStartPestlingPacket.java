package xyz.yfrostyf.toxony.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.client.gui.MortarPestleMenu;

public class ClientStartPestlingPacket implements CustomPacketPayload {
    public static final Type<ClientStartPestlingPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "start_pestling"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientStartPestlingPacket> STREAM_CODEC = CustomPacketPayload.codec(
            ClientStartPestlingPacket::write,
            ClientStartPestlingPacket::new
    );


    public ClientStartPestlingPacket(){}

    public ClientStartPestlingPacket(FriendlyByteBuf buf){}

    public void write(FriendlyByteBuf buf){}

    public static void handle(ClientStartPestlingPacket pestlingPacket, IPayloadContext context){
        context.enqueueWork(() -> {
            if(!(context.player() instanceof ServerPlayer svplayer))return;
            if(!(svplayer.containerMenu instanceof MortarPestleMenu menu))return;
            if(menu.blockEntity.getResultItem() == ItemStack.EMPTY)return;

            menu.blockEntity.startPestling();
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type(){
        return TYPE;
    }
}
