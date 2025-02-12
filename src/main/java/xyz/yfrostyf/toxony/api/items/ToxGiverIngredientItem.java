package xyz.yfrostyf.toxony.api.items;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import xyz.yfrostyf.toxony.api.affinity.Affinity;
import xyz.yfrostyf.toxony.api.tox.ToxData;
import xyz.yfrostyf.toxony.api.util.AffinityUtil;
import xyz.yfrostyf.toxony.network.SyncToxPacket;
import xyz.yfrostyf.toxony.registries.DataAttachmentRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ToxGiverIngredientItem extends ToxGiverItem implements AffinityIngredient{
    protected final Supplier<List<Affinity>> affinities;

    public ToxGiverIngredientItem(Properties properties, float tox, float tolerance, int tier, Supplier<ItemStack> returnItem, List<MobEffectInstance> mobEffectInstances, Supplier<List<Affinity>> affinities) {
        super(properties, tox, tolerance, tier, returnItem, mobEffectInstances);
        this.affinities = affinities;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if(!(entity instanceof Player player))return stack;
        ToxData plyToxData = player.getData(DataAttachmentRegistry.TOX_DATA);
        plyToxData.addTox(tox);

        if(!plyToxData.getDeathState()){
            if(plyToxData.getThreshold() >= tier){
                plyToxData.addTolerance(tolerance);
            }
        }

        if(level instanceof ServerLevel svlevel) {
            for (Affinity affinity : affinities.get()) {
                if (AffinityUtil.matchesAffinityMap(stack, affinity, svlevel)) {
                    plyToxData.addAffinity(affinity, 1);
                }
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

        return ItemUtils.createFilledResult(stack, player, returnItem.get());
    }

    @Override
    public List<Affinity> getPossibleAffinities() {
        return affinities.get();
    }

    public static ToxGiverIngredientItem.Builder builder(){
        return new ToxGiverIngredientItem.Builder();
    }

    public static class Builder extends ToxGiverItem.Builder {
        protected Supplier<List<Affinity>> affinities = ArrayList::new;

        public Builder affinity(Supplier<List<Affinity>> affinities){
            this.affinities = affinities;
            return this;
        }

        public ToxGiverIngredientItem build() {
            return new ToxGiverIngredientItem(properties, this.tox, this.tolerance, this.tier, this.returnItem, this.mobEffectInstances, this.affinities);
        }
    }
}
