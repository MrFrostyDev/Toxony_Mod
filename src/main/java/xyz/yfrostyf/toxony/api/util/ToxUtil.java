package xyz.yfrostyf.toxony.api.util;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
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
     *  Helper method to add tolerance taking tier into account.
     */
    public static void addToleranceWithTier(ToxData toxData, float tolerance, float tier, Level level){
        if(!toxData.getDeathState() && level.isClientSide()){
            if(toxData.getThreshold() >= tier){
                toxData.addTolerance(tolerance);
                return;
            }
            Minecraft.getInstance().gui.setOverlayMessage(
                    Component.translatable("message.toxony.tolerance.weak_tier_warning"),
                    false
            );
        }
    }
}
