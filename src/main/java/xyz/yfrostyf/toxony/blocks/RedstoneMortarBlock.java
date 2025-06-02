package xyz.yfrostyf.toxony.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import xyz.yfrostyf.toxony.blocks.entities.RedstoneMortarBlockEntity;
import xyz.yfrostyf.toxony.registries.BlockRegistry;

public class RedstoneMortarBlock extends HorizontalDirectionalBlock implements EntityBlock {
    public static final int PESTLE_TOTAL_TICK = 30;
    public static final int PESTLE_TOTAL_COUNT = 5;

    public static final MapCodec<RedstoneMortarBlock> CODEC = simpleCodec(RedstoneMortarBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
    public static final IntegerProperty INGREDIENTS = IntegerProperty.create("ingredients", 0, 5);

    public RedstoneMortarBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(INGREDIENTS, 0)
                .setValue(TRIGGERED, false)
        );
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(!(level.getBlockEntity(pos) instanceof RedstoneMortarBlockEntity blockEntity))return InteractionResult.FAIL;
        player.openMenu(blockEntity, pos);
        player.playSound(SoundEvents.COPPER_HIT);
        return InteractionResult.CONSUME;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state){
        return new RedstoneMortarBlockEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, INGREDIENTS, TRIGGERED);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(level.isClientSide())return;
        if(!(level.getBlockEntity(pos) instanceof RedstoneMortarBlockEntity blockEntity))return;
        if (state.is(newState.getBlock()))return;

        ItemStackHandler inventory = blockEntity.getItemContainer();
        for(int i=0; i<inventory.getSlots(); i++){
            var itemstack = new ItemStack(inventory.getStackInSlot(i).getItemHolder(), inventory.getStackInSlot(i).getCount());
            popResource(level, pos, itemstack);
        }

        level.updateNeighbourForOutputSignal(pos, this);
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
        if(!(level.getBlockEntity(pos) instanceof RedstoneMortarBlockEntity blockEntity))return;
        if (level.hasNeighborSignal(pos) && !state.getValue(TRIGGERED)) {
            if(!blockEntity.isPestling){
                blockEntity.startPestling();
            }
            if(blockEntity.pestleCount < PESTLE_TOTAL_COUNT && blockEntity.pestleTick <= 0 && blockEntity.isPestling){
                blockEntity.pestleTick = PESTLE_TOTAL_TICK;
                blockEntity.pestleCount++;
                level.playSound(null, pos, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 0.8F, 1.1F);
            }
            else if(blockEntity.pestleCount >= PESTLE_TOTAL_COUNT && blockEntity.pestleTick <= 0 && blockEntity.isPestling){
                blockEntity.finishPestling(level);
                level.playSound(null, pos, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 0.8F, 1.1F);
            }
            level.setBlock(pos, state.setValue(TRIGGERED, Boolean.valueOf(true)), Block.UPDATE_ALL);
        }
        else if (!level.hasNeighborSignal(pos) && state.getValue(TRIGGERED)){
            level.setBlock(pos, state.setValue(TRIGGERED, Boolean.valueOf(false)), Block.UPDATE_ALL);
        }
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    /**
     * Returns the analog signal this block emits. This is the signal a comparator can read from it.
     *
     */
    @Override
    protected int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
        if(!(level.getBlockEntity(pos) instanceof RedstoneMortarBlockEntity blockEntity)) return 0;
        return blockEntity.pestleCount;
    }

    @SuppressWarnings("unchecked") // Due to generics, an unchecked cast is necessary here.
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == BlockRegistry.REDSTONE_MORTAR_ENTITY.get() ? (BlockEntityTicker<T>) RedstoneMortarBlockEntity::tick : null;
    }

    @Override
    public MapCodec<RedstoneMortarBlock> codec(){
        return CODEC;
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return true;
    }

}
