package xyz.yfrostyf.toxony.effects.mutagens;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.mutagens.MutagenData;
import xyz.yfrostyf.toxony.api.mutagens.MutagenEffect;
import xyz.yfrostyf.toxony.entities.GuidedSpiritEntity;
import xyz.yfrostyf.toxony.registries.DataAttachmentRegistry;
import xyz.yfrostyf.toxony.registries.EntityRegistry;
import xyz.yfrostyf.toxony.registries.MobEffectRegistry;
import xyz.yfrostyf.toxony.registries.TagRegistry;

public class SpiritMutagenEffect extends MutagenEffect {
    public static final String GUIDED_SPIRIT_ACTIVE = "guided_spirit_active";
    public static final String GUIDED_SPIRIT_COOLDOWN = "guided_spirit_cooldown";
    public static final int DEFAULT_GUIDED_SPIRIT_COOLDOWN = 400;

    private static final AttributeModifier FALL_MODIFIER = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "spirit_mutagen_fall_modifier"), -0.5f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

    public SpiritMutagenEffect(MobEffectCategory category) {
        super(category, 0xffffff);
    }

    @Override
    public void onEffectStarted(LivingEntity entity, int amplifier) {
        if(amplifier >= 0){
            addModifier(entity, Attributes.FALL_DAMAGE_MULTIPLIER, FALL_MODIFIER);
        }
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        MutagenData mutagenData = livingEntity.getData(DataAttachmentRegistry.MUTAGEN_DATA);
        if(amplifier >= 1 && !livingEntity.level().isClientSide){
            if(!mutagenData.getBool(GUIDED_SPIRIT_ACTIVE)){
                int cooldown = mutagenData.getInt(GUIDED_SPIRIT_COOLDOWN);
                mutagenData.addInt(GUIDED_SPIRIT_COOLDOWN, cooldown-1);
                if(cooldown <= 0){
                    mutagenData.addBool(GUIDED_SPIRIT_ACTIVE, true);
                }
            }
        }
        if(amplifier >= 2){
            livingEntity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 40, 0, true, false, false));
        }
        return true;
    }

    @Override
    public void removeModifiers(LivingEntity entity) {
        super.removeModifiers(entity);
        removeModifier(entity, Attributes.FALL_DAMAGE_MULTIPLIER, FALL_MODIFIER);
    }

    @EventBusSubscriber
    public static class SpiritMutagenEvents {

        @SubscribeEvent
        public static void onMutagenDamaged(LivingDamageEvent.Pre event){
            MobEffectInstance victimMutagen = event.getEntity().getEffect(MobEffectRegistry.SPIRIT_MUTAGEN);
            Entity causingIndirectEntity = event.getSource().getEntity();
            Entity causingEntity = event.getSource().getDirectEntity();
            if (victimMutagen == null || event.getEntity().level().isClientSide())return;
            LivingEntity victim = event.getEntity();
            Holder<DamageType> damageType = event.getSource().typeHolder();
            float newDamage = event.getOriginalDamage();

            boolean isIndirectSpirt = causingIndirectEntity != null && causingIndirectEntity.getType().is(TagRegistry.SPIRIT_RESISTANT);
            boolean isDirectSpirt = causingEntity != null && causingEntity.getType().is(TagRegistry.SPIRIT_RESISTANT);
            if (isIndirectSpirt || isDirectSpirt || damageType.is(DamageTypeTags.IS_FALL)){
                newDamage *= 0.5F;
            }

            if(victimMutagen.getAmplifier() >= 1 && !victim.level().isClientSide()){
                if(!damageType.is(DamageTypeTags.WITCH_RESISTANT_TO)
                        && !damageType.is(DamageTypeTags.IS_FIRE)
                        && event.getEntity().getRandom().nextInt(5) == 0){
                    victim.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 200, 0, false, false, false));
                    newDamage *= 0.0F;
                }
            }

            if (victimMutagen.getAmplifier() >= 2){
                if(damageType.is(DamageTypeTags.WITCH_RESISTANT_TO)){
                    newDamage *= 1.3F;
                }
            }

            event.setNewDamage(newDamage);
        }

        @SubscribeEvent
        public static void onDamageMutagenAttacker(LivingDamageEvent.Post event){
            if (event.getSource().getEntity() == null || !(event.getSource().getEntity() instanceof LivingEntity attackerEntity)) return;
            MobEffectInstance attackerMutagen = attackerEntity.getEffect(MobEffectRegistry.SPIRIT_MUTAGEN);
            Level level = attackerEntity.level();
            if(attackerMutagen == null || !(level instanceof ServerLevel svlevel))return;

            MutagenData mutagenData = attackerEntity.getData(DataAttachmentRegistry.MUTAGEN_DATA);
            if(mutagenData.getBool(GUIDED_SPIRIT_ACTIVE)){
                if(attackerMutagen.getAmplifier() == 1){
                    GuidedSpiritEntity spirit = EntityRegistry.GUIDED_SPIRIT.get().create(level);
                    BlockPos pos = attackerEntity.blockPosition();
                    if(spirit != null){
                        spirit.initSummon(
                                attackerEntity,
                                attackerEntity.getX(), attackerEntity.getEyeY(), attackerEntity.getZ(),
                                1
                        );
                        spirit.setBoundOrigin(pos);
                        svlevel.addFreshEntity(spirit);
                        svlevel.gameEvent(GameEvent.ENTITY_PLACE, pos, GameEvent.Context.of(attackerEntity));
                    }
                }
                else if(attackerMutagen.getAmplifier() >= 2){
                    for(int i=0;i<3;i++){
                        GuidedSpiritEntity spirit = EntityRegistry.GUIDED_SPIRIT.get().create(level);
                        BlockPos pos = attackerEntity.blockPosition();
                        if(spirit != null){
                            spirit.initSummon(
                                    attackerEntity,
                                    attackerEntity.getX(), attackerEntity.getEyeY(), attackerEntity.getZ(),
                                    i
                            );
                            spirit.setBoundOrigin(pos);
                            svlevel.addFreshEntity(spirit);
                            svlevel.gameEvent(GameEvent.ENTITY_PLACE, pos, GameEvent.Context.of(attackerEntity));
                        }
                    }

                }
                if(attackerMutagen.getAmplifier() >= 1){
                    mutagenData.addBool(GUIDED_SPIRIT_ACTIVE, false);
                    mutagenData.addInt(GUIDED_SPIRIT_COOLDOWN, DEFAULT_GUIDED_SPIRIT_COOLDOWN);
                }
            }
        }
    }
}
