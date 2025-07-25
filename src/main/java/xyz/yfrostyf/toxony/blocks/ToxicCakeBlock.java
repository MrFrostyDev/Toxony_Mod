package xyz.yfrostyf.toxony.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import xyz.yfrostyf.toxony.registries.ItemRegistry;

public class ToxicCakeBlock extends Block {
    public static final MapCodec<ToxicCakeBlock> CODEC = simpleCodec(ToxicCakeBlock::new);
    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 5);
    public static final int FULL_CAKE_SIGNAL = getOutputSignal(0);
    public static final VoxelShape SHAPE_TOP = Block.box(5.0, 11.0, 5.0, 11.0, 15.0, 11.0);
    public static final VoxelShape SHAPE_TOP_HALF = Block.box(5.0, 11.0, 5.0, 8.0, 15.0, 11.0);
    public static final VoxelShape SHAPE_MID = Block.box(3.0, 6.0, 3.0, 13.0, 11.0, 13.0);
    public static final VoxelShape SHAPE_MID_HALF = Block.box(3.0, 6.0, 3.0, 8.0, 11.0, 13.0);
    public static final VoxelShape SHAPE_BOTTOM = Block.box(1.0, 0.0, 1.0, 15.0, 6.0, 15.0);
    public static final VoxelShape SHAPE_BOTTOM_HALF = Block.box(1.0, 0.0, 1.0, 8.0, 6.0, 15.0);
    public static final VoxelShape[] SHAPE_BY_BITE = new VoxelShape[]{
            Shapes.or(SHAPE_TOP, SHAPE_MID, SHAPE_BOTTOM),
            Shapes.or(SHAPE_TOP_HALF, SHAPE_MID, SHAPE_BOTTOM),
            Shapes.or(SHAPE_MID, SHAPE_BOTTOM),
            Shapes.or(SHAPE_MID_HALF, SHAPE_BOTTOM),
            SHAPE_BOTTOM,
            SHAPE_BOTTOM_HALF
    };

    public ToxicCakeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(BITES, 0)
        );
    }

    @Override
    protected ItemInteractionResult useItemOn(
            ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult
    ) {
        Item item = stack.getItem();
        if (stack.is(ItemTags.CANDLES) && state.getValue(BITES) == 0 && Block.byItem(item) instanceof CandleBlock candleblock) {
            stack.consume(1, player);
            level.playSound(null, pos, SoundEvents.CAKE_ADD_CANDLE, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.setBlockAndUpdate(pos, CandleToxicCakeBlock.byCandle(candleblock));
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            player.awardStat(Stats.ITEM_USED.get(item));
            return ItemInteractionResult.SUCCESS;
        } else {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        InteractionResult result = take(level, pos, state, player);
        return result;
    }

    public static InteractionResult take(Level level, BlockPos pos, BlockState state, Player player){
        int i = state.getValue(BITES);
        player.addItem(new ItemStack(ItemRegistry.TOXIC_CAKE_SLICE, 1));
        level.playSound(null, pos, SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);

        if (i < 5) {
            level.setBlock(pos, state.setValue(BITES, Integer.valueOf(i + 1)), Block.UPDATE_ALL);
        } else {
            level.removeBlock(pos, false);
            level.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(BITES, 0);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_BITE[state.getValue(BITES)];
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return facing == Direction.DOWN && !state.canSurvive(level, currentPos)
                ? Blocks.AIR.defaultBlockState()
                : super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isSolid();
    }

    @Override
    public MapCodec<ToxicCakeBlock> codec(){
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BITES);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        return ItemRegistry.TOXIC_CAKE.get().getDefaultInstance();
    }

    /**
     * Returns the analog signal this block emits. This is the signal a comparator can read from it.
     *
     */
    @Override
    protected int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
        return getOutputSignal(blockState.getValue(BITES));
    }

    public static int getOutputSignal(int eaten) {
        return (7 - eaten) * 2;
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }
}
