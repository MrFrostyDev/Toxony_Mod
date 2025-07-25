package xyz.yfrostyf.toxony.registries;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.affinity.Affinity;
import xyz.yfrostyf.toxony.api.items.ToxGiverBlockItem;
import xyz.yfrostyf.toxony.api.items.ToxGiverItem;
import xyz.yfrostyf.toxony.api.oils.ItemOil;
import xyz.yfrostyf.toxony.api.oils.Oil;
import xyz.yfrostyf.toxony.items.*;
import xyz.yfrostyf.toxony.items.armor.HunterArmorItem;
import xyz.yfrostyf.toxony.items.armor.PlagueDoctorArmorItem;
import xyz.yfrostyf.toxony.items.armor.PlaguebringerArmorItem;
import xyz.yfrostyf.toxony.items.armor.ProfessionalHunterArmorItem;
import xyz.yfrostyf.toxony.items.weapons.*;

import java.util.List;
import java.util.Optional;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, ToxonyMain.MOD_ID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    // |-----------------------------------------------------------------------------------|
    // |--------------------------------------Armors---------------------------------------|
    // |-----------------------------------------------------------------------------------|
    public static final DeferredHolder<Item, Item> PLAGUE_DOCTOR_HOOD = ITEMS.register("plague_doctor_hood", () -> new PlagueDoctorArmorItem(
            ArmorMaterialRegistry.PLAGUE_DOCTOR_ARMOR_MATERIAL, ArmorItem.Type.HELMET, new Item.Properties()
            .stacksTo(1).durability(165)
    ));

    public static final DeferredHolder<Item, Item> PLAGUE_DOCTOR_COAT = ITEMS.register("plague_doctor_coat", () -> new PlagueDoctorArmorItem(
            ArmorMaterialRegistry.PLAGUE_DOCTOR_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Properties()
            .stacksTo(1).durability(240)
    ));

    public static final DeferredHolder<Item, Item> PLAGUE_DOCTOR_LEGGINGS = ITEMS.register("plague_doctor_leggings", () -> new PlagueDoctorArmorItem(
            ArmorMaterialRegistry.PLAGUE_DOCTOR_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Properties()
            .stacksTo(1).durability(225)
    ));

    public static final DeferredHolder<Item, Item> PLAGUE_DOCTOR_BOOTS = ITEMS.register("plague_doctor_boots", () -> new PlagueDoctorArmorItem(
            ArmorMaterialRegistry.PLAGUE_DOCTOR_ARMOR_MATERIAL, ArmorItem.Type.BOOTS, new Item.Properties()
            .stacksTo(1).durability(195)
    ));

    public static final DeferredHolder<Item, Item> PLAGUEBRINGER_MASK = ITEMS.register("plaguebringer_mask", () -> new PlaguebringerArmorItem(
            ArmorMaterialRegistry.PLAGUEBRINGER_ARMOR_MATERIAL, ArmorItem.Type.HELMET, new Item.Properties()
            .stacksTo(1).durability(407)
    ));

    public static final DeferredHolder<Item, Item> PLAGUEBRINGER_COAT = ITEMS.register("plaguebringer_coat", () -> new PlaguebringerArmorItem(
            ArmorMaterialRegistry.PLAGUEBRINGER_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Properties()
            .stacksTo(1).durability(592)
    ));

    public static final DeferredHolder<Item, Item> PLAGUEBRINGER_LEGGINGS = ITEMS.register("plaguebringer_leggings", () -> new PlaguebringerArmorItem(
            ArmorMaterialRegistry.PLAGUEBRINGER_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Properties()
            .stacksTo(1).durability(555)
    ));

    public static final DeferredHolder<Item, Item> PLAGUEBRINGER_BOOTS = ITEMS.register("plaguebringer_boots", () -> new PlaguebringerArmorItem(
            ArmorMaterialRegistry.PLAGUEBRINGER_ARMOR_MATERIAL, ArmorItem.Type.BOOTS, new Item.Properties()
            .stacksTo(1).durability(481)
    ));

    public static final DeferredHolder<Item, Item> HUNTER_HAT = ITEMS.register("hunter_hat", () -> new HunterArmorItem(
            ArmorMaterialRegistry.HUNTER_ARMOR_MATERIAL, ArmorItem.Type.HELMET, new Item.Properties()
            .stacksTo(1).durability(165)
    ));

    public static final DeferredHolder<Item, Item> HUNTER_COAT = ITEMS.register("hunter_coat", () -> new HunterArmorItem(
            ArmorMaterialRegistry.HUNTER_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Properties()
            .stacksTo(1).durability(240)
    ));

    public static final DeferredHolder<Item, Item> HUNTER_LEGGINGS = ITEMS.register("hunter_leggings", () -> new HunterArmorItem(
            ArmorMaterialRegistry.HUNTER_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Properties()
            .stacksTo(1).durability(225)
    ));

    public static final DeferredHolder<Item, Item> HUNTER_BOOTS = ITEMS.register("hunter_boots", () -> new HunterArmorItem(
            ArmorMaterialRegistry.HUNTER_ARMOR_MATERIAL, ArmorItem.Type.BOOTS, new Item.Properties()
            .stacksTo(1).durability(195)
    ));

    public static final DeferredHolder<Item, Item> PROFESSIONAL_HUNTER_HAT = ITEMS.register("professional_hunter_hat", () -> new ProfessionalHunterArmorItem(
            ArmorMaterialRegistry.PROFESSIONAL_HUNTER_ARMOR_MATERIAL, ArmorItem.Type.HELMET, new Item.Properties()
            .stacksTo(1).durability(407).fireResistant()
    ));

    public static final DeferredHolder<Item, Item> PROFESSIONAL_HUNTER_COAT = ITEMS.register("professional_hunter_coat", () -> new ProfessionalHunterArmorItem(
            ArmorMaterialRegistry.PROFESSIONAL_HUNTER_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Properties()
            .stacksTo(1).durability(592).fireResistant()
    ));

    public static final DeferredHolder<Item, Item> PROFESSIONAL_HUNTER_LEGGINGS = ITEMS.register("professional_hunter_leggings", () -> new ProfessionalHunterArmorItem(
            ArmorMaterialRegistry.PROFESSIONAL_HUNTER_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Properties()
            .stacksTo(1).durability(555).fireResistant()
    ));

    public static final DeferredHolder<Item, Item> PROFESSIONAL_HUNTER_BOOTS = ITEMS.register("professional_hunter_boots", () -> new ProfessionalHunterArmorItem(
            ArmorMaterialRegistry.PROFESSIONAL_HUNTER_ARMOR_MATERIAL, ArmorItem.Type.BOOTS, new Item.Properties()
            .stacksTo(1).durability(481).fireResistant()
    ));

    // |-----------------------------------------------------------------------------------|
    // |--------------------------------------Tools----------------------------------------|
    // |-----------------------------------------------------------------------------------|
    public static final DeferredHolder<Item, Item> TOX_GAUGE = ITEMS.register("tox_gauge", () -> new ToxGaugeItem(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> GLASS_VIAL = ITEMS.register("glass_vial", () -> new VialItem(new Item.Properties().stacksTo(64)));
    public static final DeferredHolder<Item, Item> TOX_VIAL = ITEMS.register("tox_vial", () -> new FullVialItem(new Item.Properties().stacksTo(64)));

    public static final DeferredHolder<Item, Item> POTION_FLASK = ITEMS.register("potion_flask", () -> new PotionFlaskItem(
            new Item.Properties().stacksTo(1).durability(3),
            2.0F
    ));
    public static final DeferredHolder<Item, Item> TOXIN_FLASK = ITEMS.register("toxin_flask", () -> new PotionFlaskItem(
            new Item.Properties().stacksTo(1).durability(5),
            3.0F
    ));

    public static final DeferredHolder<Item, Item> REDSTONE_MIXTURE = ITEMS.register("redstone_mixture", () -> new Item(new Item.Properties().stacksTo(8)));
    public static final DeferredHolder<Item, Item> REDSTONE_SOLUTION = ITEMS.register("redstone_solution", () -> new Item(new Item.Properties().stacksTo(8)));
    public static final DeferredHolder<Item, Item> AFFINITY_SOLUTION = ITEMS.register("affinity_solution", () -> new AffinitySolutionItem(new Item.Properties().stacksTo(8)));
    public static final DeferredHolder<Item, Item> MAGNIFYING_GLASS = ITEMS.register("magnifying_glass", () -> new MagnifyingGlassItem(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> COPPER_NEEDLE = ITEMS.register("copper_needle", () -> new NeedleItem(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> TOX_NEEDLE = ITEMS.register("tox_needle", () -> new ToxNeedleItem(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> COPPER_SCALPEL = ITEMS.register("copper_scalpel", () -> new ScalpelItem(ToxonyTiers.COPPER, new Item.Properties().stacksTo(1)
            .durability(100).attributes(ScalpelItem.createAttributes(2.0F, -2.0F))
    ));
    public static final DeferredHolder<Item, Item> NETHERITE_SCALPEL = ITEMS.register("netherite_scalpel", () -> new ScalpelItem(Tiers.NETHERITE, new Item.Properties().stacksTo(1)
            .fireResistant().durability(1350).attributes(ScalpelItem.createAttributes(4.0F, -2.0F))
    ));

    public static final DeferredHolder<Item, Item> CYCLEBOW = ITEMS.register("cyclebow", () -> new CycleBowItem(new Item.Properties()
            .stacksTo(1).durability(550), 3));

    public static final DeferredHolder<Item, Item> FLINTLOCK = ITEMS.register("flintlock", () -> new FlintlockItem(new Item.Properties()
            .stacksTo(1).durability(500).fireResistant()));

    public static final DeferredHolder<Item, Item> IRON_ROUND = ITEMS.register("iron_round", () -> new FlintlockRoundItem(new Item.Properties().stacksTo(64), 8));

    public static final DeferredHolder<Item, Item> BONE_SAW = ITEMS.register("bone_saw", () -> new BoneSawItem(new Item.Properties().stacksTo(1)
            .durability(400).attributes(BoneSawItem.createAttributes(4.0F, -2.0F))
    ));

    public static final DeferredHolder<Item, Item> VENOM_CLUB = ITEMS.register("venom_club", () -> new VenomClubItem(new Item.Properties().stacksTo(1)
            .durability(300).attributes(VenomClubItem.createAttributes(8.0F, -3.1F))
    ));

    public static final DeferredHolder<Item, Item> FLAIL = ITEMS.register("flail", () -> new FlailItem(new Item.Properties().stacksTo(1)
            .durability(250).attributes(FlailItem.createAttributes(14.0F, -3.4F))));

    // |-----------------------------------------------------------------------------------|
    // |-----------------------------------Cycle Bolts-------------------------------------|
    // |-----------------------------------------------------------------------------------|
    public static final DeferredHolder<Item, Item> BOLT_CARTRIDGE = ITEMS.register("bolt_cartridge", () -> new BoltCartridgeItem(new Item.Properties().stacksTo(32)));

    public static final DeferredHolder<Item, Item> BOLT = ITEMS.register("bolt", () -> new BoltItem(new Item.Properties().stacksTo(64)));

    public static final DeferredHolder<Item, Item> POISON_BOLT = ITEMS.register("poison_bolt", () -> new BoltItem(new Item.Properties().stacksTo(64),
            () -> ItemOil.createItemOil(OilsRegistry.POISON_OIL, 200, 0)));

    public static final DeferredHolder<Item, Item> GLOWING_BOLT = ITEMS.register("glowing_bolt", () -> new BoltItem(new Item.Properties().stacksTo(64),
            () -> ItemOil.createItemOil(OilsRegistry.GLOWING_OIL, 600, 0)));

    public static final DeferredHolder<Item, Item> WITCHFIRE_BOLT = ITEMS.register("witchfire_bolt", () -> new BoltItem(new Item.Properties().stacksTo(64),
            () -> ItemOil.createItemOil(OilsRegistry.WITCHFIRE_OIL, 200, 0)));

    public static final DeferredHolder<Item, Item> TOXIN_BOLT = ITEMS.register("toxin_bolt", () -> new BoltItem(new Item.Properties().stacksTo(64),
            () -> ItemOil.createItemOil(OilsRegistry.TOXIN_OIL, 200, 0)));

    public static final DeferredHolder<Item, Item> SMOKE_BOLT = ITEMS.register("smoke_bolt", () -> new BoltItem(new Item.Properties().stacksTo(64),
            () -> ItemOil.createItemOil(OilsRegistry.SMOKE_OIL, 200, 1)));

    public static final DeferredHolder<Item, Item> REGENERATION_BOLT = ITEMS.register("regeneration_bolt", () -> new BoltItem(new Item.Properties().stacksTo(64),
            () -> ItemOil.createItemOil(OilsRegistry.REGENERATION_OIL, 200, 0)));

    // |-----------------------------------------------------------------------------------|
    // |-------------------------------------Oil Pots--------------------------------------|
    // |-----------------------------------------------------------------------------------|
    public static final DeferredHolder<Item, Item> OIL_POT_SASH = ITEMS.register("oil_pot_sash", () -> new OilPotSashItem(new Item.Properties().durability(16)));
    public static final DeferredHolder<Item, Item> OIL_POT_BANDOLIER = ITEMS.register("oil_pot_bandolier", () -> new OilPotSashItem(new Item.Properties().durability(40)));
    public static final DeferredHolder<Item, Item> ETERNAL_PLAGUE = ITEMS.register("eternal_plague", () -> new OilPotSashItem(new Item.Properties().durability(64)));


    public static final DeferredHolder<Item, Item> OIL_BASE = ITEMS.register("oil_base", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> CLAY_OIL_POT = ITEMS.register("clay_oil_pot", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> EMPTY_OIL_POT = ITEMS.register("empty_oil_pot", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> EMPTY_TOX_POT = ITEMS.register("empty_tox_pot", () -> new Item(new Item.Properties()));


    // |-------------------------------Tier 0 -------------------------------|

    public static final DeferredHolder<Item, Item> POISON_OIL_POT = createOilPot(
            "poison_oil_pot", 5, OilsRegistry.POISON_OIL, BlockRegistry.POISON_OIL_POT,
            200, 0, 150);

    public static final DeferredHolder<Item, Item> GLOWING_OIL_POT = createOilPot(
            "glowing_oil_pot", 5, OilsRegistry.GLOWING_OIL, BlockRegistry.GLOWING_OIL_POT,
            600, 0, 150);

    public static final DeferredHolder<Item, Item> FIRE_RESISTANCE_OIL_POT = createOilPot(
            "fire_resistance_oil_pot", 5, OilsRegistry.FIRE_RESISTANCE_OIL, BlockRegistry.FIRE_RESISTANCE_OIL_POT,
            200, 0, 150);

    public static final DeferredHolder<Item, Item> FATIGUE_OIL_POT = createOilPot(
            "fatigue_oil_pot", 5, OilsRegistry.FATIGUE_OIL, BlockRegistry.FATIGUE_OIL_POT,
            300, 0, 150);

    // |-------------------------------Tier 1 -------------------------------|

    public static final DeferredHolder<Item, Item> ACID_OIL_POT = createOilPot(
            "acid_oil_pot", 5, OilsRegistry.ACID_OIL, BlockRegistry.ACID_OIL_POT,
            400, 0, 150);

    public static final DeferredHolder<Item, Item> MENDING_OIL_POT = ITEMS.register("mending_oil_pot",
            () -> new MendingOilPotItem(new Item.Properties().durability(5).stacksTo(1), BlockRegistry.MENDING_OIL_POT));


    // |-------------------------------Tier 2 -------------------------------|

    public static final DeferredHolder<Item, Item> TOXIN_TOX_POT = createOilPot(
            "toxin_tox_pot", 3, OilsRegistry.TOXIN_OIL, BlockRegistry.TOXIN_TOX_POT,
            120, 0, 100);

    public static final DeferredHolder<Item, Item> REGENERATION_TOX_POT = createOilPot(
            "regeneration_tox_pot", 3, OilsRegistry.REGENERATION_OIL, BlockRegistry.REGENERATION_TOX_POT,
            200, 0, 100);

    public static final DeferredHolder<Item, Item> SMOKE_TOX_POT = createOilPot(
            "smoke_tox_pot",3, OilsRegistry.SMOKE_OIL, BlockRegistry.SMOKE_TOX_POT,
            300, 1, 100);

    public static final DeferredHolder<Item, Item> ACID_TOX_POT = createOilPot(
            "acid_tox_pot", 3, OilsRegistry.ACID_OIL, BlockRegistry.ACID_TOX_POT,
            400, 1, 100);

    public static final DeferredHolder<Item, Item> WITCHFIRE_TOX_POT = createOilPot(
            "witchfire_tox_pot", 3, OilsRegistry.WITCHFIRE_OIL, BlockRegistry.WITCHFIRE_TOX_POT,
            200, 0, 100);


    // |----------------------------------------------------------------------------------|
    // |-------------------------------------Blends---------------------------------------|
    // |----------------------------------------------------------------------------------|
    public static final DeferredHolder<Item, Item> POISON_BLEND = ITEMS.register("poison_blend", () -> BlendItem.builder()
            .properties(new Item.Properties().stacksTo(1).craftRemainder(Items.BOWL))
            .tox(30).tolerance(15).tier(0)
            .returnItem(() -> new ItemStack(Items.BOWL))
            .effect(new MobEffectInstance(MobEffects.POISON, 1000, 0))
            .build()
    );

    public static final DeferredHolder<Item, Item> TOXIC_BLEND = ITEMS.register("toxic_blend", () -> BlendItem.builder()
            .properties(new Item.Properties().stacksTo(1).craftRemainder(Items.BOWL))
            .tox(40).tolerance(25).tier(1)
            .returnItem(() -> new ItemStack(Items.BOWL))
            .effect(new MobEffectInstance(MobEffectRegistry.TOXIN, 1000, 0))
            .build()
    );

    public static final DeferredHolder<Item, Item> PURE_BLEND = ITEMS.register("pure_blend", () -> BlendItem.builder()
            .properties(new Item.Properties().stacksTo(1).craftRemainder(Items.BOWL))
            .tox(65).tolerance(40).tier(3)
            .returnItem(() -> new ItemStack(Items.BOWL))
            .effect(new MobEffectInstance(MobEffectRegistry.TOXIN, 1800, 0))
            .build()
    );

    // |-----------------------------------------------------------------------------------|
    // |------------------------------------Ingredients------------------------------------|
    // |-----------------------------------------------------------------------------------|
    public static final DeferredHolder<Item, Item> POISON_PASTE = ITEMS.register("poison_paste", () -> new PoisonPasteItem(new Item.Properties().stacksTo(32)));
    public static final DeferredHolder<Item, Item> TOXIC_PASTE = ITEMS.register("toxic_paste", () -> new PoisonPasteItem(new Item.Properties().stacksTo(32)));
    public static final DeferredHolder<Item, Item> TOXIC_FORMULA = ITEMS.register("toxic_formula", () -> new PoisonPasteItem(new Item.Properties().stacksTo(8)));
    public static final DeferredHolder<Item, Item> TOXIN = ITEMS.register("toxin", () -> ToxinItem.builder()
            .properties(new Item.Properties().stacksTo(8))
            .tox(50).tolerance(10).tier(1)
            .returnItem(() -> new ItemStack(ItemRegistry.GLASS_VIAL))
            .effect(new MobEffectInstance(MobEffectRegistry.TOXIN, 600, 1, false, false, false))
            .build()
    );

    public static final DeferredHolder<Item, Item> FALSE_BERRIES = ITEMS.register("false_berries", () -> ToxGiverBlockItem.builder()
            .properties(createAffinitiesProperty(AffinityRegistry.FOREST.getKey(), AffinityRegistry.COLD.getKey()).food(Foods.SWEET_BERRIES))
            .block(BlockRegistry.FALSE_BERRY_BUSH)
            .tox(3).tolerance(1).tier(0)
            .effect(new MobEffectInstance(MobEffects.POISON, 600, 0, false, false, false))
            .build()
    );

    public static final DeferredHolder<Item, Item> OCELOT_MINT = ITEMS.register("ocelot_mint", () -> ToxGiverItem.builder()
            .properties(createAffinitiesProperty(AffinityRegistry.FOREST.getKey(), AffinityRegistry.HEAT.getKey()))
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
            .properties(createAffinitiesProperty(AffinityRegistry.COLD.getKey(), AffinityRegistry.FOREST.getKey()))
            .tox(2).tolerance(1).tier(0)
            .effect(new MobEffectInstance(MobEffects.POISON, 600, 0, false, false, false))
            .build()
    );

    public static final DeferredHolder<Item, Item> SUNSPOT = ITEMS.register("sunspot", () -> ToxGiverItem.builder()
            .properties(createAffinitiesProperty(AffinityRegistry.SUN.getKey()))
            .tox(2).tolerance(1).tier(1)
            .effect(new MobEffectInstance(MobEffects.POISON, 600, 0, false, false, false))
            .build()
    );

    public static final DeferredHolder<Item, Item> WATER_HEMLOCK = ITEMS.register("water_hemlock", () -> ToxGiverItem.builder()
            .properties(createAffinitiesProperty(AffinityRegistry.OCEAN.getKey(), AffinityRegistry.DECAY.getKey()))
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

    public static final DeferredHolder<Item, Item> COLDSNAP = ITEMS.register("coldsnap", () -> ToxGiverItem.builder()
            .properties(createAffinitiesProperty(AffinityRegistry.COLD.getKey(), AffinityRegistry.DECAY.getKey()))
            .tox(2).tolerance(1).tier(0)
            .effect(new MobEffectInstance(MobEffects.POISON, 600, 0, false, false, false))
            .build()
    );

    public static final DeferredHolder<Item, Item> WHIRLSNAP = ITEMS.register("whirlsnap", () -> ToxGiverItem.builder()
            .properties(createAffinitiesProperty(AffinityRegistry.WIND.getKey()))
            .tox(2).tolerance(1).tier(1)
            .effect(new MobEffectInstance(MobEffects.POISON, 600, 0, false, false, false))
            .build()
    );

    public static final DeferredHolder<Item, Item> BLOODROOT = ITEMS.register("bloodroot", () -> ToxGiverItem.builder()
            .properties(createAffinitiesProperty(AffinityRegistry.HEAT.getKey()))
            .tox(2).tolerance(1).tier(1)
            .effect(new MobEffectInstance(MobEffects.POISON, 600, 0, false, false, false))
            .build()
    );

    public static final DeferredHolder<Item, Item> WARPROOT = ITEMS.register("warproot", () -> ToxGiverItem.builder()
            .properties(createAffinitiesProperty(AffinityRegistry.SOUL.getKey()))
            .tox(2).tolerance(1).tier(1)
            .effect(new MobEffectInstance(MobEffects.POISON, 600, 0, false, false, false))
            .build()
    );

    public static final DeferredHolder<Item, Item> VENOM_TOOTH = ITEMS.register("venom_tooth", () -> new Item(
            createAffinitiesProperty(AffinityRegistry.FOREST.getKey(), AffinityRegistry.COLD.getKey())
    ));

    public static final DeferredHolder<Item, Item> ACID_SLIMEBALL = ITEMS.register("acid_slimeball", () -> new Item(
            createAffinitiesProperty(AffinityRegistry.DECAY.getKey(), AffinityRegistry.HEAT.getKey(), AffinityRegistry.OCEAN.getKey())
    ));

    public static final DeferredHolder<Item, Item> POISON_SAC = ITEMS.register("poison_sac", () -> new Item(
            createAffinitiesProperty(AffinityRegistry.OCEAN.getKey())
    ));

    public static final DeferredHolder<Item, Item> BOG_BONE = ITEMS.register("bog_bone", () -> new Item(
            createAffinitiesProperty(AffinityRegistry.DECAY.getKey())
    ));

    public static final DeferredHolder<Item, Item> TOXIC_SPIT = ITEMS.register("toxic_spit", () -> new Item(
            createAffinitiesProperty(AffinityRegistry.OCEAN.getKey())
    ));


    // Fusion Ingredients
    public static final DeferredHolder<Item, Item> AFFINITY_FUSION_MIX = ITEMS.register("affinity_fusion_mix", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> UNKNOWN_SUBSTANCE = ITEMS.register("affinity_unknown_substance", () -> new AffinityFusionSubstance(new Item.Properties()));

    public static final DeferredHolder<Item, Item> OCEAN_SUBSTANCE = ITEMS.register("ocean_substance", () -> new AffinitySubstance(
            createAffinitiesProperty(AffinityRegistry.OCEAN.getKey())
    ));

    public static final DeferredHolder<Item, Item> FOREST_SUBSTANCE = ITEMS.register("forest_substance", () -> new AffinitySubstance(
            createAffinitiesProperty(AffinityRegistry.FOREST.getKey())
    ));

    public static final DeferredHolder<Item, Item> DECAY_SUBSTANCE = ITEMS.register("decay_substance", () -> new AffinitySubstance(
            createAffinitiesProperty(AffinityRegistry.DECAY.getKey())
    ));

    public static final DeferredHolder<Item, Item> HEAT_SUBSTANCE = ITEMS.register("heat_substance", () -> new AffinitySubstance(
            createAffinitiesProperty(AffinityRegistry.HEAT.getKey())
    ));

    public static final DeferredHolder<Item, Item> COLD_SUBSTANCE = ITEMS.register("cold_substance", () -> new AffinitySubstance(
            createAffinitiesProperty(AffinityRegistry.COLD.getKey())
    ));

    public static final DeferredHolder<Item, Item> NETHER_SUBSTANCE = ITEMS.register("nether_substance", () -> new AffinitySubstance(
            createAffinitiesProperty(AffinityRegistry.NETHER.getKey())
    ));

    // |-----------------------------------------------------------------------------------|
    // |------------------------------------Tox Fueled-------------------------------------|
    // |-----------------------------------------------------------------------------------|
    public static final DeferredHolder<Item, Item> WITCHING_BLADE = ITEMS.register("witching_blade", () -> WitchingBladeItem.builder()
            .properties(new Item.Properties().attributes(WitchingBladeItem.createAttributes(6.0F, -2.4F)).durability(1600))
            .tickrate(40)
            .cooldown(60)
            .sound(SoundEvents.FIRECHARGE_USE)
            .build()
    );
    public static final DeferredHolder<Item, Item> LETHAL_DOSE = ITEMS.register("lethal_dose", () -> ToxScalpelItem.builder()
            .properties(new Item.Properties().fireResistant().attributes(ScalpelItem.createAttributes(5.0F, -2.0F)).durability(1650))
            .tickrate(40)
            .cooldown(60)
            .sound(SoundEvents.FIRECHARGE_USE)
            .build()
    );

    // |-----------------------------------------------------------------------------------|
    // |------------------------------------Block Items------------------------------------|
    // |-----------------------------------------------------------------------------------|
    public static final DeferredHolder<Item, Item> MORTAR_PESTLE = ITEMS.register("mortar_pestle", () -> new BlockItem(BlockRegistry.MORTAR_PESTLE.get(), new Item.Properties().stacksTo(16)));
    public static final DeferredHolder<Item, Item> REDSTONE_MORTAR = ITEMS.register("redstone_mortar", () -> new BlockItem(BlockRegistry.REDSTONE_MORTAR.get(), new Item.Properties().stacksTo(16)));
    public static final DeferredHolder<Item, Item> COPPER_CRUCIBLE = ITEMS.register("copper_crucible", () -> new BlockItem(BlockRegistry.COPPER_CRUCIBLE.get(), new Item.Properties().stacksTo(16)));
    public static final DeferredHolder<Item, Item> ALEMBIC = ITEMS.register("alembic", () -> new BlockItem(BlockRegistry.ALEMBIC.get(), new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> ALCHEMICAL_FORGE_PART = ITEMS.register("alchemical_forge_part", () -> new BlockItem(BlockRegistry.ALCHEMICAL_FORGE_PART.get(), new Item.Properties().stacksTo(2)));
    public static final DeferredHolder<Item, Item> POISON_FARMLAND = ITEMS.register("poison_farmland", () -> new BlockItem(BlockRegistry.POISON_FARMLAND.get(), new Item.Properties()));

    // Seeds
    public static final DeferredHolder<Item, Item> OCELOT_MINT_SEEDS = ITEMS.register("ocelot_mint_seeds", () -> new ItemNameBlockItem(BlockRegistry.OCELOT_MINT.get(), new Item.Properties()));
    public static final DeferredHolder<Item, Item> SNOW_MINT_SEEDS = ITEMS.register("snow_mint_seeds", () -> new ItemNameBlockItem(BlockRegistry.SNOW_MINT.get(), new Item.Properties()));
    public static final DeferredHolder<Item, Item> NIGHTSHADE_SEEDS = ITEMS.register("nightshade_seeds", () -> new ItemNameBlockItem(BlockRegistry.NIGHTSHADE.get(), new Item.Properties()));
    public static final DeferredHolder<Item, Item> SUNSPOT_SEEDS = ITEMS.register("sunspot_seeds", () -> new ItemNameBlockItem(BlockRegistry.SUNSPOT.get(), new Item.Properties()));
    public static final DeferredHolder<Item, Item> WATER_HEMLOCK_SEEDS = ITEMS.register("water_hemlock_seeds", () -> new ItemNameBlockItem(BlockRegistry.WATER_HEMLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<Item, Item> MOONLIGHT_HEMLOCK_SEEDS = ITEMS.register("moonlight_hemlock_seeds", () -> new ItemNameBlockItem(BlockRegistry.MOONLIGHT_HEMLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<Item, Item> COLDSNAP_SEEDS = ITEMS.register("coldsnap_seeds", () -> new ItemNameBlockItem(BlockRegistry.COLDSNAP.get(), new Item.Properties()));
    public static final DeferredHolder<Item, Item> WHIRLSNAP_SEEDS = ITEMS.register("whirlsnap_seeds", () -> new ItemNameBlockItem(BlockRegistry.WHIRLSNAP.get(), new Item.Properties()));
    public static final DeferredHolder<Item, Item> BLOODROOT_FUNGUS = ITEMS.register("bloodroot_fungus", () -> new ItemNameBlockItem(BlockRegistry.BLOODROOT.get(), new Item.Properties()));
    public static final DeferredHolder<Item, Item> WARPROOT_FUNGUS = ITEMS.register("warproot_fungus", () -> new ItemNameBlockItem(BlockRegistry.WARPROOT.get(), new Item.Properties()));

    // Plants
    public static final DeferredHolder<Item, Item> WILD_OCELOT_MINT = ITEMS.register("wild_ocelot_mint", () -> new BlockItem(BlockRegistry.WILD_OCELOT_MINT.get(), new Item.Properties()));
    public static final DeferredHolder<Item, Item> WILD_COLDSNAP = ITEMS.register("wild_coldsnap", () -> new BlockItem(BlockRegistry.WILD_COLDSNAP.get(), new Item.Properties()));
    public static final DeferredHolder<Item, Item> WILD_NIGHTSHADE = ITEMS.register("wild_nightshade", () -> new BlockItem(BlockRegistry.WILD_NIGHTSHADE.get(), new Item.Properties()));
    public static final DeferredHolder<Item, Item> WILD_WATER_HEMLOCK = ITEMS.register("wild_water_hemlock", () -> new BlockItem(BlockRegistry.WILD_WATER_HEMLOCK.get(), new Item.Properties()));
    public static final DeferredHolder<Item, Item> WILD_BLOODROOT = ITEMS.register("wild_bloodroot", () -> new BlockItem(BlockRegistry.WILD_BLOODROOT.get(), new Item.Properties()));

    // Decoration
    public static final DeferredHolder<Item, Item> VIAL_RACK = ITEMS.register("vial_rack", () -> new BlockItem(BlockRegistry.VIAL_RACK.get(), new Item.Properties().stacksTo(16)));
    public static final DeferredHolder<Item, Item> BELL_JAR = ITEMS.register("bell_jar", () -> new BlockItem(BlockRegistry.BELL_JAR.get(), new Item.Properties().stacksTo(16)));
    public static final DeferredHolder<Item, Item> COPPER_SCALE = ITEMS.register("copper_scale", () -> new BlockItem(BlockRegistry.COPPER_SCALE.get(), new Item.Properties().stacksTo(16)));
    public static final DeferredHolder<Item, Item> LOOSE_PAPER = ITEMS.register("loose_paper", () -> new BlockItem(BlockRegistry.LOOSE_PAPER.get(), new Item.Properties().stacksTo(64)));

    // Material
    public static final DeferredHolder<Item, Item> ANCIENT_SILVER = ITEMS.register("ancient_silver", () -> new BlockItem(BlockRegistry.ANCIENT_SILVER.get(), new Item.Properties().stacksTo(64).fireResistant()));

    // |-----------------------------------------------------------------------------------|
    // |------------------------------------Misc Items-------------------------------------|
    // |-----------------------------------------------------------------------------------|
    public static final DeferredHolder<Item, Item> LOST_JOURNAL = ITEMS.register("lost_journal", () -> new JournalItem(new Item.Properties().stacksTo(1)));

    public static final DeferredHolder<Item, Item> FLINTLOCK_COMPONENTS = ITEMS.register("flintlock_components", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredHolder<Item, Item> SILVER_SCRAP = ITEMS.register("silver_scrap", () -> new Item(new Item.Properties().stacksTo(64).fireResistant()));
    public static final DeferredHolder<Item, Item> SILVER_STEEL_BLEND = ITEMS.register("silver_steel_blend", () -> new Item(new Item.Properties().stacksTo(64).fireResistant()));
    public static final DeferredHolder<Item, Item> SILVER_STEEL_INGOT = ITEMS.register("silver_steel_ingot", () -> new Item(new Item.Properties().stacksTo(64).fireResistant()));
    public static final DeferredHolder<Item, Item> SILVER_STEEL_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("silver_steel_upgrade_smithing_template",
            () -> new SmithingTemplateItem(
                    Component.translatable(Util.makeDescriptionId("item", ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "silver_steel_upgrade.applies_to"))),
                    Component.translatable(Util.makeDescriptionId("item", ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "silver_steel_upgrade.ingredients"))),
                    Component.translatable(Util.makeDescriptionId("upgrade", ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "silver_steel_upgrade"))),
                    Component.translatable(Util.makeDescriptionId("item", ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "silver_steel_upgrade.base_slot_description"))),
                    Component.translatable(Util.makeDescriptionId("item", ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "silver_steel_upgrade.additions_slot_description"))),
                    List.of(
                            ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "item/empty_slot_fishing_rod"),
                            ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "item/empty_slot_flintlock_components"),
                            ResourceLocation.withDefaultNamespace("item/empty_armor_slot_helmet"),
                            ResourceLocation.withDefaultNamespace("item/empty_armor_slot_chestplate"),
                            ResourceLocation.withDefaultNamespace("item/empty_armor_slot_leggings"),
                            ResourceLocation.withDefaultNamespace("item/empty_armor_slot_boots")
                    ),
                    List.of(ResourceLocation.withDefaultNamespace("item/empty_slot_ingot"))
            )
    );

    public static final DeferredHolder<Item, Item> TOXIC_LEATHER = ITEMS.register("toxic_leather", () -> new Item(new Item.Properties().stacksTo(64)));
    public static final DeferredHolder<Item, Item> ALEMBIC_BASE = ITEMS.register("alembic_base", () -> new Item(new Item.Properties().stacksTo(16)));
    public static final DeferredHolder<Item, Item> TOXIN_CANISTER = ITEMS.register("toxin_canister", () -> new Item(new Item.Properties().stacksTo(64)));

    public static final DeferredHolder<Item, Item> VALENTINES_BOX = ITEMS.register("valentines_box", () -> new BlockItem(BlockRegistry.VALENTINES_BOX.get(), new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> TOXIC_CAKE = ITEMS.register("toxic_cake", () -> new BlockItem(BlockRegistry.TOXIC_CAKE.get(), new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> TOXIC_CAKE_SLICE = ITEMS.register("toxic_cake_slice", () -> new ThrowableToxicCakeItem(new Item.Properties().stacksTo(16)));
    public static final DeferredHolder<Item, Item> MINT_CHOCOLATE = ITEMS.register("mint_chocolate", () -> new Item(new Item.Properties().food(new FoodProperties(
            2, 2,
            false, 1,
            Optional.empty(),
            List.of(new FoodProperties.PossibleEffect(
                    () -> new MobEffectInstance(MobEffects.WATER_BREATHING, 100, 0), 0.5F)
            )
    ))));

    public static final DeferredHolder<Item, Item> MILK_CHOCOLATE = ITEMS.register("milk_chocolate", () -> new Item(new Item.Properties().food(new FoodProperties(
            2, 2,
            false, 1,
            Optional.empty(),
            List.of(new FoodProperties.PossibleEffect(
                    () -> new MobEffectInstance(MobEffects.HEALTH_BOOST, 200, 0), 0.5F)
            )
    ))));

    public static final DeferredHolder<Item, Item> DARK_CHOCOLATE = ITEMS.register("dark_chocolate", () -> new Item(new Item.Properties().food(new FoodProperties(
            2, 2,
            false, 1,
            Optional.empty(),
            List.of(new FoodProperties.PossibleEffect(
                    () -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 0), 0.5F)
            )
    ))));

    public static final DeferredHolder<Item, Item> GUIDED_SPIRIT_SPAWN_EGG = ITEMS.register("guided_spirit_spawn_egg", () -> new DeferredSpawnEggItem(
            EntityRegistry.GUIDED_SPIRIT,0x478cfc, 0x9cc1ff, new Item.Properties()
    ));

    // |----------------------------------------------------------------------------------|
    // |------------------------------------Methods---------------------------------------|
    // |----------------------------------------------------------------------------------|
    @SafeVarargs
    public static Item.Properties createAffinitiesProperty(ResourceKey<Affinity>... affinities){
        List<ResourceKey<Affinity>> list = List.of(affinities);
        return new Item.Properties().component(DataComponentsRegistry.POSSIBLE_AFFINITIES, list);
    }

    private static DeferredHolder<Item, Item> createOilPot(String name, int durability, Holder<Oil> oil, Holder<Block> oilPotBlock, int duration, int amplifier, int maxUses){
        return ITEMS.register(name, () -> new OilPotItem(new Item.Properties().durability(durability).stacksTo(1),
                new ItemOil(oil, duration, amplifier, maxUses, true),
                oilPotBlock));
    }
}
