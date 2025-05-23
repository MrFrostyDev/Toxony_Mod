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
                        || stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY) == PotionContents.EMPTY
                        || stack.getDamageValue() >= stack.getMaxDamage() && stack.isDamageableItem()
                        ? -1
                        : FastColor.ARGB32.opaque(stack.get(DataComponents.POTION_CONTENTS).getColor()),
                ItemRegistry.TOX_NEEDLE.get(),
                ItemRegistry.TOX_VIAL.get(),
                ItemRegistry.POTION_FLASK.get(),
                ItemRegistry.TOXIN_FLASK.get()
        );
    }
}
