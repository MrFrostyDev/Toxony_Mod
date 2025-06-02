package xyz.yfrostyf.toxony.effects.mutagens;

import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.mutagens.MutagenData;
import xyz.yfrostyf.toxony.api.mutagens.MutagenEffect;
import xyz.yfrostyf.toxony.network.ServerNightPredatorPacket;
import xyz.yfrostyf.toxony.network.SyncMutagenDataPacket;
import xyz.yfrostyf.toxony.registries.DataAttachmentRegistry;
import xyz.yfrostyf.toxony.registries.MobEffectRegistry;
import xyz.yfrostyf.toxony.registries.SoundEventRegistry;

public class BeastMutagenEffect extends MutagenEffect {
    public static final String NIGHT_PREDATOR_ACTIVE = "night_predator_active";

    private static final AttributeModifier DAMAGEBOOST_MODIFIER = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "wolf_mutagen_damage_modifier"), 0.15F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    private static final AttributeModifier SPEED_MODIFIER = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "wolf_mutagen_speed_modifier"), 0.3F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

    public BeastMutagenEffect(MobEffectCategory category) {
        super(category, 0xffffff);
    }

    @Override
    public void onEffectStarted(LivingEntity entity, int amplifier) {
        if(amplifier >= 1){
            addModifier(entity, Attributes.ATTACK_DAMAGE, DAMAGEBOOST_MODIFIER);
        }
    }

    @Override
    public void removeModifiers(LivingEntity entity) {
        super.removeModifiers(entity);
        removeModifier(entity, Attributes.ATTACK_DAMAGE, DAMAGEBOOST_MODIFIER);
        if(entity.hasEffect(MobEffectRegistry.BEAST_MUTAGEN)
                && entity.getEffect(MobEffectRegistry.BEAST_MUTAGEN).getAmplifier() >= 2){
            DeactivateNightPredator(entity, entity.getData(DataAttachmentRegistry.MUTAGEN_DATA));
        }
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {return true;}

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        MutagenData mutagenData = livingEntity.getData(DataAttachmentRegistry.MUTAGEN_DATA);

        if(amplifier >= 2){
            // Because isDay() is inconsistent with server on the client
            if(!livingEntity.level().isClientSide()){
                if(!livingEntity.level().isDay() && !mutagenData.getBool(NIGHT_PREDATOR_ACTIVE)){
                    mutagenData.addBool(NIGHT_PREDATOR_ACTIVE, true);
                    ActivateNightPredator(livingEntity, mutagenData);
                }
                else if(livingEntity.level().isDay() && mutagenData.getBool(NIGHT_PREDATOR_ACTIVE)){
                    mutagenData.addBool(NIGHT_PREDATOR_ACTIVE, false);
                    DeactivateNightPredator(livingEntity, mutagenData);
                }
            }
        }
        return true;
    }

    private static void ActivateNightPredator(LivingEntity livingEntity, MutagenData data){
        Level level = livingEntity.level();
        addModifier(livingEntity, Attributes.MOVEMENT_SPEED, SPEED_MODIFIER);
        level.playSound(
                null, livingEntity,
                SoundEventRegistry.MUTAGEN_TRANSFORM.get(), SoundSource.NEUTRAL,
                0.8F, 2.0F);
        level.playSound(
                null, livingEntity,
                SoundEvents.WOLF_GROWL, SoundSource.NEUTRAL,
                0.8F, 0.9F);


        if(livingEntity instanceof ServerPlayer svplayer){
            PacketDistributor.sendToPlayer(svplayer, SyncMutagenDataPacket.create(data));
            PacketDistributor.sendToPlayer(svplayer, ServerNightPredatorPacket.create(true));
        }
    }

    private static void DeactivateNightPredator(LivingEntity livingEntity, MutagenData data){
        Level level = livingEntity.level();
        removeModifier(livingEntity, Attributes.MOVEMENT_SPEED, SPEED_MODIFIER);
        level.playSound(
                null, livingEntity,
                SoundEventRegistry.MUTAGEN_TRANSFORM.get(), SoundSource.NEUTRAL,
                0.8F, 2.0F);

        if(livingEntity instanceof ServerPlayer svplayer){
            PacketDistributor.sendToPlayer(svplayer, SyncMutagenDataPacket.create(data));
            PacketDistributor.sendToPlayer(svplayer, ServerNightPredatorPacket.create(false));
        }
    }

    @EventBusSubscriber
    public static class BeastMutagenEvents {
        @SubscribeEvent
        public static void onDamageMutagenAttacker(LivingDamageEvent.Post event){
            if (event.getSource().getEntity() == null || !(event.getSource().getEntity() instanceof LivingEntity attackerEntity)) return;
            MobEffectInstance attackerMutagen = attackerEntity.getEffect(MobEffectRegistry.BEAST_MUTAGEN);
            MutagenData mutagenData = attackerEntity.getData(DataAttachmentRegistry.MUTAGEN_DATA);
            if(attackerMutagen == null)return;

            if(attackerMutagen.getAmplifier() >= 0){
                if(!attackerEntity.hasEffect(MobEffects.MOVEMENT_SPEED)){
                    attackerEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 40, 0));
                }
            }

            if(attackerMutagen.getAmplifier() >= 2){
                if(mutagenData.getBool(NIGHT_PREDATOR_ACTIVE)){
                    if(!event.getEntity().level().isClientSide()){
                        event.getEntity().addEffect(new MobEffectInstance(MobEffectRegistry.HUNT, 100, 0));
                        attackerEntity.playSound(SoundEvents.WOLF_GROWL, 6, 1);
                    }
                }
            }

        }

        @SubscribeEvent
        public static void onEatingAsBeast(LivingEntityUseItemEvent.Finish event){
            ItemStack foodstack = event.getItem();
            LivingEntity livingEntity = event.getEntity();
            MobEffectInstance attackerMutagen = livingEntity.getEffect(MobEffectRegistry.BEAST_MUTAGEN);
            if(attackerMutagen != null && attackerMutagen.getAmplifier() >= 1){
                if(!foodstack.has(DataComponents.FOOD)) return;

                if(foodstack.is(Tags.Items.FOODS_COOKED_MEAT) || foodstack.is(Tags.Items.FOODS_COOKED_FISH)){
                    FoodProperties foodProperties = foodstack.get(DataComponents.FOOD);
                    livingEntity.eat(livingEntity.level(), foodstack, new FoodProperties(
                            foodProperties.nutrition() / 2,
                            foodProperties.saturation() / 2,
                            foodProperties.canAlwaysEat(),
                            foodProperties.eatSeconds(),
                            foodProperties.usingConvertsTo(),
                            foodProperties.effects()
                    ));
                }
                else if(foodstack.is(Tags.Items.FOODS_RAW_MEAT) || foodstack.is(Tags.Items.FOODS_RAW_FISH)){
                    FoodProperties foodProperties = foodstack.get(DataComponents.FOOD);
                    livingEntity.eat(livingEntity.level(), foodstack, new FoodProperties(
                            foodProperties.nutrition() * 2,
                            foodProperties.saturation() * 2,
                            foodProperties.canAlwaysEat(),
                            foodProperties.eatSeconds(),
                            foodProperties.usingConvertsTo(),
                            foodProperties.effects()
                    ));
                }
            }
        }
    }

}
