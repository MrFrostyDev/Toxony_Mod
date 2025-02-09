package xyz.yfrostyf.toxony.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.blocks.entities.MortarPestleBlockEntity;
import xyz.yfrostyf.toxony.registries.BlockRegistry;

public class MortarPestleBlock extends Block implements EntityBlock {
    public static final int PESTLE_TOTAL_TICK = 20;
    public static final int PESTLE_TOTAL_COUNT = 3;

    public static final MapCodec<MortarPestleBlock> CODEC = simpleCodec(MortarPestleBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final IntegerProperty HAS_INGREDIENTS = IntegerProperty.create("has_ingredients", 0, 3);
    protected static final VoxelShape SHAPE = Block.box(3, 0, 3, 13, 5, 13);

    public MortarPestleBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(HAS_INGREDIENTS, 0));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(!(level.getBlockEntity(pos) instanceof MortarPestleBlockEntity blockEntity))return InteractionResult.FAIL;

        // Client/Server Side actions
        //
        if (blockEntity.isPestling && blockEntity.pestleTick <= 0 && blockEntity.pestleCount < PESTLE_TOTAL_COUNT){
            player.playSound(SoundEvents.GRINDSTONE_USE);
        }

        // Check if item in hand is the right useItem. Ignore if useItem is an empty stack.
        if(level.isClientSide() && blockEntity.isPestling && blockEntity.pestleCount >= PESTLE_TOTAL_COUNT){
            ItemStack useItem = blockEntity.findRecipe().get().value().getUseItem();
            if(player.getMainHandItem().getItem() != useItem.getItem() && !useItem.isEmpty()){
                Minecraft.getInstance().gui.setOverlayMessage(
                        Component.translatable("message.toxony.mortar.warning", useItem.getDisplayName()),
                        false
                );
            }
        }

        // Server Side actions
        //
        if (level.isClientSide())return InteractionResult.SUCCESS;
        level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);

        if (!blockEntity.isPestling) {
            player.openMenu(blockEntity, pos);
            return InteractionResult.CONSUME;
        }
        else if (blockEntity.pestleCount >= PESTLE_TOTAL_COUNT && blockEntity.pestleTick <= 0) {
            blockEntity.finishPestling(player);
            return InteractionResult.SUCCESS;
        }
        else if (blockEntity.pestleTick <= 0){
            blockEntity.pestleTick = PESTLE_TOTAL_TICK;
            blockEntity.pestleCount++;
            player.playSound(SoundEvents.GRINDSTONE_USE);

            ToxonyMain.LOGGER.info("[MortarPestleBlock pestling]: count: {}", blockEntity.pestleCount);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
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
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state){
        return new MortarPestleBlockEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        // this is where the properties are actually added to the state
        builder.add(FACING, HAS_INGREDIENTS);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(level.isClientSide())return;
        if(!(level.getBlockEntity(pos) instanceof MortarPestleBlockEntity blockEntity))return;
        if (state.is(newState.getBlock()))return;

        ItemStackHandler inventory = blockEntity.getItemContainer();
        for(int i=0; i<inventory.getSlots(); i++){
            var itemstack = new ItemStack(inventory.getStackInSlot(i).getItemHolder(), inventory.getStackInSlot(i).getCount());
            popResource(level, pos, itemstack);
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
        return type == BlockRegistry.MORTAR_PESTLE_ENTITY.get() ? (BlockEntityTicker<T>) MortarPestleBlockEntity::tick : null;
    }

    @Override
    public MapCodec<MortarPestleBlock> codec(){
        return CODEC;
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

}
