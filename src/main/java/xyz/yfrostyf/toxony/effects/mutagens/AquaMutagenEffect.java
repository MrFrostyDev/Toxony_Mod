package xyz.yfrostyf.toxony.effects.mutagens;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.mutagens.MutagenData;
import xyz.yfrostyf.toxony.api.mutagens.MutagenEffect;
import xyz.yfrostyf.toxony.registries.DataAttachmentRegistry;
import xyz.yfrostyf.toxony.registries.MobEffectRegistry;
import xyz.yfrostyf.toxony.registries.ParticleRegistry;

public class AquaMutagenEffect extends MutagenEffect {
    public static final String WATER_SPLASH_ACTIVE = "water_splash_active";
    public static final String WATER_SPLASH_COOLDOWN = "water_splash_cooldown";
    public static final int DEFAULT_SPLASH_COOLDOWN = 40;

    private static final AttributeModifier SWIM_MODIFIER = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "aqua_mutagen_swim_modifier"), 0.4f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    private static final AttributeModifier SUBMERGEDMINING_MODIFIER = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "aqua_mutagen_submergedmining_modifier"), 4.0f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    private static final AttributeModifier OXYGEN_MODIFIER = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "aqua_mutagen_oxygen_add"), 2.0, AttributeModifier.Operation.ADD_VALUE);
    private static final AttributeModifier OXYGEN_ENHANCED_MODIFIER = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "aqua_mutagen_oxygen_mul"), 2.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

    public AquaMutagenEffect(MobEffectCategory category) {
        super(category, 0xffffff);
    }

    @Override
    public void onEffectStarted(LivingEntity entity, int amplifier) {
        if(amplifier >= 0){
            addModifier(entity, Attributes.OXYGEN_BONUS, OXYGEN_MODIFIER);
        }
        if(amplifier >= 1){
            addModifier(entity, Attributes.SUBMERGED_MINING_SPEED, SUBMERGEDMINING_MODIFIER);
            addModifier(entity, net.neoforged.neoforge.common.NeoForgeMod.SWIM_SPEED, SWIM_MODIFIER);
        }
        if(amplifier >= 2){
            addModifier(entity, Attributes.OXYGEN_BONUS, OXYGEN_ENHANCED_MODIFIER);
        }
    }

    @Override
    public void removeModifiers(LivingEntity entity) {
        super.removeModifiers(entity);
        removeModifier(entity, Attributes.OXYGEN_BONUS, OXYGEN_MODIFIER);
        removeModifier(entity, Attributes.SUBMERGED_MINING_SPEED, SUBMERGEDMINING_MODIFIER);
        removeModifier(entity, net.neoforged.neoforge.common.NeoForgeMod.SWIM_SPEED, SWIM_MODIFIER);
        removeModifier(entity, Attributes.OXYGEN_BONUS, OXYGEN_ENHANCED_MODIFIER);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {return true;}

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        MutagenData mutagenData = livingEntity.getData(DataAttachmentRegistry.MUTAGEN_DATA);

        if(amplifier >= 2 && !livingEntity.level().isClientSide){
            if(livingEntity.isInWaterRainOrBubble() && !livingEntity.hasEffect(MobEffects.REGENERATION)){
                livingEntity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20, 0, false, false));
            }
            if(!mutagenData.getBool(WATER_SPLASH_ACTIVE)){
                int cooldown = mutagenData.getInt(WATER_SPLASH_COOLDOWN);
                mutagenData.addInt(WATER_SPLASH_COOLDOWN, cooldown-1);
                if(cooldown <= 0){
                    mutagenData.addBool(WATER_SPLASH_ACTIVE, true);
                }
            }
        }
        return true;
    }

    @EventBusSubscriber
    public static class AquaMutagenEvents {

        @SubscribeEvent
        public static void onMutagenDamaged(LivingDamageEvent.Pre event){
            MobEffectInstance victimMutagen = event.getEntity().getEffect(MobEffectRegistry.AQUA_MUTAGEN);
            if (victimMutagen == null)return;

            if (victimMutagen.getAmplifier() >= 2){
                Holder<DamageType> damageType = event.getSource().typeHolder();
                if(damageType.is(DamageTypeTags.IS_FIRE)){
                    event.setNewDamage(event.getOriginalDamage() * 1.3F);
                }
            }
        }

        @SubscribeEvent
        public static void onMobKnockbackSplash(LivingKnockBackEvent event){
            LivingEntity victimEntity = event.getEntity();
            LivingEntity causingEntity = victimEntity.getLastHurtByMob();
            if(causingEntity == null || !causingEntity.hasEffect(MobEffectRegistry.AQUA_MUTAGEN) || !(event.getEntity().level() instanceof ServerLevel svlevel)) return;

            MobEffectInstance causingMutagen = causingEntity.getEffect(MobEffectRegistry.AQUA_MUTAGEN);
            int timeDifference = victimEntity.tickCount - victimEntity.getLastHurtByMobTimestamp();

            if(timeDifference == 0 && causingMutagen.getAmplifier() >= 2){
                MutagenData causingMutagenData = causingEntity.getData(DataAttachmentRegistry.MUTAGEN_DATA);
                if(causingMutagenData.getBool(WATER_SPLASH_ACTIVE)){
                    event.setStrength(event.getOriginalStrength() * 3.0F);
                    svlevel.sendParticles(ParticleRegistry.AQUA_SPLASH.get(),
                            victimEntity.getX(), victimEntity.getEyeY() - 0.2F, victimEntity.getZ(),
                            1, 0.1, 0.1, 0.1, 0);
                    svlevel.sendParticles(ParticleTypes.SPLASH,
                            victimEntity.getX(), victimEntity.getEyeY() - 0.2F, victimEntity.getZ(),
                            30, 0.3, 0.3, 0.3, 0);
                    svlevel.playSound(
                            null, victimEntity,
                            SoundEvents.GENERIC_SPLASH, SoundSource.NEUTRAL,
                            1.5F, 0.9F
                    );

                    causingMutagenData.addBool(WATER_SPLASH_ACTIVE, false);
                    causingMutagenData.addInt(WATER_SPLASH_COOLDOWN, DEFAULT_SPLASH_COOLDOWN);
                }
            }
        }
    }
}
