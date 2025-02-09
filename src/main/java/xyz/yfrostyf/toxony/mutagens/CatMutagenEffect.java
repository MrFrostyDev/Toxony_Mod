package xyz.yfrostyf.toxony.mutagens;

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
import xyz.yfrostyf.toxony.registries.MutagenRegistry;

public class CatMutagenEffect extends MutagenEffect {
    private static final AttributeModifier fallModifier = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "cat_mutagen_fall_modifier"), -0.5f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    private static final AttributeModifier speedModifier = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "cat_mutagen_speed_modifier"), 0.3f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    private static final AttributeModifier jumpModifier = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "cat_mutagen_jump_modifier"), 0.5f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    private static final AttributeModifier safeFallModifier = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "cat_mutagen_safefall_add"), 1f, AttributeModifier.Operation.ADD_VALUE);

    public CatMutagenEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void onEffectStarted(LivingEntity entity, int amplifier) {
        addModifier(entity, Attributes.FALL_DAMAGE_MULTIPLIER, fallModifier);

        if(amplifier > 0){
            addModifier(entity, Attributes.MOVEMENT_SPEED, speedModifier);
            addModifier(entity, Attributes.JUMP_STRENGTH, jumpModifier);
            addModifier(entity, Attributes.SAFE_FALL_DISTANCE, safeFallModifier);
        }
        if(amplifier > 1){
            entity.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, MobEffectInstance.INFINITE_DURATION, 0, false, false, false));
        }
    }

    @EventBusSubscriber
    public static class CatMutagenEvents {

        @SubscribeEvent
        public static void onEffectRemove(MobEffectEvent.Remove event){
            MobEffectInstance effectInst = event.getEffectInstance();

            if(effectInst == null){return;}
            if(!effectInst.is(MutagenRegistry.CAT_MUTAGEN)){return;}

            removeModifier(event.getEntity(), Attributes.FALL_DAMAGE_MULTIPLIER, fallModifier);

            if(effectInst.getAmplifier() >= 1) {
                removeModifier(event.getEntity(), Attributes.MOVEMENT_SPEED, speedModifier);
                removeModifier(event.getEntity(), Attributes.JUMP_STRENGTH, jumpModifier);
                removeModifier(event.getEntity(), Attributes.SAFE_FALL_DISTANCE, safeFallModifier);
            }

            if(event.getEntity().hasEffect(MobEffects.NIGHT_VISION)){
                event.getEntity().removeEffect(MobEffects.NIGHT_VISION);
            }
        }
    }
}
