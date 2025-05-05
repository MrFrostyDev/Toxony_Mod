package xyz.yfrostyf.toxony.items;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import xyz.yfrostyf.toxony.api.util.VialUtil;
import xyz.yfrostyf.toxony.registries.DataComponentsRegistry;
import xyz.yfrostyf.toxony.registries.ItemRegistry;

public class NeedleItem extends Item {
    public NeedleItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack thisStack = player.getItemInHand(hand);
        ItemStack otherStack = hand == InteractionHand.MAIN_HAND ? player.getOffhandItem() : player.getMainHandItem();

        if(otherStack.is(this)) return InteractionResultHolder.pass(player.getItemInHand(hand));

        // Add affinity solution if its in the other hand
        if(otherStack.is(ItemRegistry.AFFINITY_SOLUTION) && otherStack.has(DataComponentsRegistry.AFFINITY_STORED_ITEM)){
            Holder<Item> storedHolder = otherStack.get(DataComponentsRegistry.AFFINITY_STORED_ITEM);
            otherStack.consume(1, player);
            thisStack.set(DataComponentsRegistry.AFFINITY_STORED_ITEM, storedHolder);
            player.getInventory().add(new ItemStack(ItemRegistry.GLASS_VIAL.get()));
            player.playSound(SoundEvents.BREWING_STAND_BREW, 1.0F, 0.8F);
            return InteractionResultHolder.sidedSuccess(
                    ItemUtils.createFilledResult(
                            Items.AIR.getDefaultInstance(),
                            player,
                            VialUtil.createAffinityStoredItemStack(ItemRegistry.TOX_NEEDLE.get(), storedHolder)
                    ), level.isClientSide()
            );
        }
        // Add potion contents if its in the other hand
        else if(otherStack.has(DataComponents.POTION_CONTENTS)){
            PotionContents potionContents = otherStack.get(DataComponents.POTION_CONTENTS);
            if(otherStack.is(ItemRegistry.TOX_VIAL)){
                player.getInventory().add(new ItemStack(ItemRegistry.GLASS_VIAL.get()));
            }
            else if(otherStack.is(Items.POTION) || otherStack.is(Items.LINGERING_POTION) | otherStack.is(Items.SPLASH_POTION)){
                player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
            }
            otherStack.consume(1, player);
            player.playSound(SoundEvents.BREWING_STAND_BREW, 1.0F, 0.8F);
            return InteractionResultHolder.sidedSuccess(
                    ItemUtils.createFilledResult(
                            Items.AIR.getDefaultInstance(),
                            player,
                            VialUtil.createPotionItemStack(ItemRegistry.TOX_NEEDLE.get(), potionContents.potion().get())
                    ), level.isClientSide()
            );
        }
        return InteractionResultHolder.pass(thisStack);
    }
}
