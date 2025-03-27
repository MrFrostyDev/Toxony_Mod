package xyz.yfrostyf.toxony.effects.mutagens;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.mutagens.MutagenEffect;
import xyz.yfrostyf.toxony.registries.MobEffectRegistry;

public class WolfMutagenEffect extends MutagenEffect {
    private static final AttributeModifier DAMAGEBOOST_MODIFIER = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "wolf_mutagen_damage_modifier"), 0.2f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    private static final AttributeModifier SPEED_MODIFIER = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "wolf_mutagen_speed_modifier"), 0.2f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    private static final AttributeModifier SNEAKSPEED_MODIFIER = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "wolf_mutagen_sneakspeed_modifier"), 0.4f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);


    public WolfMutagenEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void onEffectStarted(LivingEntity entity, int amplifier) {
        addModifier(entity, Attributes.ATTACK_DAMAGE, DAMAGEBOOST_MODIFIER);

        if(amplifier >= 1){
            addModifier(entity, Attributes.MOVEMENT_SPEED, SPEED_MODIFIER);
            addModifier(entity, Attributes.SNEAKING_SPEED, SNEAKSPEED_MODIFIER);
        }
    }

    @EventBusSubscriber
    public static class WolfMutagenEvents {

        @SubscribeEvent
        public static void onMutagenRemove(MobEffectEvent.Remove event){
            MobEffectInstance effectInst = event.getEffectInstance();
            if(effectInst == null || !effectInst.is(MobEffectRegistry.WOLF_MUTAGEN))return;

            removeModifier(event.getEntity(), Attributes.ATTACK_DAMAGE, DAMAGEBOOST_MODIFIER);

            if(effectInst.getAmplifier() >= 1) {
                removeModifier(event.getEntity(), Attributes.MOVEMENT_SPEED, SPEED_MODIFIER);
                removeModifier(event.getEntity(), Attributes.SNEAKING_SPEED, SNEAKSPEED_MODIFIER);
            }
        }

        @SubscribeEvent
        public static void onDamageMutagenAttacker(LivingDamageEvent.Post event){
            if (event.getSource().getEntity() == null || !(event.getSource().getEntity() instanceof LivingEntity entity)) return;
            MobEffectInstance attackerMutagen = entity.getEffect(MobEffectRegistry.WOLF_MUTAGEN);

            if(attackerMutagen == null)return;
            if(attackerMutagen.getAmplifier() < 2)return;

            if(RANDOM.nextInt(4 ) == 0){
                event.getEntity().addEffect(new MobEffectInstance(MobEffectRegistry.HUNT, 120, 0));
                entity.playSound(SoundEvents.WOLF_GROWL, 6, 1);
            }
        }
    }

}
