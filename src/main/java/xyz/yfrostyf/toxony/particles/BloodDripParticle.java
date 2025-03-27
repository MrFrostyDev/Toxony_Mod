package xyz.yfrostyf.toxony.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class BloodDripParticle extends TextureSheetParticle {
    public BloodDripParticle(ClientLevel level, double x, double y, double z, double color) {
        super(level, x, y, z, 0.0, 0.0, 0.0);
        this.xd *= 0.3F;
        this.yd = Math.random() * 0.2F + 0.1F;
        this.zd *= 0.3F;
        this.setSize(0.2F, 0.2F);
        this.gravity = 0.06F;
        this.lifetime = (int)(8.0 / (Math.random() * 0.8 + 0.2));
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.lifetime-- <= 0) {
            this.remove();
        } else {
            this.yd = this.yd - (double)this.gravity;
            this.move(this.xd, this.yd, this.zd);
            this.xd *= 0.98F;
            this.yd *= 0.98F;
            this.zd *= 0.98F;
            if (this.onGround) {
                if (Math.random() < 0.5) {
                    this.remove();
                }

                this.xd *= 0.7F;
                this.zd *= 0.7F;
            }

            BlockPos blockpos = BlockPos.containing(this.x, this.y, this.z);
            double d0 = Math.max(
                    this.level
                            .getBlockState(blockpos)
                            .getCollisionShape(this.level, blockpos)
                            .max(Direction.Axis.Y, this.x - (double)blockpos.getX(), this.z - (double)blockpos.getZ()),
                    this.level.getFluidState(blockpos).getHeight(this.level, blockpos)
            );
            if (d0 > 0.0 && this.y < (double)blockpos.getY() + d0) {
                this.remove();
            }
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet sprites) {
            this.sprite = sprites;
        }

        public Particle createParticle(
                SimpleParticleType type,
                ClientLevel level,
                double x,
                double y,
                double z,
                double xSpeed,
                double ySpeed,
                double zSpeed
        ) {
            BloodDripParticle particle = new BloodDripParticle(level, x, y, z, xSpeed);
            particle.pickSprite(this.sprite);
            return particle;
        }
    }
}
