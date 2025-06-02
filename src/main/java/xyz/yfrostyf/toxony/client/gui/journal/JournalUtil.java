package xyz.yfrostyf.toxony.client.gui.journal;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.data.datagen.enchantments.ToxonyEnchantments;
import xyz.yfrostyf.toxony.registries.AffinityRegistry;
import xyz.yfrostyf.toxony.registries.ItemRegistry;

import java.util.*;

public class JournalUtil {
    public static String lastPageID = "journal.toxony.page.cover";
    private static final Item EMPTY = Items.AIR;
    private static final Level level = Minecraft.getInstance().level;
    private static final RecipeManager manager = level != null ? level.getRecipeManager() : null;

    public static void startPage(){
        JournalPages pages = JournalUtil.createPages().build(lastPageID);
        pages.updatePage();
    }

    public static void setLastPageID(String id){
     lastPageID = id;
    }

    public static Map<String, String> createIndexMap(){
        Map<String, String> map = new LinkedHashMap<>();
        map.put("Poison Basics", "journal.toxony.page.poison_basics.cover");
        map.put("Poisonous Flora", "journal.toxony.page.poisonous_flora.cover");
        map.put("Oil Basics", "journal.toxony.page.oil_basics.cover");
        map.put("Basic Tools", "journal.toxony.page.basic_tools.cover");
        map.put("Refined Process", "journal.toxony.page.refined_process.cover");
        map.put("Alchemical Warfare", "journal.toxony.page.alchemical_warfare.cover");
        map.put("Pure Chemistry", "journal.toxony.page.pure_chemistry.cover");
        map.put("Mutagens", "journal.toxony.page.mutagens.cover");
        map.put("Evolved Flora", "journal.toxony.page.evolved_flora.cover");
        map.put("Advanced Oils", "journal.toxony.page.advanced_oils.cover");
        map.put("Evolving Warfare", "journal.toxony.page.evolving_warfare.cover");
        map.put("Lost Chemistry", "journal.toxony.page.lost_chemistry.cover");
        return map;
    }

