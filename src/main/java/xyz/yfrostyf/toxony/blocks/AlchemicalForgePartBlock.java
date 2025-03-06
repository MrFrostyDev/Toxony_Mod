package xyz.yfrostyf.toxony.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import xyz.yfrostyf.toxony.registries.BlockRegistry;

import java.util.Set;


public class AlchemicalForgePartBlock extends Block {
    public static final MapCodec<AlchemicalForgePartBlock> CODEC = simpleCodec(AlchemicalForgePartBlock::new);
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public AlchemicalForgePartBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.valueOf(false)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        Player player = context.getPlayer();
        Direction horizontalDirection = context.getHorizontalDirection();

        if(player == null || player.level().isClientSide()) return this.defaultBlockState();

        // Detect if right of clicked pos has alchemical forge part.
        BlockPos posToRight = pos.relative(horizontalDirection.getClockWise());
        if(level.getBlockState(posToRight).is(BlockRegistry.ALCHEMICAL_FORGE_PART)){
            return BlockRegistry.ALCHEMICAL_FORGE.get().defaultBlockState()
                    .setValue(AlchemicalForgeBlock.PART, ChestType.LEFT).setValue(LIT, Boolean.valueOf(false))
                    .setValue(AlchemicalForgeBlock.FACING, player.getDirection());
        }

        // Detect if left of clicked pos has alchemical forge part.
        BlockPos posToLeft = pos.relative(horizontalDirection.getCounterClockWise());
        if(level.getBlockState(posToLeft).is(BlockRegistry.ALCHEMICAL_FORGE_PART)){
            return BlockRegistry.ALCHEMICAL_FORGE.get().defaultBlockState()
                    .setValue(AlchemicalForgeBlock.PART, ChestType.RIGHT).setValue(LIT, Boolean.valueOf(false))
                    .setValue(AlchemicalForgeBlock.FACING, player.getDirection());
        }

        // Detect if forward of clicked pos has alchemical forge part.
        BlockPos posToForward = pos.relative(player.getDirection());
        if(level.getBlockState(posToForward).is(BlockRegistry.ALCHEMICAL_FORGE_PART)){
            return BlockRegistry.ALCHEMICAL_FORGE.get().defaultBlockState()
                    .setValue(AlchemicalForgeBlock.PART, ChestType.RIGHT).setValue(LIT, Boolean.valueOf(false))
                    .setValue(AlchemicalForgeBlock.FACING, player.getDirection().getClockWise());
        }

        // Detect if back of clicked pos has alchemical forge part.
        BlockPos posToBack = pos.relative(player.getDirection().getOpposite());
        if(level.getBlockState(posToBack).is(BlockRegistry.ALCHEMICAL_FORGE_PART)){
            return BlockRegistry.ALCHEMICAL_FORGE.get().defaultBlockState()
                    .setValue(AlchemicalForgeBlock.PART, ChestType.LEFT).setValue(LIT, Boolean.valueOf(false))
                    .setValue(AlchemicalForgeBlock.FACING, player.getDirection().getClockWise());
        }

        return super.getStateForPlacement(context);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        for(BlockPos blockPos : Set.of(pos.north(), pos.east(), pos.south(), pos.west())){
            BlockState selBlockState = level.getBlockState(blockPos);
            if (selBlockState.is(BlockRegistry.ALCHEMICAL_FORGE)) {
                ChestType type = selBlockState.getValue(BlockStateProperties.CHEST_TYPE);
                Direction direction = selBlockState.getValue(HorizontalDirectionalBlock.FACING);
                level.setBlock(pos, BlockRegistry.ALCHEMICAL_FORGE.get().defaultBlockState()
                        .setValue(BlockStateProperties.CHEST_TYPE, type == ChestType.LEFT ? ChestType.RIGHT : ChestType.LEFT)
                        .setValue(HorizontalDirectionalBlock.FACING, direction), Block.UPDATE_ALL);
                return;
            }
        }
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    private static Direction getNeighbourDirection(ChestType part, Direction direction) {
        return part == ChestType.LEFT ? direction.getClockWise() : direction.getCounterClockWise();
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public MapCodec<AlchemicalForgePartBlock> codec(){
        return CODEC;
    }
}
