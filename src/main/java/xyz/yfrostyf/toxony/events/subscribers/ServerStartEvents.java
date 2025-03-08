package xyz.yfrostyf.toxony.events.subscribers;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.LevelEvent;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.util.AffinityUtil;
import xyz.yfrostyf.toxony.data.world.IngredientAffinityMapData;

@EventBusSubscriber(modid = ToxonyMain.MOD_ID)
public class ServerStartEvents {

    @SubscribeEvent
    public static void onLevelLoadForAffinityMap(LevelEvent.Load event){
        if (event.getLevel() instanceof ServerLevel svlevel && svlevel.dimension() == Level.OVERWORLD) {
            if(svlevel.getDataStorage().get(IngredientAffinityMapData.factory(), "toxony_affinity_map") == null){
                AffinityUtil.computeIngredientAffinityMap(svlevel);
            }
        }
    }
}
