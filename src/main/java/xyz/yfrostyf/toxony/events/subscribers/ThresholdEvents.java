package xyz.yfrostyf.toxony.events.subscribers;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.affinity.Affinity;
import xyz.yfrostyf.toxony.api.events.ChangeThresholdEvent;
import xyz.yfrostyf.toxony.api.tox.ToxData;
import xyz.yfrostyf.toxony.registries.MobEffectRegistry;

import java.util.*;

@EventBusSubscriber(modid = ToxonyMain.MOD_ID)
public class ThresholdEvents {
    //
    // Event that allows player's to gain mutagens when tox is sufficiently high enough.
    //
    @SubscribeEvent
    public static void onReachingNewThreshold(ChangeThresholdEvent event) {
        if(event.getEntity().level().isClientSide())return;
        if (!event.isAdding())return;

        ToxData plyToxData = event.getToxData();
        List<Holder<MobEffect>> mobEffects = new ArrayList<>();

        // For-Loop for when the player skips a few thresholds at once. Apply mutagen multiple times!
        for(int i=0; i<event.getNewThreshold()-event.getOldThreshold(); i++) {
            mobEffects.add(getMutagenFromAffinities(plyToxData.getAffinities()));
        }
        plyToxData.addAndApplyMutagens(mobEffects);
    }

    //
    // Calculate and find the mutagen that best represents the player's collective affinities.
    //
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
                ToxonyMain.LOGGER.info("[getMutagenFromAffinities called]: affinityToValueMap: {}", affinityToValueMap);
            }
        }
        Pair<Holder<MobEffect>, Integer> largest = null;
        for(Map.Entry<Holder<MobEffect>, Integer> entry : affinityToValueMap.entrySet()){
            ToxonyMain.LOGGER.info("[getMutagenFromAffinities called]: mob effect pair entry: {}", entry);
            if(largest == null){
                largest = new Pair<>(entry.getKey(), entry.getValue());
            }
            else if(entry.getValue() > largest.getSecond()){
                largest = new Pair<>(entry.getKey(), entry.getValue());
            }
        }
        if(largest == null) return MobEffectRegistry.WOLF_MUTAGEN;
        ToxonyMain.LOGGER.info("[getMutagenFromAffinities called]: largest pair found: {}", largest);
        return largest.getFirst();
    }
}
