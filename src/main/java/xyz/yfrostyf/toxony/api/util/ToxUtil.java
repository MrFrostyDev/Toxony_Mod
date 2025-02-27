package xyz.yfrostyf.toxony.api.util;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import xyz.yfrostyf.toxony.api.tox.ToxData;

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
    public static void addToleranceWithTier(ToxData toxData, float tolerance, float tier, Level level){
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
}
