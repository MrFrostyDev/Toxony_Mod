package xyz.yfrostyf.toxony.entities.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import xyz.yfrostyf.toxony.api.oils.ItemOil;
import xyz.yfrostyf.toxony.registries.*;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class ThrownOilPot extends ThrowableItemProjectile implements ItemSupplier {
    public static final double SPLASH_RANGE = 4.0;
    private static final double SPLASH_RANGE_SQ = 16.0;
    private ItemStack itemStack;

    public ThrownOilPot(EntityType<? extends ThrownOilPot> entityType, Level level) {
        super(entityType, level);
    }

    public ThrownOilPot(Level level, LivingEntity shooter, ItemStack itemStack) {
        super(EntityRegistry.OIL_POT.get(), shooter, level);
        this.itemStack = itemStack;
    }

    public ThrownOilPot(Level level, ItemStack itemStack, double x, double y, double z) {
        super(EntityRegistry.OIL_POT.get(), x, y, z, level);
        this.itemStack = itemStack;
    }

    @Override
    protected Item getDefaultItem() {
        return ItemRegistry.EMPTY_OIL_POT.get();
    }

    public ItemStack getItem(){
        return new ItemStack(this.getDefaultItem());
    }

    @Override
    protected double getDefaultGravity() {
        return 0.04;
    }

    // Thank you Farmer's Delight for this method and a reminder to register this entity.
    @Override
    public void handleEntityEvent(byte id) {
        ItemStack entityStack = new ItemStack(this.getDefaultItem());
        if (id == 3) {
            ParticleOptions itemParticleOption = new ItemParticleOption(ParticleTypes.ITEM, entityStack);

            for (int i = 0; i < 12; ++i) {
                this.level().addParticle(itemParticleOption, this.getX(), this.getY(), this.getZ(),
                        ((double) this.random.nextFloat() * 2.0D - 1.0D) * 0.1F,
                        ((double) this.random.nextFloat() * 2.0D - 1.0D) * 0.1F + 0.1F,
                        ((double) this.random.nextFloat() * 2.0D - 1.0D) * 0.1F);
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level().isClientSide) {
            Direction direction = result.getDirection();
            BlockPos blockpos = result.getBlockPos();
            ItemOil itemOil = this.itemStack.getOrDefault(DataComponentsRegistry.OIL, ItemOil.EMPTY);
            if (!itemOil.isEmpty()) {
                this.placeOilBlock(blockpos);
                this.placeOilBlock(blockpos.relative(direction.getOpposite()));
                for (BlockPos blockNeighbourPos : Set.of(blockpos.north(), blockpos.east(), blockpos.south(), blockpos.west())) {
                    this.placeOilBlock(blockNeighbourPos);
                }
            }
        }
    }

    private void placeOilBlock(BlockPos pos) {
        BlockState blockstate = this.level().getBlockState(pos);
        BlockState blockstateUp = this.level().getBlockState(pos.above());
        BlockState blockstateDown = this.level().getBlockState(pos.below());
        BlockState oilblockstate = BlockRegistry.OIL_LAYER.get().defaultBlockState();
        if(blockstateDown.is(BlockRegistry.OIL_LAYER)) return;
        if(blockstateUp.isEmpty() && blockstate.isFaceSturdy(this.level(), pos, Direction.UP)){
            this.level().setBlockAndUpdate(pos.above(), oilblockstate);
        }
        else if(this.level().isEmptyBlock(pos) && blockstateDown.isFaceSturdy(this.level(), pos.below(), Direction.UP)){
            this.level().setBlockAndUpdate(pos, oilblockstate);
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (this.level() instanceof ServerLevel svlevel) {
            ItemStack itemstack = this.itemStack;
            ItemOil itemOil = itemstack.getOrDefault(DataComponentsRegistry.OIL, ItemOil.EMPTY);
            svlevel.playSound(null, result.getLocation().x, result.getLocation().y, result.getLocation().z, SoundEvents.DECORATED_POT_SHATTER, SoundSource.NEUTRAL);
            svlevel.sendParticles(ParticleRegistry.OIL_SMOKE.get(),
                    result.getLocation().x, result.getLocation().y+0.5, result.getLocation().z,
                    1, 0, 0, 0, 0);
            if (!itemOil.isEmpty()) {
                this.applySplash(
                        itemOil, result.getType() == HitResult.Type.ENTITY ? ((EntityHitResult)result).getEntity() : null
                );
            }
            this.discard();
        }
    }
    private void applySplash(ItemOil itemOil, @Nullable Entity p_entity) {
        AABB aabb = this.getBoundingBox().inflate(SPLASH_RANGE, 2.0, SPLASH_RANGE);
        List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, aabb);
        if (!list.isEmpty()) {
            for (LivingEntity livingentity : list) {
                if (livingentity.isAffectedByPotions()) {
                    double dist = this.distanceToSqr(livingentity);
                    if (dist < SPLASH_RANGE_SQ) {
                        double distMult;
                        if (livingentity == p_entity) {
                            distMult = 1.0;
                        } else {
                            distMult = 1.0 - Math.sqrt(dist) / SPLASH_RANGE;
                        }
                        MobEffectInstance mobeffectInstOil = new MobEffectInstance(MobEffectRegistry.FLAMMABLE, 600, 0);
                        livingentity.addEffect(mobeffectInstOil, this.getEffectSource());
                        for (Holder<MobEffect> holder : itemOil.getOil().getEffects()) {
                            MobEffectInstance mobeffectInst = new MobEffectInstance(holder, 400, itemOil.amplifier());
                            if (holder.value().isInstantenous()) {
                                holder.value().applyInstantenousEffect(this, this.getOwner(), livingentity, mobeffectInst.getAmplifier(), distMult);
                            } else {
                                int i = mobeffectInst.mapDuration(duration -> (int)(distMult * (double)duration + 0.5));
                                MobEffectInstance modifedMobEffectInstance = new MobEffectInstance(
                                        holder, i, mobeffectInst.getAmplifier(), mobeffectInst.isAmbient(), mobeffectInst.isVisible()
                                );
                                if (!modifedMobEffectInstance.endsWithin(20)) {
                                    livingentity.addEffect(modifedMobEffectInstance, this.getEffectSource());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public EntityType<?> getType() {
        return EntityRegistry.OIL_POT.get();
    }
}
