package xyz.yfrostyf.toxony.effects.mutagens;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.mutagens.MutagenEffect;
import xyz.yfrostyf.toxony.registries.MobEffectRegistry;

public class HollowMutagenEffect extends MutagenEffect {
    private static final AttributeModifier MOVEEFFICIENCY_MODIFIER = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "hollow_mutagen_moveeff_modifier"), 1.5f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    private static final AttributeModifier KNOCKBACK_MODIFIER = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "hollow_mutagen_knockbackresist_add"), 0.2F, AttributeModifier.Operation.ADD_VALUE);
    private static final AttributeModifier KNOCKBACK_ENHANCED_MODIFIER = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "hollow_mutagen_knockbackresist_add"), 0.3F, AttributeModifier.Operation.ADD_VALUE);

    private static final float SPIDER_CLIMB_RATE = 0.1f;

    public HollowMutagenEffect(MobEffectCategory category) {
        super(category, 0xffffff);
    }

    @Override
    public void onEffectStarted(LivingEntity entity, int amplifier) {
        addModifier(entity, Attributes.MOVEMENT_EFFICIENCY, MOVEEFFICIENCY_MODIFIER);
        addModifier(entity, Attributes.KNOCKBACK_RESISTANCE, KNOCKBACK_MODIFIER);

        if(amplifier >= 2){
            addModifier(entity, Attributes.KNOCKBACK_RESISTANCE, KNOCKBACK_ENHANCED_MODIFIER);
        }
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if(amplifier >= 2) {
            if(entity.horizontalCollision) {
                entity.fallDistance = 0.0F;
                final float maxVel = 0.15F;

                Vec3 deltaMovement = entity.getDeltaMovement();

                double velX = Math.clamp(deltaMovement.x, -maxVel, maxVel);
                double velY = SPIDER_CLIMB_RATE;
                double velZ = Math.clamp(deltaMovement.z, -maxVel, maxVel);
                if(entity.isSuppressingSlidingDownLadder()) {
                    velY = 0.0;
                }

                entity.setDeltaMovement(velX, velY, velZ);
            }

            if(entity.tickCount % 20 == 0){
                entity.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 240, 0, false, false, false));
                if(isUnderSunTick(entity.level(), entity)){
                    entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 0, true, false, false));
                }
            }
        }
        return true;
    }

    // Minecraft's default implementation of Zombie#isSunBurnTick
    private static boolean isUnderSunTick(Level level, LivingEntity entity) {
        if (level.isDay() && !level.isClientSide) {
            float f = entity.getLightLevelDependentMagicValue();
            BlockPos blockpos = BlockPos.containing(entity.getX(), entity.getEyeY(), entity.getZ());
            boolean flag = entity.isInWaterRainOrBubble() || entity.isInPowderSnow || entity.wasInPowderSnow;
            if (f > 0.5F && !flag && level.canSeeSky(blockpos)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void removeModifiers(LivingEntity entity) {
        super.removeModifiers(entity);
        removeModifier(entity, Attributes.MOVEMENT_EFFICIENCY, MOVEEFFICIENCY_MODIFIER);
        removeModifier(entity, Attributes.KNOCKBACK_RESISTANCE, KNOCKBACK_MODIFIER);
        removeModifier(entity, Attributes.KNOCKBACK_RESISTANCE, KNOCKBACK_ENHANCED_MODIFIER);
    }

    @EventBusSubscriber
    public static class HollowMutagenEvents {

        @SubscribeEvent
        public static void onHollowDamaged(LivingDamageEvent.Pre event){
            LivingEntity livingEntity = event.getEntity();
            MobEffectInstance victimMutagen = livingEntity.getEffect(MobEffectRegistry.HOLLOW_MUTAGEN);

            if(victimMutagen == null)return;
            if(victimMutagen.getAmplifier() >= 1){
                event.setNewDamage(event.getOriginalDamage() * 0.85F);
            }
        }
    }
}
