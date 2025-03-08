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
                output.accept(ItemRegistry.GLASS_VIAL.get());
                output.accept(ItemRegistry.COPPER_NEEDLE.get());

                output.accept(ItemRegistry.COPPER_SCALPEL.get());
                output.accept(ItemRegistry.NETHERITE_SCALPEL.get());
                output.accept(ItemRegistry.LETHAL_DOSE.get());
                output.accept(ItemRegistry.WITCHING_BLADE.get());

                output.accept(ItemRegistry.MORTAR_PESTLE.get());
                output.accept(ItemRegistry.COPPER_CRUCIBLE.get());
                output.accept(ItemRegistry.ALEMBIC.get());
                output.accept(ItemRegistry.ALCHEMICAL_FORGE_PART.get());

                output.accept(ItemRegistry.REDSTONE_MIXTURE.get());
                output.accept(ItemRegistry.REDSTONE_SOLUTION.get());

                output.accept(ItemRegistry.POISON_PASTE.get());
                output.accept(ItemRegistry.TOXIC_PASTE.get());
                output.accept(ItemRegistry.TOXIC_FORMULA.get());
                output.accept(ItemRegistry.TOXIN.get());

                output.accept(ItemRegistry.WOLF_TOOTH.get());
                output.accept(ItemRegistry.BRITTLE_SCUTE.get());

                output.accept(ItemRegistry.OCELOT_MINT.get());
                output.accept(ItemRegistry.SNOW_MINT.get());
                output.accept(ItemRegistry.NIGHTSHADE.get());
                output.accept(ItemRegistry.SUNSPOT.get());
                output.accept(ItemRegistry.WATER_HEMLOCK.get());
                output.accept(ItemRegistry.MOONLIGHT_HEMLOCK.get());
                output.accept(ItemRegistry.COLDSNAP_LEAF.get());
                output.accept(ItemRegistry.WHIRLSNAP_LEAF.get());

                output.accept(ItemRegistry.OCELOT_MINT_SEED.get());
                output.accept(ItemRegistry.SNOW_MINT_SEED.get());
                output.accept(ItemRegistry.NIGHTSHADE_SEED.get());
                output.accept(ItemRegistry.SUNSPOT_SEED.get());
                output.accept(ItemRegistry.WATER_HEMLOCK_SEED.get());
                output.accept(ItemRegistry.MOONLIGHT_HEMLOCK_SEED.get());
                output.accept(ItemRegistry.COLDSNAP_SEED.get());
                output.accept(ItemRegistry.WHIRLSNAP_SEED.get());

                output.accept(ItemRegistry.CLAY_OIL_POT.get());
                output.accept(ItemRegistry.EMPTY_OIL_POT.get());
                output.accept(ItemRegistry.EMPTY_TOX_POT.get());
                output.accept(ItemRegistry.POISON_OIL_POT.get());
                output.accept(ItemRegistry.FATIGUE_OIL_POT.get());
                output.accept(ItemRegistry.FIRE_RESISTANCE_OIL_POT.get());
                output.accept(ItemRegistry.TOXIN_TOX_POT.get());
                output.accept(ItemRegistry.REGENERATION_TOX_POT.get());
                output.accept(ItemRegistry.WITCHFIRE_TOX_POT.get());
                output.accept(ItemRegistry.OIL_POT_SASH.get());
                output.accept(ItemRegistry.OIL_POT_BANDOLIER.get());

                output.accept(ItemRegistry.ALEMBIC_BASE.get());
                output.accept(ItemRegistry.VALENTINES_BOX.get());

            })
            .build()
    );

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
