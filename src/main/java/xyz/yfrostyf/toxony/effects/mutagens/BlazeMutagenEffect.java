package xyz.yfrostyf.toxony.effects.mutagens;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import xyz.yfrostyf.toxony.api.mutagens.MutagenEffect;
import xyz.yfrostyf.toxony.registries.MobEffectRegistry;

public class BlazeMutagenEffect extends MutagenEffect {
    public BlazeMutagenEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if(entity.isOnFire() && amplifier >= 1){
            entity.clearFire();
        }
        return true;
    }

    @EventBusSubscriber
    public static class BlazeMutagenEvents {

        @SubscribeEvent
        public static void onMutagenDamaged(LivingDamageEvent.Pre event){
            if(!(event.getEntity().level() instanceof ServerLevel svlevel))return;

            MobEffectInstance victimMutagen = event.getEntity().getEffect(MobEffectRegistry.BLAZE_MUTAGEN);
            DamageType damageType = event.getSource().type();
            HolderLookup.RegistryLookup<DamageType> registryAccess = svlevel.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE);

            if(victimMutagen == null)return;

            if(event.getSource().getEntity() instanceof LivingEntity attacker && RANDOM.nextInt(3 ) == 0){
                if(victimMutagen.getAmplifier() >= 3){
                    attacker.igniteForSeconds(12);
                }
                else{
                    attacker.igniteForSeconds(6);
                }
            }
            if(victimMutagen.getAmplifier() >= 1){
                if (damageType == registryAccess.getOrThrow(DamageTypes.IN_FIRE).value()
                        || damageType == registryAccess.getOrThrow(DamageTypes.ON_FIRE).value()
                        || damageType == registryAccess.getOrThrow(DamageTypes.CAMPFIRE).value()){
                    event.setNewDamage(0);
                }
            }
        }

        @SubscribeEvent
        public static void onDamageMutagenAttacker(LivingDamageEvent.Post event){
            if(!(event.getSource().getEntity() instanceof LivingEntity livingEntity))return;
            MobEffectInstance attackerMutagen = livingEntity.getEffect(MobEffectRegistry.BLAZE_MUTAGEN);
            if(attackerMutagen == null || attackerMutagen.getAmplifier() < 2)return;
            event.getEntity().igniteForSeconds(6);
        }
    }
}
