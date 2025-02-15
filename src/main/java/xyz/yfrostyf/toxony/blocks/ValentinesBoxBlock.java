package xyz.yfrostyf.toxony.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import xyz.yfrostyf.toxony.registries.ItemRegistry;

import java.util.List;
import java.util.Random;

public class ValentinesBoxBlock extends HorizontalDirectionalBlock {
    public static final MapCodec<ValentinesBoxBlock> CODEC = simpleCodec(ValentinesBoxBlock::new);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final IntegerProperty CHOCOLATE_COUNT = IntegerProperty.create("chocolates", 0, 6);
    protected static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 3, 14);

    public ValentinesBoxBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(CHOCOLATE_COUNT, 6)
                .setValue(OPEN, false)
        );
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        // Client/Server Side actions
        //
        if(player.isCrouching()){
            if(state.getValue(OPEN)){
                level.setBlock(pos, state.setValue(OPEN, Boolean.valueOf(false)), Block.UPDATE_ALL);
            } else{
                level.setBlock(pos, state.setValue(OPEN, Boolean.valueOf(true)), Block.UPDATE_ALL);
            }
            return InteractionResult.SUCCESS;
        }

        if(state.getValue(OPEN)){
            if (state.getValue(CHOCOLATE_COUNT) > 0){
                player.playSound(SoundEvents.WOOL_PLACE);

                List<Item> chocolates = List.of(ItemRegistry.MILK_CHOCOLATE.get(), ItemRegistry.MINT_CHOCOLATE.get(), ItemRegistry.DARK_CHOCOLATE.get());
                level.setBlock(pos, state.setValue(CHOCOLATE_COUNT, state.getValue(CHOCOLATE_COUNT)-1), Block.UPDATE_ALL);
                player.addItem(new ItemStack(chocolates.get(new Random().nextInt(chocolates.size()))));
            }
            else{
                Minecraft.getInstance().gui.setOverlayMessage(
                        Component.translatable("message.toxony.valetines_box.empty"),
                        false
                );
            }
            return InteractionResult.SUCCESS;
        }
        else if(level.isClientSide()){
            Minecraft.getInstance().gui.setOverlayMessage(
                    Component.translatable("message.toxony.valetines_box.warning"),
                    false
            );
        }

        return InteractionResult.FAIL;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        // this is where the properties are actually added to the state
        builder.add(FACING, CHOCOLATE_COUNT, OPEN);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public MapCodec<ValentinesBoxBlock> codec(){
        return CODEC;
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return true;
    }
}
