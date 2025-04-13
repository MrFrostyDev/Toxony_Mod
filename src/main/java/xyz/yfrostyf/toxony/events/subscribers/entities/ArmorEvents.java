package xyz.yfrostyf.toxony.events.subscribers.entities;

import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.registries.AttributeRegistry;

import java.util.Optional;


// thank you Modding by Kaupenjoe for their
// custom forge armor set tutorial as reference

@EventBusSubscriber(modid = ToxonyMain.MOD_ID)
public class ArmorEvents {

    @SubscribeEvent
    public static void onEffectAddedWithEffectReduction(MobEffectEvent.Added event){
        if(event.getEntity() instanceof LivingEntity livingEntity
                && !event.getEffectInstance().isInfiniteDuration()
                && !event.getEffectInstance().getEffect().value().isBeneficial()){

            MobEffectInstance effectInst = event.getEffectInstance();
            int duration = event.getEffectInstance().getDuration();

            for(ItemStack armor : livingEntity.getArmorSlots()){
                Optional<ItemAttributeModifiers.Entry> optional = armor.getAttributeModifiers().modifiers()
                        .stream()
                        .filter(entry -> entry.attribute().is(AttributeRegistry.EFFECT_REDUCTION.getKey()) && entry.slot().test(livingEntity.getEquipmentSlotForItem(armor)))
                        .findFirst();

                if(optional.isPresent()){
                    duration -= Mth.floor(optional.get().modifier().amount()) * 20;
                }
            }

            if(duration == event.getEffectInstance().getDuration()) return;
            effectInst.setDetailsFrom(new MobEffectInstance(effectInst.getEffect(), Math.max(duration, 0), effectInst.getAmplifier()));
        }
    }
}
