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
            .icon(() -> new ItemStack(ItemRegistry.LOST_JOURNAL.get()))
            //Add your items to the tab.
            .displayItems((params, output) -> {

                output.accept(ItemRegistry.LOST_JOURNAL.get());
                output.accept(ItemRegistry.GLASS_VIAL.get());
                output.accept(ItemRegistry.COPPER_NEEDLE.get());
                output.accept(ItemRegistry.TOX_GAUGE.get());
                output.accept(ItemRegistry.MAGNIFYING_GLASS.get());

                output.accept(ItemRegistry.REDSTONE_MIXTURE.get());
                output.accept(ItemRegistry.REDSTONE_SOLUTION.get());

                output.accept(ItemRegistry.POTION_FLASK.get());
                output.accept(ItemRegistry.TOXIN_FLASK.get());

                output.accept(ItemRegistry.COPPER_SCALPEL.get());
                output.accept(ItemRegistry.NETHERITE_SCALPEL.get());
                output.accept(ItemRegistry.LETHAL_DOSE.get());
                output.accept(ItemRegistry.WITCHING_BLADE.get());
                output.accept(ItemRegistry.BONE_SAW.get());
                output.accept(ItemRegistry.VENOM_CLUB.get());
                output.accept(ItemRegistry.FLAIL.get());
                output.accept(ItemRegistry.FLINTLOCK.get());
                output.accept(ItemRegistry.IRON_ROUND.get());
                output.accept(ItemRegistry.CYCLEBOW.get());

                output.accept(ItemRegistry.BOLT_CARTRIDGE.get());
                output.accept(ItemRegistry.BOLT.get());
                output.accept(ItemRegistry.POISON_BOLT.get());
                output.accept(ItemRegistry.GLOWING_BOLT.get());
                output.accept(ItemRegistry.TOXIN_BOLT.get());
                output.accept(ItemRegistry.REGENERATION_BOLT.get());
                output.accept(ItemRegistry.SMOKE_BOLT.get());
                output.accept(ItemRegistry.WITCHFIRE_BOLT.get());

                output.accept(ItemRegistry.MORTAR_PESTLE.get());
                output.accept(ItemRegistry.REDSTONE_MORTAR.get());
                output.accept(ItemRegistry.COPPER_CRUCIBLE.get());
                output.accept(ItemRegistry.ALEMBIC.get());
                output.accept(ItemRegistry.ALCHEMICAL_FORGE_PART.get());

                output.accept(ItemRegistry.PLAGUE_DOCTOR_HOOD.get());
                output.accept(ItemRegistry.PLAGUE_DOCTOR_COAT.get());
                output.accept(ItemRegistry.PLAGUE_DOCTOR_LEGGINGS.get());
                output.accept(ItemRegistry.PLAGUE_DOCTOR_BOOTS.get());

                output.accept(ItemRegistry.PLAGUEBRINGER_MASK.get());
                output.accept(ItemRegistry.PLAGUEBRINGER_COAT.get());
                output.accept(ItemRegistry.PLAGUEBRINGER_LEGGINGS.get());
                output.accept(ItemRegistry.PLAGUEBRINGER_BOOTS.get());

                output.accept(ItemRegistry.HUNTER_HAT.get());
                output.accept(ItemRegistry.HUNTER_COAT.get());
                output.accept(ItemRegistry.HUNTER_LEGGINGS.get());
                output.accept(ItemRegistry.HUNTER_BOOTS.get());

                output.accept(ItemRegistry.PROFESSIONAL_HUNTER_HAT.get());
                output.accept(ItemRegistry.PROFESSIONAL_HUNTER_COAT.get());
                output.accept(ItemRegistry.PROFESSIONAL_HUNTER_LEGGINGS.get());
                output.accept(ItemRegistry.PROFESSIONAL_HUNTER_BOOTS.get());

                output.accept(ItemRegistry.OIL_BASE.get());
                output.accept(ItemRegistry.CLAY_OIL_POT.get());
                output.accept(ItemRegistry.EMPTY_OIL_POT.get());
                output.accept(ItemRegistry.EMPTY_TOX_POT.get());
                output.accept(ItemRegistry.POISON_OIL_POT.get());
                output.accept(ItemRegistry.GLOWING_OIL_POT.get());
                output.accept(ItemRegistry.FATIGUE_OIL_POT.get());
                output.accept(ItemRegistry.FIRE_RESISTANCE_OIL_POT.get());
                output.accept(ItemRegistry.ACID_OIL_POT.get());
                output.accept(ItemRegistry.MENDING_OIL_POT.get());
                output.accept(ItemRegistry.TOXIN_TOX_POT.get());
                output.accept(ItemRegistry.REGENERATION_TOX_POT.get());
                output.accept(ItemRegistry.SMOKE_TOX_POT.get());
                output.accept(ItemRegistry.ACID_TOX_POT.get());
                output.accept(ItemRegistry.WITCHFIRE_TOX_POT.get());
                output.accept(ItemRegistry.OIL_POT_SASH.get());
                output.accept(ItemRegistry.OIL_POT_BANDOLIER.get());
                output.accept(ItemRegistry.ETERNAL_PLAGUE.get());

                output.accept(ItemRegistry.POISON_BLEND.get());
                output.accept(ItemRegistry.TOXIC_BLEND.get());
                output.accept(ItemRegistry.PURE_BLEND.get());
                output.accept(ItemRegistry.TOXIN_CANISTER.get());
                output.accept(ItemRegistry.ALEMBIC_BASE.get());
                output.accept(ItemRegistry.VALENTINES_BOX.get());
                output.accept(ItemRegistry.TOXIC_CAKE.get());

                output.accept(ItemRegistry.VIAL_RACK.get());
                output.accept(ItemRegistry.BELL_JAR.get());
                output.accept(ItemRegistry.COPPER_SCALE.get());
                output.accept(ItemRegistry.LOOSE_PAPER.get());

                output.accept(ItemRegistry.GUIDED_SPIRIT_SPAWN_EGG.get());
            })
            .build()
    );

    public static final Supplier<CreativeModeTab> TOXONY_MATERIAL_TAB = CREATIVE_MODE_TABS.register("toxony_material_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + ToxonyMain.MOD_ID + ".tab.materials"))
            .icon(() -> new ItemStack(ItemRegistry.NIGHTSHADE.get()))
            .displayItems((params, output) -> {

                output.accept(ItemRegistry.POISON_PASTE.get());
                output.accept(ItemRegistry.TOXIC_PASTE.get());
                output.accept(ItemRegistry.TOXIC_FORMULA.get());
                output.accept(ItemRegistry.TOXIN.get());
                output.accept(ItemRegistry.TOXIC_LEATHER.get());

                output.accept(ItemRegistry.VENOM_TOOTH.get());
                output.accept(ItemRegistry.BOG_BONE.get());
                output.accept(ItemRegistry.ACID_SLIMEBALL.get());
                output.accept(ItemRegistry.TOXIC_SPIT.get());
                output.accept(ItemRegistry.POISON_SAC.get());

                output.accept(ItemRegistry.FALSE_BERRIES.get());
                output.accept(ItemRegistry.OCELOT_MINT.get());
                output.accept(ItemRegistry.SNOW_MINT.get());
                output.accept(ItemRegistry.NIGHTSHADE.get());
                output.accept(ItemRegistry.SUNSPOT.get());
                output.accept(ItemRegistry.WATER_HEMLOCK.get());
                output.accept(ItemRegistry.MOONLIGHT_HEMLOCK.get());
                output.accept(ItemRegistry.COLDSNAP.get());
                output.accept(ItemRegistry.WHIRLSNAP.get());
                output.accept(ItemRegistry.BLOODROOT.get());
                output.accept(ItemRegistry.WARPROOT.get());

                output.accept(ItemRegistry.OCELOT_MINT_SEEDS.get());
                output.accept(ItemRegistry.SNOW_MINT_SEEDS.get());
                output.accept(ItemRegistry.NIGHTSHADE_SEEDS.get());
                output.accept(ItemRegistry.SUNSPOT_SEEDS.get());
                output.accept(ItemRegistry.WATER_HEMLOCK_SEEDS.get());
                output.accept(ItemRegistry.MOONLIGHT_HEMLOCK_SEEDS.get());
                output.accept(ItemRegistry.COLDSNAP_SEEDS.get());
                output.accept(ItemRegistry.WHIRLSNAP_SEEDS.get());
                output.accept(ItemRegistry.BLOODROOT_FUNGUS.get());
                output.accept(ItemRegistry.WARPROOT_FUNGUS.get());

                output.accept(ItemRegistry.AFFINITY_FUSION_MIX.get());
                output.accept(ItemRegistry.UNKNOWN_SUBSTANCE.get());
                output.accept(ItemRegistry.HEAT_SUBSTANCE.get());
                output.accept(ItemRegistry.DECAY_SUBSTANCE.get());
                output.accept(ItemRegistry.OCEAN_SUBSTANCE.get());
                output.accept(ItemRegistry.FOREST_SUBSTANCE.get());
                output.accept(ItemRegistry.NETHER_SUBSTANCE.get());
                output.accept(ItemRegistry.COLD_SUBSTANCE.get());

                output.accept(ItemRegistry.ANCIENT_SILVER.get());
                output.accept(ItemRegistry.SILVER_SCRAP.get());
                output.accept(ItemRegistry.SILVER_STEEL_BLEND.get());
                output.accept(ItemRegistry.SILVER_STEEL_INGOT.get());
                output.accept(ItemRegistry.SILVER_STEEL_UPGRADE_SMITHING_TEMPLATE.get());
                output.accept(ItemRegistry.FLINTLOCK_COMPONENTS.get());
            })
            .build()
    );

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
