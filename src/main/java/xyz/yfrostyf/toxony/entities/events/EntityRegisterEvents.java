package xyz.yfrostyf.toxony.entities.events;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.entities.GuidedSpiritEntity;
import xyz.yfrostyf.toxony.registries.EntityRegistry;

@EventBusSubscriber(modid = ToxonyMain.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class EntityRegisterEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event){
        event.put(EntityRegistry.GUIDED_SPIRIT.get(), GuidedSpiritEntity.createAttributes().build());
    }

}
