package xyz.yfrostyf.toxony.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.effects.*;
import xyz.yfrostyf.toxony.effects.mutagens.*;

public class MobEffectRegistry {
    private static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, ToxonyMain.MOD_ID);
    public static final DeferredRegister<MobEffect> MOB_MUTAGENS = DeferredRegister.create(Registries.MOB_EFFECT, ToxonyMain.MOD_ID);


    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
        MOB_MUTAGENS.register(eventBus);
    }

    public static final DeferredHolder<MobEffect, MobEffect> HUNT = MOB_EFFECTS.register(
            "hunt",
            () -> new HuntMobEffect(MobEffectCategory.HARMFUL)
    );

    public static final DeferredHolder<MobEffect, MobEffect> TOXIN = MOB_EFFECTS.register(
            "toxin",
            () -> new ToxinMobEffect(MobEffectCategory.HARMFUL)
    );

    public static final DeferredHolder<MobEffect, MobEffect> ACID = MOB_EFFECTS.register(
            "acid",
            () -> new AcidMobEffect(MobEffectCategory.HARMFUL)
    );

    public static final DeferredHolder<MobEffect, MobEffect> FLAMMABLE = MOB_EFFECTS.register(
            "flammable",
            () -> new FlammableMobEffect(MobEffectCategory.HARMFUL)
    );

    public static final DeferredHolder<MobEffect, MobEffect> CRIPPLE = MOB_EFFECTS.register(
            "cripple",
            () -> new CrippleMobEffect(MobEffectCategory.HARMFUL)
    );

    // |-----------------------------------------------------------------------------------|
    // |-------------------------------------Mutagens--------------------------------------|
    // |-----------------------------------------------------------------------------------|

    public static final DeferredHolder<MobEffect, MobEffect> BEAST_MUTAGEN = MOB_MUTAGENS.register(
            "beast_mutagen",
            () -> new BeastMutagenEffect(MobEffectCategory.BENEFICIAL)
    );

    public static final DeferredHolder<MobEffect, MobEffect> SPIRIT_MUTAGEN = MOB_MUTAGENS.register(
            "spirit_mutagen",
            () -> new SpiritMutagenEffect(MobEffectCategory.BENEFICIAL)
    );

    public static final DeferredHolder<MobEffect, MobEffect> AQUA_MUTAGEN = MOB_MUTAGENS.register(
            "aqua_mutagen",
            () -> new AquaMutagenEffect(MobEffectCategory.BENEFICIAL)
    );

    public static final DeferredHolder<MobEffect, MobEffect> HOLLOW_MUTAGEN = MOB_MUTAGENS.register(
            "hollow_mutagen",
            () -> new HollowMutagenEffect(MobEffectCategory.BENEFICIAL)
    );

    public static final DeferredHolder<MobEffect, MobEffect> NECROTIC_MUTAGEN = MOB_MUTAGENS.register(
            "necrotic_mutagen",
            () -> new NecroticMutagenEffect(MobEffectCategory.BENEFICIAL)
    );

    public static final DeferredHolder<MobEffect, MobEffect> INFERNAL_MUTAGEN = MOB_MUTAGENS.register(
            "infernal_mutagen",
            () -> new InfernalMutagenEffect(MobEffectCategory.BENEFICIAL)
    );

    public static final DeferredHolder<MobEffect, MobEffect> MOB_MUTAGEN = MOB_MUTAGENS.register(
            "mob_mutagen",
            () -> new MobMutagenEffect(MobEffectCategory.BENEFICIAL)
    );

}

