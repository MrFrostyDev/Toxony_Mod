package xyz.yfrostyf.toxony.api.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.affinity.Affinity;
import xyz.yfrostyf.toxony.api.tox.ToxData;

import java.util.List;
import java.util.Map;

@Mod(value = ToxonyMain.MOD_ID, dist = Dist.CLIENT)
public class ClientToxData {
    private static ToxData toxData;

    public static ToxData newToxData(){
        toxData = new ToxData(Minecraft.getInstance() != null ? Minecraft.getInstance().player : null);
        return toxData;
    }

    public static void changeTox(float tox) {
        toxData.setToxSynced(tox);
    }

    public static void changeToxData(float tox,
                              float tolerance,
                              int threshold,
                              Map<Affinity, Integer> affinities,
                              List<Holder<MobEffect>> mutagens,
                              Map<ResourceLocation, Integer> knownIngredients,
                              boolean deathState) {
        toxData.handleSyncedToxData(tox, tolerance, threshold, affinities, mutagens, knownIngredients , deathState);
    }

    public static ToxData getToxData(){
        return toxData;
    }
}
