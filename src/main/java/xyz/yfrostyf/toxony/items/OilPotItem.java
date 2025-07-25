package xyz.yfrostyf.toxony.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.CollisionContext;
import xyz.yfrostyf.toxony.api.oils.ItemOil;
import xyz.yfrostyf.toxony.api.util.OilUtil;
import xyz.yfrostyf.toxony.blocks.OilPotBlock;
import xyz.yfrostyf.toxony.blocks.entities.OilPotBlockEntity;
import xyz.yfrostyf.toxony.registries.TagRegistry;

import javax.annotation.Nullable;

public class OilPotItem extends Item {
    protected static final int USE_DURATION = 64;
    protected final ItemOil itemOil;
    protected final Holder<Block> oilPotBlock;

    public OilPotItem(Properties properties, ItemOil itemOil, Holder<Block> oilPotBlock) {
        super(properties.stacksTo(1));
        this.itemOil = itemOil;
        this.oilPotBlock = oilPotBlock;
    }

    public ItemOil getItemOil(){
        return itemOil;
    }

    public Holder<Block> getOilPotBlock() {
        return oilPotBlock;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if(!(entity instanceof Player player))return stack;
        ItemStack applied = player.getMainHandItem().is(this) ? player.getOffhandItem() : player.getMainHandItem();
        int damage = stack.getDamageValue();

        if (!applied.is(getItemOil().getOil().getSupportedItems())) return stack;
        if (getItemOil().isEmpty()) return stack;

        if (damage < stack.getMaxDamage()) {
            OilUtil.updateOil(applied, getItemOil());
            if(stack.isDamageableItem()){
                stack.setDamageValue(damage + 1);
            }
        }
        return stack;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack oilPot = player.getItemInHand(hand);
        ItemStack otherStack = hand == InteractionHand.MAIN_HAND ? player.getOffhandItem() : player.getMainHandItem();

        if(otherStack.is(TagRegistry.CAN_REFILL_OIL) && oilPot.getDamageValue() > 0){
            otherStack.consume(1, player);
            oilPot.setDamageValue(0);
        }
        else if(otherStack.is(getItemOil().getOil().getSupportedItems())
                && !getItemOil().isEmpty()
                && this.getDamage(oilPot) < this.getMaxDamage(oilPot)){
            return ItemUtils.startUsingInstantly(level, player, hand);
        }
        return InteractionResultHolder.pass(oilPot);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        InteractionResult result = this.place(new BlockPlaceContext(context));
        if (!result.consumesAction()) {
            result = this.use(context.getLevel(), context.getPlayer(), context.getHand()).getResult();
            return result == InteractionResult.CONSUME ? InteractionResult.CONSUME_PARTIAL : result;
        }
        return result;
    }

    public InteractionResult place(BlockPlaceContext context) {
        if(!context.getPlayer().isCrouching()) return InteractionResult.FAIL;

        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack placingStack = context.getItemInHand();
        BlockState levelBlockState = level.getBlockState(blockpos);

        Block block = this.getOilPotBlock().value();
        BlockState newBlockState = block.defaultBlockState()
                .setValue(OilPotBlock.OIL_LEFT,
                        OilPotBlock.getBlockDamage(
                                placingStack.getMaxDamage(),
                                placingStack.getDamageValue()
                        )
                );

        if(!(placingStack.getItem() instanceof OilPotItem)) return InteractionResult.FAIL;
        if (!newBlockState.getBlock().isEnabled(context.getLevel().enabledFeatures())) return InteractionResult.FAIL;
        if (!context.canPlace()) return InteractionResult.FAIL;

        BlockState blockstate = this.getPlacementState(context, block);
        if (blockstate == null) return InteractionResult.FAIL;
        if (!this.placeBlock(context, blockstate)) return InteractionResult.FAIL;

        if (!context.getLevel().setBlock(blockpos, newBlockState, OilPotBlock.UPDATE_ALL_IMMEDIATE)) return InteractionResult.FAIL;

        if(level.getBlockEntity(blockpos) instanceof OilPotBlockEntity blockEntity){
            blockEntity.setMaxDamage(placingStack.getMaxDamage());
            blockEntity.setDamage(placingStack.getDamageValue());
        }

        if (levelBlockState.is(newBlockState.getBlock())) {
            levelBlockState = this.updateBlockStateFromTag(blockpos, level, placingStack, levelBlockState);
            updateCustomBlockEntityTag(level, player, blockpos, placingStack);
            updateBlockEntityComponents(level, blockpos, placingStack);
            levelBlockState.getBlock().setPlacedBy(level, blockpos, levelBlockState, player, placingStack);
        }

        SoundType soundtype = levelBlockState.getSoundType(level, blockpos, context.getPlayer());
        level.playSound(
                player,
                blockpos,
                SoundEvents.DECORATED_POT_PLACE,
                SoundSource.BLOCKS,
                (soundtype.getVolume() + 1.0F) / 2.0F,
                soundtype.getPitch() * 0.8F
        );
        level.gameEvent(GameEvent.BLOCK_PLACE, blockpos, GameEvent.Context.of(player, levelBlockState));
        placingStack.consume(1, player);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    protected BlockState getPlacementState(BlockPlaceContext context, Block block) {
        BlockState blockstate = block.getStateForPlacement(context);
        return blockstate != null && this.canPlace(context, blockstate) ? blockstate : null;
    }

    protected boolean canPlace(BlockPlaceContext context, BlockState state) {
        Player player = context.getPlayer();
        CollisionContext collisioncontext = player == null ? CollisionContext.empty() : CollisionContext.of(player);
        return (state.canSurvive(context.getLevel(), context.getClickedPos()))
                && context.getLevel().isUnobstructed(state, context.getClickedPos(), collisioncontext);
    }

    protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
        return context.getLevel().setBlock(context.getClickedPos(), state, 11);
    }

    private static void updateBlockEntityComponents(Level level, BlockPos poa, ItemStack stack) {
        BlockEntity blockentity = level.getBlockEntity(poa);
        if (blockentity != null) {
            blockentity.applyComponentsFromItemStack(stack);
            blockentity.setChanged();
        }
    }

    public static boolean updateCustomBlockEntityTag(Level level, @Nullable Player player, BlockPos pos, ItemStack stack) {
        MinecraftServer minecraftserver = level.getServer();
        if (minecraftserver == null) {
            return false;
        } else {
            CustomData customdata = stack.getOrDefault(DataComponents.BLOCK_ENTITY_DATA, CustomData.EMPTY);
            if (!customdata.isEmpty()) {
                BlockEntity blockentity = level.getBlockEntity(pos);
                if (blockentity != null) {
                    if (level.isClientSide || !blockentity.onlyOpCanSetNbt() || player != null && player.canUseGameMasterBlocks()) {
                        return customdata.loadInto(blockentity, level.registryAccess());
                    }

                    return false;
                }
            }
            return false;
        }
    }

    private BlockState updateBlockStateFromTag(BlockPos pos, Level level, ItemStack stack, BlockState state) {
        BlockItemStateProperties blockitemstateproperties = stack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY);
        if (blockitemstateproperties.isEmpty()) {
            return state;
        } else {
            BlockState blockstate = blockitemstateproperties.apply(state);
            if (blockstate != state) {
                level.setBlock(pos, blockstate, OilPotBlock.UPDATE_CLIENTS);
            }
            return blockstate;
        }
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return USE_DURATION;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public SoundEvent getEatingSound() {
        return SoundEvents.HONEY_BLOCK_PLACE;
    }
}
