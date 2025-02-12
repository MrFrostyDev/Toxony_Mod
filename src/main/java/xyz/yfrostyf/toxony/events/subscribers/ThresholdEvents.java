package xyz.yfrostyf.toxony.events.subscribers;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.affinity.Affinity;
import xyz.yfrostyf.toxony.api.events.ChangeThresholdEvent;
import xyz.yfrostyf.toxony.api.events.ChangeToxEvent;
import xyz.yfrostyf.toxony.api.mutagens.MutagenEffect;
import xyz.yfrostyf.toxony.api.tox.ToxData;
import xyz.yfrostyf.toxony.network.SyncToxPacket;
import xyz.yfrostyf.toxony.registries.MobEffectRegistry;

import java.util.*;

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

        for (Holder<MobEffect> effect : MobEffectRegistry.MOB_MUTAGENS.getEntries()){
            if(!event.getEntity().hasEffect(effect)){continue;}
            event.getEntity().removeEffect(effect);
        }

        PacketDistributor.sendToPlayer(svplayer, SyncToxPacket.create(event.getToxData()));
    }


    //
    // Event that allows player's to gain mutagens when tox is sufficiently high enough.
    //
    @SubscribeEvent
    public static void onReachingNewThreshold(ChangeThresholdEvent event) {
        if(!(event.getEntity() instanceof ServerPlayer svplayer)){return;}
        if (!event.isAdding()) {return;}

        ToxData plyToxData = event.getToxData();

        // For-Loop for when the player skips a few thresholds at once. Apply mutagen multiple times!
        for(int i=0; i<event.getNewThreshold()-event.getOldThreshold(); i++) {
            handleMutagenEffect(svplayer, getMutagenFromAffinities(plyToxData.getAffinities()));
        }

        PacketDistributor.sendToPlayer(svplayer, SyncToxPacket.create(plyToxData));
    }

    //
    // Calculate and find the mutagen that best represents the player's collective affinities.
    //
    @OnlyIn(Dist.DEDICATED_SERVER)
    private static Holder<MobEffect> getMutagenFromAffinities(Map<Affinity, Integer> affinities) {
        Map<Holder<MobEffect>, Integer> affinityToValueMap = new HashMap<>();
        for(Map.Entry<Affinity, Integer> affinity : affinities.entrySet()){
            List<Holder<MobEffect>> mutagens = affinity.getKey().getMutagens();
            for(Holder<MobEffect> mutagen : mutagens){
                if(affinityToValueMap.containsKey(mutagen)){
                    affinityToValueMap.replace(mutagen, affinityToValueMap.get(mutagen)+affinity.getValue());
                    continue;
                }
                affinityToValueMap.put(mutagen, affinity.getValue());
            }
        }
        Pair<Holder<MobEffect>, Integer> largest = null;
        for(Map.Entry<Holder<MobEffect>, Integer> entry : affinityToValueMap.entrySet()){
            if(largest == null){
                largest = new Pair<>(entry.getKey(), entry.getValue());
                continue;
            }
            if(entry.getValue() < largest.getSecond()){
                largest = new Pair<>(entry.getKey(), entry.getValue());
            }
        }
        if(largest == null) return MobEffects.POISON;
        return largest.getFirst();
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
