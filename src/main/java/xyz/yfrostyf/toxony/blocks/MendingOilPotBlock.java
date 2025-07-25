package xyz.yfrostyf.toxony.blocks;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import xyz.yfrostyf.toxony.blocks.entities.OilPotBlockEntity;
import xyz.yfrostyf.toxony.items.MendingOilPotItem;
import xyz.yfrostyf.toxony.registries.TagRegistry;

import java.util.function.Supplier;

public class MendingOilPotBlock extends OilPotBlock {
    public MendingOilPotBlock(Properties properties, Supplier<Holder<Item>> oilPotItem) {
        super(properties, oilPotItem);
    }

    public MendingOilPotBlock(Properties properties, Holder<Item> oilPotItem) {
        super(properties, oilPotItem);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(!(level.getBlockEntity(pos) instanceof OilPotBlockEntity blockEntity) || !(state.getBlock() instanceof MendingOilPotBlock oilPotBlock) || stack.isEmpty())
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        int damage = blockEntity.getDamage();
        int maxDamage = blockEntity.getMaxDamage();
        boolean hasNoOil = damage >= maxDamage;

        boolean isItemDamageable = stack.isDamaged() && stack.is(TagRegistry.OIL_REPAIRABLE) && !hasNoOil;
        boolean canAddOilBase = stack.is(TagRegistry.CAN_REFILL_OIL) && damage > 0;

        ItemInteractionResult interactionResult = ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if(level.isClientSide()){
            if(hasNoOil && !stack.is(TagRegistry.CAN_REFILL_OIL)){
                Minecraft.getInstance().gui.setOverlayMessage(
                        Component.translatable("message.toxony.oilpot.empty").withStyle(ChatFormatting.WHITE),
                        false
                );
                return ItemInteractionResult.FAIL;
            }
        }
        if(isItemDamageable){
            stack.setDamageValue(Math.max(0, stack.getDamageValue() - MendingOilPotItem.REPAIR_AMOUNT));
            BlockState newBlockState = state.setValue(MendingOilPotBlock.OIL_LEFT, MendingOilPotBlock.getBlockDamage(maxDamage, damage + 1));

            level.setBlock(pos, newBlockState, MendingOilPotBlock.UPDATE_ALL_IMMEDIATE);
            blockEntity.setDamage(damage + 1);

            level.playSound(player, pos,
                    SoundEvents.HONEY_BLOCK_PLACE, SoundSource.BLOCKS,
                    1.0F, 0.8F
            );

            interactionResult = ItemInteractionResult.SUCCESS;
        }
        else if(canAddOilBase){
            blockEntity.setDamage(0);
            stack.consume(1, player);

            BlockState newBlockState = state.setValue(MendingOilPotBlock.OIL_LEFT, 3);
            level.setBlock(pos, newBlockState, MendingOilPotBlock.UPDATE_ALL_IMMEDIATE);

            level.playSound(player, pos,
                    SoundEvents.HONEY_BLOCK_PLACE, SoundSource.BLOCKS,
                    1.0F, 0.8F
            );

            interactionResult = ItemInteractionResult.SUCCESS;
        }

        if(interactionResult.consumesAction()){
            level.sendBlockUpdated(pos, blockEntity.getBlockState(), blockEntity.getBlockState(), Block.UPDATE_ALL);
            blockEntity.setChanged();
        }

        return interactionResult;
    }
}
