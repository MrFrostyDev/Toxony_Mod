package xyz.yfrostyf.toxony.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class FlintlockBlastLargeParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;

    public FlintlockBlastLargeParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet) {
        super(level, x, y, z, 0, 0, 0);
        this.spriteSet = spriteSet;

        this.quadSize = 0.6F;
        this.setSize(0.6F, 0.6F);
        this.lifetime = 8 + this.random.nextInt(2);;
        this.setSpriteFromAge(spriteSet);

        this.rCol = 1.0f;
        this.gCol = 1.0f;
        this.bCol = 1.0f;

        // We set the initial sprite here since ticking is not guaranteed to set the sprite
        // before the render method is called.
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public int getLightColor(float partialTick) {
        int i = super.getLightColor(partialTick);
        int k = i >> 16 & 0xFF;
        return 240 | k << 16;
    }

    @Override
    public void tick() {
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(spriteSet);
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        // A set of particle sprites.
        private final SpriteSet spriteSet;

        // The registration function passes a SpriteSet, so we accept that and store it for further use.
        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                                 double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FlintlockBlastLargeParticle(level, x, y, z, spriteSet);
        }
    }
}
