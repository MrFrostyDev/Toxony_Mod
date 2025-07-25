package xyz.yfrostyf.toxony.events.subscribers.entities;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.BasicItemListing;
import net.neoforged.neoforge.event.village.WandererTradesEvent;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.registries.ItemRegistry;

@EventBusSubscriber(modid = ToxonyMain.MOD_ID)
public class WanderingTraderEvents {

    @SubscribeEvent
    public static void AddWanderingTraderTrades(WandererTradesEvent event){
        BasicItemListing coldsnap_trade = new BasicItemListing(8, ItemRegistry.COLDSNAP.get().getDefaultInstance(), 2, 2);
        BasicItemListing nightshade_trade = new BasicItemListing(8, ItemRegistry.NIGHTSHADE.get().getDefaultInstance(), 2, 2);
        BasicItemListing ocelot_mint_trade = new BasicItemListing(8, ItemRegistry.OCELOT_MINT.get().getDefaultInstance(), 2, 2);
        BasicItemListing false_berries_trade = new BasicItemListing(8, ItemRegistry.FALSE_BERRIES.get().getDefaultInstance(), 2, 2);
        BasicItemListing water_hemlock_trade = new BasicItemListing(8, ItemRegistry.WATER_HEMLOCK.get().getDefaultInstance(), 2, 2);
        event.getRareTrades().add(coldsnap_trade);
        event.getRareTrades().add(nightshade_trade);
        event.getRareTrades().add(ocelot_mint_trade);
        event.getRareTrades().add(false_berries_trade);
        event.getRareTrades().add(water_hemlock_trade);
    }
}
