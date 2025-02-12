package xyz.yfrostyf.toxony.events.subscribers;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.util.AffinityUtil;

@EventBusSubscriber(modid = ToxonyMain.MOD_ID, value = Dist.DEDICATED_SERVER)
public class ServerStartEvents {

    @SubscribeEvent
    public static void onServerStartingForAffinityMap(ServerStartingEvent event){
        AffinityUtil.computeIngredientAffinityMap(event.getServer().overworld());
    }
}
