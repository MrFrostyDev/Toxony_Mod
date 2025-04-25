package xyz.yfrostyf.toxony.events.subscribers;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.oils.ItemOil;
import xyz.yfrostyf.toxony.api.util.OilUtil;
import xyz.yfrostyf.toxony.registries.DataComponentsRegistry;
import xyz.yfrostyf.toxony.registries.TagRegistry;

@EventBusSubscriber(modid = ToxonyMain.MOD_ID)
public class OilEvents {

    @SubscribeEvent
    public static void onPlayerAttackOil(AttackEntityEvent event){
        ItemStack itemInHand = event.getEntity().getMainHandItem();
        ItemOil itemoil = itemInHand.getOrDefault(DataComponentsRegistry.OIL, ItemOil.EMPTY);
        if(itemInHand.isEmpty() || itemoil.isEmpty() || !itemInHand.is(TagRegistry.OIL_APPLICABLE)) return;

        Level level = event.getEntity().level();
        if(event.getTarget() instanceof LivingEntity targetEntity){
            itemoil.getOil().applyOil(itemoil, event.getEntity(), targetEntity, level);
            OilUtil.useOil(level, itemInHand, 1);
        }
    }
}
