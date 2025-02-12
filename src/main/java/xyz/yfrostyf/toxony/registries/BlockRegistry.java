package xyz.yfrostyf.toxony.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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
import xyz.yfrostyf.toxony.blocks.*;
import xyz.yfrostyf.toxony.blocks.entities.AlembicBlockEntity;
import xyz.yfrostyf.toxony.blocks.entities.CopperCrucibleBlockEntity;
import xyz.yfrostyf.toxony.blocks.entities.MortarPestleBlockEntity;
import xyz.yfrostyf.toxony.blocks.plants.FalseBerryBushBlock;
import xyz.yfrostyf.toxony.blocks.PoisonFarmBlock;
import xyz.yfrostyf.toxony.blocks.plants.WildOcelotMintBlock;

import java.util.List;
import java.util.function.Supplier;

public class BlockRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ToxonyMain.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, ToxonyMain.MOD_ID);

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
        BLOCKS.register(eventBus);
    }

    public static final DeferredHolder<Block, Block> POISON_FARMLAND = BLOCKS.register(
            "poison_farmland",
            () -> new PoisonFarmBlock(
                    BlockBehaviour.Properties.of()
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
            () -> new FalseBerryBushBlock(
                    BlockBehaviour.Properties.of()
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
            () -> new WildOcelotMintBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.PLANT)
                            .randomTicks()
                            .noCollission()
                            .sound(SoundType.CROP)
                            .pushReaction(PushReaction.DESTROY)
                            .isRedstoneConductor((state,level,pos) -> false),
                    List.of(MobEffects.POISON)
            )
    );

    public static final DeferredHolder<Block, Block> OCELOT_MINT = BLOCKS.register(
            "ocelot_mint",
            () -> new PoisonCropBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.PLANT)
                            .randomTicks()
                            .noCollission()
                            .sound(SoundType.CROP)
                            .pushReaction(PushReaction.DESTROY)
                            .isRedstoneConductor((state,level,pos) -> false),
                    List.of(MobEffects.POISON)
            )
    );

    // |--------------------------------------------------------------------------------------|
    // |------------------------------------Block Entities------------------------------------|
    // |--------------------------------------------------------------------------------------|

    public static final DeferredHolder<Block, Block> MORTAR_PESTLE = BLOCKS.register(
            "mortar_pestle",
            () -> new MortarPestleBlock(
                BlockBehaviour.Properties.of()
                        .strength(0.5f)
                        .sound(SoundType.STONE)
                        .isRedstoneConductor((state,level,pos) -> false)
            )
    );

    public static final DeferredHolder<Block, Block> COPPER_CRUCIBLE = BLOCKS.register(
            "copper_crucible",
            () -> new CopperCrucibleBlock(
                    BlockBehaviour.Properties.of()
                            .strength(0.5f)
                            .sound(SoundType.COPPER)
                            .isRedstoneConductor((state,level,pos) -> false)
            )
    );

    public static final DeferredHolder<Block, Block> ALEMBIC = BLOCKS.register(
            "alembic",
            () -> new AlembicBlock(
                    BlockBehaviour.Properties.of()
                            .strength(0.8f)
                            .sound(SoundType.COPPER)
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
                    // The supplier to use for constructing the block entity instances.
                    CopperCrucibleBlockEntity::new,
                    // A vararg of blocks that can have this block entity.
                    // This assumes the existence of the referenced blocks as DeferredBlock<Block>s.
                    COPPER_CRUCIBLE.get()
            ).build(null)
    );

    public static final Supplier<BlockEntityType<AlembicBlockEntity>> ALEMBIC_ENTITY = BLOCK_ENTITY_TYPES.register(
            "alembic_entity",
            () -> BlockEntityType.Builder.of(
                    // The supplier to use for constructing the block entity instances.
                    AlembicBlockEntity::new,
                    // A vararg of blocks that can have this block entity.
                    // This assumes the existence of the referenced blocks as DeferredBlock<Block>s.
                    ALEMBIC.get()
            ).build(null)
    );
}
