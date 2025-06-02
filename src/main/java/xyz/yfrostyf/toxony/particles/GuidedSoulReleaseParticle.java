package xyz.yfrostyf.toxony.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class GuidedSoulReleaseParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;

    public GuidedSoulReleaseParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet) {
        super(level, x, y, z, 0, 0, 0);
        this.spriteSet = spriteSet;

        this.quadSize = 1.2F;
        this.setSize(1.0F, 1.0F);
        this.lifetime = 30 + this.random.nextInt(5);
        this.setSpriteFromAge(spriteSet);

        this.rCol = 1.0f;
        this.gCol = 1.0f;
        this.bCol = 1.0f;

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
            return new GuidedSoulReleaseParticle(level, x, y, z, spriteSet);
        }
    }
}
