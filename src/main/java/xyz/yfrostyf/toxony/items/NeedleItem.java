package xyz.yfrostyf.toxony.items;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import xyz.yfrostyf.toxony.api.tox.ToxData;
import xyz.yfrostyf.toxony.damages.NeedleDamageSource;
import xyz.yfrostyf.toxony.registries.DataAttachmentRegistry;
import xyz.yfrostyf.toxony.registries.DataComponentsRegistry;
import xyz.yfrostyf.toxony.registries.ItemRegistry;

import java.util.List;

public class NeedleItem extends Item {
    private final float HEALTH_THRESHOLD_PERCENT = 0.1F;

    public NeedleItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack otherStack = player.getMainHandItem().is(this) ? player.getOffhandItem() : player.getMainHandItem();
        ItemStack thisStack = player.getItemInHand(usedHand);

        if(otherStack.is(this)) return InteractionResultHolder.pass(player.getItemInHand(usedHand));

        // Remove contents of needle when crouched
        if(thisStack.has(DataComponentsRegistry.NEEDLE_STORED_ITEM) || thisStack.has(DataComponents.POTION_CONTENTS)){
            if(player.isCrouching()){
                if(thisStack.has(DataComponentsRegistry.NEEDLE_STORED_ITEM)){
                    thisStack.remove(DataComponentsRegistry.NEEDLE_STORED_ITEM);
                }
                if(thisStack.has(DataComponents.POTION_CONTENTS)){
                    thisStack.remove(DataComponents.POTION_CONTENTS);
                }
                player.playSound(SoundEvents.GENERIC_BURN, 0.5F, 0.5F);
                return InteractionResultHolder.sidedSuccess(thisStack, level.isClientSide());
            }
        }

        // Add affinity solution if its in the other hand
        if(otherStack.is(ItemRegistry.AFFINITY_SOLUTION) && otherStack.has(DataComponentsRegistry.NEEDLE_STORED_ITEM)){
            Holder<Item> storedHolder = otherStack.get(DataComponentsRegistry.NEEDLE_STORED_ITEM);
            otherStack.consume(1, player);
            thisStack.set(DataComponentsRegistry.NEEDLE_STORED_ITEM, storedHolder);
            player.playSound(SoundEvents.BREWING_STAND_BREW, 1.0F, 0.8F);
            return InteractionResultHolder.sidedSuccess(thisStack, level.isClientSide());
        }
        // Add potion contents if its in the other hand
        else if(otherStack.has(DataComponents.POTION_CONTENTS)){
            PotionContents potionContents = otherStack.get(DataComponents.POTION_CONTENTS);
            thisStack.set(DataComponents.POTION_CONTENTS, potionContents);
            otherStack.remove(DataComponents.POTION_CONTENTS);
            player.playSound(SoundEvents.BREWING_STAND_BREW, 1.0F, 0.8F);
            return InteractionResultHolder.sidedSuccess(thisStack, level.isClientSide());
        }
        return InteractionResultHolder.pass(player.getItemInHand(usedHand));
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity targetEntity, InteractionHand usedHand) {
        float targetHealth = targetEntity.getHealth();
        float targetMaxHealth = targetEntity.getMaxHealth();
        ToxData plyToxData = player.getData(DataAttachmentRegistry.TOX_DATA.get());

        if(stack.has(DataComponentsRegistry.NEEDLE_STORED_ITEM)){
            if (targetHealth > targetMaxHealth * HEALTH_THRESHOLD_PERCENT) {
                if (player.level().isClientSide()) {
                    Minecraft.getInstance().gui.setOverlayMessage(Component.translatable("message.toxony.needle.fail"), false);
                }
                return InteractionResult.FAIL;
            }
            plyToxData.addKnownIngredients(new ItemStack(stack.get(DataComponentsRegistry.NEEDLE_STORED_ITEM).value()), 10);
            targetEntity.hurt(new NeedleDamageSource(player.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(DamageTypes.PLAYER_ATTACK), player), 8.0F);

            stack.remove(DataComponentsRegistry.NEEDLE_STORED_ITEM);
            player.playSound(SoundEvents.BEE_STING, 1.0F, 0.5F);

            return InteractionResult.sidedSuccess(targetEntity.level().isClientSide());
        }
        else if(stack.has(DataComponents.POTION_CONTENTS)){
            PotionContents potionContents = stack.get(DataComponents.POTION_CONTENTS);
            if( potionContents.hasEffects()){
                potionContents.forEachEffect(effect -> {
                    if (effect.getEffect().value().isInstantenous()) {
                        effect.getEffect().value().applyInstantenousEffect(player, player, targetEntity, effect.getAmplifier(), 1.0);
                    } else {
                        targetEntity.addEffect(effect);
                    }
                });
            }
            targetEntity.hurt(new NeedleDamageSource(player.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(DamageTypes.PLAYER_ATTACK), player), 2.0F);

            stack.remove(DataComponents.POTION_CONTENTS);
            player.playSound(SoundEvents.BEE_STING, 1.0F, 0.5F);

            return InteractionResult.sidedSuccess(targetEntity.level().isClientSide());
        }

        return InteractionResult.PASS;
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have different names based on their damage or NBT.
     */
    @Override
    public String getDescriptionId(ItemStack stack) {
        return Potion.getName(stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).potion(), this.getDescriptionId() + ".effect.");
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        PotionContents potioncontents = stack.get(DataComponents.POTION_CONTENTS);
        if (potioncontents != null) {
            potioncontents.addPotionTooltip(tooltipComponents::add, 1.0F, context.tickRate());
        }
    }
}
