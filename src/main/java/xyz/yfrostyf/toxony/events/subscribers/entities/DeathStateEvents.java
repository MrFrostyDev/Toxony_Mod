package xyz.yfrostyf.toxony.events.subscribers.entities;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageTypes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.events.ChangeToxEvent;
import xyz.yfrostyf.toxony.api.tox.ToxData;
import xyz.yfrostyf.toxony.damages.ToxinDamageSource;

@EventBusSubscriber(modid = ToxonyMain.MOD_ID)
public class DeathStateEvents {
    private static final int TOLERANCE_DEDUCTION = 20;

    //
    // Event removes entity from death state if they lose tox or kills
    // them if they gain tox while in death state.
    //
    @SubscribeEvent
    public static void onChangeTox(ChangeToxEvent event){
        if (!event.getToxData().getDeathState())return;

        ToxData plyToxData = event.getToxData();

        if(!event.isAdding()){
            plyToxData.setDeathState(false);
        }

        if(event.isAdding()) {
            event.setCanceled(true);
            plyToxData.setDeathState(false);
            plyToxData.setTox(plyToxData.getTox() - TOLERANCE_DEDUCTION * 2);
            plyToxData.setTolerance(plyToxData.getTolerance() - TOLERANCE_DEDUCTION);

            event.getEntity().hurt(new ToxinDamageSource(
                            event.getEntity().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(DamageTypes.GENERIC_KILL), null),
                    9999
            );
        }
    }

}

