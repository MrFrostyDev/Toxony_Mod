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

    public static final DeferredHolder<MobEffect, MobEffect> WOLF_MUTAGEN = MOB_MUTAGENS.register(
            "wolf_mutagen",
            () -> new WolfMutagenEffect(MobEffectCategory.BENEFICIAL, 0xffffff)
    );

    public static final DeferredHolder<MobEffect, MobEffect> CAT_MUTAGEN = MOB_MUTAGENS.register(
            "cat_mutagen",
            () -> new CatMutagenEffect(MobEffectCategory.BENEFICIAL, 0xffffff)
    );

    public static final DeferredHolder<MobEffect, MobEffect> TURTLE_MUTAGEN = MOB_MUTAGENS.register(
            "turtle_mutagen",
            () -> new TurtleMutagenEffect(MobEffectCategory.BENEFICIAL, 0xffffff)
    );

    public static final DeferredHolder<MobEffect, MobEffect> SPIDER_MUTAGEN = MOB_MUTAGENS.register(
            "spider_mutagen",
            () -> new SpiderMutagenEffect(MobEffectCategory.BENEFICIAL, 0xffffff)
    );

    public static final DeferredHolder<MobEffect, MobEffect> WITHER_MUTAGEN = MOB_MUTAGENS.register(
            "wither_mutagen",
            () -> new WitherMutagenEffect(MobEffectCategory.BENEFICIAL, 0xffffff)
    );

    public static final DeferredHolder<MobEffect, MobEffect> BLAZE_MUTAGEN = MOB_MUTAGENS.register(
            "blaze_mutagen",
            () -> new BlazeMutagenEffect(MobEffectCategory.BENEFICIAL, 0xffffff)
    );

}

