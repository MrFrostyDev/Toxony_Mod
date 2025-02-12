package xyz.yfrostyf.toxony.effects;

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

    public HuntMobEffect(MobEffectCategory category) {
        super(category, color);
    }

    @EventBusSubscriber
    public static class HuntEvents {

        @SubscribeEvent
        public static void onLivingDamage(LivingDamageEvent.Pre event){
            if(!(event.getSource().getEntity() instanceof LivingEntity attacker)){return;}
            LivingEntity victim = event.getEntity();


            if (attacker.hasEffect(MobEffectRegistry.WOLF_MUTAGEN)){
                MobEffectInstance attMutagen = attacker.getEffect(MobEffectRegistry.WOLF_MUTAGEN);

                if(attMutagen == null){return;}
                if(attMutagen.getAmplifier() < 2){return;}
                if(new Random().nextInt(4 ) == 0){
                    event.getEntity().addEffect(new MobEffectInstance(MobEffectRegistry.HUNT, 200, 0));
                }
            }

            if (!victim.hasEffect(MobEffectRegistry.HUNT)){return;}
            if (attacker.hasEffect(MobEffectRegistry.WOLF_MUTAGEN) || attacker.getType() == EntityType.WOLF){
                float amp = victim.getEffect(MobEffectRegistry.HUNT).getAmplifier();
                float dmgMod = attacker.getType() == EntityType.WOLF ? 0.5f+(amp/4) : 0.25f+(amp/4);

                event.setNewDamage(event.getOriginalDamage() + (event.getOriginalDamage()*dmgMod));
            }

            ToxonyMain.LOGGER.info("[WolfMutagen Attack]: damage: {}",event.getNewDamage());
        }
    }
}
