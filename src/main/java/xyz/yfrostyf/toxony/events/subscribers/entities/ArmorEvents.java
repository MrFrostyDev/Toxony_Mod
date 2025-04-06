package xyz.yfrostyf.toxony.events.subscribers.entities;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.registries.ArmorMaterialRegistry;

@EventBusSubscriber(modid = ToxonyMain.MOD_ID)
public class ArmorEvents {

    @SubscribeEvent
    public static void onPlagueDoctorEffectAdded(MobEffectEvent.Added event){
        if(event.getEntity() instanceof Player player
                && !event.getEffectInstance().isInfiniteDuration()
                && !event.getEffectInstance().getEffect().value().isBeneficial()){

            MobEffectInstance effectInst = event.getEffectInstance();
            int duration = event.getEffectInstance().getDuration();

            if(isPlagueHoodEquipped(player)){
                duration -= 60;
            }
            if(isPlagueCoatEquipped(player)){
                duration -= 40;
            }
            if(isPlagueLeggingsEquipped(player)){
                duration -= 20;
            }
            if(isPlagueBootsEquipped(player)){
                duration -= 20;
            }

            effectInst.setDetailsFrom(new MobEffectInstance(effectInst.getEffect(), Math.max(duration, 0), effectInst.getAmplifier()));
        }
    }

    // thank you Modding by Kaupenjoe for their
    // custom forge armor set tutorial as reference
    private static boolean isPlagueBootsEquipped(Player player){
        if(player.getInventory().getArmor(0).getItem() instanceof ArmorItem boots){
            return boots.getMaterial() == ArmorMaterialRegistry.PLAGUE_DOCTOR_ARMOR_MATERIAL;
        }
        return false;
    }

    private static boolean isPlagueLeggingsEquipped(Player player){
        if(player.getInventory().getArmor(1).getItem() instanceof ArmorItem leggings){
            return leggings.getMaterial() == ArmorMaterialRegistry.PLAGUE_DOCTOR_ARMOR_MATERIAL;
        }
        return false;
    }

    private static boolean isPlagueCoatEquipped(Player player){
        if(player.getInventory().getArmor(2).getItem() instanceof ArmorItem coat){
            return coat.getMaterial() == ArmorMaterialRegistry.PLAGUE_DOCTOR_ARMOR_MATERIAL;
        }
        return false;
    }

    private static boolean isPlagueHoodEquipped(Player player){
        if(player.getInventory().getArmor(3).getItem() instanceof ArmorItem hood){
            return hood.getMaterial() == ArmorMaterialRegistry.PLAGUE_DOCTOR_ARMOR_MATERIAL;
        }
        return false;
    }

}
