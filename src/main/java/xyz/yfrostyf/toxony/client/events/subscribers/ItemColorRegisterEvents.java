package xyz.yfrostyf.toxony.client.events.subscribers;

import net.minecraft.core.component.DataComponents;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.registries.ItemRegistry;

@EventBusSubscriber(modid = ToxonyMain.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ItemColorRegisterEvents {

    @SubscribeEvent
    public static void onItemColorRegister(RegisterColorHandlersEvent.Item event){
        event.register((stack, tintIndex) -> tintIndex > 0
                        ? -1
                        : FastColor.ARGB32.opaque(stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).getColor()),
                ItemRegistry.COPPER_NEEDLE.get(),
                ItemRegistry.TOX_VIAL.get()
        );
    }
}
