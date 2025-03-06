package xyz.yfrostyf.toxony.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.items.ItemStackHandler;
import xyz.yfrostyf.toxony.blocks.entities.AlchemicalForgeBlockEntity;
import xyz.yfrostyf.toxony.registries.BlockRegistry;


public class AlchemicalForgeBlock extends HorizontalDirectionalBlock implements EntityBlock {
    public static final MapCodec<AlchemicalForgeBlock> CODEC = simpleCodec(AlchemicalForgeBlock::new);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final EnumProperty<ChestType> PART = BlockStateProperties.CHEST_TYPE;

    public AlchemicalForgeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(PART, ChestType.LEFT).setValue(LIT, Boolean.valueOf(false)));
    }

    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(!(level.getBlockEntity(pos) instanceof AlchemicalForgeBlockEntity blockEntity))return InteractionResult.FAIL;

        // Client/Server Side actions
        player.playSound(SoundEvents.SMITHING_TABLE_USE);

        if(state.getValue(LIT)){
            if(state.getValue(PART) == ChestType.RIGHT){
                Direction directionToBody = getNeighbourDirection(state.getValue(PART), state.getValue(FACING));
                if(level.getBlockEntity(pos.relative(directionToBody)) instanceof AlchemicalForgeBlockEntity neighbourBlockEntity){
                    Minecraft.getInstance().gui.setOverlayMessage(
                            Component.translatable("message.toxony.alchemical_forge.active", Math.round(neighbourBlockEntity.getProgress()*100) + "%"),
                            false
                    );
                }
            }
            else{
                Minecraft.getInstance().gui.setOverlayMessage(
                        Component.translatable("message.toxony.alchemical_forge.active", Math.round(blockEntity.getProgress()*100) + "%"),
                        false
                );
            }
        }

        // Server Side actions
        if (level.isClientSide())return InteractionResult.SUCCESS;
        level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);


        if(state.getValue(PART) == ChestType.RIGHT){
            Direction directionToBody = getNeighbourDirection(state.getValue(PART), state.getValue(FACING));
            BlockPos neighbourBlockPos = pos.relative(directionToBody);
            if(level.getBlockEntity(neighbourBlockPos) instanceof AlchemicalForgeBlockEntity neighbourBlockEntity){
                if(!neighbourBlockEntity.isForging) {
                    player.openMenu(neighbourBlockEntity, neighbourBlockPos);
                    return InteractionResult.CONSUME;
                }
            }
        }
        else{
            if(!blockEntity.isForging) {
                player.openMenu(blockEntity, pos);
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.PASS;
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
        return new AlchemicalForgeBlockEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT, PART);
    }

    public static Direction getNeighbourDirection(ChestType part, Direction direction) {
        return part == ChestType.LEFT ? direction.getClockWise() : direction.getCounterClockWise();
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    // Update this block's block states depending on the changes of their neighbouring blocks.
    // Used by blocks like fences or powdered concrete.
    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        // Check if the new block that was changed is on the right of this one.
        if (facing == getNeighbourDirection(state.getValue(PART), state.getValue(FACING))) {
            // Check if the block beside this is not a copy or if it has the same property. (LEFT:LEFT or RIGHT:RIGHT)
            // If either is true, change the block as the neighbouring block to its right must be another alchemical forge with
            // the opposite property. (LEFT:RIGHT or RIGHT:LEFT)
            if(!facingState.is(this) || facingState.getValue(PART) == state.getValue(PART)){
                level.setBlock(currentPos, BlockRegistry.ALCHEMICAL_FORGE_PART.get().defaultBlockState(), 35);
                return BlockRegistry.ALCHEMICAL_FORGE_PART.get().defaultBlockState();
            }
            else{
                return state;
            }
        }

        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(level.isClientSide())return;
        if(!(level.getBlockEntity(pos) instanceof AlchemicalForgeBlockEntity blockEntity))return;
        if (state.is(newState.getBlock()))return;

        ItemStackHandler inventory = blockEntity.getItemContainer();
        for(int i=0;i<3;i++){
            popResource(level, pos, inventory.getStackInSlot(i).copy());
        }

        level.updateNeighbourForOutputSignal(pos, this);
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @SuppressWarnings("unchecked") // Due to generics, an unchecked cast is necessary here.
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        // You can return different tickers here, depending on whatever factors you want. A common use case would be
        // to return different tickers on the client or server, only tick one side to begin with,
        // or only return a ticker for some blockstates (e.g. when using a "my machine is working" blockstate property).
        return type == BlockRegistry.ALCHEMICAL_FORGE_ENTITY.get() ? (BlockEntityTicker<T>) AlchemicalForgeBlockEntity::tick : null;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if(!(level.getBlockEntity(pos) instanceof AlchemicalForgeBlockEntity blockEntity))return;
        if(!blockEntity.isForging)return;

        double d0 = (double)pos.getX() + 0.4 + (double)random.nextFloat() * 0.2;
        double d1 = (double)pos.getY() + 0.6 + (double)random.nextFloat() * 0.3;
        double d2 = (double)pos.getZ() + 0.4 + (double)random.nextFloat() * 0.2;
        level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0, 0.0, 0.0);
    }

    @Override
    public MapCodec<AlchemicalForgeBlock> codec(){
        return CODEC;
    }

}
