package xyz.yfrostyf.toxony.mutagens;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.mutagens.MutagenEffect;
import xyz.yfrostyf.toxony.registries.MobEffectRegistry;
import xyz.yfrostyf.toxony.registries.MutagenRegistry;
import java.util.Random;

public class WolfMutagenEffect extends MutagenEffect {
    private static final AttributeModifier damageBoostModifier = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "wolf_mutagen_damage_modifier"), 0.2f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    private static final AttributeModifier speedModifier = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "wolf_mutagen_speed_modifier"), 0.2f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    private static final AttributeModifier sneakSpeedModifier = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "wolf_mutagen_sneakspeed_modifier"), 0.4f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

    public WolfMutagenEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void onEffectStarted(LivingEntity entity, int amplifier) {
        addModifier(entity, Attributes.ATTACK_DAMAGE, damageBoostModifier);

        if(amplifier >= 1){
            addModifier(entity, Attributes.MOVEMENT_SPEED, speedModifier);
            addModifier(entity, Attributes.SNEAKING_SPEED, sneakSpeedModifier);
        }
    }

    @EventBusSubscriber
    public static class WolfMutagenEvents {

        @SubscribeEvent
        public static void onEffectRemove(MobEffectEvent.Remove event){
            MobEffectInstance effectInst = event.getEffectInstance();

            if(effectInst == null){return;}
            if(!effectInst.is(MutagenRegistry.WOLF_MUTAGEN)){return;}

            removeModifier(event.getEntity(), Attributes.ATTACK_DAMAGE, damageBoostModifier);

            if(effectInst.getAmplifier() >= 1) {
                removeModifier(event.getEntity(), Attributes.MOVEMENT_SPEED, speedModifier);
                removeModifier(event.getEntity(), Attributes.SNEAKING_SPEED, sneakSpeedModifier);
            }
        }

        @SubscribeEvent
        public static void onLivingDamage(LivingDamageEvent.Post event){
            if (event.getSource().getEntity() == null) {return;}
            if (!(event.getSource().getEntity() instanceof LivingEntity entity)){return;}

            MobEffectInstance attackerMutagen = entity.getEffect(MutagenRegistry.WOLF_MUTAGEN);

            if(attackerMutagen == null){return;}
            if(attackerMutagen.getAmplifier() < 2){return;}

            if(new Random().nextInt(4 ) == 0){
                event.getEntity().addEffect(new MobEffectInstance(MobEffectRegistry.HUNT, 120, 0));
                entity.playSound(SoundEvents.WOLF_GROWL, 6, 1);
            }

            ToxonyMain.LOGGER.debug("[WolfMutagen Attack]: damage: {}",event.getNewDamage());
        }
    }
}
