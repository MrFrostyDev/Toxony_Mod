package xyz.yfrostyf.toxony.items;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import xyz.yfrostyf.toxony.api.affinity.Affinity;
import xyz.yfrostyf.toxony.api.items.ToxGiverItem;
import xyz.yfrostyf.toxony.api.tox.ToxData;
import xyz.yfrostyf.toxony.api.util.AffinityUtil;
import xyz.yfrostyf.toxony.api.util.ToxUtil;
import xyz.yfrostyf.toxony.network.SyncToxPacket;
import xyz.yfrostyf.toxony.registries.DataAttachmentRegistry;
import xyz.yfrostyf.toxony.registries.DataComponentsRegistry;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class BlendItem extends ToxGiverItem {


    public BlendItem(Properties properties, float tox, float tolerance, int tier, Supplier<ItemStack> returnItem, List<MobEffectInstance> mobEffectInstances) {
        super(properties, tox, tolerance, tier, returnItem, mobEffectInstances);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if(!(entity instanceof Player player))return stack;
        ToxData plyToxData = player.getData(DataAttachmentRegistry.TOX_DATA);
        plyToxData.addTox(tox);

        ToxUtil.addToleranceWithTier(plyToxData, tolerance, tier, level);

        if(level instanceof ServerLevel svlevel && stack.has(DataComponentsRegistry.AFFINITIES)) {
            List<Affinity> affinities = stack.get(DataComponentsRegistry.AFFINITIES);
            for (Affinity affinity : affinities) {
                AffinityUtil.addAffinityByItem(plyToxData, stack, affinity, Math.max(new Random().nextInt(5), 2));
            }
            PacketDistributor.sendToPlayer((ServerPlayer) player, SyncToxPacket.create(plyToxData));
        }

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
                (mobEffectInstance.getEffect().value()).applyInstantenousEffect(player, player, entity, effectAmp, (double)1.0F);
            } else {
                player.addEffect(new MobEffectInstance(effect, mobEffectInstance.getDuration() + oldEffectDuration, Math.max(oldEffectAmp, effectAmp)));
            }
        });

        return ItemUtils.createFilledResult(stack, player, new ItemStack(Items.BOWL), false);
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
