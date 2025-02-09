package xyz.yfrostyf.toxony.registries;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.oils.Oil;
import xyz.yfrostyf.toxony.api.registries.ToxonyRegistries;

import java.util.List;

public class OilsRegistry {
    public static final DeferredRegister<Oil> OILS = DeferredRegister.create(ToxonyRegistries.OIL_REGISTRY, ToxonyMain.MOD_ID);

    public static final DeferredHolder<Oil, Oil> POISON_OIL = OILS.register("poison_oil", () -> new Oil(
            Component.translatable("oil.toxony.poison"),
            BuiltInRegistries.ITEM.getOrCreateTag(ItemTags.WEAPON_ENCHANTABLE),
            List.of(MobEffects.POISON)
    ));

    public static final DeferredHolder<Oil, Oil> TOXIN_OIL = OILS.register("toxin_oil", () -> new Oil(
            Component.translatable("oil.toxony.toxin"),
            BuiltInRegistries.ITEM.getOrCreateTag(ItemTags.WEAPON_ENCHANTABLE),
            List.of(MobEffectRegistry.TOXIN)
    ));

    public static final DeferredHolder<Oil, Oil> REGENERATION_OIL = OILS.register("regeneration_oil", () -> new Oil(
            Component.translatable("oil.toxony.regeneration"),
            BuiltInRegistries.ITEM.getOrCreateTag(ItemTags.WEAPON_ENCHANTABLE),
            List.of(MobEffects.REGENERATION)
    ));

    public static void register(IEventBus eventBus){
        OILS.register(eventBus);
    }
}
