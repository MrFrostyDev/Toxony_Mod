package xyz.yfrostyf.toxony.items;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.level.Level;
import xyz.yfrostyf.toxony.registries.ItemRegistry;
import xyz.yfrostyf.toxony.registries.TagRegistry;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BoltCartridgeItem extends Item {
    public static final int CAPACITY = 3;

    public BoltCartridgeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        if(player.isCrouching()){
            ChargedProjectiles projectiles = stack.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
            if (!projectiles.isEmpty()) {
                stack.remove(DataComponents.CHARGED_PROJECTILES);
                player.getCooldowns().addCooldown(this, 2);
                for(ItemStack projectileStack : projectiles.getItems()){
                    player.getInventory().add(projectileStack.copyWithCount(stack.getCount()));
                }
                player.playSound(SoundEvents.CROSSBOW_LOADING_END.value(), 1.0F, 1.2F);
                return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
            }
            else if(projectiles.isEmpty() && hasEnoughBolts(CAPACITY, player)){
                ItemStack newStack = new ItemStack(ItemRegistry.BOLT_CARTRIDGE, 1);
                newStack.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.of(getUniqueBolts(CAPACITY, player)));
                player.getInventory().add(newStack);
                stack.consume(1, player);
                player.getCooldowns().addCooldown(this, 2);
                player.playSound(SoundEvents.CROSSBOW_LOADING_END.value(), 1.0F, 0.8F);
                return InteractionResultHolder.sidedSuccess(stack.isEmpty() ? ItemStack.EMPTY : stack, level.isClientSide());
            }
            else if(level.isClientSide()) {
                Minecraft.getInstance().gui.setOverlayMessage(Component.translatable("message.toxony.bolt_cartridge.fail"), false);
                return InteractionResultHolder.fail(stack);
            }
        }
        return InteractionResultHolder.pass(stack);
    }

    public static List<ItemStack> getUniqueBolts(int amount, Player player){
        List<ItemStack> uniqueBolts = new LinkedList<>();
        List<Integer> searchedSlots = new ArrayList<>(amount);

        int i = 0;
        // Check inventory if they have a bolt, then save the slot number.
        for (int j=0; j < player.getInventory().getContainerSize(); j++) {
            ItemStack stack = player.getInventory().getItem(j);

            if(i >= amount) break;
            else if (stack.is(TagRegistry.BOLTS)) {
                uniqueBolts.add(stack.copyWithCount(1));
                stack.consume(1, player);
                searchedSlots.add(j);
                i++;
            }
        }
        // Later if we haven't filled uniqueBolts yet and hit the end of the inventory,
        // reuse the already searched slots to add into the spaces remaining.
        while (player.getInventory().contains(TagRegistry.BOLTS) && i < amount) {
            for(int slot : searchedSlots){
                ItemStack stack = player.getInventory().getItem(slot);
                if(i >= amount) break;
                if (stack.is(TagRegistry.BOLTS)) {
                    uniqueBolts.add(stack.copyWithCount(1));
                    stack.consume(1, player);
                    i++;
                }
            }
        }

        return uniqueBolts;
    }

    public static boolean hasEnoughBolts(int amount, Player player){
        int count = 0;
        List<Item> itemList = BuiltInRegistries.ITEM.getOrCreateTag(TagRegistry.BOLTS).stream().map(Holder::value).toList();
        for(Item item : itemList){
            count += player.getInventory().countItem(item);
            if(count >= amount){
                return true;
            }
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        ChargedProjectiles chargedprojectiles = stack.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
        if (!chargedprojectiles.isEmpty() && isLoaded(stack)) {
            MutableComponent component = Component.translatable("item.toxony.cyclebow.loaded")
                    .withStyle(ChatFormatting.GRAY);
            tooltipComponents.add(component);
            for(ItemStack itemstack : chargedprojectiles.getItems()){
                MutableComponent componentItem = Component.literal(itemstack.getDisplayName().getString())
                        .withStyle(ChatFormatting.DARK_GRAY);
                tooltipComponents.add(componentItem);
            }
        }
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        if(!isLoaded(stack))return this.getDescriptionId() + ".empty";
        else return super.getDescriptionId();
    }

    public static boolean isLoaded(ItemStack stack) {
        ChargedProjectiles chargedprojectiles = stack.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
        return !chargedprojectiles.isEmpty();
    }
}
