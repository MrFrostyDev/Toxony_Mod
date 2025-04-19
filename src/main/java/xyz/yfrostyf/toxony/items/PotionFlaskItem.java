package xyz.yfrostyf.toxony.items;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import xyz.yfrostyf.toxony.registries.ItemRegistry;

import java.util.List;

public class PotionFlaskItem extends Item {
    private static final int DRINK_DURATION = 48;
    private final float durationModifier;

    public PotionFlaskItem(Properties properties, float durationModifier) {
        super(properties);
        this.durationModifier = durationModifier;
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack itemstack = super.getDefaultInstance();
        itemstack.set(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        return itemstack;
    }

    /**
     * Called when the player finishes using this Item (E.g. finishes eating.). Not called when the player stops using the Item before the action is complete.
     */
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
        Player player = entityLiving instanceof Player ? (Player)entityLiving : null;

        if (!level.isClientSide) {
            PotionContents potioncontents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
            potioncontents.forEachEffect(effect -> {
                if (effect.getEffect().value().isInstantenous()) {
                    effect.getEffect().value().applyInstantenousEffect(player, player, entityLiving, effect.getAmplifier(), 1.0);
                } else {
                    entityLiving.addEffect(new MobEffectInstance(
                            effect.getEffect(), Mth.floor(effect.getDuration() * durationModifier),
                            effect.getAmplifier(), effect.isAmbient(),
                            effect.isVisible(), effect.showIcon()
                    ));
                }
            });
        }

        if (player != null) {
            player.awardStat(Stats.ITEM_USED.get(this));
        }

        if (player == null || !player.hasInfiniteMaterials()) {
            this.setDamage(stack, this.getDamage(stack)+1);
        }

        entityLiving.gameEvent(GameEvent.DRINK);
        return stack;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return DRINK_DURATION;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack otherStack = player.getMainHandItem().is(this) ? player.getOffhandItem() : player.getMainHandItem();
        ItemStack thisStack = player.getItemInHand(hand);

        if(!hasActualPotion(thisStack)){
            if(!otherStack.is(this) && hasActualPotion(otherStack)){
                if(otherStack.is(ItemRegistry.TOX_VIAL)){
                    player.getInventory().add(new ItemStack(ItemRegistry.GLASS_VIAL));
                }
                else if(otherStack.is(Items.POTION)){
                    player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
                }
                level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
                thisStack.set(DataComponents.POTION_CONTENTS, otherStack.get(DataComponents.POTION_CONTENTS));
                otherStack.consume(1, player);
                this.setDamage(thisStack, 0);
                return InteractionResultHolder.consume(thisStack);
            }
        }
        else if(otherStack.is(ItemRegistry.TOXIC_FORMULA) && thisStack.getDamageValue() > 0){
            level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
            thisStack.setDamageValue(0);
            otherStack.consume(1, player);
            player.getInventory().add(new ItemStack(ItemRegistry.GLASS_VIAL, 1));
            return InteractionResultHolder.consume(thisStack);
        }
        else if(thisStack.getDamageValue() < thisStack.getMaxDamage()){
            return ItemUtils.startUsingInstantly(level, player, hand);
        }
        return InteractionResultHolder.pass(thisStack);
    }

    private static boolean hasActualPotion(ItemStack stack){
        PotionContents contents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        return contents.potion().isPresent()
                && contents != PotionContents.EMPTY
                && contents.potion().get() != Potions.AWKWARD
                && contents.potion().get() != Potions.MUNDANE
                && contents.potion().get() != Potions.WATER;
    }

    /**
     *  Used for PotionFlaskItems. Checks if stack actually contains a potion (not water, mundane, etc.) and if it's full, partial or empty.
     *  If it's empty return 0.0F. If it's used at least once return 0.5F. If it's full and never used return 1.0F.
     */
    public static float isFull(ItemStack stack){
        if (!hasActualPotion(stack) || stack.getDamageValue() >= stack.getMaxDamage()) return 0.0F;
        if (stack.isDamaged()) return 0.5F;
        return 1.0F;
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
            potioncontents.addPotionTooltip(tooltipComponents::add, this.durationModifier, context.tickRate());
        }
    }
}
