package xyz.yfrostyf.toxony.api.client;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.affinity.Affinity;
import xyz.yfrostyf.toxony.api.tox.ToxData;
import java.util.Map;

@Mod(value = ToxonyMain.MOD_ID, dist = Dist.CLIENT)
public class ClientToxData {
    private static final ToxData toxData = new ToxData(Minecraft.getInstance() != null ? Minecraft.getInstance().player : null);

    public static ToxData getToxData(){
        return toxData;
    }

    public static void setTox(float inTox){
        toxData.setTox(inTox);
    }

    public static void setTolerance(float inTolerance){
        toxData.setTolerance(inTolerance);
    }

    public static void setThreshold(int inThreshold) {
        toxData.setThreshold(inThreshold);
    }

    public static void setKnownIngredients(Map<ResourceLocation, Integer> inKnownIngredients){
        toxData.setKnownIngredients(inKnownIngredients);
    }

    public static void setAffinities(Map<Affinity, Integer> intAffinities){
        toxData.setAffinities(intAffinities);
    }

    public static void setDeathState(boolean inDeathState){
        toxData.setDeathState(inDeathState);
    }
}
