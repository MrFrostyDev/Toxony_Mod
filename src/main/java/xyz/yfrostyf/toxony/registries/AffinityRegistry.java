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

    public static void register(IEventBus eventBus){
        AFFINITIES.register(eventBus);
    }

    public static final DeferredHolder<Affinity, Affinity> MOON = AFFINITIES.register("moon", () -> Affinity.create(
            "Moon",
            1,
            () -> List.of(
                    MobEffectRegistry.BEAST_MUTAGEN,
                    MobEffectRegistry.SPIRIT_MUTAGEN,
                    MobEffectRegistry.HOLLOW_MUTAGEN
            )
    ));
    public static final DeferredHolder<Affinity, Affinity> SUN = AFFINITIES.register("sun", () -> Affinity.create(
            "Sun",
            2,
            () -> List.of(
                    MobEffectRegistry.BEAST_MUTAGEN,
                    MobEffectRegistry.INFERNAL_MUTAGEN

            )
    ));
    public static final DeferredHolder<Affinity, Affinity> OCEAN = AFFINITIES.register("ocean", () -> Affinity.create(
            "Ocean",
            3,
            () -> List.of(
                    MobEffectRegistry.AQUA_MUTAGEN
            )
    ));
    public static final DeferredHolder<Affinity, Affinity> FOREST = AFFINITIES.register("forest", () -> Affinity.create(
            "Forest",
            4,
            () -> List.of(
                    MobEffectRegistry.BEAST_MUTAGEN
            )
    ));
    public static final DeferredHolder<Affinity, Affinity> WIND = AFFINITIES.register("wind", () -> Affinity.create(
            "Wind",
            5,
            () -> List.of(
                    MobEffectRegistry.AQUA_MUTAGEN
            )
    ));
    public static final DeferredHolder<Affinity, Affinity> COLD = AFFINITIES.register("cold", () -> Affinity.create(
            "Cold",
            6,
            () -> List.of(
                    MobEffectRegistry.HOLLOW_MUTAGEN,
                    MobEffectRegistry.NECROTIC_MUTAGEN
            )
    ));
    public static final DeferredHolder<Affinity, Affinity> SOUL = AFFINITIES.register("soul", () -> Affinity.create(
            "Soul",
            7,
            () -> List.of(
                    MobEffectRegistry.SPIRIT_MUTAGEN,
                    MobEffectRegistry.NECROTIC_MUTAGEN
            )
    ));
    public static final DeferredHolder<Affinity, Affinity> DECAY = AFFINITIES.register("decay", () -> Affinity.create(
            "Decay",
            8,
            () -> List.of(
                    MobEffectRegistry.NECROTIC_MUTAGEN
            )
    ));
    public static final DeferredHolder<Affinity, Affinity> NETHER = AFFINITIES.register("nether", () -> Affinity.create(
            "Nether",
            9,
            () -> List.of(
                    MobEffectRegistry.INFERNAL_MUTAGEN
            )
    ));
    public static final DeferredHolder<Affinity, Affinity> END = AFFINITIES.register("end", () -> Affinity.create(
            "End",
            10,
            () -> List.of()
    ));
    public static final DeferredHolder<Affinity, Affinity> HEAT = AFFINITIES.register("heat", () -> Affinity.create(
            "Heat",
            11,
            () -> List.of(
                    MobEffectRegistry.INFERNAL_MUTAGEN
            )
    ));

}
