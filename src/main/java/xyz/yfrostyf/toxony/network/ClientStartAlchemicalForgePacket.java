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
import xyz.yfrostyf.toxony.client.gui.AlchemicalForgeMenu;
import xyz.yfrostyf.toxony.client.gui.MortarPestleMenu;

public class ClientStartAlchemicalForgePacket implements CustomPacketPayload {
    public static final Type<ClientStartAlchemicalForgePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "start_alchemical_forge"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientStartAlchemicalForgePacket> STREAM_CODEC = CustomPacketPayload.codec(
            ClientStartAlchemicalForgePacket::write,
            ClientStartAlchemicalForgePacket::new
    );


    public ClientStartAlchemicalForgePacket(){}

    public ClientStartAlchemicalForgePacket(FriendlyByteBuf buf){}

    public void write(FriendlyByteBuf buf){}

    public static void handle(ClientStartAlchemicalForgePacket alchemicalForgePacket, IPayloadContext context){
        context.enqueueWork(() -> {
            if(!(context.player() instanceof ServerPlayer svplayer))return;
            if(!(svplayer.containerMenu instanceof AlchemicalForgeMenu menu))return;

            menu.blockEntity.startAlchemicalForge();
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type(){
        return TYPE;
    }
}
