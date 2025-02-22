package xyz.yfrostyf.toxony.mutagens;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.mutagens.MutagenEffect;
import xyz.yfrostyf.toxony.registries.MobEffectRegistry;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TurtleMutagenEffect extends MutagenEffect {
    private static final AttributeModifier swimModifier = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "turtle_mutagen_swim_modifier"), 0.3f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    private static final AttributeModifier oxygenModifier = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "turtle_mutagen_oxygen_add"), 2.0, AttributeModifier.Operation.ADD_VALUE);
    private static final AttributeModifier knockbackModifier = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "turtle_mutagen_knockbackresist_add"), 2.0f, AttributeModifier.Operation.ADD_VALUE);

    private static final List<ResourceKey<DamageType>> damageTypeExceptions = Arrays.asList(
            DamageTypes.DROWN, DamageTypes.WIND_CHARGE, DamageTypes.DRAGON_BREATH,
            DamageTypes.FREEZE, DamageTypes.FLY_INTO_WALL, DamageTypes.ON_FIRE, DamageTypes.HOT_FLOOR
    );

    private static final float TURTLE_DAMAGE_MODIFIER = 0.85f;
    private static final Random RANDOM = new Random();


    public TurtleMutagenEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void onEffectStarted(LivingEntity entity, int amplifier) {
        if(amplifier >= 1){
            addModifier(entity, net.neoforged.neoforge.common.NeoForgeMod.SWIM_SPEED, swimModifier);
            addModifier(entity, Attributes.OXYGEN_BONUS, oxygenModifier);
        }
        if(amplifier >= 2){
            addModifier(entity, Attributes.KNOCKBACK_RESISTANCE, knockbackModifier);
        }
    }

    @Override
    public void onMobHurt(LivingEntity livingEntity, int amplifier, DamageSource damageSource, float amount) {

    }

    @EventBusSubscriber
    public static class TurtleMutagenEvents {

        @SubscribeEvent
        public static void onEffectRemove(MobEffectEvent.Remove event){
            MobEffectInstance effectInst = event.getEffectInstance();

            if(effectInst == null){return;}
            if(!effectInst.is(MobEffectRegistry.TURTLE_MUTAGEN)){return;}

            if(effectInst.getAmplifier() >= 1) {
                removeModifier(event.getEntity(), net.neoforged.neoforge.common.NeoForgeMod.SWIM_SPEED, swimModifier);
                removeModifier(event.getEntity(), Attributes.OXYGEN_BONUS, oxygenModifier);
            }
            if(effectInst.getAmplifier() >= 2) {
                removeModifier(event.getEntity(), Attributes.KNOCKBACK_RESISTANCE, knockbackModifier);
            }
        }

        @SubscribeEvent
        public static void onLivingDamage(LivingDamageEvent.Pre event){
            MobEffectInstance victimMutagen = event.getEntity().getEffect(MobEffectRegistry.TURTLE_MUTAGEN);

            if (victimMutagen == null) {return;}

            event.setNewDamage(event.getOriginalDamage() * TURTLE_DAMAGE_MODIFIER);

            if (victimMutagen.getAmplifier() < 2){return;}

            Holder<DamageType> damageType = event.getSource().typeHolder();
            if (!damageTypeExceptions.contains(damageType) && !(event.getEntity().hasEffect(MobEffects.DAMAGE_RESISTANCE))){
                if(RANDOM.nextInt(5 ) == 0){
                    event.getEntity().addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 120, 0, false, true, true));
                    event.getEntity().playSound(SoundEvents.SHIELD_BLOCK, 6, 1.2f);
                }
            }
        }
    }
}
