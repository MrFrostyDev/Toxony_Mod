package xyz.yfrostyf.toxony.items;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.data.datagen.enchantments.effects.Impact;
import xyz.yfrostyf.toxony.entities.item.FlailBall;
import xyz.yfrostyf.toxony.registries.DataComponentsRegistry;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;


public class FlailItem extends Item {
    private static final int DEFAULT_USE_DURATION = 80;

    public FlailItem(Properties properties) {
        super(properties);
    }

    public static ItemAttributeModifiers createAttributes(float attackDamage, float attackSpeed) {
        return ItemAttributeModifiers.builder()
                .add(
                        Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(
                                BASE_ATTACK_DAMAGE_ID, attackDamage, AttributeModifier.Operation.ADD_VALUE
                        ),
                        EquipmentSlotGroup.MAINHAND
                )
                .add(
                        Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_ID, attackSpeed, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND
                )
                .build();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if(hand == InteractionHand.MAIN_HAND){
            player.startUsingItem(hand);
            player.resetAttackStrengthTicker();

            return InteractionResultHolder.consume(itemstack);
        }
        return InteractionResultHolder.pass(itemstack);
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if(livingEntity instanceof Player player && player.getOffhandItem().is(this) && player.isUsingItem()){
            player.getCooldowns().addCooldown(this, FlailBall.FLAIL_LIFETIME);
        }
        super.onUseTick(level, livingEntity, stack, remainingUseDuration);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int chargeRemaining) {
        if (livingEntity instanceof Player player) {
            level.playSound(
                    null,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    SoundEvents.TRIDENT_THROW,
                    SoundSource.NEUTRAL,
                    0.8F,
                    0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
            );

            if (!level.isClientSide()) {
                int useDuration = this.getUseDuration(stack, player);
                float newChargeRemaining = Math.max(chargeRemaining, 0);
                float chargeProgress = (float)(1.0 - newChargeRemaining / useDuration);
                double playerDamage = player.getAttribute(Attributes.ATTACK_DAMAGE) != null ? player.getAttribute(Attributes.ATTACK_DAMAGE).getValue() * chargeProgress : 1;

                level.addFreshEntity(new FlailBall(
                        player,
                        level,
                        stack,
                        Mth.floor(4 * chargeProgress),
                        (float)playerDamage,
                        chargeRemaining <= 0,
                        0.25F + getImpactBasedOnEnchant(stack)
                ));
            }

            stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
            player.getCooldowns().addCooldown(this, FlailBall.FLAIL_LIFETIME);
            player.gameEvent(GameEvent.ITEM_INTERACT_START);
        }
    }

    private static float getImpactBasedOnEnchant(ItemStack stack){
        AtomicInteger atomicValue = new AtomicInteger(0);

        EnchantmentHelper.runIterationOnItem(stack, (enchantmentHolder, enchantLevel) -> {
            // Acquire the Impact instance from the enchantment holder (or null if this is a different enchantment)
            Impact impact = enchantmentHolder.value().effects().get(DataComponentsRegistry.IMPACT.get());

            // If this enchant has an Impact component, use it.
            if(impact != null){
                atomicValue.set(enchantLevel);
            }
        });
        return (atomicValue.get()) * 0.25F;
    }

    public static boolean isFlailThrown(LivingEntity livingEntity){
        return livingEntity instanceof Player player
                && player.getMainHandItem().getItem() instanceof FlailItem item
                && player.getCooldowns().isOnCooldown(item);
    }

    public static boolean isUsingFlail(LivingEntity livingEntity){
        return livingEntity instanceof Player player
                && player.getMainHandItem().getItem() instanceof FlailItem
                && player.isUsingItem();
    }

    @Override
    public float getAttackDamageBonus(Entity target, float damage, DamageSource damageSource) {
        ItemStack stack = damageSource.getWeaponItem();
        if(stack != null && stack.is(this)){
            Optional<ItemAttributeModifiers.Entry> optional = stack.getAttributeModifiers()
                    .modifiers()
                    .stream()
                    .filter(e -> e.modifier().is(BASE_ATTACK_DAMAGE_ID))
                    .findFirst();
            return optional.isPresent() ? (float)-optional.get().modifier().amount() : 0.0F;
        }
        return super.getAttackDamageBonus(target, damage, damageSource);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        if(entity.getAttribute(Attributes.ATTACK_SPEED) != null){
            return Mth.floor((float)20 / entity.getAttribute(Attributes.ATTACK_SPEED).getValue());
        }
        return DEFAULT_USE_DURATION;

    }

    @Override
    public boolean useOnRelease(ItemStack stack) {
        return stack.is(this);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }
}
