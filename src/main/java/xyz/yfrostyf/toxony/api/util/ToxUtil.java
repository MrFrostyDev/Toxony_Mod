package xyz.yfrostyf.toxony.api.util;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import xyz.yfrostyf.toxony.api.tox.ToxData;
import xyz.yfrostyf.toxony.network.SyncMobToxDataPacket;
import xyz.yfrostyf.toxony.registries.DataAttachmentRegistry;
import xyz.yfrostyf.toxony.registries.MobEffectRegistry;
import xyz.yfrostyf.toxony.registries.SoundEventRegistry;

public class ToxUtil {

    /**
     * Math formula that returns the results 1, 3, 6, 10... for each x. Formula used to determine mutagen thresholds when tolerance reaches those values. Values are usually multiplied by 100.
     */
    public static float TriangularNumbersMult(float x, float multiplier){
        return( (((x + 1) * (x + 2)) / 2) * multiplier );
    }

    /**
     * The inverse of {@link ToxUtil#TriangularNumbersMult(float, float)}. Use this SPARINGLY.
     * Returns a double for precise comparing.
     */
    public static double TriangularNumbersMultReverse(float y, float divider){
        double x = y/divider;
        return((Math.sqrt(1 + (8 * x))-3)/2);
    }

    /**
     *  Helper method to add tolerance taking tier into account.
     */
    public static void addToleranceWithTier(Player player, ToxData toxData, float tolerance, float tier, Level level){
        if(!toxData.getDeathState()){
            double toleranceToTriangular = TriangularNumbersMultReverse(toxData.getTolerance(), 100);
            if((double)tier >= toleranceToTriangular){
                toxData.addTolerance(tolerance);
            }
            else if(level.isClientSide()){
                Minecraft.getInstance().gui.setOverlayMessage(
                        Component.translatable("message.toxony.tolerance.weak_tier_warning"),
                        false
                );
            }
            player.setData(DataAttachmentRegistry.TOX_DATA, toxData);
        }
    }

    /**
    *   Adds a mob effect as if it was a Mutagen (Infinite duration, no visuals, stacking duplicates).
    *   If the entity doesn't have it, the effect is added. If they do, amplify the effect by 1.
    */
    public static void applyMutagenEffect(LivingEntity entity, Holder<MobEffect> effect) {
        if (!entity.hasEffect(effect)) {
            entity.addEffect(new MobEffectInstance(effect, MobEffectInstance.INFINITE_DURATION, 0, false, false, false));
            return;
        }

        int effectAmp = entity.getEffect(effect).getAmplifier();
        entity.addEffect(new MobEffectInstance(effect, MobEffectInstance.INFINITE_DURATION, effectAmp+1, false, false, false));
    }

    /**
     *  Sets the mob toxin data attachment to the mob if the amount specified is greater than 0.
     *  Handles data syncing for clients tracking the entity and when the mob acquires a mutagen.
     */
    public static void setMobToxin(LivingEntity victimEntity, float amount){
        Level level = victimEntity.level();
        float maxHealth = victimEntity.getMaxHealth();
        if(level instanceof ServerLevel svlevel){
            if(amount > 0){
                if(amount > maxHealth && !victimEntity.hasEffect(MobEffectRegistry.MOB_MUTAGEN)){
                    victimEntity.addEffect(new MobEffectInstance(MobEffectRegistry.MOB_MUTAGEN, MobEffectInstance.INFINITE_DURATION, 0, false , false, false));
                    victimEntity.playSound(SoundEventRegistry.MUTAGEN_TRANSFORM.get(), 0.8F, 1.0F);
                    svlevel.sendParticles(ParticleTypes.TOTEM_OF_UNDYING,
                            victimEntity.getX(),  victimEntity.getEyeY() + 0.55,  victimEntity.getZ(),
                            15, 0.75, 0.3, 0.75, 0);
                }

                victimEntity.setData(DataAttachmentRegistry.MOB_TOXIN, amount);
                PacketDistributor.sendToPlayersTrackingEntity(victimEntity, SyncMobToxDataPacket.create(victimEntity.getId(), amount));
            }
        }
    }
}
