package xyz.yfrostyf.toxony.blocks;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import xyz.yfrostyf.toxony.registries.BlockRegistry;

import java.util.Map;

public class CandleToxicCakeBlock extends AbstractCandleBlock {
    public static final MapCodec<CandleToxicCakeBlock> CODEC = RecordCodecBuilder.mapCodec(
            p_344652_ -> p_344652_.group(BuiltInRegistries.BLOCK.byNameCodec().fieldOf("candle").forGetter(p_316072_ -> p_316072_.candleBlock), propertiesCodec())
                    .apply(p_344652_, CandleToxicCakeBlock::new)
    );
    public static final BooleanProperty LIT = AbstractCandleBlock.LIT;
    protected static final VoxelShape CANDLE_SHAPE = Block.box(7.0, 15.0, 7.0, 9.0, 18.0, 9.0);
    protected static final VoxelShape SHAPE = Shapes.or(ToxicCakeBlock.SHAPE_BY_BITE[0], CANDLE_SHAPE);
    private static final Map<CandleBlock, CandleToxicCakeBlock> BY_CANDLE = Maps.newHashMap();
    private static final Iterable<Vec3> PARTICLE_OFFSETS = ImmutableList.of(new Vec3(0.5, 1.0, 0.5));
    private final CandleBlock candleBlock;

    @Override
    public MapCodec<CandleToxicCakeBlock> codec() {
        return CODEC;
    }

    public CandleToxicCakeBlock(Block candleBlock, BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.valueOf(false)));
        if (candleBlock instanceof CandleBlock candleblock) {
            BY_CANDLE.put(candleblock, this);
            this.candleBlock = candleblock;
        } else {
            throw new IllegalArgumentException("Expected block to be of " + CandleBlock.class + " was " + candleBlock.getClass());
        }
    }

    @Override
    protected Iterable<Vec3> getParticleOffsets(BlockState state) {
        return PARTICLE_OFFSETS;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected ItemInteractionResult useItemOn(
            ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult
    ) {
        if (stack.is(Items.FLINT_AND_STEEL) || stack.is(Items.FIRE_CHARGE)) {
            return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        } else if (candleHit(hitResult) && stack.isEmpty() && state.getValue(LIT)) {
            extinguish(player, state, level, pos);
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        InteractionResult interactionresult = ToxicCakeBlock.take(level, pos, BlockRegistry.TOXIC_CAKE.get().defaultBlockState(), player);
        if (interactionresult.consumesAction()) {
            dropResources(state, level, pos);
        }

        return interactionresult;
    }

    /**
     * Called periodically clientside on blocks near the player to show effects (like furnace fire particles).
     */
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(LIT)) {
            this.getParticleOffsets(state)
                    .forEach(
                            v -> addParticlesAndSound(
                                    level, v.add((double)pos.getX(), (double)pos.getY() + 0.23, (double)pos.getZ()), random
                            )
                    );
        }
    }

    private static void addParticlesAndSound(Level level, Vec3 offset, RandomSource random) {
        float f = random.nextFloat();
        if (f < 0.3F) {
            level.addParticle(ParticleTypes.SMOKE, offset.x, offset.y, offset.z, 0.0, 0.0, 0.0);
            if (f < 0.17F) {
                level.playLocalSound(
                        offset.x + 0.5,
                        offset.y + 0.5,
                        offset.z + 0.5,
                        SoundEvents.CANDLE_AMBIENT,
                        SoundSource.BLOCKS,
                        1.0F + random.nextFloat(),
                        random.nextFloat() * 0.7F + 0.3F,
                        false
                );
            }
        }

        level.addParticle(ParticleTypes.SMALL_FLAME, offset.x, offset.y, offset.z, 0.0, 0.0, 0.0);
    }

    private static boolean candleHit(BlockHitResult hit) {
        return hit.getLocation().y - (double)hit.getBlockPos().getY() > 0.7;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack(Blocks.CAKE);
    }

    /**
     * Update the provided state given the provided neighbor direction and neighbor state, returning a new state.
     * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately returns its solidified counterpart.
     * Note that this method should ideally consider only the specific direction passed in.
     */
    @Override
    protected BlockState updateShape(
            BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos
    ) {
        return direction == Direction.DOWN && !state.canSurvive(level, pos)
                ? Blocks.AIR.defaultBlockState()
                : super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isSolid();
    }

    /**
     * Returns the analog signal this block emits. This is the signal a comparator can read from it.
     *
     */
    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return ToxicCakeBlock.FULL_CAKE_SIGNAL;
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    public static BlockState byCandle(CandleBlock candle) {
        return BY_CANDLE.get(candle).defaultBlockState();
    }

    public static boolean canLight(BlockState state) {
        return state.is(BlockTags.CANDLE_CAKES, s -> s.hasProperty(LIT) && !state.getValue(LIT));
    }
}
