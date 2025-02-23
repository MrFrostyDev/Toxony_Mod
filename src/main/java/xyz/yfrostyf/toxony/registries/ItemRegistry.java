package xyz.yfrostyf.toxony.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.affinity.Affinity;
import xyz.yfrostyf.toxony.api.items.*;
import xyz.yfrostyf.toxony.api.oils.ItemOil;
import xyz.yfrostyf.toxony.api.oils.Oil;
import xyz.yfrostyf.toxony.items.BlendItem;
import xyz.yfrostyf.toxony.items.OilPotItem;
import xyz.yfrostyf.toxony.items.PoisonPasteItem;
import xyz.yfrostyf.toxony.items.WitchingBladeItem;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, ToxonyMain.MOD_ID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    // |-----------------------------------------------------------------------------------|
    // |--------------------------------------Tools----------------------------------------|
    // |-----------------------------------------------------------------------------------|
    public static final DeferredHolder<Item, Item> MAGNIFYING_GLASS = ITEMS.register("magnifying_glass", () -> new Item(new Item.Properties()));


    // |-----------------------------------------------------------------------------------|
    // |-------------------------------------Oil Pots--------------------------------------|
    // |-----------------------------------------------------------------------------------|
    public static final DeferredHolder<Item, Item> CLAY_OIL_POT = ITEMS.register("clay_oil_pot", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> EMPTY_OIL_POT = ITEMS.register("empty_oil_pot", () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> POISON_OIL_POT = createOilPot(
            "poison_oil_pot", 5, OilsRegistry.POISON_OIL::get, 200, 0, 100);

    public static final DeferredHolder<Item, Item> TOXIN_OIL_POT = createOilPot(
            "toxin_oil_pot", 5, OilsRegistry.TOXIN_OIL::get, 120, 0, 100);

    public static final DeferredHolder<Item, Item> REGENERATION_OIL_POT = createOilPot(
            "regeneration_oil_pot", 5, OilsRegistry.REGENERATION_OIL::get, 200, 0, 100);

    // |----------------------------------------------------------------------------------|
    // |-------------------------------------Blends---------------------------------------|
    // |----------------------------------------------------------------------------------|
    public static final DeferredHolder<Item, Item> POISON_BLEND = ITEMS.register("poison_blend", () -> BlendItem.builder()
            .tox(20).tolerance(8).tier(0)
            .returnItem(() -> new ItemStack(Items.BOWL))
            .effect(new MobEffectInstance(MobEffects.POISON, 800, 0))
            .build()
    );
    public static final DeferredHolder<Item, Item> TOXIC_BLEND = ITEMS.register("toxic_blend", () -> BlendItem.builder()
            .tox(35).tolerance(18).tier(1)
            .returnItem(() -> new ItemStack(Items.BOWL))
            .effect(new MobEffectInstance(MobEffectRegistry.TOXIN, 800, 0))
            .build()
    );
    public static final DeferredHolder<Item, Item> PURE_BLEND = ITEMS.register("pure_blend", () -> BlendItem.builder()
            .tox(55).tolerance(30).tier(2)
            .returnItem(() -> new ItemStack(Items.BOWL))
            .effect(new MobEffectInstance(MobEffectRegistry.TOXIN, 1200, 1))
            .build()
    );

    // |-----------------------------------------------------------------------------------|
    // |------------------------------------Ingredients------------------------------------|
    // |-----------------------------------------------------------------------------------|
    public static final DeferredHolder<Item, Item> POISON_PASTE = ITEMS.register("poison_paste", () -> new PoisonPasteItem(new Item.Properties().stacksTo(16)));
    public static final DeferredHolder<Item, Item> TOXIC_PASTE = ITEMS.register("toxic_paste", () -> new PoisonPasteItem(new Item.Properties().stacksTo(16)));
    public static final DeferredHolder<Item, Item> TOXIC_FORMULA = ITEMS.register("toxic_formula", () -> new PoisonPasteItem(new Item.Properties().stacksTo(16)));
    public static final DeferredHolder<Item, Item> TOXIN = ITEMS.register("toxin", () -> ToxGiverItem.builder()
            .tox(40).tolerance(20).tier(2)
            .returnItem(() -> new ItemStack(Items.GLASS_BOTTLE))
            .effect(new MobEffectInstance(MobEffectRegistry.TOXIN, 500, 2, false, false, false))
            .build()
    );

    public static final DeferredHolder<Item, Item> FALSE_BERRIES = ITEMS.register("false_berries", () -> ToxGiverBlockItem.builder()
            .properties(createAffinitiesProperty(AffinityRegistry.FOREST.getKey()))
            .block(BlockRegistry.FALSE_BERRY_BUSH)
            .tox(3).tolerance(1).tier(0)
            .effect(new MobEffectInstance(MobEffects.POISON, 600, 0, false, false, false))
            .build()
    );

    public static final DeferredHolder<Item, Item> OCELOT_MINT = ITEMS.register("ocelot_mint", () -> ToxGiverItem.builder()
            .properties(createAffinitiesProperty(AffinityRegistry.SUN.getKey(), AffinityRegistry.FOREST.getKey()))
            .tox(2).tolerance(1).tier(0)
            .effect(new MobEffectInstance(MobEffects.POISON, 600, 0, false, false, false))
            .build()
    );

    public static final DeferredHolder<Item, Item> SNOW_MINT = ITEMS.register("snow_mint", () -> ToxGiverItem.builder()
            .properties(createAffinitiesProperty(AffinityRegistry.COLD.getKey()))
            .tox(3).tolerance(1).tier(1)
            .effect(new MobEffectInstance(MobEffects.POISON, 800, 0, false, false, false))
            .build()
    );

    public static final DeferredHolder<Item, Item> NIGHTSHADE = ITEMS.register("nightshade", () -> ToxGiverItem.builder()
            .properties(createAffinitiesProperty(AffinityRegistry.MOON.getKey(), AffinityRegistry.FOREST.getKey()))
            .tox(2).tolerance(1).tier(0)
            .effect(new MobEffectInstance(MobEffects.POISON, 600, 0, false, false, false))
            .build()
    );

    public static final DeferredHolder<Item, Item> SUNSPOT = ITEMS.register("sunpot", () -> ToxGiverItem.builder()
            .properties(createAffinitiesProperty(AffinityRegistry.SUN.getKey()))
            .tox(2).tolerance(1).tier(1)
            .effect(new MobEffectInstance(MobEffects.POISON, 600, 0, false, false, false))
            .build()
    );

    public static final DeferredHolder<Item, Item> WATER_HEMLOCK = ITEMS.register("water_hemlock", () -> ToxGiverItem.builder()
            .properties(createAffinitiesProperty(AffinityRegistry.OCEAN.getKey(), AffinityRegistry.WIND.getKey()))
            .tox(2).tolerance(1).tier(0)
            .effect(new MobEffectInstance(MobEffects.POISON, 600, 0, false, false, false))
            .build()
    );

    public static final DeferredHolder<Item, Item> MOONLIGHT_HEMLOCK = ITEMS.register("moonlight_hemlock", () -> ToxGiverItem.builder()
            .properties(createAffinitiesProperty(AffinityRegistry.MOON.getKey()))
            .tox(2).tolerance(1).tier(1)
            .effect(new MobEffectInstance(MobEffects.POISON, 600, 0, false, false, false))
            .build()
    );

    public static final DeferredHolder<Item, Item> BRITTLE_SCUTE = ITEMS.register("brittle_scute", () -> new Item(
            createAffinitiesProperty(AffinityRegistry.SUN.getKey(), AffinityRegistry.OCEAN.getKey())
    ));

    public static final DeferredHolder<Item, Item> WOLF_TOOTH = ITEMS.register("wolf_tooth", () -> new Item(
            createAffinitiesProperty(AffinityRegistry.MOON.getKey(), AffinityRegistry.FOREST.getKey())
    ));

    // |-----------------------------------------------------------------------------------|
    // |------------------------------------Tox Fueled-------------------------------------|
    // |-----------------------------------------------------------------------------------|
    public static final DeferredHolder<Item, Item> WITCHING_BLADE = ITEMS.register("witching_blade", () -> WitchingBladeItem.builder()
            .properties(new Item.Properties().attributes(WitchingBladeItem.createAttributes(6, -2.4F)))
            .tickrate(40)
            .cooldown(60)
            .sound(SoundEvents.FIRECHARGE_USE)
            .build()
    );

    // |-----------------------------------------------------------------------------------|
    // |------------------------------------Block Items------------------------------------|
    // |-----------------------------------------------------------------------------------|
    public static final DeferredHolder<Item, Item> MORTAR_PESTLE = ITEMS.register("mortar_pestle", () -> new BlockItem(BlockRegistry.MORTAR_PESTLE.get(), new Item.Properties()));
    public static final DeferredHolder<Item, Item> COPPER_CRUCIBLE = ITEMS.register("copper_crucible", () -> new BlockItem(BlockRegistry.COPPER_CRUCIBLE.get(), new Item.Properties()));
    public static final DeferredHolder<Item, Item> ALEMBIC = ITEMS.register("alembic", () -> new BlockItem(BlockRegistry.ALEMBIC.get(), new Item.Properties()));

    public static final DeferredHolder<Item, Item> OCELOT_MINT_SEED = ITEMS.register("ocelot_mint_seed", () -> new ItemNameBlockItem(BlockRegistry.OCELOT_MINT.get(), new Item.Properties()));
    public static final DeferredHolder<Item, Item> SNOW_MINT_SEED = ITEMS.register("snow_mint_seed", () -> new ItemNameBlockItem(BlockRegistry.SNOW_MINT.get(), new Item.Properties()));
    public static final DeferredHolder<Item, Item> NIGHTSHADE_SEED = ITEMS.register("nightshade_seed", () -> new ItemNameBlockItem(BlockRegistry.NIGHTSHADE.get(), new Item.Properties()));
    public static final DeferredHolder<Item, Item> SUNSPOT_SEED = ITEMS.register("sunspot_seed", () -> new ItemNameBlockItem(BlockRegistry.SUNSPOT.get(), new Item.Properties()));
    public static final DeferredHolder<Item, Item> WATER_HEMLOCK_SEED = ITEMS.register("water_hemlock_seed", () -> new ItemNameBlockItem(BlockRegistry.WATER_HEMLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<Item, Item> MOONLIGHT_HEMLOCK_SEED = ITEMS.register("moonlight_hemlock_seed", () -> new ItemNameBlockItem(BlockRegistry.MOONLIGHT_HEMLOCK.get(), new Item.Properties()));


    // |-----------------------------------------------------------------------------------|
    // |------------------------------------Misc Items-------------------------------------|
    // |-----------------------------------------------------------------------------------|
    public static final DeferredHolder<Item, Item> VALENTINES_BOX = ITEMS.register("valentines_box", () -> new BlockItem(BlockRegistry.VALENTINES_BOX.get(), new Item.Properties()));
    public static final DeferredHolder<Item, Item> MINT_CHOCOLATE = ITEMS.register("mint_chocolate", () -> new Item(new Item.Properties().food(new FoodProperties(
            2, 2,
            false, 2,
            Optional.empty(),
            List.of(new FoodProperties.PossibleEffect(
                    () -> new MobEffectInstance(MobEffects.WATER_BREATHING, 100, 0), 0.5F)
            )
    ))));

    public static final DeferredHolder<Item, Item> MILK_CHOCOLATE = ITEMS.register("milk_chocolate", () -> new Item(new Item.Properties().food(new FoodProperties(
            2, 2,
            false, 2,
            Optional.empty(),
            List.of(new FoodProperties.PossibleEffect(
                    () -> new MobEffectInstance(MobEffects.HEALTH_BOOST, 200, 0), 0.5F)
            )
    ))));

    public static final DeferredHolder<Item, Item> DARK_CHOCOLATE = ITEMS.register("dark_chocolate", () -> new Item(new Item.Properties().food(new FoodProperties(
            2, 2,
            false, 2,
            Optional.empty(),
            List.of(new FoodProperties.PossibleEffect(
                    () -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 0), 0.5F)
            )
    ))));

    // |----------------------------------------------------------------------------------|
    // |------------------------------------Methods---------------------------------------|
    // |----------------------------------------------------------------------------------|
    @SafeVarargs
    public static Item.Properties createAffinitiesProperty(ResourceKey<Affinity>... affinities){
        List<ResourceKey<Affinity>> list = List.of(affinities);
        return new Item.Properties().component(DataComponentsRegistry.POSSIBLE_AFFINITIES, list);
    }

    private static DeferredHolder<Item, Item> createOilPot(String name, int durability, Supplier<Oil> oil, int duration, int amplifier, int maxUses){
        return ITEMS.register(name, () -> new OilPotItem(new Item.Properties().durability(durability), () -> new ItemOil(oil.get(), duration, amplifier, maxUses, true)));
    }

}
