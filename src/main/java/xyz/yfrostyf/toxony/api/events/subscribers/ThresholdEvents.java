package xyz.yfrostyf.toxony.api.events.subscribers;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.affinity.Affinity;
import xyz.yfrostyf.toxony.api.events.ChangeThresholdEvent;
import xyz.yfrostyf.toxony.api.events.ChangeToxEvent;
import xyz.yfrostyf.toxony.api.tox.ToxData;
import xyz.yfrostyf.toxony.network.SyncToxPacket;
import xyz.yfrostyf.toxony.registries.MutagenRegistry;

import java.util.Collections;
import java.util.Map;

@EventBusSubscriber(modid = ToxonyMain.MOD_ID)
public class ThresholdEvents {
    //
    // Reset ToxThreshold whenever the player reaches 0 tox.
    //
    @SubscribeEvent
    public static void onToxGone(ChangeToxEvent event) {
        if(!(event.getEntity() instanceof ServerPlayer svplayer)){return;}
        if (event.getNewTox() > 0 || event.isCanceled()){ return;}
        event.getToxData().resetThreshold();

        for (Holder<MobEffect> effect : MutagenRegistry.MOB_MUTAGENS.getEntries()){
            if(!event.getEntity().hasEffect(effect)){continue;}
            event.getEntity().removeEffect(effect);
        }

        PacketDistributor.sendToPlayer(svplayer, new SyncToxPacket(event.getToxData()));
    }


    //
    // Event that allows player's to gain mutagens when tox is sufficiently high enough.
    //
    @SubscribeEvent
    public static void onReachingNewThreshold(ChangeThresholdEvent event) {
        if(!(event.getEntity() instanceof ServerPlayer svplayer)){return;}
        if (!event.isAdding()) {return;}

        ToxonyMain.LOGGER.info("[MutagenThreshold called]: player: {}", svplayer.getDisplayName());

        ToxData plyToxData = event.getToxData();
        Map<Integer, Integer> plyAffinities = plyToxData.getAffinities();

        int maxAffinity = Collections.max(plyAffinities.entrySet(), Map.Entry.comparingByValue()).getKey();
        ToxonyMain.LOGGER.info("[MutagenThreshold Highest Affinity]: {}", maxAffinity);

        // For-Loop for when the player skips a few thresholds at once. Apply mutagen multiple times!
        for(int i=0; i<event.getNewThreshold()-event.getOldThreshold(); i++) {
            switch (maxAffinity) {
                case Affinity.WOLF:
                    handleMutagenEffect(svplayer, MutagenRegistry.WOLF_MUTAGEN);
                    break;
                case Affinity.CAT:
                    handleMutagenEffect(svplayer, MutagenRegistry.CAT_MUTAGEN);
                    break;
                case Affinity.TURTLE:
                    handleMutagenEffect(svplayer, MutagenRegistry.TURTLE_MUTAGEN);
                    break;
                case Affinity.SPIDER:
                    handleMutagenEffect(svplayer, MutagenRegistry.SPIDER_MUTAGEN);
                    break;
                default:
                    break;
            }
        }

        PacketDistributor.sendToPlayer(svplayer, new SyncToxPacket(plyToxData));
    }

    //
    // Adds a Mutagen if the player doesn't have it. If they do, amplify the Mutagen by 1.
    //
    @OnlyIn(Dist.DEDICATED_SERVER)
    private static void handleMutagenEffect(ServerPlayer svplayer, Holder<MobEffect> effect) {
        ToxonyMain.LOGGER.info("[Mutagen added]: {}", effect);
        if (!svplayer.hasEffect(effect)) {
            svplayer.addEffect(new MobEffectInstance(effect, MobEffectInstance.INFINITE_DURATION, 0, false, false, false));
            return;
        }

        int effectAmp = svplayer.getEffect(effect).getAmplifier();
        svplayer.removeEffect(effect);
        svplayer.addEffect(new MobEffectInstance(effect, MobEffectInstance.INFINITE_DURATION, effectAmp+1, false, false, false));
    }

}
