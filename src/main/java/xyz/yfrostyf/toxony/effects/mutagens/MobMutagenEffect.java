package xyz.yfrostyf.toxony.effects.mutagens;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.mutagens.MutagenEffect;

public class MobMutagenEffect extends MutagenEffect {
    private static final AttributeModifier MAXHEALTH_MODIFIER = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "mob_mutagen_maxhealth_modifier"), 1.0F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    private static final AttributeModifier SPEED_MODIFIER = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "mob_mutagen_movespd_modifier"), 0.25F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

    public MobMutagenEffect(MobEffectCategory category) {
        super(category, 0xffffff);
    }

    @Override
    public void onEffectStarted(LivingEntity entity, int amplifier) {
        addModifier(entity, Attributes.MAX_HEALTH, MAXHEALTH_MODIFIER);
        addModifier(entity, Attributes.MOVEMENT_SPEED, SPEED_MODIFIER);
    }
    @Override
    public void removeModifiers(LivingEntity entity) {
        super.removeModifiers(entity);
        removeModifier(entity, Attributes.MAX_HEALTH, MAXHEALTH_MODIFIER);
        removeModifier(entity, Attributes.MOVEMENT_SPEED, SPEED_MODIFIER);
    }
}
