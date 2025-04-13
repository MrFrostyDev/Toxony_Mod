package xyz.yfrostyf.toxony.effects.mutagens;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.mutagens.MutagenEffect;
import xyz.yfrostyf.toxony.registries.MobEffectRegistry;

public class CatMutagenEffect extends MutagenEffect {
    private static final AttributeModifier FALL_MODIFIER = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "cat_mutagen_fall_modifier"), -0.5f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    private static final AttributeModifier SPEED_MODIFIER = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "cat_mutagen_speed_modifier"), 0.3f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    private static final AttributeModifier JUMP_MODIFIER = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "cat_mutagen_jump_modifier"), 0.5f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    private static final AttributeModifier SAFEFALL_MODIFIER = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "cat_mutagen_safefall_add"), 1f, AttributeModifier.Operation.ADD_VALUE);

    public CatMutagenEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void onEffectStarted(LivingEntity entity, int amplifier) {
        addModifier(entity, Attributes.FALL_DAMAGE_MULTIPLIER, FALL_MODIFIER);

        if(amplifier > 0){
            addModifier(entity, Attributes.MOVEMENT_SPEED, SPEED_MODIFIER);
            addModifier(entity, Attributes.JUMP_STRENGTH, JUMP_MODIFIER);
            addModifier(entity, Attributes.SAFE_FALL_DISTANCE, SAFEFALL_MODIFIER);
        }
        if(amplifier > 1){
            entity.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, MobEffectInstance.INFINITE_DURATION, 0, false, false, false));
        }
    }

    @Override
    public void removeModifiers(LivingEntity entity) {
        removeModifier(entity, Attributes.FALL_DAMAGE_MULTIPLIER, FALL_MODIFIER);
        removeModifier(entity, Attributes.MOVEMENT_SPEED, SPEED_MODIFIER);
        removeModifier(entity, Attributes.JUMP_STRENGTH, JUMP_MODIFIER);
        removeModifier(entity, Attributes.SAFE_FALL_DISTANCE, SAFEFALL_MODIFIER);

        if(entity.hasEffect(MobEffects.NIGHT_VISION)){
            entity.removeEffect(MobEffects.NIGHT_VISION);
        }
    }
}
