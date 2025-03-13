package xyz.yfrostyf.toxony.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import xyz.yfrostyf.toxony.registries.MobEffectRegistry;
import xyz.yfrostyf.toxony.registries.TagRegistry;

import java.util.stream.Stream;


public class OilLayerBlock extends Block {
    public static final MapCodec<OilLayerBlock> CODEC = simpleCodec(OilLayerBlock::new);
    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);

    @Override
    public MapCodec<OilLayerBlock> codec() {
        return CODEC;
    }

    public OilLayerBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any());
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        switch (pathComputationType) {
            case LAND:
                return true;
            default:
                return false;
        }
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        AABB area = new AABB(pos).inflate(2);
        Stream<BlockState> blocksInArea = level.getBlockStates(area);
        if (blocksInArea.anyMatch(block -> block.is(TagRegistry.OPEN_FLAME))){
            level.setBlock(pos, Blocks.FIRE.defaultBlockState(), UPDATE_ALL);
        }
        super.onPlace(state, level, pos, oldState, movedByPiston);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        BlockState neighbourBlockState = level.getBlockState(neighborPos);
        if(neighbourBlockState.is(BlockTags.FIRE)){
            level.setBlock(pos, Blocks.FIRE.defaultBlockState(), UPDATE_ALL);
        }
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
    }

    @Override
    protected boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState blockstate = level.getBlockState(pos.below());
        if (blockstate.is(TagRegistry.OIL_LAYER_CANNOT_SURVIVE_ON) || level.isEmptyBlock(pos.below())) {
            return false;
        } else {
            return Block.isFaceFull(blockstate.getCollisionShape(level, pos.below()), Direction.UP);
        }
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        AABB area = new AABB(pos).inflate(3);
        Stream<BlockState> blocksInArea = level.getBlockStates(area);
        if (blocksInArea.anyMatch(block -> block.is(TagRegistry.OPEN_FLAME))){
            level.setBlock(pos, Blocks.FIRE.defaultBlockState(), UPDATE_ALL);
        }
        super.tick(state, level, pos, random);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(stack.is(Items.FLINT_AND_STEEL) || stack.is(Items.FIRE_CHARGE) || stack.is(Items.TORCH) || stack.is(Items.SOUL_TORCH)){
            level.setBlock(pos, Blocks.FIRE.defaultBlockState(), UPDATE_ALL);
            player.playSound(SoundEvents.FLINTANDSTEEL_USE);
            return ItemInteractionResult.SUCCESS;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    /**
     * Update the provided state given the provided neighbor direction and neighbor state, returning a new state.
     * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately returns its solidified counterpart.
     * Note that this method should ideally consider only the specific direction passed in.
     */
    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return !state.canSurvive(level, currentPos)
                ? Blocks.AIR.defaultBlockState()
                : super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 500;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if(entity.isOnFire()){
            level.setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
        }
        else if (entity instanceof LivingEntity) {
            ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffectRegistry.FLAMMABLE, 600, 0));
        }
    }
}
