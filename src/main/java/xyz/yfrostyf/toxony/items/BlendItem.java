package xyz.yfrostyf.toxony.items;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import xyz.yfrostyf.toxony.api.items.ToxGiverItem;
import xyz.yfrostyf.toxony.api.tox.ToxData;
import xyz.yfrostyf.toxony.api.util.AffinityUtil;
import xyz.yfrostyf.toxony.api.util.ToxUtil;
import xyz.yfrostyf.toxony.client.gui.tooltips.StoredAffinityStacksTooltip;
import xyz.yfrostyf.toxony.registries.DataAttachmentRegistry;
import xyz.yfrostyf.toxony.registries.DataComponentsRegistry;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

public class BlendItem extends ToxGiverItem {
    private static final Random RANDOM = new Random();

    public BlendItem(Properties properties, float tox, float tolerance, int tier, Supplier<ItemStack> returnItem, List<MobEffectInstance> mobEffectInstances) {
        super(properties, tox, tolerance, tier, returnItem, mobEffectInstances);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if(!(entity instanceof Player player))return stack;
        ToxData plyToxData = player.getData(DataAttachmentRegistry.TOX_DATA);

        if(stack.has(DataComponentsRegistry.AFFINITY_STORED_ITEMS)) {
            List<Holder<Item>> stored_items = stack.get(DataComponentsRegistry.AFFINITY_STORED_ITEMS);
            for (Holder<Item> stored_item : stored_items) {
                ItemStack affinityStack = new ItemStack(stored_item);
                if(affinityStack.has(DataComponentsRegistry.POSSIBLE_AFFINITIES)){
                    AffinityUtil.addAffinityByItem(plyToxData, affinityStack, AffinityUtil.readAffinityFromIngredientMap(affinityStack), Math.max(RANDOM.nextInt(4), 2));
                }
            }
        }

        plyToxData.addTox(tox); // Add tox after affinities so they are applied before mutation occurs.
        ToxUtil.addToleranceWithTier(player, plyToxData, tolerance, tier, level);

        mobEffectInstances.forEach((mobEffectInstance) -> {
            boolean hasEffect = player.hasEffect(mobEffectInstance.getEffect());
            Holder<MobEffect> effect = mobEffectInstance.getEffect();
            int effectAmp = mobEffectInstance.getAmplifier();

            int oldEffectDuration = 0;
            int oldEffectAmp = 0;

            if(hasEffect){
                oldEffectDuration = player.getEffect(mobEffectInstance.getEffect()).getDuration();
                oldEffectAmp = player.getEffect(mobEffectInstance.getEffect()).getAmplifier();
                player.removeEffect(effect);
            }

            if (mobEffectInstance.getEffect().value().isInstantenous()) {
                (mobEffectInstance.getEffect().value()).applyInstantenousEffect(player, player, entity, effectAmp, 1.0F);
            } else {
                player.addEffect(new MobEffectInstance(effect, mobEffectInstance.getDuration() + oldEffectDuration, Math.max(oldEffectAmp, effectAmp)));
            }
        });

        return ItemUtils.createFilledResult(stack, player, new ItemStack(Items.BOWL), false);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        if(stack.has(DataComponentsRegistry.AFFINITY_STORED_ITEMS)){
            return Optional.of(new StoredAffinityStacksTooltip.StoredAffinityStacksTooltipComponent(stack));
        }
        return super.getTooltipImage(stack);
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder extends ToxGiverItem.Builder {
        public BlendItem build(){
            return new BlendItem(properties, this.tox, this.tolerance, this.tier, this.returnItem, this.mobEffectInstances);
        }
    }
}
