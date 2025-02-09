package xyz.yfrostyf.toxony.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.ItemStackHandler;
import xyz.yfrostyf.toxony.blocks.entities.CopperCrucibleBlockEntity;
import xyz.yfrostyf.toxony.registries.BlockRegistry;

public class CopperCrucibleBlock extends Block implements EntityBlock {
    public static final MapCodec<CopperCrucibleBlock> CODEC = simpleCodec(CopperCrucibleBlock::new);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    protected static final VoxelShape SHAPE = Block.box(4, 0, 4, 12, 9, 12);

    public CopperCrucibleBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.valueOf(false)));
    }

    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(!(level.getBlockEntity(pos) instanceof CopperCrucibleBlockEntity blockEntity))return InteractionResult.FAIL;

        // Client/Server Side actions
        player.playSound(SoundEvents.COPPER_HIT);

        // Server Side actions
        if (level.isClientSide())return InteractionResult.SUCCESS;
        level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);

        player.openMenu(blockEntity, pos);
        return InteractionResult.CONSUME;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }


    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CopperCrucibleBlockEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(level.isClientSide())return;
        if(!(level.getBlockEntity(pos) instanceof CopperCrucibleBlockEntity blockEntity))return;
        if (state.is(newState.getBlock()))return;

        ItemStackHandler inventory = blockEntity.getItemContainer();
        for(int i=0;i<2;i++){
            popResource(level, pos, inventory.getStackInSlot(i).copy());
        }
        level.updateNeighbourForOutputSignal(pos, this);
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if(!(level.getBlockEntity(pos) instanceof CopperCrucibleBlockEntity entity))return;
        if(!entity.isLit())return;

        double d0 = (double)pos.getX() + 0.4 + (double)random.nextFloat() * 0.2;
        double d1 = (double)pos.getY() + 0.6 + (double)random.nextFloat() * 0.3;
        double d2 = (double)pos.getZ() + 0.4 + (double)random.nextFloat() * 0.2;
        level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0, 0.0, 0.0);
    }

    @SuppressWarnings("unchecked") // Due to generics, an unchecked cast is necessary here.
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        // You can return different tickers here, depending on whatever factors you want. A common use case would be
        // to return different tickers on the client or server, only tick one side to begin with,
        // or only return a ticker for some blockstates (e.g. when using a "my machine is working" blockstate property).
        return type == BlockRegistry.COPPER_CRUCIBLE_ENTITY.get() ? (BlockEntityTicker<T>) CopperCrucibleBlockEntity::tick : null;
    }

    @Override
    public MapCodec<CopperCrucibleBlock> codec(){
        return CODEC;
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

}
