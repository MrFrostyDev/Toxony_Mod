package xyz.yfrostyf.toxony.effects.mutagens;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.mutagens.MutagenEffect;
import xyz.yfrostyf.toxony.registries.MobEffectRegistry;

public class SpiderMutagenEffect extends MutagenEffect {
    private static final AttributeModifier MOVEEFFICIENCY_MODIFIER = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "spider_mutagen_moveeff_modifier"), 1.5f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    private static final float SPIDER_CLIMB_RATE = 0.1f;

    public SpiderMutagenEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void onEffectStarted(LivingEntity entity, int amplifier) {
        addModifier(entity, Attributes.MOVEMENT_EFFICIENCY, MOVEEFFICIENCY_MODIFIER);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if(entity.horizontalCollision && amplifier >= 3) {
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
        return true;
    }

    @Override
    public void removeModifiers(LivingEntity entity) {
        removeModifier(entity, Attributes.MOVEMENT_EFFICIENCY, MOVEEFFICIENCY_MODIFIER);
    }

    @EventBusSubscriber
    public static class SpiderMutagenEvents {

        @SubscribeEvent
        public static void onDamageMutagenAttacker(LivingDamageEvent.Post event){
            if (event.getSource().getEntity() == null || !(event.getSource().getEntity() instanceof LivingEntity entity))return;
            MobEffectInstance attackerMutagen = entity.getEffect(MobEffectRegistry.SPIDER_MUTAGEN);

            if(attackerMutagen == null){return;}
            if(attackerMutagen.getAmplifier() < 1){return;}

            if(!event.getEntity().hasEffect(MobEffects.POISON)){
                if(RANDOM.nextInt(4 ) == 0){
                    event.getEntity().addEffect(new MobEffectInstance(MobEffects.POISON, 180, 0));
                    entity.playSound(SoundEvents.BEE_STING, 3, 1);
                }
            }
        }
    }
}
