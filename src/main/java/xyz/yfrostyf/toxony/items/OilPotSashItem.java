package xyz.yfrostyf.toxony.items;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import xyz.yfrostyf.toxony.entities.item.ThrownOilPot;
import xyz.yfrostyf.toxony.registries.DataComponentsRegistry;
import xyz.yfrostyf.toxony.registries.ItemRegistry;

public class OilPotSashItem extends Item implements ProjectileItem {
    public OilPotSashItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand thisHand) {
        ItemStack thisStack = player.getItemInHand(thisHand);
        InteractionHand otherHand = thisHand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        ItemStack otherStack = player.getItemInHand(otherHand);

        if(otherStack.getItem() instanceof OilPotItem oilPotItem && !otherStack.is(this)){
            thisStack.set(DataComponentsRegistry.OIL, oilPotItem.getItemOil());
            thisStack.setDamageValue(0);
            otherStack.consume(1, player);
            player.playSound(SoundEvents.BOTTLE_FILL);
            return InteractionResultHolder.sidedSuccess(thisStack, level.isClientSide());
        }
        else if(thisStack.has(DataComponentsRegistry.OIL) && otherStack.is(ItemRegistry.OIL_BASE)){
            thisStack.setDamageValue(0);
            player.playSound(SoundEvents.BOTTLE_FILL);
            otherStack.consume(1, player);
            return InteractionResultHolder.sidedSuccess(thisStack, level.isClientSide());
        }
        else if(thisStack.has(DataComponentsRegistry.OIL) && thisStack.getDamageValue() < thisStack.getMaxDamage()){
            if (!level.isClientSide) {
                ThrownOilPot thrownOilPot = new ThrownOilPot(level, player, thisStack);
                thrownOilPot.setItem(thisStack);
                thrownOilPot.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 0.5F, 1.0F);
                level.addFreshEntity(thrownOilPot);
            }

            player.getCooldowns().addCooldown(thisStack.getItem(), 30);
            if(!player.hasInfiniteMaterials()){
                thisStack.setDamageValue(thisStack.getDamageValue() + 1);
            }
            player.playSound(SoundEvents.SPLASH_POTION_THROW);
            return InteractionResultHolder.sidedSuccess(thisStack, level.isClientSide());
        }

        return InteractionResultHolder.pass(thisStack);
    }

    @Override
    public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
        ThrownOilPot thrownOilPot = new ThrownOilPot(level, stack, pos.x(), pos.y(), pos.z());
        thrownOilPot.setItem(stack);
        return thrownOilPot;
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        if(stack.has(DataComponentsRegistry.OIL)){
            return this.getDescriptionId() + ".effect." + stack.get(DataComponentsRegistry.OIL).oil().getKey().location().getPath();
        }
        return super.getDescriptionId();
    }
}
