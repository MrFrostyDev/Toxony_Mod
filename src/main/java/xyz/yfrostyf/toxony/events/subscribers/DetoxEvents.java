package xyz.yfrostyf.toxony.events.subscribers;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.tox.ToxData;
import xyz.yfrostyf.toxony.registries.DataAttachmentRegistry;

import static net.minecraft.core.component.DataComponents.POTION_CONTENTS;

@EventBusSubscriber(modid = ToxonyMain.MOD_ID)
public class DetoxEvents{
    private static final int GOLDEN_APPLE_DETOX = -30;
    private static final int REGEN_POTION_DETOX = -50;

    //
    // Checks if the player drank any regeneration potion. If so detox player.
    //
    @SubscribeEvent
    public static void onRegenPotionDrink(LivingEntityUseItemEvent.Finish event){
        if (!(event.getEntity() instanceof Player player)) {return;}

       ItemStack item = event.getItem();

        // Thank you (ChiefArug) from the NeoForge Project Discord

        if (!item.has(POTION_CONTENTS) || item.get(POTION_CONTENTS).potion().isEmpty()) return;
        if (item.get(POTION_CONTENTS).potion().filter(holder -> holder == Potions.REGENERATION || holder == Potions.LONG_REGENERATION || holder == Potions.STRONG_REGENERATION).isPresent()) {
            ToxData plyToxData = player.getData(DataAttachmentRegistry.TOX_DATA);
            plyToxData.addTox(REGEN_POTION_DETOX);
        }
    }

    //
    // Checks if the player ate a golden or enchanted golden apple. If so detox player.
    //
    @SubscribeEvent
    public static void onGoldenAppleEat(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player player)) {return;}

        Item item = event.getItem().getItem();

        if ((item != Items.GOLDEN_APPLE) && (item != Items.ENCHANTED_GOLDEN_APPLE)){return;}
        ToxData plyToxData = player.getData(DataAttachmentRegistry.TOX_DATA);
        plyToxData.addTox(GOLDEN_APPLE_DETOX);
    }
}
