package xyz.yfrostyf.toxony.events.subscribers;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.registries.ItemRegistry;
import xyz.yfrostyf.toxony.registries.MobEffectRegistry;
import xyz.yfrostyf.toxony.registries.ParticleRegistry;

import java.util.Random;

@EventBusSubscriber(modid = ToxonyMain.MOD_ID)
public class SpecialItemEvents {
    public static final Random RANDOM = new Random();

    @SubscribeEvent
    public static void onPlayerAttackBoneSaw(AttackEntityEvent event){
        ItemStack itemInHand = event.getEntity().getMainHandItem();
        Player player = event.getEntity();
        if(!itemInHand.is(ItemRegistry.BONE_SAW) || !(player.getAttackStrengthScale(0.0F) > 0.9F) || !(RANDOM.nextInt(2) == 0))return;
        if(event.getTarget() instanceof LivingEntity targetEntity && !targetEntity.hasEffect(MobEffectRegistry.CRIPPLE)){
            targetEntity.addEffect(new MobEffectInstance(MobEffectRegistry.CRIPPLE, 200, 0));

            Entity target = event.getTarget();
            if (player.level() instanceof ServerLevel svlevel) {
                svlevel.playSound(null,
                        target.getX(), target.getY(), target.getZ(), SoundEvents.TRIDENT_HIT, SoundSource.PLAYERS, 1.0F, 0.7F);
                svlevel.sendParticles(ParticleRegistry.CUT.get(),
                        target.getX(), target.getY() + (target.getBbHeight() / 2), target.getZ(), 1, 0.3, 0.2, 0.3, 0.0);
            }

        }
    }
}
