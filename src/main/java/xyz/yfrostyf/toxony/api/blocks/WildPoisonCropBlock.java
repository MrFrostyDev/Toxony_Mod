package xyz.yfrostyf.toxony.api.blocks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import xyz.yfrostyf.toxony.api.affinity.Affinity;
import xyz.yfrostyf.toxony.api.util.AffinityUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class WildPoisonCropBlock extends BushBlock implements BonemealableBlock {
    public static final MapCodec<WildPoisonCropBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> // Given an instance
            instance.group(
                    propertiesCodec(),
                    MobEffect.CODEC.listOf().fieldOf("effects").forGetter(WildPoisonCropBlock::getContactEffects)
            ).apply(instance, WildPoisonCropBlock::new)
    );

    private static final float HURT_SPEED_THRESHOLD = 0.003F;
    public static final int MAX_AGE = 3;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    private static final VoxelShape MID_SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 8.0, 13.0);
    private static final VoxelShape FULL_GROWTH_SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);

    protected final List<Holder<MobEffect>> contactEffects;

    public WildPoisonCropBlock(Properties properties, List<Holder<MobEffect>> contactEffects) {
        super(properties);
        this.contactEffects = contactEffects;
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, Integer.valueOf(0)));
    }

    @Override
    public MapCodec<WildPoisonCropBlock> codec() {
        return CODEC;
    }

    public List<Holder<MobEffect>> getContactEffects() {
        return contactEffects;
    }
    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(AGE) < 3) {
            return MID_SHAPE;
        } else {
            return state.getValue(AGE) < 3 ? FULL_GROWTH_SHAPE : super.getShape(state, level, pos, context);
        }
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return state.getValue(AGE) < 3;
    }

    /**
     * Performs a random tick on a block.
     */
    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int i = state.getValue(AGE);
        if (i < 3 && level.getRawBrightness(pos.above(), 0) >= 9 && net.neoforged.neoforge.common.CommonHooks.canCropGrow(level, pos, state, random.nextInt(5) == 0)) {
            BlockState blockstate = state.setValue(AGE, Integer.valueOf(i + 1));
            level.setBlock(pos, blockstate, 2);
            net.neoforged.neoforge.common.CommonHooks.fireCropGrowPost(level, pos, state);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(blockstate));
        }
    }

    protected void effectOnContact(LivingEntity livingEntity, BlockState state){
        for(Holder<MobEffect> effect : contactEffects) {
            if (state.getValue(AGE) > 0 && state.getValue(AGE) < MAX_AGE) {
                livingEntity.addEffect(new MobEffectInstance(effect, 400, 0));
            } else {
                livingEntity.addEffect(new MobEffectInstance(effect, 300, 1));
            }
        }
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity livingEntity && entity.getType() != EntityType.BEE) {
            entity.makeStuckInBlock(state, new Vec3(0.8F, 0.75, 0.8F));
            if (!level.isClientSide && state.getValue(AGE) > 0 && (entity.xOld != entity.getX() || entity.zOld != entity.getZ())) {
                double d0 = Math.abs(entity.getX() - entity.xOld);
                double d1 = Math.abs(entity.getZ() - entity.zOld);
                if (d0 >= 0.003F || d1 >= 0.003F) {
                    effectOnContact(livingEntity, state);
                }
            }
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(
            ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult
    ) {
        return state.getValue(AGE) != 3 && stack.is(Items.BONE_MEAL)
                ? ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION
                : super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return state.getValue(AGE) < 3;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        int i = Math.min(3, state.getValue(AGE) + 1);
        level.setBlock(pos, state.setValue(AGE, Integer.valueOf(i)), 2);
    }
}
