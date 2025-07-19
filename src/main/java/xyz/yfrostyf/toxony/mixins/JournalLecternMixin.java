package xyz.yfrostyf.toxony.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.yfrostyf.toxony.network.ServerStartLostJournal;
import xyz.yfrostyf.toxony.registries.ItemRegistry;

import static net.minecraft.world.level.block.LecternBlock.HAS_BOOK;

@Mixin(targets = "net.minecraft.world.level.block.LecternBlock")
public abstract class JournalLecternMixin {

    @Inject(method = "useWithoutItem", at = @At("HEAD"))
    protected void useWithoutItemForJournal(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (state.getValue(HAS_BOOK)) {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof LecternBlockEntity) {
                ItemStack book = ((LecternBlockEntity)blockentity).getBook();
                if(book.is(ItemRegistry.LOST_JOURNAL)){
                    if (!level.isClientSide) {
                        PacketDistributor.sendToPlayer((ServerPlayer)player, new ServerStartLostJournal());
                    }
                    cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
                }
            }
        }
    }
}
