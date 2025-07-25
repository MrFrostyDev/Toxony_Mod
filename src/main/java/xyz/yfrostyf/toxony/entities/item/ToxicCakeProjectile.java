package xyz.yfrostyf.toxony.entities.item;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import xyz.yfrostyf.toxony.registries.EntityRegistry;
import xyz.yfrostyf.toxony.registries.MobEffectRegistry;

public class ToxicCakeProjectile extends ThrowableItemProjectile {
    public ToxicCakeProjectile(EntityType<? extends ToxicCakeProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public ToxicCakeProjectile(Level level, LivingEntity shooter) {
        super(EntityRegistry.TOXIC_CAKE_PROJECTILE.get(), shooter, level);
    }

    public ToxicCakeProjectile(Level level, double x, double y, double z) {
        super(EntityRegistry.TOXIC_CAKE_PROJECTILE.get(), x, y, z, level);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.SNOWBALL;
    }

    private ParticleOptions getParticle() {
        ItemStack itemstack = this.getItem();
        return (ParticleOptions)(!itemstack.isEmpty() && !itemstack.is(this.getDefaultItem())
                ? new ItemParticleOption(ParticleTypes.ITEM, itemstack)
                : ParticleTypes.ITEM_SLIME);
    }

    /**
     * Handles an entity event received from a {@link net.minecraft.network.protocol.game.ClientboundEntityEventPacket}.
     */
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            ParticleOptions particleoptions = this.getParticle();

            for (int i = 0; i < 8; i++) {
                this.level().addParticle(particleoptions, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }

    /**
     * Called when the arrow hits an entity
     */
    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();
        entity.hurt(this.damageSources().thrown(this, this.getOwner()), (float)0);
        if(entity instanceof LivingEntity livingEntity){
            livingEntity.addEffect(new MobEffectInstance(MobEffectRegistry.TOXIN, 100, 0), this.getOwner());
        }
    }

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.SLIME_BLOCK_PLACE, SoundSource.NEUTRAL, 2.0F, 0.7F);
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte)3);
            this.discard();
        }
    }

    @Override
    protected double getDefaultGravity() {
        return 0.02;
    }
}
