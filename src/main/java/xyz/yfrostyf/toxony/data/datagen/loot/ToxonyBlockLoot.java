package xyz.yfrostyf.toxony.data.datagen.loot;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import xyz.yfrostyf.toxony.api.blocks.PoisonCropBlock;
import xyz.yfrostyf.toxony.api.blocks.WildPoisonCropBlock;
import xyz.yfrostyf.toxony.registries.BlockRegistry;
import xyz.yfrostyf.toxony.registries.ItemRegistry;

import java.util.HashSet;
import java.util.Set;


//
// Thank you Farmer's Delight's Git Repository for code reference!
//
public class ToxonyBlockLoot extends BlockLootSubProvider {

    private final Set<Block> lootTables = new HashSet<>();

    public ToxonyBlockLoot(HolderLookup.Provider provider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    public void generate() {
        dropOther(BlockRegistry.LOST_JOURNAL.get(), ItemRegistry.LOST_JOURNAL.get());

        dropSelf(BlockRegistry.MORTAR_PESTLE.get());
        dropSelf(BlockRegistry.COPPER_CRUCIBLE.get());
        dropSelf(BlockRegistry.ALEMBIC.get());
        dropSelf(BlockRegistry.ALCHEMICAL_FORGE_PART.get());
        dropOther(BlockRegistry.ALCHEMICAL_FORGE.get(), BlockRegistry.ALCHEMICAL_FORGE_PART.get());

        dropSelf(BlockRegistry.VIAL_RACK.get());
        dropSelf(BlockRegistry.BELL_JAR.get());
        dropSelf(BlockRegistry.COPPER_SCALE.get());
        dropSelf(BlockRegistry.LOOSE_PAPER.get());

        dropWildPoisonPlantDrops(BlockRegistry.WILD_OCELOT_MINT.get(), ItemRegistry.OCELOT_MINT.get());
        dropWildPoisonPlantDrops(BlockRegistry.WILD_NIGHTSHADE.get(), ItemRegistry.NIGHTSHADE.get());
        dropWildPoisonPlantDrops(BlockRegistry.WILD_WATER_HEMLOCK.get(), ItemRegistry.WATER_HEMLOCK.get());
        dropWildPoisonPlantDrops(BlockRegistry.WILD_COLDSNAP.get(), ItemRegistry.COLDSNAP.get());
        dropWildPoisonPlantDrops(BlockRegistry.WILD_BLOODROOT.get(), ItemRegistry.BLOODROOT.get());
        dropPoisonPlantDrops(BlockRegistry.OCELOT_MINT.get(), ItemRegistry.OCELOT_MINT.get());
        dropPoisonPlantDrops(BlockRegistry.SNOW_MINT.get(), ItemRegistry.SNOW_MINT.get());
        dropPoisonPlantDrops(BlockRegistry.NIGHTSHADE.get(), ItemRegistry.NIGHTSHADE.get());
        dropPoisonPlantDrops(BlockRegistry.SUNSPOT.get(), ItemRegistry.SUNSPOT.get());
        dropPoisonPlantDrops(BlockRegistry.WATER_HEMLOCK.get(), ItemRegistry.WATER_HEMLOCK.get());
        dropPoisonPlantDrops(BlockRegistry.MOONLIGHT_HEMLOCK.get(), ItemRegistry.MOONLIGHT_HEMLOCK.get());
        dropPoisonPlantDrops(BlockRegistry.COLDSNAP.get(), ItemRegistry.COLDSNAP.get());
        dropPoisonPlantDrops(BlockRegistry.WHIRLSNAP.get(), ItemRegistry.WHIRLSNAP.get());
        dropPoisonPlantDrops(BlockRegistry.BLOODROOT.get(), ItemRegistry.BLOODROOT.get());
        dropPoisonPlantDrops(BlockRegistry.WARPROOT.get(), ItemRegistry.WARPROOT.get());
    }

    @Override
    protected void add(Block block, LootTable.Builder builder) {
        this.lootTables.add(block);
        this.map.put(block.getLootTable(), builder);
    }
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return lootTables;
    }

    protected void dropWildPoisonPlantDrops(Block cropBlock, Item grownCropItem){
        HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        this.add(cropBlock, this.applyExplosionDecay(
                cropBlock,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(cropBlock)
                                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(WildPoisonCropBlock.AGE, WildPoisonCropBlock.MAX_AGE)))
                                .add(LootItem.lootTableItem(grownCropItem))
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
                                .apply(ApplyBonusCount.addUniformBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE)))
                        )
            )
        );
    }

    protected void dropPoisonPlantDrops(Block cropBlock, Item grownCropItem){
        HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        this.add(cropBlock, this.applyExplosionDecay(
                cropBlock,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(cropBlock)
                                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(PoisonCropBlock.AGE, PoisonCropBlock.MAX_AGE)))
                                .add(LootItem.lootTableItem(grownCropItem))
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                                .apply(ApplyBonusCount.addUniformBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE)))
                        )
                )
        );
    }

}
