package xyz.yfrostyf.toxony.data.loot;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
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
    protected void generate() {
        dropSelf(BlockRegistry.MORTAR_PESTLE.get());
        dropSelf(BlockRegistry.COPPER_CRUCIBLE.get());
        dropSelf(BlockRegistry.ALEMBIC.get());

        dropWildPoisonPlantDrops(BlockRegistry.WILD_OCELOT_MINT.get(), ItemRegistry.OCELOT_MINT.get());
        dropPoisonPlantDrops(BlockRegistry.OCELOT_MINT.get(), ItemRegistry.OCELOT_MINT.get());
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
                        .withPool(
                                LootPool.lootPool()
                                        .when(
                                                LootItemBlockStatePropertyCondition.hasBlockStateProperties(cropBlock)
                                                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(WildPoisonCropBlock.AGE, WildPoisonCropBlock.MAX_AGE))
                                        )
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
                                .withPool(
                                        LootPool.lootPool()
                                                .when(
                                                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(cropBlock)
                                                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(PoisonCropBlock.AGE, PoisonCropBlock.MAX_AGE))
                                                )
                                                .add(LootItem.lootTableItem(grownCropItem))
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
                                                .apply(ApplyBonusCount.addUniformBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE)))
                                )
                )
        );
    }

}
