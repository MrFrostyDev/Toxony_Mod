package xyz.yfrostyf.toxony.items;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.joml.Random;
import xyz.yfrostyf.toxony.registries.ItemRegistry;

import java.util.List;

public class AffinityFusionSubstance extends Item {
    public static Random RANDOM = new Random();

    public AffinityFusionSubstance(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        ItemStack otherStack = usedHand == InteractionHand.MAIN_HAND ? player.getOffhandItem() : player.getMainHandItem();

        if(otherStack.is(ItemRegistry.MAGNIFYING_GLASS)){
            List<Item> items = level.registryAccess().registryOrThrow(Registries.ITEM).stream()
                    .filter(i -> i instanceof AffinitySubstance).toList();

            ItemStack newStack = new ItemStack(items.get(RANDOM.nextInt(items.size())), 1);
            player.getInventory().add(newStack);
            stack.consume(1, player);
            player.getCooldowns().addCooldown(this, 5);
            player.playSound(SoundEvents.PLAYER_LEVELUP, 1.0F, 1.5F);
            return InteractionResultHolder.sidedSuccess(stack.isEmpty() ? ItemStack.EMPTY : stack, level.isClientSide());
        }
        else if(level.isClientSide()) {
            Minecraft.getInstance().gui.setOverlayMessage(Component.translatable("message.toxony.unknown_substance.fail"), false);
            return InteractionResultHolder.fail(stack);
        }
        return InteractionResultHolder.pass(stack);
    }
}
