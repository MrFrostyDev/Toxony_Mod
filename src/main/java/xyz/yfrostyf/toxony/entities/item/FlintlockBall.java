package xyz.yfrostyf.toxony.entities.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import xyz.yfrostyf.toxony.api.util.CompatibilityUtil;
import xyz.yfrostyf.toxony.registries.EntityRegistry;
import xyz.yfrostyf.toxony.registries.ItemRegistry;
import xyz.yfrostyf.toxony.registries.MobEffectRegistry;

import static xyz.yfrostyf.toxony.api.util.CompatibilityUtil.VAMPIRE;
import static xyz.yfrostyf.toxony.api.util.CompatibilityUtil.WEREWOLF;

public class FlintlockBall extends Projectile implements ItemSupplier {
    public static final int BALL_LIFETIME = 80;
    private int life;

    private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(
            FlintlockBall.class, EntityDataSerializers.ITEM_STACK
    );

    private int acidLevel = 0;
    private float damage = 0.0F;
    private ItemStack item;

    public FlintlockBall(EntityType<? extends FlintlockBall> entityType, Level level) {
        super(entityType, level);
        this.life = 0;
    }

    public FlintlockBall(Level level, ItemStack item, float damage, int acidLevel) {
        this(EntityRegistry.FLINTLOCK_BALL.get(), level);
        this.damage = damage;
        this.acidLevel = acidLevel;
        this.item = item;
    }

    public void shoot(LivingEntity shooter, double x, double y, double z, float velocity, float inaccuracy) {
        this.setOwner(shooter);
        this.setPos(shooter.getX(), shooter.getEyeY() - 0.1F, shooter.getZ());
        Vec3 vec3 = this.getMovementToShoot(x, y, z, velocity, inaccuracy);

        this.setDeltaMovement(vec3);
        this.hasImpulse = true;

        this.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * 180.0F / (float)Math.PI));
        this.setXRot((float)(Mth.atan2(vec3.y, vec3.horizontalDistance()) * 180.0F / (float)Math.PI));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        super.shoot(x, y, z, velocity, inaccuracy);
    }

    /**
     * Checks if the entity is in range to render.
     */
    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        double d0 = this.getBoundingBox().getSize() * 4.0;
        if (Double.isNaN(d0)) {
            d0 = 4.0;
        }

        d0 *= 128.0;
        return distance < d0 * d0;
    }

    @Override
    public boolean canUsePortal(boolean allowPassengers) {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        this.life++;
        if (this.life >= BALL_LIFETIME) {
            this.discard();
            return;
        }

        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS && !net.neoforged.neoforge.event.EventHooks.onProjectileImpact(this, hitresult)) {
            this.hitTargetOrDeflectSelf(hitresult);
        }

        this.checkInsideBlocks();
        Vec3 vec3 = this.getDeltaMovement();
        double d0 = this.getX() + vec3.x;
        double d1 = this.getY() + vec3.y;
        double d2 = this.getZ() + vec3.z;
        this.updateRotation();
        float f;
        if (this.isInWater()) {
            for (int i = 0; i < 4; i++) {
                this.level().addParticle(ParticleTypes.BUBBLE, d0 - vec3.x * 0.25, d1 - vec3.y * 0.25, d2 - vec3.z * 0.25, vec3.x, vec3.y, vec3.z);
            }

            f = 0.8F;
        } else {
            f = 0.99F;
        }

        this.setDeltaMovement(vec3.scale(f));
        this.applyGravity();
        this.setPos(d0, d1, d2);
    }

    /**
     * Handles an entity event received from a {@link net.minecraft.network.protocol.game.ClientboundEntityEventPacket}.
     */
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            //ParticleOptions particleoptions = this.getParticle();

            for (int i = 0; i < 8; i++) {
                //this.level().addParticle(particleoptions, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();
        entity.hurt(this.damageSources().mobProjectile(this, this.getOwner() instanceof LivingEntity livingEntity ? livingEntity : null ), CompatibilityUtil.modifyDamageFromSilver(entity, this.damage));
        if(acidLevel > 0 && entity instanceof LivingEntity livingEntity){
            livingEntity.addEffect(new MobEffectInstance(MobEffectRegistry.ACID, acidLevel * 80, 0));
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte)3);
            this.discard();
        }
    }

    public void setItem(ItemStack stack) {
        this.getEntityData().set(DATA_ITEM_STACK, stack.copyWithCount(1));
    }


    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_ITEM_STACK, this.getDefaultItem());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("Item", this.getItem().save(this.registryAccess()));
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Item", 10)) {
            this.setItem(ItemStack.parse(this.registryAccess(), compound.getCompound("Item")).orElseGet(this::getDefaultItem));
        } else {
            this.setItem(getDefaultItem());
        }
    }

    @Override
    protected double getDefaultGravity() {
        return 0.005;
    }

    @Override
    public ItemStack getItem() {
        return this.getEntityData().get(DATA_ITEM_STACK);
    }

    public ItemStack getDefaultItem() {
        return ItemRegistry.IRON_ROUND.get().getDefaultInstance();
    }
}
