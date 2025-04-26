package xyz.yfrostyf.toxony.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import xyz.yfrostyf.toxony.api.items.ToxFueledItem;
import xyz.yfrostyf.toxony.registries.DataAttachmentRegistry;

import java.util.List;

public class WitchingBladeItem extends ToxFueledItem {
    private static final float MAX_BONUS_DAMAGE = 10;

    public WitchingBladeItem(Properties properties, int tickrate, int cooldown, SoundEvent sound) {
        super(properties.component(DataComponents.TOOL, createToolProperties()), tickrate, cooldown, sound);
    }

    @Override
    public float getAttackDamageBonus(Entity target, float damage, DamageSource damageSource) {
        ItemStack item = damageSource.getWeaponItem();
        Entity entity = damageSource.getEntity();

        if (item == null || !(entity instanceof ServerPlayer svplayer) || !isActive(item)) return 0.0F;
        if (!svplayer.hasData(DataAttachmentRegistry.TOX_DATA)) return 0.0F;

        float plyTox = svplayer.getData(DataAttachmentRegistry.TOX_DATA).getTox();
        return Math.clamp(plyTox/100, 0, MAX_BONUS_DAMAGE);
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

    public static Tool createToolProperties() {
        return new Tool(List.of(Tool.Rule.minesAndDrops(List.of(Blocks.COBWEB), 15.0F), Tool.Rule.overrideSpeed(BlockTags.SWORD_EFFICIENT, 1.5F)), 1.0F, 2);
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return !player.isCreative();
    }

    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise the damage on the stack.
     */
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return true;
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.neoforged.neoforge.common.ItemAbility itemAbility) {
        return net.neoforged.neoforge.common.ItemAbilities.DEFAULT_SWORD_ACTIONS.contains(itemAbility);
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 15;
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return repair.is(Items.IRON_INGOT);
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder extends ToxFueledItem.Builder {
        public WitchingBladeItem build(){
            return new WitchingBladeItem(this.properties, this.tickrate, this.cooldown, this.sound);
        }
    }
}
