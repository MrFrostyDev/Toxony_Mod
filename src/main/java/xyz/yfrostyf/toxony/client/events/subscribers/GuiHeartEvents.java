package xyz.yfrostyf.toxony.client.events.subscribers;

import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.event.entity.player.PlayerHeartTypeEvent;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.registries.MobEffectRegistry;

@EventBusSubscriber(modid = ToxonyMain.MOD_ID, value = Dist.CLIENT)
public class GuiHeartEvents {
    public static final EnumProxy<Gui.HeartType> TOXIN_HEARTS_ENUM_PROXY = new EnumProxy<>(
            Gui.HeartType.class,
            ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "heart/toxin_full"),
            ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "heart/toxin_full_blinking"),
            ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "heart/toxin_half"),
            ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "heart/toxin_half_blinking"),
            ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "heart/toxin_hardcore_full"),
            ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "heart/toxin_hardcore_full_blinking"),
            ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "heart/toxin_hardcore_half"),
            ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "heart/toxin_hardcore_half_blinking")
    );

    @SubscribeEvent
    public static void onToxinHeartRender(PlayerHeartTypeEvent event){
        if(event.getEntity().hasEffect(MobEffectRegistry.TOXIN) || event.getEntity().hasEffect(MobEffectRegistry.ACID)){
            event.setType(TOXIN_HEARTS_ENUM_PROXY.getValue());
        }
    }
}
