package xyz.yfrostyf.toxony.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.ItemStackHandler;
import xyz.yfrostyf.toxony.blocks.entities.AlembicBlockEntity;
import xyz.yfrostyf.toxony.blocks.entities.CopperCrucibleBlockEntity;
import xyz.yfrostyf.toxony.registries.BlockRegistry;

import static net.minecraft.world.level.block.BedBlock.PART;


public class AlembicBlock extends HorizontalDirectionalBlock implements EntityBlock {
    public static final MapCodec<AlembicBlock> CODEC = simpleCodec(AlembicBlock::new);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final EnumProperty<ChestType> PART = BlockStateProperties.CHEST_TYPE;

    protected static final VoxelShape SHAPE_BASE = Block.box(4, 0, 4, 12, 8, 12);
    protected static final VoxelShape SHAPE_ROD = Block.box(7, 10, 7, 9, 20, 9);
    protected static final VoxelShape SHAPE_BOTTLE_N = Block.box(3, 0, 5, 9, 8, 11);
    protected static final VoxelShape SHAPE_BOTTLE_E = Block.box(5, 0, 3, 11, 8, 9);
    protected static final VoxelShape SHAPE_BOTTLE_S = Block.box(7, 0, 5, 13, 8, 11);
    protected static final VoxelShape SHAPE_BOTTLE_W = Block.box(5, 0, 7, 11, 8, 13);

    public static final VoxelShape SHAPE = Shapes.or(SHAPE_BASE, SHAPE_ROD);

    public AlembicBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(PART, ChestType.LEFT).setValue(LIT, Boolean.valueOf(false)));
    }

    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(!(level.getBlockEntity(pos) instanceof AlembicBlockEntity blockEntity))return InteractionResult.FAIL;

        // Client/Server Side actions
        player.playSound(SoundEvents.AMETHYST_BLOCK_BREAK);

        // Server Side actions
        if (level.isClientSide())return InteractionResult.SUCCESS;
        level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);

        player.openMenu(blockEntity, pos);
        return InteractionResult.CONSUME;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getHorizontalDirection();
        BlockPos blockpos = context.getClickedPos();
        BlockPos blockposRight = blockpos.relative(direction.getClockWise());
        Level level = context.getLevel();
        return level.getBlockState(blockposRight).canBeReplaced(context) && level.getWorldBorder().isWithinBounds(blockposRight)
                ? this.defaultBlockState().setValue(FACING, direction)
                : null;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AlembicBlockEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT, PART);
    }

    private static Direction getNeighbourDirection(ChestType part, Direction direction) {
        return part == ChestType.LEFT ? direction.getClockWise() : direction.getCounterClockWise();
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if(state.getValue(PART) == ChestType.LEFT) return SHAPE;

        Direction direction = state.getValue(FACING);
        return switch (direction) {
            case SOUTH -> SHAPE_BOTTLE_S;
            case EAST -> SHAPE_BOTTLE_E;
            case WEST -> SHAPE_BOTTLE_W;
            default -> SHAPE_BOTTLE_N;
        };
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        // Only render the left side since the right is just
        // a placeholder to represent the full block.
        if (blockState.getValue(PART).equals(ChestType.LEFT))
            return RenderShape.MODEL;
        else
            return RenderShape.INVISIBLE;
    }

    // Update this block's block states depending on the changes of their neighbouring blocks.
    // Used by blocks like fences or powdered concrete.
    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        // Check if the new block that was changed is on the right of this one.
        if (facing == getNeighbourDirection(state.getValue(PART), state.getValue(FACING))) {
            // Check if the block beside this is not a copy or if it has the same property. (LEFT:LEFT or RIGHT:RIGHT)
            // If either is true, remove the block as the neighbouring block to its right must be another alembic with
            // the opposite property. (LEFT:RIGHT or RIGHT:LEFT)
            if(!facingState.is(this) || facingState.getValue(PART) == state.getValue(PART)){
                level.setBlock(currentPos, Blocks.AIR.defaultBlockState(), 35);
                level.levelEvent(null, 2001, currentPos, Block.getId(Blocks.AIR.defaultBlockState()));
                return Blocks.AIR.defaultBlockState();
            }
            else{
                return state;
            }
        }

        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide) {
            // Set the left part to wherever we are placed it.
            level.setBlock(pos, state.setValue(PART, ChestType.LEFT), 3);

            // Get the position of the right PART of the Alembic
            // then set the block at that position to the right part.
            BlockPos blockpos = pos.relative(getNeighbourDirection(state.getValue(PART), state.getValue(FACING)));
            level.setBlock(blockpos, state.setValue(PART, ChestType.RIGHT), 3);

            level.blockUpdated(pos, Blocks.AIR);
            state.updateNeighbourShapes(level, pos, 3);
        }
        super.setPlacedBy(level, pos, state, placer, stack);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(level.isClientSide())return;
        if(!(level.getBlockEntity(pos) instanceof AlembicBlockEntity blockEntity))return;
        if (state.is(newState.getBlock()))return;

        ItemStackHandler inventory = blockEntity.getItemContainer();
        for(int i=0;i<3;i++){
            popResource(level, pos, inventory.getStackInSlot(i).copy());
        }

        level.updateNeighbourForOutputSignal(pos, this);
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if(state.getValue(PART) == ChestType.RIGHT)return;
        if(!(level.getBlockEntity(pos) instanceof AlembicBlockEntity entity))return;
        if(!entity.hasFuel())return;

        double d0 = (double)pos.getX() + 0.4 + (double)random.nextFloat() * 0.2;
        double d1 = (double)pos.getY() + 1.2 + (double)random.nextFloat() * 0.3;
        double d2 = (double)pos.getZ() + 0.4 + (double)random.nextFloat() * 0.2;
        level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0, 0.0, 0.0);
    }

    @SuppressWarnings("unchecked") // Due to generics, an unchecked cast is necessary here.
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        // You can return different tickers here, depending on whatever factors you want. A common use case would be
        // to return different tickers on the client or server, only tick one side to begin with,
        // or only return a ticker for some blockstates (e.g. when using a "my machine is working" blockstate property).
        return type == BlockRegistry.ALEMBIC_ENTITY.get() ? (BlockEntityTicker<T>) AlembicBlockEntity::tick : null;
    }

    @Override
    public MapCodec<AlembicBlock> codec(){
        return CODEC;
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

}
