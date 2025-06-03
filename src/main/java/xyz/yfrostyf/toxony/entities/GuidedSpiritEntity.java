package xyz.yfrostyf.toxony.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import xyz.yfrostyf.toxony.registries.ParticleRegistry;

import java.util.EnumSet;

// Heavily based on Minecraft's GuidedSpiritEntityes
public class GuidedSpiritEntity extends PathfinderMob implements TraceableEntity, FlyingAnimal {
    private static final double DEFAULT_ATTACK_REACH = Math.sqrt(2.04F) - 1.2F;

    LivingEntity owner;

    private BlockPos boundOrigin;
    private boolean hasLimitedLife;
    private int limitedLifeTicks;

    public GuidedSpiritEntity(EntityType<GuidedSpiritEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
    }

    public static AttributeSupplier.Builder createAttributes(){
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.FLYING_SPEED, 0.8F)
                .add(Attributes.MOVEMENT_SPEED, 0.8F)
                .add(Attributes.ATTACK_DAMAGE, 6.0)
                .add(Attributes.FOLLOW_RANGE, 48.0);
    }

    @Override
    protected void registerGoals(){
        super.registerGoals();
        this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 2.0F, true));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 2.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Mob.class, 6.0f));
        this.targetSelector.addGoal(1, new GuidedSpiritOwnerTargetGoal(this, this.getOwner()));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Monster.class, true));
    }

    public void initSummon(LivingEntity owner, double x, double y, double z, int index){
        this.moveTo(owner.position(), 0.0F, 0.0F);;
        Vec3 ownerViewVec3 = owner.getViewVector(1.0F);
        Vec3 ownerUpVec3 = owner.getUpVector(1.0F);
        Vec3 ownerPerpViewVec3 = ownerViewVec3.cross(ownerUpVec3);
        Vector3f pushVec3 = ownerViewVec3.toVector3f().normalize();
        pushVec3.mul(0.2F);
        pushVec3.rotate(new Quaternionf().setAngleAxis(Math.PI / 2, ownerPerpViewVec3.x, ownerPerpViewVec3.y, ownerPerpViewVec3.z));
        pushVec3.rotate(new Quaternionf().setAngleAxis(Math.PI / 3, ownerViewVec3.x, ownerViewVec3.y, ownerViewVec3.z));
        pushVec3.rotate(new Quaternionf().setAngleAxis(-(Math.PI / 3) * index, ownerViewVec3.x, ownerViewVec3.y, ownerViewVec3.z));

        this.setOwner(owner);
        this.setLimitedLife(20 * (10 + owner.getRandom().nextInt(5)));
        this.moveTo(x, y, z, 0.0F, 0.0F);
        this.push(pushVec3.x, pushVec3.y, pushVec3.z);
    }

    @Override
    public void tick() {
        this.noPhysics = true;
        super.tick();
        this.noPhysics = false;
        this.setNoGravity(true);
        if (this.hasLimitedLife && --this.limitedLifeTicks <= 0) {
            this.limitedLifeTicks = 20;
            this.hurt(this.damageSources().starve(), this.getMaxHealth());
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.random.nextInt(40) == 0) {
            this.playSound(SoundEvents.ALLAY_AMBIENT_WITHOUT_ITEM, 0.6F, 1.5F);
        }
    }

    @Override
    protected AABB getAttackBoundingBox() {
        AABB aabb = this.getBoundingBox();
        return aabb.inflate(DEFAULT_ATTACK_REACH, 0.0, DEFAULT_ATTACK_REACH);
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        boolean hurtTarget = super.doHurtTarget(entity);
        if(entity instanceof LivingEntity livingEntity && hurtTarget){
            livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 0, true, true, true));
        }
        this.hurt(this.damageSources().starve(), this.getMaxHealth());
        return hurtTarget;
    }

    @Override
    public void die(DamageSource damageSource) {
        super.die(damageSource);
        this.setInvisible(true);
        if(this.level() instanceof ServerLevel svlevel){
            svlevel.sendParticles(ParticleRegistry.GUIDED_SPIRIT_RELEASE.get(),
                    this.getX(), this.getEyeY() + 0.5F, this.getZ(),
                    1, 0, 0, 0, 0);
        }
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return super.getDeathSound();
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return level.getBlockState(pos).isAir() ? 10.0F : 0.0F;
    }

    @Override
    public LivingEntity getOwner() {
        return this.owner;
    }

    public void setOwner(LivingEntity owner) {
        this.owner = owner;
    }

    @Nullable
    public BlockPos getBoundOrigin() {
        return this.boundOrigin;
    }

    public void setBoundOrigin(@Nullable BlockPos boundOrigin) {
        this.boundOrigin = boundOrigin;
    }

    public void setLimitedLife(int limitedLifeTicks) {
        this.hasLimitedLife = true;
        this.limitedLifeTicks = limitedLifeTicks;
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, level) {
            @Override
            public boolean isStableDestination(BlockPos pos) {
                return !this.level.getBlockState(pos.below()).isAir();
            }
        };
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    @Override
    public void restoreFrom(Entity entity) {
        super.restoreFrom(entity);
        if (entity instanceof GuidedSpiritEntity spirit) {
            this.owner = spirit.getOwner();
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("BoundX")) {
            this.boundOrigin = new BlockPos(compound.getInt("BoundX"), compound.getInt("BoundY"), compound.getInt("BoundZ"));
        }

        if (compound.contains("LifeTicks")) {
            this.setLimitedLife(compound.getInt("LifeTicks"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.boundOrigin != null) {
            compound.putInt("BoundX", this.boundOrigin.getX());
            compound.putInt("BoundY", this.boundOrigin.getY());
            compound.putInt("BoundZ", this.boundOrigin.getZ());
        }

        if (this.hasLimitedLife) {
            compound.putInt("LifeTicks", this.limitedLifeTicks);
        }
    }

    @Override
    public boolean isFlying() {
        return true;
    }

    class GuidedSpiritOwnerTargetGoal extends TargetGoal {
        private final LivingEntity owner;
        private LivingEntity ownerLastHurt;
        private int timestamp;

        public GuidedSpiritOwnerTargetGoal(Mob mob, LivingEntity owner) {
            super(mob, false);
            this.owner = owner;
            this.setFlags(EnumSet.of(Goal.Flag.TARGET));
        }

        @Override
        public boolean canUse() {
            if (this.owner == null) {
                return false;
            } else {
                this.ownerLastHurt = this.owner.getLastHurtMob();
                int i = this.owner.getLastHurtMobTimestamp();
                return i != this.timestamp && this.canAttack(this.ownerLastHurt, TargetingConditions.DEFAULT);
            }
        }

        @Override
        public void start() {
            if (this.owner != null) {
                this.mob.setTarget(this.ownerLastHurt);
                this.timestamp = this.owner.getLastHurtMobTimestamp();
            }
            super.start();
        }
    }
}
