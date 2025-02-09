package xyz.yfrostyf.toxony.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.mutagens.CatMutagenEffect;
import xyz.yfrostyf.toxony.mutagens.SpiderMutagenEffect;
import xyz.yfrostyf.toxony.mutagens.TurtleMutagenEffect;
import xyz.yfrostyf.toxony.mutagens.WolfMutagenEffect;

public class MutagenRegistry {
    public static final DeferredRegister<MobEffect> MOB_MUTAGENS = DeferredRegister.create(Registries.MOB_EFFECT, ToxonyMain.MOD_ID);

    public static void register(IEventBus eventBus) {
        MOB_MUTAGENS.register(eventBus);
    }

    public static final DeferredHolder<MobEffect, MobEffect> WOLF_MUTAGEN = MOB_MUTAGENS.register(
            "wolf_mutagen",
            () -> new WolfMutagenEffect(MobEffectCategory.HARMFUL, 0xffffff)
    );

    public static final DeferredHolder<MobEffect, MobEffect> CAT_MUTAGEN = MOB_MUTAGENS.register(
            "cat_mutagen",
            () -> new CatMutagenEffect(MobEffectCategory.HARMFUL, 0xffffff)
    );

    public static final DeferredHolder<MobEffect, MobEffect> TURTLE_MUTAGEN = MOB_MUTAGENS.register(
            "turtle_mutagen",
            () -> new TurtleMutagenEffect(MobEffectCategory.HARMFUL, 0xffffff)
    );

    public static final DeferredHolder<MobEffect, MobEffect> SPIDER_MUTAGEN = MOB_MUTAGENS.register(
            "spider_mutagen",
            () -> new SpiderMutagenEffect(MobEffectCategory.HARMFUL, 0xffffff)
    );

}
