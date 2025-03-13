package xyz.yfrostyf.toxony.entities.item;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import xyz.yfrostyf.toxony.api.oils.Oil;
import xyz.yfrostyf.toxony.registries.ItemRegistry;
import xyz.yfrostyf.toxony.registries.OilsRegistry;
import xyz.yfrostyf.toxony.registries.ParticleRegistry;

import javax.annotation.Nullable;
import java.util.List;

public class SmokeBolt extends Bolt {
    public static final double SPLASH_RANGE = 4.0;
    private static final double SPLASH_RANGE_SQ = 16.0;

    public SmokeBolt(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public SmokeBolt(Level level, LivingEntity owner, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(level, owner, pickupItemStack, firedFromWeapon);
    }

    public SmokeBolt(Level level, double x, double y, double z, ItemStack itemStack, @Nullable ItemStack firedFromWeapon) {
        super(level, x, y, z, itemStack, firedFromWeapon);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        this.applySmoke();
        if(this.level() instanceof ServerLevel svlevel){
            svlevel.playSound(null, result.getLocation().x, result.getLocation().y, result.getLocation().z, SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.NEUTRAL);
            svlevel.sendParticles(ParticleRegistry.SMOKE.get(),
                    result.getLocation().x, result.getLocation().y+1.0, result.getLocation().z,
                    3, 0.5, 0.5, 0.5, this.random.nextInt(4) * 0.1);
        }
    }

    private void applySmoke() {
        AABB aabb = this.getBoundingBox().inflate(SPLASH_RANGE, 2.0, SPLASH_RANGE);
        List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, aabb);
        if (!list.isEmpty()) {
            for (LivingEntity livingentity : list) {
                if (livingentity.isAffectedByPotions()) {
                    double dist = this.distanceToSqr(livingentity);
                    if (dist < SPLASH_RANGE_SQ) {
                        Oil smokeOil = OilsRegistry.SMOKE_OIL.get();
                        for(Holder<MobEffect> holder : smokeOil.getEffects()){
                            MobEffectInstance mobeffectInstOil = new MobEffectInstance(holder, 100, 1);
                            livingentity.addEffect(mobeffectInstOil, this.getEffectSource());
                        }
                    }
                }
            }
        }
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ItemRegistry.SMOKE_BOLT);
    }
}
