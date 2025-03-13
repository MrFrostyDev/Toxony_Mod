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
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.registries.MobEffectRegistry;

import java.util.Random;

public class HuntMobEffect extends MobEffect {
    private static final int color = 0x6e2f2B;
    private static final Random RANDOM = new Random();

    public HuntMobEffect(MobEffectCategory category) {
        super(category, color);
    }

    @Override
    public void onMobHurt(LivingEntity livingEntity, int amplifier, DamageSource damageSource, float amount) {
        if(!(damageSource.getEntity() instanceof LivingEntity sourceLivingEntity))return;
        if(sourceLivingEntity.hasEffect(MobEffectRegistry.WOLF_MUTAGEN) || sourceLivingEntity.getType() == EntityType.WOLF){
            float dmgMod = sourceLivingEntity.getType() == EntityType.WOLF
                    ? 0.5F + ((float)amplifier/4)
                    : 0.25F + ((float)amplifier/4);
            livingEntity.hurt(damageSource, amount + (amount*dmgMod));
        }
    }
}
