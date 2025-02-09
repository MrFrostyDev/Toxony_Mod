package xyz.yfrostyf.toxony.api.mutagens;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.neoforge.common.EffectCure;
import java.util.Set;

public class MutagenEffect extends MobEffect {
    protected MutagenEffect (MobEffectCategory category, int color) {
        super(category, color);
    }

    public void fillEffectCures(Set<EffectCure> cures, MobEffectInstance effectInstance) {
        cures.clear();
    }

    //
    // Method to add an attribute and a modifier to a livingEntity.
    //
    protected static void addModifier(LivingEntity entity, Holder<Attribute> attribute, AttributeModifier modifier) {
        AttributeInstance atrInstance = entity.getAttribute(attribute);
        if (atrInstance == null) {return;}
        atrInstance.addOrReplacePermanentModifier(modifier);
    }

    //
    // Method to remove a modifier on an attribute from the livingEntity.
    //
    protected static void removeModifier(LivingEntity entity, Holder<Attribute> attribute, AttributeModifier modifier) {
        AttributeInstance atrInstance = entity.getAttribute(attribute);
        if (atrInstance == null) {return;}
        atrInstance.removeModifier(modifier);
    }
}
