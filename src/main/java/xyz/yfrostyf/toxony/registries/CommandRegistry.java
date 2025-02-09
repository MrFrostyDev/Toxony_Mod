package xyz.yfrostyf.toxony.registries;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.commands.ToxCommand;

@EventBusSubscriber(modid = ToxonyMain.MOD_ID)
public class CommandRegistry {
    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {

        var commandDispatcher = event.getDispatcher();

        ToxCommand.register(commandDispatcher);
    }
}
