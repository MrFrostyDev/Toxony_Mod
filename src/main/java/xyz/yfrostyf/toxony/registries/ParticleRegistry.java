package xyz.yfrostyf.toxony.registries;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import xyz.yfrostyf.toxony.ToxonyMain;

public class ParticleRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, ToxonyMain.MOD_ID);

    public static void register(IEventBus event){
        PARTICLE_TYPES.register(event);
    }

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> OIL_SMOKE = PARTICLE_TYPES.register(
            "oil_smoke",
            // The supplier. The boolean parameter denotes whether setting the Particles option in the
            // video settings to Minimal will affect this particle type or not; this is false for
            // most vanilla particles, but true for e.g. explosions, campfire smoke, or squid ink.
            () -> new SimpleParticleType(false)
    );

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SMOKE = PARTICLE_TYPES.register(
            "smoke",
            () -> new SimpleParticleType(false)
    );

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLOOD_DRIP = PARTICLE_TYPES.register(
            "blood_drip",
            () -> new SimpleParticleType(false)
    );

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> CUT = PARTICLE_TYPES.register(
            "cut",
            () -> new SimpleParticleType(false)
    );

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLINTLOCK_BLAST_LARGE = PARTICLE_TYPES.register(
            "flintlock_blast_large",
            () -> new SimpleParticleType(false)
    );
}
