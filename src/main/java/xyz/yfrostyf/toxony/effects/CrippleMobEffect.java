package xyz.yfrostyf.toxony.effects;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.registries.MobEffectRegistry;
import xyz.yfrostyf.toxony.registries.ParticleRegistry;

public class CrippleMobEffect extends MobEffect {
    private static final AttributeModifier SPEED_MODIFIER = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "cripple_speed_modifier"), -0.25F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    private static final int color = 0x6e2f2B;


    public CrippleMobEffect(MobEffectCategory category) {
        super(category, color);
    }

    @Override
    public ParticleOptions createParticleOptions(MobEffectInstance effect) {
        return ParticleRegistry.BLOOD_DRIP.get();
    }

    @Override
    public void onEffectStarted(LivingEntity entity, int amplifier) {
        AttributeInstance atrInstance = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (atrInstance != null){
            atrInstance.addOrReplacePermanentModifier(SPEED_MODIFIER);
        }
    }

    @EventBusSubscriber
    public static class CrippleEvents {
        @SubscribeEvent
        public static void onEffectRemove(MobEffectEvent.Remove event){
            MobEffectInstance effectInst = event.getEffectInstance();
            AttributeInstance atrInstance = event.getEntity().getAttribute(Attributes.MOVEMENT_SPEED);
            if(effectInst != null && atrInstance != null && effectInst.is(MobEffectRegistry.CRIPPLE)){
                atrInstance.removeModifier(SPEED_MODIFIER);
            }
        }

        @SubscribeEvent
        public static void onLivingDamageWithCripple(LivingDamageEvent.Pre event){
            LivingEntity victim = event.getEntity();
            MobEffectInstance effectInst = victim.getEffect(MobEffectRegistry.CRIPPLE);
            if (effectInst == null) return;
            float damage = event.getOriginalDamage();
            DamageSource source = event.getSource();
            float dmgMod = 0;

            if(source.is(DamageTypes.GENERIC) || source.is(DamageTypes.PLAYER_ATTACK)
                    || source.is(DamageTypes.ARROW) || source.is(DamageTypes.TRIDENT)
                    || source.is(DamageTypes.MOB_ATTACK) || source.is(DamageTypes.MOB_PROJECTILE)){
                dmgMod = 0.25F + ((float)effectInst.getAmplifier() / 4);
                event.setNewDamage(damage + (damage * dmgMod));
            }
            else if(source.is(DamageTypes.FALL)) {
                dmgMod = 0.5F + ((float)effectInst.getAmplifier() / 2);
            }
            event.setNewDamage(damage + (damage * dmgMod));
        }
    }
}
