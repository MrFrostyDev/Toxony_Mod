package xyz.yfrostyf.toxony.api.events.subscribers;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.oils.ItemOil;
import xyz.yfrostyf.toxony.api.util.OilUtil;
import xyz.yfrostyf.toxony.registries.DataComponentsRegistry;

@EventBusSubscriber(modid = ToxonyMain.MOD_ID)
public class OilEvents {

    @SubscribeEvent
    public static void onPlayerAttackOil(AttackEntityEvent event){
        ItemStack itemInHand = event.getEntity().getMainHandItem();
        ItemOil itemoil = itemInHand.getOrDefault(DataComponentsRegistry.OIL, ItemOil.EMPTY);
        if(itemInHand.isEmpty() || itemoil.isEmpty())return;

        LivingEntity targetEntity = (LivingEntity)event.getTarget();

        for (Holder<MobEffect> effect : itemoil.oil().effects()) {
            if (effect.value().isInstantenous()) {
                effect.value().applyInstantenousEffect(event.getEntity(), event.getEntity(), targetEntity, itemoil.amplifier(), 1);
            } else {
                if(!targetEntity.hasEffect(effect)){
                    OilUtil.useOil(event.getEntity().level(), itemInHand, 1);
                    MobEffectInstance effectInstance = new MobEffectInstance(effect, itemoil.duration(), itemoil.amplifier());
                    targetEntity.addEffect(effectInstance, event.getEntity());
                }
            }
        }
    }
}
