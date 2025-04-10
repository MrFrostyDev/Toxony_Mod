package xyz.yfrostyf.toxony.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import xyz.yfrostyf.toxony.registries.ItemRegistry;

public class VialRackBlock extends HorizontalDirectionalBlock {
    public static final MapCodec<VialRackBlock> CODEC = simpleCodec(VialRackBlock::new);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final IntegerProperty COUNT = IntegerProperty.create("count", 0, 6);
    protected static final VoxelShape SHAPE = Block.box(3, 0, 4, 13, 7, 12);
    protected static final VoxelShape SHAPE_SIDEWAYS = Block.box(4, 0, 3, 12, 7, 13);

    public VialRackBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(COUNT, Integer.valueOf(0)));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(!state.is(this)) return super.useItemOn(stack, state, level, pos, player, hand, hitResult);

        int count = state.getValue(COUNT);
        if(stack.is(ItemRegistry.GLASS_VIAL) && count < 6){
            stack.consume(1, player);
            level.setBlock(pos, state.setValue(COUNT, Integer.valueOf(count + 1)), AlembicBlock.UPDATE_ALL);
            return ItemInteractionResult.SUCCESS;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(!state.is(this)) return super.useWithoutItem(state, level, pos, player, hitResult);

        int count = state.getValue(COUNT);
        if(count > 0){
            player.getInventory().add(new ItemStack(ItemRegistry.GLASS_VIAL, 1));
            level.setBlock(pos, state.setValue(COUNT, Integer.valueOf(count - 1)), AlembicBlock.UPDATE_ALL);
            return InteractionResult.SUCCESS;
        }

        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(COUNT, Integer.valueOf(0));
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch(state.getValue(FACING)){
            case EAST, WEST -> SHAPE_SIDEWAYS;
            default -> SHAPE;
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        // this is where the properties are actually added to the state
        builder.add(FACING, COUNT);
    }

    @Override
    public MapCodec<VialRackBlock> codec(){
        return CODEC;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(level.isClientSide())return;
        if (state.is(newState.getBlock()))return;
        int count = state.getValue(COUNT);

        if(count > 0){
            popResource(level, pos, new ItemStack(ItemRegistry.GLASS_VIAL, count));
        }

        level.updateNeighbourForOutputSignal(pos, this);
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return true;
    }
}
