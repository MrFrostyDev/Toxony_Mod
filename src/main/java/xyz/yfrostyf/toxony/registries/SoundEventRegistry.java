package xyz.yfrostyf.toxony.registries;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import xyz.yfrostyf.toxony.ToxonyMain;

public class SoundEventRegistry {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, ToxonyMain.MOD_ID);

    public static void register(IEventBus event){
        SOUND_EVENTS.register(event);
    }

    // All vanilla sounds use variable range events.
    public static final DeferredHolder<SoundEvent, SoundEvent> MUTAGEN_TRANSFORM = SOUND_EVENTS.register(
            "mutagen_transform", // must match the resource location on the next line
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID,
                    "mutagen_transform"
            ))
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> FLINTLOCK_SHOOT = SOUND_EVENTS.register(
            "flintlock_shoot",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID,
                    "flintlock_shoot"
            ))
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> FLINTLOCK_CLICK = SOUND_EVENTS.register(
            "flintlock_click",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID,
                    "flintlock_click"
            ))
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> FLINTLOCK_LOAD = SOUND_EVENTS.register(
            "flintlock_load",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID,
                    "flintlock_load"
            ))
    );
}
