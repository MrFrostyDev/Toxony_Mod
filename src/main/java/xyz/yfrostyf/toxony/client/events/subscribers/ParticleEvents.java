package xyz.yfrostyf.toxony.client.events.subscribers;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.particles.*;
import xyz.yfrostyf.toxony.registries.ParticleRegistry;

@EventBusSubscriber(modid = ToxonyMain.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ParticleEvents {

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        // There are multiple ways to register providers, all differing in the functional type they provide in the
        // second parameter. For example, #registerSpriteSet represents a Function<SpriteSet, ParticleProvider<?>>:
        event.registerSpriteSet(ParticleRegistry.OIL_SMOKE.get(), OilSmokeParticle.Provider::new);
        // Other methods include #registerSprite, which is essentially a Supplier<TextureSheetParticle>,
        // and #registerSpecial, which maps to a Supplier<Particle>. See the source code of the event for further info.
        event.registerSpriteSet(ParticleRegistry.SMOKE.get(), SmokeParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.CUT.get(), CutParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.BLOOD_DRIP.get(), BloodDripParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.FLINTLOCK_BLAST_LARGE.get(), FlintlockBlastLargeParticle.Provider::new);
    }
}