    public static JournalPagesBuilder createPages(){
        Recipe<?> recipe_poison_blend = getRecipe(manager, "mortar/poison_blend").value();
        Recipe<?> recipe_toxic_blend = getRecipe(manager, "mortar/toxic_blend").value();
        Recipe<?> recipe_poison_paste = getRecipe(manager, "mortar/poison_paste").value();
        Recipe<?> recipe_toxin_tox_pot = getRecipe(manager, "mortar/toxin_tox_pot").value();

        Recipe<?> recipe_toxin = getRecipe(manager, "alembic/toxin").value();
        Recipe<?> recipe_affinity_solution = getRecipe(manager, "alembic/affinity_solution").value();

        Recipe<?> recipe_toxic_paste = getRecipe(manager, "crucible/toxic_paste").value();

        return new JournalPagesBuilder()
                // |----------------- Intro/Index ----------------- |
                .ImagePageScreen("journal.toxony.page.cover", "textures/gui/journal/journal_cover.png")
                .TextPageScreen("journal.toxony.page.introduction.0")
                .TextPageScreen("journal.toxony.page.introduction.1")
                .IndexPageScreen("journal.toxony.page.index.0", createIndexMap())

                // |----------------- Poison Basics ----------------- |
                .ImagePageScreen("journal.toxony.page.poison_basics.cover", "textures/gui/journal/journal_poison_basics_cover.png")
                .TextPageScreen("journal.toxony.page.poison_basics.0")
                .TextCraftingPageScreenItem("journal.toxony.page.poison_basics.1", ItemRegistry.MORTAR_PESTLE.get(),
                        List.of(EMPTY, EMPTY, EMPTY,
                                Items.STONE, Items.STICK, Items.STONE,
                                Items.STONE, Items.STONE, Items.STONE)
                )
                .TextPageScreen("journal.toxony.page.poison_basics.2")
                .TextPageScreen("journal.toxony.page.poison_basics.3")
                .TextMortarPageScreenIngredients("journal.toxony.page.poison_basics.4", ItemRegistry.POISON_PASTE.get(),
                        List.of(recipe_poison_paste.getIngredients().get(0), recipe_poison_paste.getIngredients().get(1),
                                recipe_poison_paste.getIngredients().get(2), Ingredient.EMPTY)
                )
                .TextPageScreen("journal.toxony.page.poison_basics.5")

                // |----------------- Poisonous Flora ----------------- |
                .ImagePageScreen("journal.toxony.page.poisonous_flora.cover", "textures/gui/journal/journal_poisonous_flora_cover.png")
                .TextSingleItemTopScreen("journal.toxony.page.poisonous_flora.0", ItemRegistry.OCELOT_MINT.get())
                .TextSingleItemTopScreen("journal.toxony.page.poisonous_flora.1", ItemRegistry.NIGHTSHADE.get())
                .TextSingleItemTopScreen("journal.toxony.page.poisonous_flora.2", ItemRegistry.WATER_HEMLOCK.get())
                .TextSingleItemTopScreen("journal.toxony.page.poisonous_flora.3", ItemRegistry.COLDSNAP.get())
                .TextSingleItemTopScreen("journal.toxony.page.poisonous_flora.4", ItemRegistry.BLOODROOT.get())
                .TextSingleItemTopScreen("journal.toxony.page.poisonous_flora.5", ItemRegistry.POISON_FARMLAND.get())
                .TextPageScreen("journal.toxony.page.poisonous_flora.6")
                .TextSingleItemTopScreen("journal.toxony.page.poisonous_flora.7", ItemRegistry.VENOM_TOOTH.get())
                .TextSingleItemTopScreen("journal.toxony.page.poisonous_flora.8", ItemRegistry.TOXIC_SPIT.get())
                .TextSingleItemTopScreen("journal.toxony.page.poisonous_flora.9", ItemRegistry.POISON_SAC.get())
                .TextSingleItemTopScreen("journal.toxony.page.poisonous_flora.10", ItemRegistry.BOG_BONE.get())
                .TextSingleItemTopScreen("journal.toxony.page.poisonous_flora.11", ItemRegistry.ACID_SLIMEBALL.get())
                .TextPageScreen("journal.toxony.page.poisonous_flora.12")
                .TextMortarPageScreenIngredients("journal.toxony.page.poisonous_flora.13", ItemRegistry.POISON_BLEND.get(),
                        List.of(recipe_poison_blend.getIngredients().get(0), recipe_poison_blend.getIngredients().get(1),
                                Ingredient.EMPTY, Ingredient.EMPTY
                        )
                )
                .TextPageScreen("journal.toxony.page.poisonous_flora.14")

                // |----------------- Oil Basics ----------------- |
                .ImagePageScreen("journal.toxony.page.oil_basics.cover", "textures/gui/journal/journal_oil_basics_cover.png")
                .TextPageScreen("journal.toxony.page.oil_basics.0")
                .TextCraftingPageScreenItem("journal.toxony.page.oil_basics.1", ItemRegistry.CLAY_OIL_POT.get(),
                        List.of(EMPTY, EMPTY, EMPTY,
                                Items.CLAY_BALL, ItemRegistry.POISON_PASTE.get(), Items.CLAY_BALL,
                                EMPTY, Items.CLAY_BALL, EMPTY)
                )
                .TextMortarPageScreen("journal.toxony.page.oil_basics.2", ItemRegistry.POISON_OIL_POT.get(),
                        List.of(Items.HONEYCOMB, ItemRegistry.POISON_PASTE.get(),
                                EMPTY, EMPTY
                        )
                )
                .TextMortarPageScreen("journal.toxony.page.oil_basics.3", ItemRegistry.FATIGUE_OIL_POT.get(),
                        List.of(Items.HONEYCOMB, Items.FERMENTED_SPIDER_EYE,
                                ItemRegistry.WATER_HEMLOCK.get(), EMPTY
                        )
                )
                .TextMortarPageScreen("journal.toxony.page.oil_basics.4", ItemRegistry.GLOWING_OIL_POT.get(),
                        List.of(Items.HONEYCOMB, Items.GLOW_INK_SAC,
                                EMPTY, EMPTY
                        )
                )
                .TextMortarPageScreen("journal.toxony.page.oil_basics.5", ItemRegistry.FIRE_RESISTANCE_OIL_POT.get(),
                        List.of(Items.HONEYCOMB, ItemRegistry.BLOODROOT.get(),
                                EMPTY, EMPTY
                        )
                )
                .TextPageScreen("journal.toxony.page.oil_basics.6")

                // |----------------- Basic Tools ----------------- |
                .ImagePageScreen("journal.toxony.page.basic_tools.cover", "textures/gui/journal/journal_basic_tools_cover.png")
                .TextPageScreen("journal.toxony.page.basic_tools.0")
                .TextCraftingPageScreenItem("journal.toxony.page.basic_tools.1", ItemRegistry.TOX_GAUGE.get(),
                        List.of(EMPTY, ItemRegistry.POISON_PASTE.get(), EMPTY,
                                Items.COPPER_INGOT, Items.REDSTONE, Items.COPPER_INGOT,
                                EMPTY, ItemRegistry.POISON_PASTE.get(), EMPTY)
                )
                .TextImagePageScreen("journal.toxony.page.basic_tools.2", "textures/gui/journal/journal_gauge_image.png")
                .TextImagePageScreen("journal.toxony.page.basic_tools.3", "textures/gui/journal/journal_toxin_gauge_image.png")
                .TextImagePageScreen("journal.toxony.page.basic_tools.4", "textures/gui/journal/journal_tolerance_gauge_image.png")
                .TextImagePageScreen("journal.toxony.page.basic_tools.5", "textures/gui/journal/journal_tolerance_gauge_image.png")
                .TextImagePageScreen("journal.toxony.page.basic_tools.6", "textures/gui/journal/journal_death_gauge_image.png")
                .TextPageScreen("journal.toxony.page.basic_tools.7")
                .TextCraftingPageScreenItem("journal.toxony.page.basic_tools.8", ItemRegistry.COPPER_SCALPEL.get(),
                        List.of(EMPTY, Items.COPPER_INGOT, EMPTY,
                                EMPTY, Items.IRON_NUGGET, EMPTY,
                                EMPTY, Items.IRON_NUGGET, EMPTY)
                )
                .TextCraftingPageScreenItem("journal.toxony.page.basic_tools.9", ItemRegistry.MAGNIFYING_GLASS.get(),
                        List.of(Items.COPPER_INGOT, Items.GLASS_PANE, Items.COPPER_INGOT,
                                EMPTY, Items.STICK, EMPTY,
                                EMPTY, Items.STICK, EMPTY)
                )

                // |----------------- Refined Process ----------------- |
                .ImagePageScreen("journal.toxony.page.refined_process.cover", "textures/gui/journal/journal_refined_process_cover.png")
                .TextPageScreen("journal.toxony.page.refined_process.0")
                .TextCraftingPageScreenItem("journal.toxony.page.refined_process.1", ItemRegistry.COPPER_CRUCIBLE.get(),
                        List.of(EMPTY, Items.COPPER_INGOT, EMPTY,
                                Items.COPPER_INGOT, Items.COPPER_INGOT, Items.COPPER_INGOT,
                                Items.IRON_INGOT, Items.CHARCOAL, Items.IRON_INGOT)
                )
                .TextCruciblePageScreenIngredient("journal.toxony.page.refined_process.2", ItemRegistry.TOXIC_PASTE.get(),
                        Ingredient.of(ItemRegistry.POISON_PASTE.get())
                )
                .TextPageScreen("journal.toxony.page.refined_process.3")
                .TextMortarPageScreen("journal.toxony.page.refined_process.4", ItemRegistry.ACID_OIL_POT.get(),
                        List.of(Items.HONEYCOMB, ItemRegistry.TOXIC_PASTE.get(),
                                ItemRegistry.ACID_SLIMEBALL.get(), ItemRegistry.ACID_SLIMEBALL.get()
                        )
                )
                .TextMortarPageScreen("journal.toxony.page.refined_process.5", ItemRegistry.MENDING_OIL_POT.get(),
                        List.of(Items.HONEYCOMB, ItemRegistry.TOXIC_PASTE.get(),
                                ItemRegistry.TOXIC_SPIT.get(), ItemRegistry.OCELOT_MINT.get()
                        )
                )
                .TextMortarPageScreenIngredients("journal.toxony.page.refined_process.6", ItemRegistry.TOXIC_BLEND.get(),
                        List.of(recipe_toxic_blend.getIngredients().get(0), recipe_toxic_blend.getIngredients().get(1),
                                recipe_toxic_blend.getIngredients().get(2), Ingredient.EMPTY
                        )
                )
                .TextCraftingPageScreenItem("journal.toxony.page.refined_process.7", ItemRegistry.REDSTONE_MORTAR.get(),
                        List.of(Items.CHISELED_POLISHED_BLACKSTONE, ItemRegistry.MORTAR_PESTLE.get(), Items.CHISELED_POLISHED_BLACKSTONE,
                                Items.COPPER_INGOT, Items.IRON_INGOT, Items.COPPER_INGOT,
                                Items.COPPER_INGOT, Items.COMPARATOR, Items.COPPER_INGOT)
                )
                .TextPageScreen("journal.toxony.page.refined_process.8")
                // |----------------- Alchemical Warfare ----------------- |
                .ImagePageScreen("journal.toxony.page.alchemical_warfare.cover", "textures/gui/journal/journal_alchemical_warfare_cover.png")
                .TextCraftingPageScreenItem("journal.toxony.page.alchemical_warfare.0", ItemRegistry.OIL_POT_SASH.get(),
                        List.of(EMPTY, EMPTY, Items.LEATHER,
                                EMPTY, Items.IRON_INGOT, ItemRegistry.EMPTY_OIL_POT.get(),
                                Items.LEATHER, ItemRegistry.EMPTY_OIL_POT.get(), EMPTY)
                )
                .TextPageScreen("journal.toxony.page.alchemical_warfare.1")
                .TextCraftingPageScreenItem("journal.toxony.page.alchemical_warfare.2", ItemRegistry.CYCLEBOW.get(),
                        List.of(Items.COPPER_INGOT, ItemRegistry.POISON_PASTE.get(), Items.COPPER_INGOT,
                                Items.STRING, Items.TRIPWIRE_HOOK, Items.STRING,
                                EMPTY, Items.COPPER_INGOT, EMPTY)
                )
                .TextCraftingPageScreenItem("journal.toxony.page.alchemical_warfare.3", ItemRegistry.BOLT.get(),
                        List.of(EMPTY, Items.FLINT, EMPTY,
                                EMPTY, Items.IRON_NUGGET, EMPTY,
                                EMPTY, Items.IRON_NUGGET, EMPTY)
                )
                .TextPageScreen("journal.toxony.page.alchemical_warfare.4")
                .TextCraftingPageScreenItem("journal.toxony.page.alchemical_warfare.5", ItemRegistry.BOLT_CARTRIDGE.get(),
                        List.of(EMPTY, EMPTY, EMPTY,
                                EMPTY, Items.LEATHER, EMPTY,
                                Items.STRING, Items.COPPER_INGOT, Items.STRING)
                )
                .TextPageScreen("journal.toxony.page.alchemical_warfare.6")
                .TextPageScreen("journal.toxony.page.alchemical_warfare.7")
                .TextCraftingPageScreenItem("journal.toxony.page.alchemical_warfare.8", ItemRegistry.POTION_FLASK.get(),
                        List.of(EMPTY, ItemRegistry.TOXIC_PASTE.get(), EMPTY,
                                Items.LEATHER, Items.GLASS_BOTTLE, Items.LEATHER,
                                EMPTY, Items.LEATHER, EMPTY)
                )
                .TextPageScreen("journal.toxony.page.alchemical_warfare.9")
                .TextSingleItemTopScreen("journal.toxony.page.alchemical_warfare.10", createEnchantedItemStack(Items.ENCHANTED_BOOK, ToxonyEnchantments.REFILL))

                // |----------------- Pure Chemistry ----------------- |
                .ImagePageScreen("journal.toxony.page.pure_chemistry.cover", "textures/gui/journal/journal_pure_chemistry_cover.png")
                .TextPageScreen("journal.toxony.page.pure_chemistry.0")
                .TextCraftingPageScreenItem("journal.toxony.page.pure_chemistry.1", ItemRegistry.GLASS_VIAL.get(),
                        List.of(EMPTY, EMPTY, EMPTY,
                                Items.GLASS, EMPTY, Items.GLASS,
                                EMPTY, Items.QUARTZ, EMPTY)
                )
                .TextCraftingPageScreenItem("journal.toxony.page.pure_chemistry.2", ItemRegistry.TOXIN_CANISTER.get(),
                        List.of(Items.COPPER_INGOT, ItemRegistry.TOXIC_PASTE.get(), Items.COPPER_INGOT,
                                Items.COPPER_INGOT, ItemRegistry.GLASS_VIAL.get(), Items.COPPER_INGOT,
                                Items.COPPER_INGOT, ItemRegistry.TOXIC_PASTE.get(), Items.COPPER_INGOT)
                )
                .TextCraftingPageScreenItem("journal.toxony.page.pure_chemistry.3", ItemRegistry.ALEMBIC.get(),
                        List.of(Items.COPPER_INGOT, EMPTY, EMPTY,
                                Items.COPPER_INGOT, Items.IRON_INGOT, EMPTY,
                                ItemRegistry.ALEMBIC_BASE.get(), EMPTY, Items.COPPER_INGOT)
                )
                .TextCraftingPageScreenItem("journal.toxony.page.pure_chemistry.4", ItemRegistry.ALEMBIC_BASE.get(),
                        List.of(EMPTY, Items.IRON_INGOT, EMPTY,
                                Items.COPPER_INGOT, ItemRegistry.TOXIN_CANISTER.get(), Items.COPPER_INGOT,
                                Items.COPPER_INGOT, Items.COPPER_INGOT, Items.COPPER_INGOT)
                )
                .TextPageScreen("journal.toxony.page.pure_chemistry.5")
                .TextPageScreen("journal.toxony.page.pure_chemistry.6")
                .TextCraftingPageScreenStack("journal.toxony.page.pure_chemistry.7", ItemRegistry.TOXIC_FORMULA.get(),
                        List.of(Items.NETHER_WART.getDefaultInstance(), PotionContents.createItemStack(ItemRegistry.TOX_VIAL.get(), Potions.WATER), EMPTY.getDefaultInstance(),
                                new ItemStack(ItemRegistry.TOXIC_PASTE.get()), EMPTY.getDefaultInstance(), EMPTY.getDefaultInstance(),
                                EMPTY.getDefaultInstance(), EMPTY.getDefaultInstance(), EMPTY.getDefaultInstance())
                )
                .TextPageScreen("journal.toxony.page.pure_chemistry.8")
                .TextAlembicPageScreenIngredients("journal.toxony.page.pure_chemistry.9", ItemRegistry.TOXIN.get(),
                        List.of(recipe_toxin.getIngredients().get(0), recipe_toxin.getIngredients().get(1))
                )

                // |----------------- Mutagens ----------------- |
                .ImagePageScreen("journal.toxony.page.mutagens.cover", "textures/gui/journal/journal_mutagens_cover.png")
                .TextPageScreen("journal.toxony.page.mutagens.0")
                .TextPageScreen("journal.toxony.page.mutagens.1")
                .TextImagePageScreen("journal.toxony.page.mutagens.2", "textures/gui/journal/journal_full_gauge_image.png")
                .TextPageScreen("journal.toxony.page.mutagens.3")
                .TextPageScreen("journal.toxony.page.mutagens.4")
                .TextPageScreen("journal.toxony.page.mutagens.5")
                .TextCraftingPageScreenStack("journal.toxony.page.mutagens.6", ItemRegistry.REDSTONE_MIXTURE.get(),
                        List.of(PotionContents.createItemStack(ItemRegistry.TOX_VIAL.get(), Potions.WATER), Items.REDSTONE.getDefaultInstance(), EMPTY.getDefaultInstance(),
                                EMPTY.getDefaultInstance(), EMPTY.getDefaultInstance(), EMPTY.getDefaultInstance(),
                                EMPTY.getDefaultInstance(), EMPTY.getDefaultInstance(), EMPTY.getDefaultInstance())
                )
                .TextCruciblePageScreenIngredient("journal.toxony.page.mutagens.7", ItemRegistry.REDSTONE_SOLUTION.get(),
                        Ingredient.of(ItemRegistry.REDSTONE_MIXTURE.get())
                )
                .TextAlembicPageScreenIngredients("journal.toxony.page.mutagens.8", ItemRegistry.AFFINITY_SOLUTION.get(),
                        List.of(recipe_affinity_solution.getIngredients().get(0), recipe_affinity_solution.getIngredients().get(1))
                )
                .TextCraftingPageScreenItem("journal.toxony.page.mutagens.9", ItemRegistry.COPPER_NEEDLE.get(),
                        List.of(EMPTY, EMPTY, Items.IRON_NUGGET,
                                EMPTY, ItemRegistry.TOXIN_CANISTER.get(), EMPTY,
                                Items.COPPER_INGOT, EMPTY, EMPTY)
                )
                .TextPageScreen("journal.toxony.page.mutagens.10")
                .TextPageScreen("journal.toxony.page.mutagens.11")
                .TextPageScreen("journal.toxony.page.mutagens.12")

                // |----------------- Evolved Flora ----------------- |
                .ImagePageScreen("journal.toxony.page.evolved_flora.cover", "textures/gui/journal/journal_evolved_flora_cover.png")
                .TextPageScreen("journal.toxony.page.evolved_flora.0")
                .TextPageScreen("journal.toxony.page.evolved_flora.1")
                .TextPageScreen("journal.toxony.page.evolved_flora.2")
                .GraftingPageScreen("journal.toxony.page.evolved_flora.3",
                        Map.of(
                                new Pair<>(new ItemStack(ItemRegistry.OCELOT_MINT), new ItemStack(ItemRegistry.SNOW_MINT)), AffinityRegistry.DECAY.get(),
                                new Pair<>(new ItemStack(ItemRegistry.NIGHTSHADE), new ItemStack(ItemRegistry.SUNSPOT)), AffinityRegistry.HEAT.get(),
                                new Pair<>(new ItemStack(ItemRegistry.WATER_HEMLOCK), new ItemStack(ItemRegistry.MOONLIGHT_HEMLOCK)), AffinityRegistry.FOREST.get(),
                                new Pair<>(new ItemStack(ItemRegistry.COLDSNAP), new ItemStack(ItemRegistry.WHIRLSNAP)), AffinityRegistry.OCEAN.get(),
                                new Pair<>(new ItemStack(ItemRegistry.BLOODROOT), new ItemStack(ItemRegistry.WARPROOT)), AffinityRegistry.COLD.get()
                        )
                )

                // |----------------- Advanced Oils ----------------- |
                .ImagePageScreen("journal.toxony.page.advanced_oils.cover", "textures/gui/journal/journal_advanced_oils_cover.png")
                .TextPageScreen("journal.toxony.page.advanced_oils.0")
                .TextAlembicPageScreenIngredients("journal.toxony.page.advanced_oils.1", ItemRegistry.EMPTY_TOX_POT.get(),
                        List.of(Ingredient.of(ItemRegistry.TOXIN.get()), Ingredient.of(ItemRegistry.EMPTY_OIL_POT.get()))
                )
                .TextMortarPageScreenIngredients("journal.toxony.page.advanced_oils.2", ItemRegistry.TOXIN_TOX_POT.get(),
                        List.of(recipe_toxin_tox_pot.getIngredients().get(0), recipe_toxin_tox_pot.getIngredients().get(1),
                                recipe_toxin_tox_pot.getIngredients().get(2), Ingredient.EMPTY
                        )
                )
                .TextMortarPageScreenIngredients("journal.toxony.page.advanced_oils.3", ItemRegistry.SMOKE_TOX_POT.get(),
                        List.of(Ingredient.of(Items.HONEYCOMB), Ingredient.of(Items.FERMENTED_SPIDER_EYE),
                                Ingredient.of(ItemRegistry.WATER_HEMLOCK.get()), Ingredient.EMPTY
                        )
                )
                .TextMortarPageScreenIngredients("journal.toxony.page.advanced_oils.4", ItemRegistry.REGENERATION_TOX_POT.get(),
                        List.of(Ingredient.of(Items.HONEYCOMB), Ingredient.of(Items.GHAST_TEAR),
                                Ingredient.of(ItemRegistry.SUNSPOT.get()), Ingredient.EMPTY
                        )
                )
                .TextMortarPageScreen("journal.toxony.page.advanced_oils.5", ItemRegistry.ACID_TOX_POT.get(),
                        List.of(Items.HONEYCOMB, ItemRegistry.WARPROOT.get(),
                                ItemRegistry.BOG_BONE.get(), ItemRegistry.ACID_SLIMEBALL.get()
                        )
                )
                .TextMortarPageScreenIngredients("journal.toxony.page.advanced_oils.6", ItemRegistry.WITCHFIRE_TOX_POT.get(),
                        List.of(Ingredient.of(Items.HONEYCOMB), Ingredient.of(ItemRegistry.WARPROOT.get()),
                                Ingredient.of(Items.BLAZE_POWDER), Ingredient.EMPTY
                        )
                )
                .TextMortarPageScreenIngredients("journal.toxony.page.advanced_oils.7", ItemRegistry.OIL_BASE.get(),
                        List.of(Ingredient.of(Items.HONEYCOMB), Ingredient.of(ItemRegistry.TOXIC_PASTE.get()),
                                Ingredient.EMPTY, Ingredient.EMPTY
                        )
                )

                // |----------------- Evolving Warfare ----------------- |
                .ImagePageScreen("journal.toxony.page.evolving_warfare.cover", "textures/gui/journal/journal_evolving_warfare_cover.png")
                .TextPageScreen("journal.toxony.page.evolving_warfare.0")
                .TextAlembicPageScreenIngredients("journal.toxony.page.evolving_warfare.1", ItemRegistry.TOXIC_LEATHER.get(),
                        List.of(Ingredient.of(ItemRegistry.TOXIN.get()), Ingredient.of(Items.LEATHER))
                )
                .TextCraftingPageScreenItem("journal.toxony.page.evolving_warfare.2", ItemRegistry.PLAGUE_DOCTOR_COAT.get(),
                        List.of(Items.CHAIN, EMPTY, Items.CHAIN,
                                ItemRegistry.TOXIC_LEATHER.get(), ItemRegistry.TOXIC_LEATHER.get(), ItemRegistry.TOXIC_LEATHER.get(),
                                ItemRegistry.GLASS_VIAL.get(), ItemRegistry.TOXIC_LEATHER.get(), ItemRegistry.GLASS_VIAL.get())
                )
                .TextCraftingPageScreenItem("journal.toxony.page.evolving_warfare.3", ItemRegistry.PLAGUE_DOCTOR_LEGGINGS.get(),
                        List.of(Items.CHAIN, Items.LEATHER, Items.CHAIN,
                                ItemRegistry.TOXIC_LEATHER.get(), EMPTY, ItemRegistry.TOXIC_LEATHER.get(),
                                ItemRegistry.TOXIC_LEATHER.get(), EMPTY, ItemRegistry.TOXIC_LEATHER.get())
                )
                .TextCraftingPageScreenItem("journal.toxony.page.evolving_warfare.4", ItemRegistry.PLAGUE_DOCTOR_BOOTS.get(),
                        List.of(EMPTY, EMPTY, EMPTY,
                                ItemRegistry.TOXIC_LEATHER.get(), EMPTY, ItemRegistry.TOXIC_LEATHER.get(),
                                Items.LEATHER, EMPTY, Items.LEATHER)
                )
                .TextCraftingPageScreenItem("journal.toxony.page.evolving_warfare.5", ItemRegistry.PLAGUE_DOCTOR_HOOD.get(),
                        List.of(EMPTY, ItemRegistry.TOXIC_LEATHER.get(), EMPTY,
                                ItemRegistry.TOXIC_LEATHER.get(), EMPTY, ItemRegistry.TOXIC_LEATHER.get(),
                                ItemRegistry.TOXIC_LEATHER.get(), Items.CHAIN, ItemRegistry.TOXIC_LEATHER.get())
                )
                .TextPageScreen("journal.toxony.page.evolving_warfare.6")
                .TextPageScreen("journal.toxony.page.evolving_warfare.7")
                .TextCraftingPageScreenItem("journal.toxony.page.evolving_warfare.8", ItemRegistry.OIL_POT_BANDOLIER.get(),
                        List.of(EMPTY, EMPTY, ItemRegistry.TOXIC_LEATHER.get(),
                                EMPTY, Items.NETHERITE_INGOT, ItemRegistry.EMPTY_TOX_POT.get(),
                                ItemRegistry.TOXIC_LEATHER.get(), ItemRegistry.EMPTY_TOX_POT.get(), EMPTY)
                )
                .TextCraftingPageScreenItem("journal.toxony.page.evolving_warfare.9", ItemRegistry.TOXIN_FLASK.get(),
                        List.of(Items.QUARTZ, Items.NETHERITE_INGOT, Items.QUARTZ,
                                ItemRegistry.TOXIC_LEATHER.get(), Items.GLASS_BOTTLE, ItemRegistry.TOXIC_LEATHER.get(),
                                EMPTY, ItemRegistry.TOXIC_LEATHER.get(), EMPTY)
                )

                // |----------------- Lost Chemistry ----------------- |
                .ImagePageScreen("journal.toxony.page.lost_chemistry.cover", "textures/gui/journal/journal_lost_chemistry_cover.png")
                .TextPageScreen("journal.toxony.page.lost_chemistry.0")
                .TextCraftingPageScreenItem("journal.toxony.page.lost_chemistry.1", ItemRegistry.ALCHEMICAL_FORGE_PART.get(),
                        List.of(ItemRegistry.TOXIN.get(), Items.NETHERITE_INGOT, ItemRegistry.TOXIN.get(),
                                ItemRegistry.TOXIN_CANISTER.get(), Items.COPPER_BLOCK, ItemRegistry.TOXIN_CANISTER.get(),
                                Items.CHISELED_POLISHED_BLACKSTONE, Items.CHISELED_POLISHED_BLACKSTONE, Items.CHISELED_POLISHED_BLACKSTONE)
                )
                .TextPageScreen("journal.toxony.page.lost_chemistry.2");

    }

    private static RecipeHolder<?> getRecipe(RecipeManager manager, String location){
        Optional<RecipeHolder<?>> optional = Optional.empty();
        if(level != null){
            optional = manager.byKey(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, location));
        }
        return optional.orElseThrow();
    }

    private static ItemStack createEnchantedItemStack(Item itemStack, ResourceKey<Enchantment> key){
        if(Minecraft.getInstance().getConnection() == null) return ItemStack.EMPTY;
        Holder.Reference<Enchantment> holder = Minecraft.getInstance().getConnection()
                .registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(key);

        ItemStack stack = new ItemStack(itemStack);
        ItemEnchantments.Mutable enchantmentMutable = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        enchantmentMutable.set(holder, 1);

        EnchantmentHelper.setEnchantments(stack, enchantmentMutable.toImmutable());
        return stack;
    }

}
