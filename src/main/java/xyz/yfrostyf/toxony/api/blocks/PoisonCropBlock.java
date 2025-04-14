package xyz.yfrostyf.toxony.api.blocks;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import xyz.yfrostyf.toxony.api.affinity.Affinity;
import xyz.yfrostyf.toxony.api.affinity.AffinityBlockPair;
import xyz.yfrostyf.toxony.api.tox.ToxData;
import xyz.yfrostyf.toxony.api.util.AffinityUtil;
import xyz.yfrostyf.toxony.blocks.PoisonFarmBlock;
import xyz.yfrostyf.toxony.registries.DataAttachmentRegistry;
import xyz.yfrostyf.toxony.registries.DataComponentsRegistry;
import xyz.yfrostyf.toxony.registries.TagRegistry;

import java.util.*;
import java.util.function.Supplier;

public class PoisonCropBlock extends BushBlock implements BonemealableBlock {
    public static final MapCodec<PoisonCropBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> // Given an instance
            instance.group(
                    propertiesCodec(),
                    BuiltInRegistries.ITEM.holderByNameCodec().fieldOf("grown_item").forGetter(PoisonCropBlock::getGrownItem),
                    MobEffect.CODEC.listOf().fieldOf("effects").forGetter(PoisonCropBlock::getContactEffects),
                    AffinityBlockPair.CODEC.optionalFieldOf("evolved_plant").forGetter(PoisonCropBlock::getEvolvedPlant)
            ).apply(instance, PoisonCropBlock::new)
    );

    public static final int MAX_AGE = 5;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_5;
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(3.0, 0.0, 3.0, 13.0, 2.0, 13.0),
            Block.box(3.0, 0.0, 3.0, 13.0, 4.0, 13.0),
            Block.box(3.0, 0.0, 3.0, 13.0, 6.0, 13.0),
            Block.box(3.0, 0.0, 3.0, 13.0, 8.0, 13.0),
            Block.box(3.0, 0.0, 3.0, 13.0, 10.0, 13.0),
            Block.box(3.0, 0.0, 3.0, 13.0, 12.0, 13.0)
    };

    protected final Supplier<Holder<Item>> grownItem;
    protected final List<Holder<MobEffect>> contactEffects;
    protected final Supplier<Optional<AffinityBlockPair>> evolvedPlant;

    public PoisonCropBlock(Properties properties,
                           Holder<Item> grownItem,
                           List<Holder<MobEffect>> contactEffects,
                           Optional<AffinityBlockPair> evolvedPlant){
        super(properties);
        this.grownItem = () -> grownItem;
        this.contactEffects = contactEffects;
        this.evolvedPlant = () -> evolvedPlant;
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, Integer.valueOf(0)));
    }

    public PoisonCropBlock(Properties properties,
                           Supplier<Holder<Item>> grownItem,
                           List<Holder<MobEffect>> contactEffects,
                           Supplier<Optional<AffinityBlockPair>> evolvedPlant){
        super(properties);
        this.grownItem = grownItem;
        this.contactEffects = contactEffects;
        this.evolvedPlant = evolvedPlant;
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, Integer.valueOf(0)));
    }

    @Override
    public MapCodec<? extends PoisonCropBlock> codec() {
        return CODEC;
    }

    public List<Holder<MobEffect>> getContactEffects() {
        return contactEffects;
    }

    public Optional<AffinityBlockPair> getEvolvedPlant() {
        return evolvedPlant.get();
    }

    public Holder<Item> getGrownItem() {
        return grownItem.get();
    }

    protected ItemInteractionResult tryTransformEvolved(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player){
        if(!stack.has(DataComponentsRegistry.POSSIBLE_AFFINITIES)
                || stack.getItem().equals(this.grownItem.get().value())) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        ToxData plyToxData = player.getData(DataAttachmentRegistry.TOX_DATA);
        if(!plyToxData.knowsIngredient(stack) || !plyToxData.knowsIngredient(this.getGrownItem())){
            if(!level.isClientSide()){
                return ItemInteractionResult.FAIL;
            }
            else{
                Minecraft.getInstance().gui.setOverlayMessage(Component.translatable("message.toxony.graft.unknown"), false);
                return ItemInteractionResult.CONSUME;
            }
        }

        if(this.getEvolvedPlant().isPresent()) {
            Holder<Affinity> affinityHolder = this.getEvolvedPlant().get().affinity();
            Holder<Block> blockHolder = this.getEvolvedPlant().get().block();
            if (blockHolder.value() instanceof PoisonCropBlock poisonCropBlock) {
                if (affinityHolder.value().equals(AffinityUtil.readAffinityFromIngredientMap(stack, level))) {
                    if(!level.isClientSide()) {
                        stack.consume(1, player);
                        level.setBlock(pos, poisonCropBlock.defaultBlockState(), Block.UPDATE_ALL);
                    }
                    else{
                        Minecraft.getInstance().gui.setOverlayMessage(Component.translatable("message.toxony.graft.success"), false);
                    }
                    return ItemInteractionResult.SUCCESS;
                }
                else {
                    if(!level.isClientSide()) {
                        stack.consume(1, player);
                        level.destroyBlock(pos, false);
                        return ItemInteractionResult.FAIL;
                    }
                    else {
                        Minecraft.getInstance().gui.setOverlayMessage(Component.translatable("message.toxony.graft.fail"), false);
                        return ItemInteractionResult.CONSUME;
                    }
                }
            }
            else throw new ClassCastException("This block contains incorrect data within its possible evolved blocks.");
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    // |===========================================================================|
    // |============================= Block Behaviour =============================|
    // |===========================================================================|

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        int age = this.getAge(state);
        ItemStack graftStack = player.getMainHandItem().has(DataComponentsRegistry.POSSIBLE_AFFINITIES) ? player.getMainHandItem() : player.getOffhandItem();
        if (graftStack.has(DataComponentsRegistry.POSSIBLE_AFFINITIES) && age <= 1){
            return tryTransformEvolved(stack, state, level, pos, player);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    /**
     * Performs a random tick on a block.
     */
    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        if (level.getRawBrightness(pos, 0) >= 9) {
            int age = this.getAge(state);
            if (age < this.getMaxAge()) {
                float f = getGrowthSpeed(state, level, pos);
                if (net.neoforged.neoforge.common.CommonHooks.canCropGrow(level, pos, state, random.nextInt((int)(30.0F / f) + 1) == 0)) {
                    level.setBlock(pos, this.getStateForAge(age + 1), Block.UPDATE_CLIENTS);
                    net.neoforged.neoforge.common.CommonHooks.fireCropGrowPost(level, pos, state);
                }
            }
        }
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[this.getAge(state)];
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getBlock() instanceof PoisonFarmBlock;
    }

    protected IntegerProperty getAgeProperty() {
        return AGE;
    }

    public int getMaxAge() {
        return 5;
    }

    public int getAge(BlockState state) {
        return state.getValue(this.getAgeProperty());
    }

    public BlockState getStateForAge(int age) {
        return this.defaultBlockState().setValue(this.getAgeProperty(), Integer.valueOf(age));
    }

    public final boolean isMaxAge(BlockState state) {
        return this.getAge(state) >= this.getMaxAge();
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return !this.isMaxAge(state);
    }

    public void growCrops(Level level, BlockPos pos, BlockState state) {
        int inc = this.getBonemealAgeIncrease(level);
        int age = this.getAge(state) + inc;
        int maxAge = this.getMaxAge();
        if (age > maxAge) {
            age = maxAge;
        }
        level.setBlock(pos, this.getStateForAge(age), Block.UPDATE_CLIENTS);
    }

    protected static float getGrowthSpeed(BlockState blockState, BlockGetter blockGetter, BlockPos pos) {
        Block block = blockState.getBlock();
        float f = 1.0F;
        BlockPos blockpos = pos.below();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                float f1 = 0.0F;
                BlockState blockstate = blockGetter.getBlockState(blockpos.offset(i, 0, j));
                net.neoforged.neoforge.common.util.TriState soilDecision = blockstate.canSustainPlant(blockGetter, blockpos.offset(i, 0, j), Direction.UP, blockState);
                if (soilDecision.isDefault() ? blockstate.getBlock() instanceof net.minecraft.world.level.block.FarmBlock : soilDecision.isTrue()) {
                    f1 = 1.0F;
                    if (blockstate.isFertile(blockGetter, pos.offset(i, 0, j))) {
                        f1 = 3.0F;
                    }
                }

                if (i != 0 || j != 0) {
                    f1 /= 4.0F;
                }

                f += f1;
            }
        }

        BlockPos blockpos1 = pos.north();
        BlockPos blockpos2 = pos.south();
        BlockPos blockpos3 = pos.west();
        BlockPos blockpos4 = pos.east();
        boolean flag = blockGetter.getBlockState(blockpos3).is(block) || blockGetter.getBlockState(blockpos4).is(block);
        boolean flag1 = blockGetter.getBlockState(blockpos1).is(block) || blockGetter.getBlockState(blockpos2).is(block);
        if (flag && flag1) {
            f /= 2.0F;
        } else {
            boolean flag2 = blockGetter.getBlockState(blockpos3.north()).is(block)
                    || blockGetter.getBlockState(blockpos4.north()).is(block)
                    || blockGetter.getBlockState(blockpos4.south()).is(block)
                    || blockGetter.getBlockState(blockpos3.south()).is(block);
            if (flag2) {
                f /= 2.0F;
            }
        }
        return f;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState belowBlockState = level.getBlockState(blockpos);
        net.neoforged.neoforge.common.util.TriState soilDecision = belowBlockState.canSustainPlant(level, blockpos, Direction.UP, state);
        return soilDecision.isDefault() && belowBlockState.getBlock() instanceof PoisonFarmBlock;
    }

    public static boolean hasSufficientLight(LevelReader level, BlockPos pos) {
        return level.getRawBrightness(pos, 0) >= 8;
    }

    protected void effectOnContact(LivingEntity livingEntity, BlockState state){
        for(Holder<MobEffect> effect : contactEffects) {
            if (!isMaxAge(state)) {
                livingEntity.addEffect(new MobEffectInstance(effect, 300, 0));
            } else {
                livingEntity.addEffect(new MobEffectInstance(effect, 200, 1));
            }
        }
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof Ravager && net.neoforged.neoforge.event.EventHooks.canEntityGrief(level, entity)) {
            level.destroyBlock(pos, true, entity);
        }
        if (entity instanceof LivingEntity livingEntity) {
            if ((!level.isClientSide && entity.xOld != entity.getX() || entity.zOld != entity.getZ())) {
                double d0 = Math.abs(entity.getX() - entity.xOld);
                double d1 = Math.abs(entity.getZ() - entity.zOld);
                if (d0 >= 0.003F || d1 >= 0.003F) {
                    this.effectOnContact(livingEntity, state);
                }
            }
        }
        super.entityInside(state, level, pos, entity);
    }

    // |===========================================================================|
    // |=========================== Bonemeal Behaviour  ===========================|
    // |===========================================================================|

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return !this.isMaxAge(state);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    protected int getBonemealAgeIncrease(Level level) {
        return Mth.nextInt(level.random, 1, 1);
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        this.growCrops(level, pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }
}
