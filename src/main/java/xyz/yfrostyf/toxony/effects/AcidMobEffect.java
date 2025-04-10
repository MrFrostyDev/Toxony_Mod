package xyz.yfrostyf.toxony.effects;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.EffectCure;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import xyz.yfrostyf.toxony.damages.AcidDamageSource;
import xyz.yfrostyf.toxony.registries.MobEffectRegistry;

import java.util.Set;

public class AcidMobEffect extends MobEffect {
    private static final int color = 0x20b809;
    private static final int BASE_TICK = 20;

    public void fillEffectCures(Set<EffectCure> cures, MobEffectInstance effectInstance) {
        cures.clear();
    }

    public AcidMobEffect(MobEffectCategory category) {
        super(category, color);
    }

    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if(entity.level().isClientSide()) return true;

        int tick = entity.level().getServer().getTickCount();
        if(tick % BASE_TICK == 0){
            entity.hurt(new AcidDamageSource(
                    entity.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(DamageTypes.MAGIC), null),
                    1.0F + amplifier
            );
        }
        return true;
    }

    @EventBusSubscriber
    public static class AcidEvents {
        @SubscribeEvent
        public static void onIncomingDamage(LivingIncomingDamageEvent event){
            LivingEntity victim = event.getEntity();
            MobEffectInstance effectInst = victim.getEffect(MobEffectRegistry.ACID);
            if (effectInst == null) return;
            event.addReductionModifier(
                    DamageContainer.Reduction.ARMOR,
                    (container, reduction) ->
                            reduction * Math.max(1.0F - (effectInst.getAmplifier() + 1) * 0.25F, 0)
            );
        }
    }
}
