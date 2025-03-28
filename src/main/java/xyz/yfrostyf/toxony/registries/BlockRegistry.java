package xyz.yfrostyf.toxony.registries;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.blocks.PoisonCropBlock;
import xyz.yfrostyf.toxony.api.blocks.WildPoisonCropBlock;
import xyz.yfrostyf.toxony.blocks.*;
import xyz.yfrostyf.toxony.blocks.entities.*;
import xyz.yfrostyf.toxony.blocks.AlchemicalForgeBlock;
import xyz.yfrostyf.toxony.blocks.plants.FalseBerryBushBlock;
import xyz.yfrostyf.toxony.blocks.PoisonFarmBlock;
import xyz.yfrostyf.toxony.blocks.plants.LostJournalBlock;
import xyz.yfrostyf.toxony.blocks.plants.WildOcelotMintBlock;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class BlockRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ToxonyMain.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, ToxonyMain.MOD_ID);

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
        BLOCKS.register(eventBus);
    }

    // |--------------------------------------------------------------------------------------|
    // |------------------------------------Block Plants--------------------------------------|
    // |--------------------------------------------------------------------------------------|
    public static final DeferredHolder<Block, Block> POISON_FARMLAND = BLOCKS.register(
            "poison_farmland",
            () -> new PoisonFarmBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DIRT)
                    .randomTicks()
                    .strength(0.6F)
                    .sound(SoundType.GRAVEL)
                    .isViewBlocking((state, get, pos) -> true)
                    .isSuffocating((state, get, pos) -> true)
            )
    );

    public static final DeferredHolder<Block, Block> FALSE_BERRY_BUSH = BLOCKS.register(
            "false_berry_bush",
            () -> new FalseBerryBushBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .randomTicks()
                    .noCollission()
                    .sound(SoundType.SWEET_BERRY_BUSH)
                    .pushReaction(PushReaction.DESTROY)
                    .isRedstoneConductor((state,level,pos) -> false)
            )
    );

    public static final DeferredHolder<Block, Block> WILD_OCELOT_MINT = BLOCKS.register(
            "wild_ocelot_mint",
            () -> new WildOcelotMintBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .randomTicks()
                    .noCollission()
                    .sound(SoundType.CROP)
                    .pushReaction(PushReaction.DESTROY)
                    .isRedstoneConductor((state,level,pos) -> false),
                    List.of(MobEffects.POISON)
            )
    );

    public static final DeferredHolder<Block, Block> OCELOT_MINT = createPoisonCrop(
            "ocelot_mint",
            () -> ItemRegistry.OCELOT_MINT, List.of(MobEffects.POISON), () -> BlockRegistry.SNOW_MINT);

    public static final DeferredHolder<Block, Block> SNOW_MINT = createPoisonCrop(
            "snow_mint",
            () -> ItemRegistry.SNOW_MINT, List.of(MobEffects.POISON), null);

    public static final DeferredHolder<Block, Block> WILD_NIGHTSHADE = BLOCKS.register(
            "wild_nightshade",
            () -> new WildPoisonCropBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .randomTicks()
                    .noCollission()
                    .sound(SoundType.CROP)
                    .pushReaction(PushReaction.DESTROY)
                    .isRedstoneConductor((state,level,pos) -> false),
                    List.of(MobEffects.POISON)
            )
    );

    public static final DeferredHolder<Block, Block> NIGHTSHADE = createPoisonCrop(
            "nightshade",
            () -> ItemRegistry.NIGHTSHADE, List.of(MobEffects.POISON), () -> BlockRegistry.SUNSPOT);

    public static final DeferredHolder<Block, Block> SUNSPOT = createPoisonCrop(
            "sunspot",
            () -> ItemRegistry.SUNSPOT, List.of(MobEffects.POISON), null);

    public static final DeferredHolder<Block, Block> WILD_WATER_HEMLOCK = BLOCKS.register(
            "wild_water_hemlock",
            () -> new WildPoisonCropBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .randomTicks()
                    .noCollission()
                    .sound(SoundType.CROP)
                    .pushReaction(PushReaction.DESTROY)
                    .isRedstoneConductor((state,level,pos) -> false),
                    List.of(MobEffects.POISON)
            )
    );

    public static final DeferredHolder<Block, Block> WATER_HEMLOCK = createPoisonCrop(
            "water_hemlock",
            () -> ItemRegistry.WATER_HEMLOCK, List.of(MobEffects.POISON), () -> BlockRegistry.MOONLIGHT_HEMLOCK);

    public static final DeferredHolder<Block, Block> MOONLIGHT_HEMLOCK = createPoisonCrop(
            "moonlight_hemlock",
            () -> ItemRegistry.MOONLIGHT_HEMLOCK, List.of(MobEffects.POISON), null);

    public static final DeferredHolder<Block, Block> WILD_COLDSNAP = BLOCKS.register(
            "wild_coldsnap",
            () -> new WildPoisonCropBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .randomTicks()
                    .noCollission()
                    .sound(SoundType.CROP)
                    .pushReaction(PushReaction.DESTROY)
                    .isRedstoneConductor((state,level,pos) -> false),
                    List.of(MobEffects.POISON)
            )
    );

    public static final DeferredHolder<Block, Block> COLDSNAP = createPoisonCrop(
            "coldsnap",
            () -> ItemRegistry.COLDSNAP, List.of(MobEffects.POISON), () -> BlockRegistry.WHIRLSNAP);

    public static final DeferredHolder<Block, Block> WHIRLSNAP = createPoisonCrop(
            "whirlsnap",
            () -> ItemRegistry.WHIRLSNAP, List.of(MobEffects.POISON), null);

    public static final DeferredHolder<Block, Block> WILD_BLOODROOT = BLOCKS.register(
            "wild_bloodroot",
            () -> new WildPoisonCropBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.CRIMSON_STEM)
                    .randomTicks()
                    .noCollission()
                    .sound(SoundType.CROP)
                    .pushReaction(PushReaction.DESTROY)
                    .isRedstoneConductor((state,level,pos) -> false),
                    List.of(MobEffects.POISON)
            )
    );

    public static final DeferredHolder<Block, Block> BLOODROOT = createPoisonCrop(
            "bloodroot",
            () -> ItemRegistry.BLOODROOT, List.of(MobEffects.POISON), () -> BlockRegistry.WARPROOT);

    public static final DeferredHolder<Block, Block> WARPROOT = createPoisonCrop(
            "warproot",
            () -> ItemRegistry.WARPROOT, List.of(MobEffects.POISON), null);

    // |--------------------------------------------------------------------------------------|
    // |------------------------------------Block Entities------------------------------------|
    // |--------------------------------------------------------------------------------------|

    public static final DeferredHolder<Block, Block> MORTAR_PESTLE = BLOCKS.register(
            "mortar_pestle",
            () -> new MortarPestleBlock(BlockBehaviour.Properties.of()
                    .strength(0.5f)
                    .sound(SoundType.STONE)
                    .isRedstoneConductor((state,level,pos) -> false)
            )
    );

    public static final DeferredHolder<Block, Block> COPPER_CRUCIBLE = BLOCKS.register(
            "copper_crucible",
            () -> new CopperCrucibleBlock(BlockBehaviour.Properties.of()
                    .strength(0.5f)
                    .sound(SoundType.COPPER)
                    .isRedstoneConductor((state,level,pos) -> false)
            )
    );

    public static final DeferredHolder<Block, Block> ALEMBIC = BLOCKS.register(
            "alembic",
            () -> new AlembicBlock(BlockBehaviour.Properties.of()
                    .strength(0.8F)
                    .sound(SoundType.COPPER)
                    .isRedstoneConductor((state,level,pos) -> false)
            )
    );

    public static final DeferredHolder<Block, Block> ALCHEMICAL_FORGE = BLOCKS.register(
            "alchemical_forge",
            () -> new AlchemicalForgeBlock(BlockBehaviour.Properties.of()
                    .strength(20.0F, 1200.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.NETHERITE_BLOCK)
                    .isRedstoneConductor((state,level,pos) -> false)
            )
    );

    public static final DeferredHolder<Block, Block> ALCHEMICAL_FORGE_PART = BLOCKS.register(
            "alchemical_forge_part",
            () -> new AlchemicalForgePartBlock(BlockBehaviour.Properties.of()
                    .strength(10.0F, 1200.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.NETHERITE_BLOCK)
                    .isRedstoneConductor((state,level,pos) -> false)
            )
    );

    public static final Supplier<BlockEntityType<MortarPestleBlockEntity>> MORTAR_PESTLE_ENTITY = BLOCK_ENTITY_TYPES.register(
            "mortar_pestle_entity",
            () -> BlockEntityType.Builder.of(
                    // The supplier to use for constructing the block entity instances.
                    MortarPestleBlockEntity::new,
                    // A vararg of blocks that can have this block entity.
                    // This assumes the existence of the referenced blocks as DeferredBlock<Block>s.
                    MORTAR_PESTLE.get()
            ).build(null)
    );

    public static final Supplier<BlockEntityType<CopperCrucibleBlockEntity>> COPPER_CRUCIBLE_ENTITY = BLOCK_ENTITY_TYPES.register(
            "copper_crucible_entity",
            () -> BlockEntityType.Builder.of(
                    CopperCrucibleBlockEntity::new,
                    COPPER_CRUCIBLE.get()
            ).build(null)
    );

    public static final Supplier<BlockEntityType<AlembicBlockEntity>> ALEMBIC_ENTITY = BLOCK_ENTITY_TYPES.register(
            "alembic_entity",
            () -> BlockEntityType.Builder.of(
                    AlembicBlockEntity::new,
                    ALEMBIC.get()
            ).build(null)
    );

    public static final Supplier<BlockEntityType<AlchemicalForgeBlockEntity>> ALCHEMICAL_FORGE_ENTITY = BLOCK_ENTITY_TYPES.register(
            "alchemical_forge_entity",
            () -> BlockEntityType.Builder.of(
                    AlchemicalForgeBlockEntity::new,
                    ALCHEMICAL_FORGE.get()
            ).build(null)
    );

    // |-----------------------------------------------------------------------------------|
    // |-------------------------------------Oil Pot---------------------------------------|
    // |-----------------------------------------------------------------------------------|

    public static final DeferredHolder<Block, Block> POISON_OIL_POT = createOilPotBlock("poison_oil_pot", () -> ItemRegistry.POISON_OIL_POT);
    public static final DeferredHolder<Block, Block> GLOWING_OIL_POT = createOilPotBlock("glowing_oil_pot", () -> ItemRegistry.GLOWING_OIL_POT);
    public static final DeferredHolder<Block, Block> FIRE_RESISTANCE_OIL_POT = createOilPotBlock("fire_resistance_oil_pot", () -> ItemRegistry.FIRE_RESISTANCE_OIL_POT);
    public static final DeferredHolder<Block, Block> FATIGUE_OIL_POT = createOilPotBlock("fatigue_oil_pot", () -> ItemRegistry.FATIGUE_OIL_POT);
    public static final DeferredHolder<Block, Block> TOXIN_TOX_POT = createOilPotBlock("toxin_tox_pot", () -> ItemRegistry.TOXIN_TOX_POT);
    public static final DeferredHolder<Block, Block> REGENERATION_TOX_POT = createOilPotBlock("regeneration_tox_pot", () -> ItemRegistry.REGENERATION_TOX_POT);
    public static final DeferredHolder<Block, Block> SMOKE_TOX_POT = createOilPotBlock("smoke_tox_pot", () -> ItemRegistry.SMOKE_TOX_POT);
    public static final DeferredHolder<Block, Block> WITCHFIRE_TOX_POT = createOilPotBlock("witchfire_tox_pot", () -> ItemRegistry.WITCHFIRE_TOX_POT);


    public static final Supplier<BlockEntityType<OilPotBlockEntity>> OIL_POT_ENTITY = BLOCK_ENTITY_TYPES.register(
            "oil_pot_entity",
            () -> BlockEntityType.Builder.of(
                    OilPotBlockEntity::new,
                    POISON_OIL_POT.get(),
                    GLOWING_OIL_POT.get(),
                    FIRE_RESISTANCE_OIL_POT.get(),
                    FATIGUE_OIL_POT.get(),
                    TOXIN_TOX_POT.get(),
                    REGENERATION_TOX_POT.get(),
                    SMOKE_TOX_POT.get(),
                    WITCHFIRE_TOX_POT.get()
            ).build(null)
    );

    // |-----------------------------------------------------------------------------------|
    // |------------------------------------Misc Items-------------------------------------|
    // |-----------------------------------------------------------------------------------|
    public static final DeferredHolder<Block, Block> LOST_JOURNAL = BLOCKS.register(
            "lost_journal",
            () -> new LostJournalBlock(BlockBehaviour.Properties.of()
                    .sound(SoundType.WOOL)
                    .strength(0.2F)
                    .pushReaction(PushReaction.DESTROY)
            )
    );


    public static final DeferredHolder<Block, Block> OIL_LAYER = BLOCKS.register(
            "oil_layer",
            () -> new OilLayerBlock(BlockBehaviour.Properties.of()
                    .sound(SoundType.SLIME_BLOCK)
                    .noCollission()
                    .randomTicks()
                    .mapColor(MapColor.SNOW)
                    .strength(3.0F)
                    .pushReaction(PushReaction.DESTROY)
                    .ignitedByLava()
            )
    );

    public static final DeferredHolder<Block, Block> VALENTINES_BOX = BLOCKS.register(
            "valentines_box",
            () -> new ValentinesBoxBlock(BlockBehaviour.Properties.of()
                    .strength(0.2F)
                    .sound(SoundType.WOOL)
            )
    );

    // |----------------------------------------------------------------------------------|
    // |------------------------------------Methods---------------------------------------|
    // |----------------------------------------------------------------------------------|
    private static DeferredHolder<Block, Block> createOilPotBlock(String name, Supplier<Holder<Item>> oilPotItem){
        return BLOCKS.register(name, () -> new OilPotBlock(BlockBehaviour.Properties.of()
                .strength(0.6f)
                .sound(SoundType.DECORATED_POT)
                .isRedstoneConductor((state,level,pos) -> false),
                oilPotItem)
        );
    }

    private static DeferredHolder<Block, Block> createPoisonCrop(String name, Supplier<Holder<Item>> grownItem, List<Holder<MobEffect>> effects, Supplier<Holder<Block>> evolvedBlock){
        return BLOCKS.register(name, () -> new PoisonCropBlock(BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .randomTicks()
                .noCollission()
                .sound(SoundType.CROP)
                .pushReaction(PushReaction.DESTROY)
                .isRedstoneConductor((state,level,pos) -> false),
                grownItem,
                effects,
                () -> Optional.ofNullable(evolvedBlock.get())
        ));
    }
}
