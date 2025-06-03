package xyz.yfrostyf.toxony.effects.mutagens;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import xyz.yfrostyf.toxony.api.mutagens.MutagenData;
import xyz.yfrostyf.toxony.api.mutagens.MutagenEffect;
import xyz.yfrostyf.toxony.registries.DataAttachmentRegistry;
import xyz.yfrostyf.toxony.registries.MobEffectRegistry;

public class NecroticMutagenEffect extends MutagenEffect {
    public static final String RESURRECTION_ACTIVE = "resurrection_active";
    public static final String RESURRECTION_COOLDOWN = "resurrection_cooldown";
    public static final int DEFAULT_RESURRECTION_COOLDOWN = 200; // 48000;

    public NecroticMutagenEffect(MobEffectCategory category) {
        super(category, 0xffffff);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        MutagenData mutagenData = livingEntity.getData(DataAttachmentRegistry.MUTAGEN_DATA);
        if(amplifier >= 2 && livingEntity.tickCount % 20 == 0){
            if(isUnderSunTick(livingEntity.level(), livingEntity)){
                livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 0, true, false, false));
            }
        }
        if(amplifier >= 2 && !livingEntity.level().isClientSide){
            if(!mutagenData.getBool(RESURRECTION_ACTIVE)){
                int cooldown = mutagenData.getInt(RESURRECTION_COOLDOWN);
                mutagenData.addInt(RESURRECTION_COOLDOWN, cooldown-1);
                if(cooldown <= 0){
                    mutagenData.addBool(RESURRECTION_ACTIVE, true);
                }
            }
        }
        return true;
    }

    // Minecraft's default implementation of Zombie#isSunBurnTick
    private static boolean isUnderSunTick(Level level, LivingEntity entity) {
        if (level.isDay() && !level.isClientSide) {
            float f = entity.getLightLevelDependentMagicValue();
            BlockPos blockpos = BlockPos.containing(entity.getX(), entity.getEyeY(), entity.getZ());
            boolean flag = entity.isInWaterRainOrBubble() || entity.isInPowderSnow || entity.wasInPowderSnow;
            if (f > 0.5F && !flag && level.canSeeSky(blockpos)) {
                return true;
            }
        }
        return false;
    }

    @EventBusSubscriber
    public static class NecroticMutagenEvents {

        @SubscribeEvent
        public static void onEatingAsNecrotic(LivingEntityUseItemEvent.Finish event){
            ItemStack foodstack = event.getItem();
            LivingEntity livingEntity = event.getEntity();
            MobEffectInstance attackerMutagen = livingEntity.getEffect(MobEffectRegistry.NECROTIC_MUTAGEN);
            if(attackerMutagen != null && attackerMutagen.getAmplifier() >= 0){
                if(!foodstack.has(DataComponents.FOOD)) return;
                if(foodstack.is(Tags.Items.FOODS_RAW_MEAT)
                        || foodstack.is(Tags.Items.FOODS_RAW_FISH)
                        || foodstack.is(Items.ROTTEN_FLESH)){
                    livingEntity.heal(4.0F);
                }
            }
        }

        @SubscribeEvent
        public static void onEffectAddedToNecrotic(MobEffectEvent.Applicable event){
            MobEffectInstance mutagen = event.getEntity().getEffect(MobEffectRegistry.NECROTIC_MUTAGEN);
            if(mutagen == null) return;

            if (mutagen.getAmplifier() >= 0 && event.getEffectInstance().is(MobEffects.HUNGER)) {
                event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
            }
            else if (mutagen.getAmplifier() >= 1) {
                if (event.getEffectInstance().is(MobEffects.POISON) || event.getEffectInstance().is(MobEffects.WITHER)) {
                    event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
                }
            }
        }


        @SubscribeEvent
        public static void onHealingApplicable(MobEffectEvent.Applicable event){
            MobEffectInstance victimMutagen = event.getEntity().getEffect(MobEffectRegistry.NECROTIC_MUTAGEN);
            MobEffectInstance effect = event.getEffectInstance();
            if(victimMutagen == null || victimMutagen.getAmplifier() < 1)return;

            if(effect.is(MobEffects.REGENERATION)){
                event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
            }
        }

        @SubscribeEvent
        public static void onMutagenDamaged(LivingDamageEvent.Pre event){
            if(!(event.getEntity().level() instanceof ServerLevel svlevel))return;

            MobEffectInstance victimMutagen = event.getEntity().getEffect(MobEffectRegistry.NECROTIC_MUTAGEN);
            DamageType damageType = event.getSource().type();
            HolderLookup.RegistryLookup<DamageType> registryAccess = svlevel.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE);

            if(victimMutagen == null)return;
            if(event.getSource().getEntity() instanceof LivingEntity attacker && victimMutagen.getAmplifier() >= 1){
                attacker.addEffect(new MobEffectInstance(MobEffects.POISON, 200));
            }
            if(victimMutagen.getAmplifier() >= 1){
                if (damageType == registryAccess.getOrThrow(DamageTypes.WITHER).value()){
                    event.setNewDamage(0);
                }
            }
        }

        @SubscribeEvent
        public static void onMutagenResurrect(LivingDamageEvent.Post event){
            LivingEntity entity = event.getEntity();
            MobEffectInstance victimMutagen = entity.getEffect(MobEffectRegistry.NECROTIC_MUTAGEN);
            MutagenData mutagenData = entity.getData(DataAttachmentRegistry.MUTAGEN_DATA);
            if(victimMutagen != null && mutagenData.getBool(RESURRECTION_ACTIVE) && entity.isDeadOrDying()){
                entity.setHealth(entity.getMaxHealth() / 2.0F);
                mutagenData.addBool(RESURRECTION_ACTIVE, false);
                mutagenData.addInt(RESURRECTION_COOLDOWN, DEFAULT_RESURRECTION_COOLDOWN);
                if(entity.level() instanceof ServerLevel svlevel){
                    svlevel.playSound(
                            null, entity,
                            SoundEvents.WITHER_AMBIENT, SoundSource.NEUTRAL,
                            0.8F, 0.8F);
                    svlevel.playSound(
                            null, entity,
                            SoundEvents.TOTEM_USE, SoundSource.NEUTRAL,
                            0.6F, 0.9F);
                    svlevel.sendParticles(ParticleTypes.TOTEM_OF_UNDYING,
                            entity.getX(),  entity.getY()+1.5,  entity.getZ(),
                            15, 0.75, 0.3, 0.75, 0);
                }
            }
        }

        @SubscribeEvent
        public static void onDamageMutagenAttacker(LivingDamageEvent.Post event){
            if(!(event.getSource().getEntity() instanceof LivingEntity livingEntity))return;
            MobEffectInstance attackerMutagen = livingEntity.getEffect(MobEffectRegistry.NECROTIC_MUTAGEN);
            if(attackerMutagen != null && attackerMutagen.getAmplifier() >= 2) {
                event.getEntity().addEffect(new MobEffectInstance(MobEffects.WITHER,100));
            }
        }
    }
}
