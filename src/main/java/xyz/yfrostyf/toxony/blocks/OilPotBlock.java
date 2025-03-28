package xyz.yfrostyf.toxony.blocks;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import xyz.yfrostyf.toxony.api.oils.ItemOil;
import xyz.yfrostyf.toxony.api.util.OilUtil;
import xyz.yfrostyf.toxony.blocks.entities.OilPotBlockEntity;
import xyz.yfrostyf.toxony.items.OilPotItem;
import xyz.yfrostyf.toxony.registries.DataComponentsRegistry;
import xyz.yfrostyf.toxony.registries.ItemRegistry;

import java.util.Optional;
import java.util.function.Supplier;

public class OilPotBlock extends Block implements EntityBlock {
    public static final MapCodec<OilPotBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    propertiesCodec(),
                    BuiltInRegistries.ITEM.holderByNameCodec().fieldOf("oilpot_item").forGetter(OilPotBlock::getOilPotItem)
            ).apply(instance, OilPotBlock::new)
    );

    protected static final VoxelShape SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 6.0, 11.0);
    public static final IntegerProperty OIL_LEFT = IntegerProperty.create("oil", 0, 3);

    protected final Supplier<Holder<Item>> oilPotItem;

    public OilPotBlock(Properties properties, Supplier<Holder<Item>> oilPotItem) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(OIL_LEFT, 0));
        this.oilPotItem = oilPotItem;
    }

    public OilPotBlock(Properties properties, Holder<Item> oilPotItem) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(OIL_LEFT, 0));
        this.oilPotItem = () -> oilPotItem;
    }

    @Override
    public MapCodec<OilPotBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(OIL_LEFT);
    }

    public Holder<Item> getOilPotItem() {
        return this.oilPotItem.get();
    }

    public ItemOil getOil(){
        if(this.getOilPotItem().value() instanceof OilPotItem item){
            return item.getItemOil();
        }
        return ItemOil.EMPTY;
    }

    public static int getBlockDamage(int max, int value){
        return Mth.ceil(3 * Math.min((float)(max - value) / (float)max, 1.0F));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(!(level.getBlockEntity(pos) instanceof OilPotBlockEntity blockEntity) || !(state.getBlock() instanceof OilPotBlock oilPotBlock) || stack.isEmpty())
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        int damage = blockEntity.getDamage();
        int maxDamage = blockEntity.getMaxDamage();
        boolean hasNoOil = damage >= maxDamage;
        ItemOil itemOil = oilPotBlock.getOil();

        boolean itemHasFullOil = itemOil != null && stack.has(DataComponentsRegistry.OIL_USES) && stack.getOrDefault(DataComponentsRegistry.OIL_USES, 0) <= 0;
        boolean canAddOilTool = !itemOil.isEmpty() && stack.is(itemOil.getOil().getSupportedItems()) && !hasNoOil && !itemHasFullOil;
        boolean canAddOilBolt = !itemOil.isEmpty() && stack.is(ItemRegistry.BOLT) && !hasNoOil;
        boolean canAddOilBase = stack.is(ItemRegistry.OIL_BASE) && damage > 0;

        if(level.isClientSide()){
            if(hasNoOil && !stack.is(ItemRegistry.OIL_BASE)){
                Minecraft.getInstance().gui.setOverlayMessage(
                        Component.translatable("message.toxony.oilpot.empty").withStyle(ChatFormatting.WHITE),
                        false
                );
                return ItemInteractionResult.FAIL;
            }
            if(!canAddOilTool && stack.is(itemOil.getOil().getSupportedItems())){
                Minecraft.getInstance().gui.setOverlayMessage(
                        Component.translatable("message.toxony.oilpot.already_oiled").withStyle(ChatFormatting.WHITE),
                        false
                );
                return ItemInteractionResult.FAIL;
            }
        }
        if(canAddOilTool){
            OilUtil.updateOil(stack, itemOil);
            BlockState newBlockState = state.setValue(OilPotBlock.OIL_LEFT, OilPotBlock.getBlockDamage(maxDamage, damage + 1));

            level.setBlock(pos, newBlockState, OilPotBlock.UPDATE_ALL_IMMEDIATE);
            blockEntity.setDamage(damage + 1);

            level.playSound(player, pos,
                    SoundEvents.HONEY_BLOCK_PLACE, SoundSource.BLOCKS,
                    1.0F, 0.8F
            );

            return ItemInteractionResult.SUCCESS;
        }
        else if(canAddOilBolt){
            Optional<Holder.Reference<Item>> optionalHolder = OilUtil.getBoltByOilItem(oilPotBlock.getOil(), level);
            if(optionalHolder.isPresent()){
                ItemStack boltStack = new ItemStack(optionalHolder.get(), stack.getCount());
                player.setItemInHand(hand, boltStack);

                level.playSound(player, pos,
                        SoundEvents.HONEY_BLOCK_PLACE, SoundSource.BLOCKS,
                        1.0F, 0.8F
                );
            }
        }
        else if(canAddOilBase){
            BlockState newBlockState = state.setValue(OilPotBlock.OIL_LEFT, 3);

            level.setBlock(pos, newBlockState, OilPotBlock.UPDATE_ALL_IMMEDIATE);
            blockEntity.setDamage(0);

            stack.consume(1, player);

            level.playSound(player, pos,
                    SoundEvents.HONEY_BLOCK_PLACE, SoundSource.BLOCKS,
                    1.0F, 0.8F
            );
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(level.isClientSide())return;
        if(!(level.getBlockEntity(pos) instanceof OilPotBlockEntity blockEntity))return;
        if(!(state.getBlock() instanceof OilPotBlock block))return;
        if (state.is(newState.getBlock()))return;

        ItemStack stack = new ItemStack(block.getOilPotItem());
        stack.setDamageValue(blockEntity.getDamage());
        popResource(level, pos, stack);

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new OilPotBlockEntity(pos, state);
    }
}
