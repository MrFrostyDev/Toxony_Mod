package xyz.yfrostyf.toxony.registries;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.affinity.Affinity;
import xyz.yfrostyf.toxony.api.registries.ToxonyRegistries;

import java.util.List;


public class AffinityRegistry {
    public static final DeferredRegister<Affinity> AFFINITIES = DeferredRegister.create(ToxonyRegistries.AFFINITY_REGISTRY, ToxonyMain.MOD_ID);

    public static final DeferredHolder<Affinity, Affinity> MOON = AFFINITIES.register("moon", () -> Affinity.create(
            "Moon",
            0,
            () -> List.of(
                    MobEffectRegistry.WOLF_MUTAGEN,
                    MobEffectRegistry.CAT_MUTAGEN
            )
    ));
    public static final DeferredHolder<Affinity, Affinity> SUN = AFFINITIES.register("sun", () -> Affinity.create(
            "Sun",
            1,
            () -> List.of(
                    MobEffectRegistry.TURTLE_MUTAGEN,
                    MobEffectRegistry.CAT_MUTAGEN
            )
    ));
    public static final DeferredHolder<Affinity, Affinity> OCEAN = AFFINITIES.register("ocean", () -> Affinity.create(
            "ocean",
            2,
            () -> List.of(
                    MobEffectRegistry.TURTLE_MUTAGEN
            )
    ));
    public static final DeferredHolder<Affinity, Affinity> FOREST = AFFINITIES.register("forest", () -> Affinity.create(
            "forest",
            3,
            () -> List.of(
                    MobEffectRegistry.WOLF_MUTAGEN,
                    MobEffectRegistry.SPIDER_MUTAGEN
            )
    ));
    public static final DeferredHolder<Affinity, Affinity> END = AFFINITIES.register("end", () -> Affinity.create(
            "end",
            4,
            () -> List.of()
    ));

    public static void register(IEventBus eventBus){
        AFFINITIES.register(eventBus);
    }
}
