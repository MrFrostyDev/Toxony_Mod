package xyz.yfrostyf.toxony.data.datagen.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import xyz.yfrostyf.toxony.registries.ItemRegistry;
import xyz.yfrostyf.toxony.registries.TagRegistry;

import java.util.concurrent.CompletableFuture;

public class    ToxonyCraftingTableRecipes extends RecipeProvider {

    public ToxonyCraftingTableRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    public static void get(RecipeOutput output){
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.CLAY_OIL_POT.get())
                // Create the lines of your pattern. Each call to #pattern adds a new line.
                // Patterns will be validated, i.e. their shape will be checked.
                .pattern("   ")
                .pattern("#X#")
                .pattern(" # ")
                // Create the keys for the pattern. All non-space characters used in the pattern must be defined.
                // This can either accept Ingredients, TagKey<Item>s or ItemLikes, i.e. items or blocks.
                .define('X', ItemRegistry.POISON_PASTE.get())
                .define('#', Items.CLAY_BALL)
                // Creates the recipe advancement. While not mandated by the consuming background systems,
                // the recipe builder will crash if you omit this. The first parameter is the advancement name,
                // and the second one is the condition. Normally, you want to use the has() shortcut for the condition.
                // Multiple advancement requirements can be added by calling #unlockedBy multiple times.
                .unlockedBy("has_poison_paste", has(ItemRegistry.POISON_PASTE.get()))
                // Stores the recipe in the passed RecipeOutput, to be written to disk.
                // If you want to add conditions to the recipe, those can be set on the output.
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemRegistry.LOST_JOURNAL.get())
                .requires(TagRegistry.POISONOUS_INGREDIENTS_ITEM)
                .requires(Items.BOOK)
                .unlockedBy("has_book", has(Items.BOOK))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemRegistry.LOST_JOURNAL.get())
                .requires(TagRegistry.POISONOUS_PLANTS_ITEM)
                .requires(Items.BOOK)
                .unlockedBy("has_book", has(Items.BOOK))
                .save(output, "lost_journal_alt");


        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.POISONOUS_POTATO)
                .requires(ItemRegistry.POISON_PASTE.get())
                .requires(Items.POTATO)
                .unlockedBy("has_poison_paste", has(ItemRegistry.POISON_PASTE.get()))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.ROTTEN_FLESH)
                .requires(ItemRegistry.POISON_PASTE.get())
                .requires(ItemTags.MEAT)
                .unlockedBy("has_poison_paste", has(ItemRegistry.POISON_PASTE.get()))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, PotionContents.createItemStack(Items.POTION, Potions.POISON))
                .requires(ItemRegistry.POISON_PASTE.get())
                .requires(DataComponentIngredient.of(false, PotionContents.createItemStack(Items.POTION, Potions.WATER)))
                .unlockedBy("has_poison_paste", has(ItemRegistry.POISON_PASTE.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, new ItemStack(ItemRegistry.BOLT.get(), 9))
                .pattern(" F ")
                .pattern(" I ")
                .pattern(" I ")
                .define('F', Items.FLINT)
                .define('I', Items.IRON_NUGGET)
                .unlockedBy("has_flint", has(Items.FLINT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.TOXIN_CANISTER.get())
                .pattern("CPC")
                .pattern("CVC")
                .pattern("CPC")
                .define('P', ItemRegistry.TOXIC_PASTE.get())
                .define('V', ItemRegistry.GLASS_VIAL.get())
                .define('C', Items.COPPER_INGOT)
                .unlockedBy("has_quartz", has(Items.QUARTZ))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, new ItemStack(ItemRegistry.GLASS_VIAL.get(), 3))
                .pattern("   ")
                .pattern("G G")
                .pattern(" Q ")
                .define('G', Items.GLASS)
                .define('Q', Items.QUARTZ)
                .unlockedBy("has_quartz", has(Items.QUARTZ))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.COPPER_CRUCIBLE.get())
                .pattern(" C ")
                .pattern("CCC")
                .pattern("IFI")
                .define('C', Items.COPPER_INGOT)
                .define('F', Items.CHARCOAL)
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.ALEMBIC.get())
                .pattern("C  ")
                .pattern("CI ")
                .pattern("B T")
                .define('B', ItemRegistry.ALEMBIC_BASE.get())
                .define('C', Items.COPPER_INGOT)
                .define('I', Items.IRON_INGOT)
                .define('T', ItemRegistry.TOXIN_CANISTER.get())
                .unlockedBy("has_toxin_canister", has(ItemRegistry.TOXIN_CANISTER.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.ALEMBIC_BASE.get())
                .pattern(" I ")
                .pattern("CTC")
                .pattern("CCC")
                .define('I', Items.IRON_INGOT)
                .define('C', Items.COPPER_INGOT)
                .define('T', ItemRegistry.TOXIN_CANISTER.get())
                .unlockedBy("has_toxin_canister", has(ItemRegistry.TOXIN_CANISTER.get()))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemRegistry.REDSTONE_MIXTURE.get())
                .requires(DataComponentIngredient.of(false, PotionContents.createItemStack(ItemRegistry.TOX_VIAL.get(), Potions.WATER)))
                .requires(Items.REDSTONE)
                .unlockedBy("has_glass_vial", has(ItemRegistry.GLASS_VIAL.get()))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemRegistry.TOXIC_FORMULA.get())
                .requires(ItemRegistry.TOXIC_PASTE.get())
                .requires(DataComponentIngredient.of(false, PotionContents.createItemStack(ItemRegistry.TOX_VIAL.get(), Potions.WATER)))
                .requires(Items.NETHER_WART)
                .unlockedBy("has_toxic_paste", has(ItemRegistry.TOXIC_PASTE.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistry.ALCHEMICAL_FORGE_PART.get())
                .pattern("TNT")
                .pattern("RCR")
                .pattern("BBB")
                .define('N', Items.NETHERITE_INGOT)
                .define('T', ItemRegistry.TOXIN.get())
                .define('R', ItemRegistry.TOXIN_CANISTER.get())
                .define('C', Items.COPPER_BLOCK)
                .define('B', Items.CHISELED_POLISHED_BLACKSTONE)
                .unlockedBy("has_poison_paste", has(ItemRegistry.POISON_PASTE.get()))
                .save(output);

        // Armor
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.PLAGUE_DOCTOR_COAT.get())
                .pattern("C C")
                .pattern("TTT")
                .pattern("VTV")
                .define('C', Items.CHAIN)
                .define('T', ItemRegistry.TOXIC_LEATHER.get())
                .define('V', ItemRegistry.GLASS_VIAL.get())
                .unlockedBy("has_toxic_leather", has(ItemRegistry.TOXIC_LEATHER.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.PLAGUE_DOCTOR_LEGGINGS.get())
                .pattern("CLC")
                .pattern("T T")
                .pattern("T T")
                .define('C', Items.CHAIN)
                .define('L', Items.LEATHER)
                .define('T', ItemRegistry.TOXIC_LEATHER.get())
                .unlockedBy("has_toxic_leather", has(ItemRegistry.TOXIC_LEATHER.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.PLAGUE_DOCTOR_HOOD.get())
                .pattern(" T ")
                .pattern("T T")
                .pattern("TCT")
                .define('C', Items.CHAIN)
                .define('T', ItemRegistry.TOXIC_LEATHER.get())
                .unlockedBy("has_toxic_leather", has(ItemRegistry.TOXIC_LEATHER.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.PLAGUE_DOCTOR_BOOTS.get())
                .pattern("   ")
                .pattern("T T")
                .pattern("L L")
                .define('L', Items.LEATHER)
                .define('T', ItemRegistry.TOXIC_LEATHER.get())
                .unlockedBy("has_toxic_leather", has(ItemRegistry.TOXIC_LEATHER.get()))
                .save(output);


        // Tools
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ItemRegistry.TOX_GAUGE.get())
                .pattern(" P ")
                .pattern("CRC")
                .pattern(" P ")
                .define('P', ItemRegistry.POISON_PASTE.get())
                .define('C', Items.COPPER_INGOT)
                .define('R', Items.REDSTONE)
                .unlockedBy("has_poison_paste", has(ItemRegistry.POISON_PASTE.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ItemRegistry.MAGNIFYING_GLASS.get())
                .pattern("CGC")
                .pattern(" S ")
                .pattern(" S ")
                .define('G', Items.GLASS_PANE)
                .define('C', Items.COPPER_INGOT)
                .define('S', Items.STICK)
                .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ItemRegistry.COPPER_SCALPEL.get())
                .pattern(" C ")
                .pattern(" I ")
                .pattern(" I ")
                .define('I', Items.IRON_NUGGET)
                .define('C', Items.COPPER_INGOT)
                .unlockedBy("has_iron_nugget", has(Items.IRON_NUGGET))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ItemRegistry.COPPER_NEEDLE.get())
                .pattern("  I")
                .pattern(" T ")
                .pattern("C  ")
                .define('I', Items.IRON_NUGGET)
                .define('T', ItemRegistry.TOXIN_CANISTER.get())
                .define('C', Items.COPPER_INGOT)
                .unlockedBy("has_toxin_canister", has(ItemRegistry.TOXIN_CANISTER.get()))
                .save(output);

        // Weapons
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.CYCLEBOW.get())
                .pattern("CPC")
                .pattern("STS")
                .pattern(" C ")
                .define('P', ItemRegistry.TOXIC_PASTE.get())
                .define('C', Items.COPPER_INGOT)
                .define('T', Items.TRIPWIRE_HOOK)
                .define('S', Items.STRING)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.OIL_POT_SASH.get())
                .pattern("  L")
                .pattern(" IP")
                .pattern("LP ")
                .define('P', ItemRegistry.EMPTY_OIL_POT.get())
                .define('I', Items.IRON_INGOT)
                .define('L', Items.LEATHER)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemRegistry.OIL_POT_BANDOLIER.get())
                .pattern("  L")
                .pattern(" IP")
                .pattern("LP ")
                .define('P', ItemRegistry.EMPTY_TOX_POT.get())
                .define('I', Items.NETHERITE_INGOT)
                .define('L', ItemRegistry.TOXIC_LEATHER.get())
                .unlockedBy("has_netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(output);

        // Tox Seeds
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemRegistry.OCELOT_MINT_SEEDS.get())
                .requires(ItemRegistry.OCELOT_MINT.get())
                .unlockedBy("has_ocelot_mint", has(ItemRegistry.OCELOT_MINT.get()))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemRegistry.NIGHTSHADE_SEEDS.get())
                .requires(ItemRegistry.NIGHTSHADE.get())
                .unlockedBy("has_nightshade", has(ItemRegistry.NIGHTSHADE.get()))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemRegistry.WATER_HEMLOCK_SEEDS.get())
                .requires(ItemRegistry.WATER_HEMLOCK.get())
                .unlockedBy("has_water_hemlock", has(ItemRegistry.WATER_HEMLOCK.get()))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemRegistry.COLDSNAP_SEEDS.get())
                .requires(ItemRegistry.COLDSNAP.get())
                .unlockedBy("has_coldsnap", has(ItemRegistry.COLDSNAP.get()))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemRegistry.BLOODROOT_FUNGUS.get())
                .requires(ItemRegistry.BLOODROOT.get())
                .unlockedBy("has_bloodroot", has(ItemRegistry.BLOODROOT.get()))
                .save(output);
    }
}
