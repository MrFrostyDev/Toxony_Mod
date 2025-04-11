package xyz.yfrostyf.toxony.entities.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import xyz.yfrostyf.toxony.damages.FlailDamageSource;
import xyz.yfrostyf.toxony.items.FlailItem;
import xyz.yfrostyf.toxony.registries.EntityRegistry;

// Based on Minecraft's FishingHook entity.

public class FlailBall extends Projectile {
    public static final int FLAIL_LIFETIME = 20;
    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(FlailBall.class, EntityDataSerializers.BOOLEAN);
    private static final double MAX_RANGE = 160.0;
    private final RandomSource syncronizedRandom = RandomSource.create();

    protected float damage;
    private int life;
    private boolean isFlying;

    public FlailBall(EntityType<? extends FlailBall> entityType, Level level) {
        super(entityType, level);
        this.noCulling = true;
        this.damage = 0;
        this.entityData.set(ID_FOIL, false);
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return super.getDimensions(pose);
    }

    public FlailBall(Player player, Level level, ItemStack stack, int force, float damage) {
        this(EntityRegistry.FLAIL_BALL.get(), level);
        this.entityData.set(ID_FOIL, stack.hasFoil());
        this.damage = damage;
        this.setOwner(player);
        float f = player.getXRot();
        float f1 = player.getYRot();
        float f2 = Mth.cos(-f1 * (float) (Math.PI / 180.0) - (float) Math.PI);
        float f3 = Mth.sin(-f1 * (float) (Math.PI / 180.0) - (float) Math.PI);
        float f4 = -Mth.cos(-f * (float) (Math.PI / 180.0));
        float f5 = Mth.sin(-f * (float) (Math.PI / 180.0));
        double d0 = player.getX() - (double)f3 * 0.3;
        double d1 = player.getEyeY();
        double d2 = player.getZ() - (double)f2 * 0.3;
        this.moveTo(d0, d1, d2, f1, f);
        Vec3 vec3 = new Vec3((double)(-f3), (double)Mth.clamp(-(f5 / f4), -5.0F, 5.0F), (double)(-f2));
        double d3 = vec3.length();
        vec3 = vec3.multiply(
                0.6 / d3 + this.random.triangle(0.5, 0.0103365), 0.6 / d3 + this.random.triangle(0.5, 0.0103365), 0.6 / d3 + this.random.triangle(0.5, 0.0103365)
        );
        Vec3 forceMultiplier = new Vec3(1 + (force - 1) * 0.25, 1 + (force - 1) * 0.25, 1 + (force - 1) * 0.25);
        this.setDeltaMovement(vec3.multiply(forceMultiplier));

        this.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * 180.0F / (float)Math.PI));
        this.setXRot((float)(Mth.atan2(vec3.y, vec3.horizontalDistance()) * 180.0F / (float)Math.PI));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(ID_FOIL, false);
    }

    @Override
    public void tick() {
        this.syncronizedRandom.setSeed(this.getUUID().getLeastSignificantBits() ^ this.level().getGameTime());
        super.tick();
        if (!(this.getOwner() instanceof Player player)) {
            this.discard();
        }
        else {
            if(FlailItem.isUsingFlail(player) || this.distanceToSqr(player) > MAX_RANGE){
                this.discard();
                return;
            }
            if (this.onGround()) {
                this.life++;
                if (this.life >= FLAIL_LIFETIME) {
                    this.discard();
                    return;
                }
            }
            else {
                this.isFlying = true;
                this.life = 0;
            }

            if (this.isFlying) {
                this.checkCollision();
            }

            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -this.getDefaultGravity(), 0.0));
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.updateRotation();
            if (this.isFlying && (this.onGround() || this.horizontalCollision)) {
                this.setDeltaMovement(Vec3.ZERO);
            }
            this.setDeltaMovement(this.getDeltaMovement().scale(0.92));
            this.reapplyPosition();
        }
    }

    protected double getDefaultGravity() {
        return 0.04;
    }

    /**
     * Checks if the entity is in range to render.
     */
    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 4096.0;
    }

    private void checkCollision() {
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() == HitResult.Type.MISS || !net.neoforged.neoforge.event.EventHooks.onProjectileImpact(this, hitresult)) this.onHit(hitresult);
    }

    @Override
    protected void onHit(HitResult result) {
        HitResult.Type hitresult$type = result.getType();
        if (hitresult$type == HitResult.Type.ENTITY) {
            EntityHitResult entityhitresult = (EntityHitResult)result;
            this.onHitEntity(entityhitresult);
            this.level().gameEvent(GameEvent.PROJECTILE_LAND, result.getLocation(), GameEvent.Context.of(this, null));
        }
        else if (result.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockhitresult = (BlockHitResult)result;
            this.onHitBlock(blockhitresult);
            BlockPos blockpos = blockhitresult.getBlockPos();
            this.level().gameEvent(GameEvent.PROJECTILE_LAND, blockpos, GameEvent.Context.of(this, this.level().getBlockState(blockpos)));
        }
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        return super.canHitEntity(target);
    }

    /**
     * Called when the arrow hits an entity
     */
    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        this.setDeltaMovement(Vec3.ZERO);
        if (!this.level().isClientSide) {
            if(result.getEntity() != this.getOwner()){
                result.getEntity().hurt(new FlailDamageSource(
                        registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(DamageTypes.GENERIC),
                        this.getOwner()
                ), this.damage);
                this.playSound(SoundEvents.TRIDENT_HIT, 1.0F, 0.8F);
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        this.setDeltaMovement(this.getDeltaMovement().normalize().scale(result.distanceTo(this)));
    }

    public boolean isFoil(){
        return this.entityData.get(ID_FOIL);
    }
}
