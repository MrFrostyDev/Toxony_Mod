package xyz.yfrostyf.toxony.events.subscribers.entities.player;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.events.ChangeToxEvent;
import xyz.yfrostyf.toxony.api.tox.ToxData;
import xyz.yfrostyf.toxony.network.ServerSendMessagePacket;
import xyz.yfrostyf.toxony.registries.DataAttachmentRegistry;

import static net.minecraft.core.component.DataComponents.POTION_CONTENTS;

@EventBusSubscriber(modid = ToxonyMain.MOD_ID)
public class DetoxEvents{
    private static final int GOLDEN_APPLE_DETOX = -30;
    private static final int REGEN_POTION_DETOX = -50;

    //
    // Checks if the player drank any regeneration potion. If so detox player.
    //
    @SubscribeEvent
    public static void onRegenPotionDrink(LivingEntityUseItemEvent.Finish event){
        if (!(event.getEntity() instanceof Player player)) return;
        ItemStack item = event.getItem();

        // Thank you (ChiefArug) from the NeoForge Project Discord
        if (!item.has(POTION_CONTENTS) || item.get(POTION_CONTENTS).potion().isEmpty()) return;
        if (item.get(POTION_CONTENTS).potion().filter(holder -> holder == Potions.REGENERATION || holder == Potions.LONG_REGENERATION || holder == Potions.STRONG_REGENERATION).isPresent()) {
            ToxData plyToxData = player.getData(DataAttachmentRegistry.TOX_DATA);
            plyToxData.addTox(REGEN_POTION_DETOX);
            player.setData(DataAttachmentRegistry.TOX_DATA, plyToxData);
        }

    }

    //
    // Checks if the player ate a golden or enchanted golden apple. If so detox player.
    //
    @SubscribeEvent
    public static void onGoldenAppleEat(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player player))return;
        Item item = event.getItem().getItem();

        if ((item != Items.GOLDEN_APPLE) && (item != Items.ENCHANTED_GOLDEN_APPLE))return;
        ToxData plyToxData = player.getData(DataAttachmentRegistry.TOX_DATA);
        plyToxData.addTox(GOLDEN_APPLE_DETOX);
        player.setData(DataAttachmentRegistry.TOX_DATA, plyToxData);
    }

    //
    // Audio and visual cue for removal of mutagens when tox is zero.
    // When player goes back to normal.
    //
    @SubscribeEvent
    public static void onToxBodyReset(ChangeToxEvent event) {
        if (event.getNewTox() > 0
                || event.isAdding()
                || event.getToxData().getMutagens().isEmpty()) return;

        LivingEntity entity = event.getEntity();
        ToxData toxData = event.getToxData();


        toxData.clearMutagens();
        toxData.clearAffinities();

        entity.playSound(SoundEvents.AMETHYST_BLOCK_BREAK);
        if(entity.level() instanceof ServerLevel svlevel){
            svlevel.sendParticles(ParticleTypes.TOTEM_OF_UNDYING,
                    entity.getX(),  entity.getY()+1.5,  entity.getZ(),
                    15, 0.75, 0.3, 0.75, 0);
            if(entity instanceof Player){
                PacketDistributor.sendToPlayer((ServerPlayer)entity, ServerSendMessagePacket.create("message.toxony.tox.mutagen_clear"));
            }
        }

        entity.setData(DataAttachmentRegistry.TOX_DATA, toxData);
    }
}
