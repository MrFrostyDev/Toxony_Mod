package xyz.yfrostyf.toxony.effects;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import xyz.yfrostyf.toxony.registries.MobEffectRegistry;

import java.util.Random;

public class HuntMobEffect extends MobEffect {
    private static final int color = 0x6e2f2B;

    public HuntMobEffect(MobEffectCategory category) {
        super(category, color);
    }

    @Override
    public void onMobHurt(LivingEntity livingEntity, int amplifier, DamageSource damageSource, float amount) {

    }

    @EventBusSubscriber
    public static class HuntEvents {

        @SubscribeEvent
        public static void onLivingDamageWithHunt(LivingDamageEvent.Pre event){
            LivingEntity victim = event.getEntity();
            MobEffectInstance effectInst = victim.getEffect(MobEffectRegistry.HUNT);
            if (effectInst == null) return;
            if(event.getSource().getEntity() instanceof LivingEntity attacker){
                if(attacker.hasEffect(MobEffectRegistry.WOLF_MUTAGEN) || attacker.getType() == EntityType.WOLF){
                    float damage = event.getOriginalDamage();
                    float dmgMod = attacker.getType() == EntityType.WOLF
                            ? 0.5F + ((float)effectInst.getAmplifier()/4)
                            : 0.25F + ((float)effectInst.getAmplifier()/4);
                    event.setNewDamage(damage + (damage*dmgMod));
                }
            }
        }
    }
}
