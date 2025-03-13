package xyz.yfrostyf.toxony.effects;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.EffectCure;
import xyz.yfrostyf.toxony.damages.ToxinDamageSource;
import java.util.Set;

public class ToxinMobEffect extends MobEffect {
    private static final int color = 0x20b809;
    private static final int BASE_TOXIN_TICK = 50;

    public void fillEffectCures(Set<EffectCure> cures, MobEffectInstance effectInstance) {
        cures.clear();
    }

    public ToxinMobEffect(MobEffectCategory category) {
        super(category, color);
    }

    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if(entity.level().isClientSide()) return true;

        int tick = entity.level().getServer().getTickCount();
        if(tick % getTickRateFromAmp(amplifier) == 0){
            entity.hurt(new ToxinDamageSource(
                    entity.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(DamageTypes.MAGIC), null),
                    3.0F + amplifier
            );
        }
        return true;
    }

    private int getTickRateFromAmp(int amplifier){
        return (int)(BASE_TOXIN_TICK/((0.5*amplifier)+1));
    }
}
