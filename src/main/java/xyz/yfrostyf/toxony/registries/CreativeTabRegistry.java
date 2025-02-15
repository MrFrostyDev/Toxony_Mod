package xyz.yfrostyf.toxony.registries;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import xyz.yfrostyf.toxony.ToxonyMain;

import java.util.function.Supplier;

public class CreativeTabRegistry {
    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, ToxonyMain.MOD_ID);

    public static final Supplier<CreativeModeTab> TOXONY_TAB = CREATIVE_MODE_TABS.register("toxony_tab", () -> CreativeModeTab.builder()
            //Set the title of the tab. Don't forget to add a translation!
            .title(Component.translatable("itemGroup." + ToxonyMain.MOD_ID + ".tab"))
            //Set the icon of the tab.
            .icon(() -> new ItemStack(ItemRegistry.MORTAR_PESTLE.get()))
            //Add your items to the tab.
            .displayItems((params, output) -> {
                output.accept(ItemRegistry.WITCHING_BLADE.get());
                output.accept(ItemRegistry.MORTAR_PESTLE.get());
                output.accept(ItemRegistry.COPPER_CRUCIBLE.get());
                output.accept(ItemRegistry.ALEMBIC.get());
                output.accept(ItemRegistry.POISON_OIL_POT.get());

            })
            .build()
    );

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
