package xyz.yfrostyf.toxony.registries;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.affinity.Affinity;
import xyz.yfrostyf.toxony.api.items.ToxGiverBlockItem;
import xyz.yfrostyf.toxony.api.items.ToxGiverItem;
import xyz.yfrostyf.toxony.api.oils.ItemOil;
import xyz.yfrostyf.toxony.api.oils.Oil;
import xyz.yfrostyf.toxony.items.BlendItem;
import xyz.yfrostyf.toxony.items.OilPotItem;
import xyz.yfrostyf.toxony.items.PoisonPasteItem;
import xyz.yfrostyf.toxony.items.WitchingBladeItem;

import java.util.function.Supplier;

public class ItemRegistry {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, ToxonyMain.MOD_ID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    // |-----------------------------------------------------------------------------------|
    // |-------------------------------------Oil Pots--------------------------------------|
    // |-----------------------------------------------------------------------------------|

    public static final DeferredHolder<Item, Item> POISON_OIL_POT = createOilPot(
            "poison_oil_pot", 5, OilsRegistry.POISON_OIL::get, 200, 0, 100);

    public static final DeferredHolder<Item, Item> TOXIN_OIL_POT = createOilPot(
            "toxin_oil_pot", 5, OilsRegistry.TOXIN_OIL::get, 120, 0, 100);

    public static final DeferredHolder<Item, Item> REGENERATION_OIL_POT = createOilPot(
            "regeneration_oil_pot", 5, OilsRegistry.REGENERATION_OIL::get, 200, 0, 100);

    // |-----------------------------------------------------------------------------------|
    // |------------------------------------Ingredients------------------------------------|
    // |-----------------------------------------------------------------------------------|

    public static final DeferredHolder<Item, Item> POISON_PASTE = ITEMS.register("poison_paste", () -> new PoisonPasteItem(new Item.Properties().stacksTo(16)));
    public static final DeferredHolder<Item, Item> TOXIC_PASTE = ITEMS.register("toxic_paste", () -> new PoisonPasteItem(new Item.Properties().stacksTo(16)));
    public static final DeferredHolder<Item, Item> CLAY_OIL_POT = ITEMS.register("clay_oil_pot", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> EMPTY_OIL_POT = ITEMS.register("empty_oil_pot", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> CUB_BLEND = ITEMS.register("cub_blend", () -> BlendItem.builder()
            .tox(10)
            .tolerance(10)
            .tier(1)
            .affinity(Affinity.WOLF, 1)
            .effect(new MobEffectInstance(MobEffects.WEAKNESS, 1200, 0, false, false, false))
            .build()
    );
    public static final DeferredHolder<Item, Item> FALSE_BERRIES = ITEMS.register("false_berries", () -> ToxGiverBlockItem.builder()
            .block(BlockRegistry.FALSE_BERRY_BUSH)
            .tox(20)
            .tolerance(2)
            .tier(0)
            .effect(new MobEffectInstance(MobEffects.POISON, 600, 0, false, false, false))
            .build()
    );

    public static final DeferredHolder<Item, Item> OCELOT_MINT = ITEMS.register("ocelot_mint", () -> ToxGiverItem.builder()
            .tox(20)
            .tolerance(2)
            .tier(0)
            .effect(new MobEffectInstance(MobEffects.POISON, 600, 0, false, false, false))
            .build()
    );
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



    // |----------------------------------------------------------------------------------|
    // |------------------------------------Methods---------------------------------------|
    // |----------------------------------------------------------------------------------|
    private static DeferredHolder<Item, Item> createOilPot(String name, int durability, Supplier<Oil> oil, int duration, int amplifier, int maxUses){
        return ITEMS.register(name, () -> new OilPotItem(new Item.Properties().durability(durability), () -> new ItemOil(oil.get(), duration, amplifier, maxUses, true)));
    }

}
