package xyz.yfrostyf.toxony.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.client.gui.AlchemicalForgeMenu;
import xyz.yfrostyf.toxony.client.gui.AlembicMenu;
import xyz.yfrostyf.toxony.client.gui.CopperCrucibleMenu;
import xyz.yfrostyf.toxony.client.gui.MortarPestleMenu;
import java.util.function.Supplier;

public class MenuRegistry {
    private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, ToxonyMain.MOD_ID);

    public static void register(IEventBus event){
            MENUS.register(event);
    }

    public static final Supplier<MenuType<MortarPestleMenu>> MORTAR_PESTLE_MENU = MENUS.register(
            "mortar_pestle_menu",
            () -> IMenuTypeExtension.create(MortarPestleMenu::new)
    );

    public static final Supplier<MenuType<CopperCrucibleMenu>> COPPER_CRUCIBLE_MENU = MENUS.register(
            "copper_crucible_menu",
            () -> IMenuTypeExtension.create(CopperCrucibleMenu::new)
    );

    public static final Supplier<MenuType<AlembicMenu>> ALEMBIC_MENU = MENUS.register(
            "alembic_menu",
            () -> IMenuTypeExtension.create(AlembicMenu::new)
    );

    public static final Supplier<MenuType<AlchemicalForgeMenu>> ALCHEMICAL_FORGE_MENU = MENUS.register(
            "alchemical_forge_menu",
            () -> IMenuTypeExtension.create(AlchemicalForgeMenu::new)
    );
}
