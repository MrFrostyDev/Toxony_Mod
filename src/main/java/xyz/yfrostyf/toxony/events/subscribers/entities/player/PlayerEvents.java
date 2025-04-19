package xyz.yfrostyf.toxony.events.subscribers.entities.player;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.tox.ToxData;
import xyz.yfrostyf.toxony.api.util.AffinityUtil;
import xyz.yfrostyf.toxony.items.PotionFlaskItem;
import xyz.yfrostyf.toxony.network.SyncIngredientAffinityMapPacket;
import xyz.yfrostyf.toxony.network.SyncToxDataPacket;
import xyz.yfrostyf.toxony.registries.DataAttachmentRegistry;

@EventBusSubscriber(modid = ToxonyMain.MOD_ID)
public class PlayerEvents {

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        ToxData plyToxData = event.getEntity().getData(DataAttachmentRegistry.TOX_DATA.get());

        if(!player.level().isClientSide()){
            PacketDistributor.sendToPlayer((ServerPlayer) player, SyncToxDataPacket.create(plyToxData));
        }

        plyToxData.applyMutagens();
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer svplayer) {
            ToxData plyToxData = svplayer.getData(DataAttachmentRegistry.TOX_DATA);
            PacketDistributor.sendToPlayer(svplayer, SyncToxDataPacket.create(plyToxData));
            PacketDistributor.sendToPlayer(svplayer, SyncIngredientAffinityMapPacket.create(AffinityUtil.getIngredientAffinityMap(svplayer.level())));
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.getEntity() instanceof ServerPlayer clonePlayer && event.isWasDeath()) {
            ToxData oldToxData = event.getOriginal().getData(DataAttachmentRegistry.TOX_DATA);
            clonePlayer.setData(DataAttachmentRegistry.TOX_DATA.get(), oldToxData);
            PacketDistributor.sendToPlayer(clonePlayer, SyncToxDataPacket.create(oldToxData));
        }
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer svplayer) {
            ToxData plyToxData = svplayer.getData(DataAttachmentRegistry.TOX_DATA);
            PacketDistributor.sendToPlayer(svplayer, SyncToxDataPacket.create(plyToxData));
            PacketDistributor.sendToPlayer(svplayer, SyncIngredientAffinityMapPacket.create(AffinityUtil.getIngredientAffinityMap(svplayer.level())));
        }
    }

    @SubscribeEvent
    public static void onPlayerUsePotion(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() instanceof Player player) {
            PotionContents contents = event.getItem().getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
            if(contents.potion().isPresent()){
                if(contents.potion().get() != Potions.WATER
                        && contents.potion().get() != Potions.AWKWARD
                        && contents.potion().get() != Potions.MUNDANE){
                    if(event.getItem().getItem() instanceof PotionFlaskItem){
                        ToxData toxData = player.getData(DataAttachmentRegistry.TOX_DATA.get());
                        toxData.addTox(15);
                    }
                    else{
                        ToxData toxData = player.getData(DataAttachmentRegistry.TOX_DATA.get());
                        toxData.addTox(5);
                    }
                }
            }
        }
    }
}
