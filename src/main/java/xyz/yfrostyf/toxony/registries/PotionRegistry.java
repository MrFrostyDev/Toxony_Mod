package xyz.yfrostyf.toxony.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import xyz.yfrostyf.toxony.ToxonyMain;

public class PotionRegistry {
    private static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(Registries.POTION, ToxonyMain.MOD_ID);

    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }

    public static final DeferredHolder<Potion, Potion> TOXIN = POTIONS.register(
            "toxin",
            () -> new Potion(new MobEffectInstance(MobEffectRegistry.TOXIN, 600))
    );

}

