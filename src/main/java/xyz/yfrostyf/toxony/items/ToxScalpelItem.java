package xyz.yfrostyf.toxony.items;

import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Blocks;
import xyz.yfrostyf.toxony.api.items.ToxFueledItem;
import xyz.yfrostyf.toxony.registries.MobEffectRegistry;

import java.util.List;

public class ToxScalpelItem extends ToxFueledItem{
    public ToxScalpelItem(Item.Properties properties, int tickrate, int cooldown, SoundEvent sound) {
        super(properties.component(DataComponents.TOOL, createToolProperties()), tickrate, cooldown, sound);
    }

    public static Tool createToolProperties() {
        return new Tool(List.of(Tool.Rule.minesAndDrops(List.of(Blocks.COBWEB), 15.0F), Tool.Rule.overrideSpeed(BlockTags.SWORD_EFFICIENT, 1.5F)), 1.0F, 2);
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
        if(isActive(stack)){
            target.addEffect(new MobEffectInstance(MobEffectRegistry.TOXIN, 120, 0));
        }
    }

    public static ToxScalpelItem.Builder builder(){
        return new ToxScalpelItem.Builder();
    }

    public static class Builder extends ToxFueledItem.Builder {
        public ToxScalpelItem build(){
            return new ToxScalpelItem(this.properties, this.tickrate, this.cooldown, this.sound);
        }
    }
}
