package xyz.yfrostyf.toxony.api.items;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import xyz.yfrostyf.toxony.api.affinity.Affinity;
import xyz.yfrostyf.toxony.api.tox.ToxData;
import xyz.yfrostyf.toxony.api.util.AffinityUtil;
import xyz.yfrostyf.toxony.network.SyncToxPacket;
import xyz.yfrostyf.toxony.registries.DataAttachmentRegistry;
import xyz.yfrostyf.toxony.registries.DataComponentsRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class ToxGiverItem extends Item
{
    protected final float tox;
    protected final float tolerance;
    protected final float tier;
    protected final Supplier<ItemStack> returnItem;
    protected final List<MobEffectInstance> mobEffectInstances;
    protected static final int EAT_DURATION = 60;

    public ToxGiverItem(Properties properties, float tox, float tolerance, int tier, Supplier<ItemStack> returnItem, List<MobEffectInstance> mobEffectInstances) {
        super(properties);
        this.tox = tox;
        this.tolerance = tolerance;
        this.tier = tier;
        this.returnItem = returnItem;
        this.mobEffectInstances = mobEffectInstances;
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

        if(level instanceof ServerLevel svlevel && stack.has(DataComponentsRegistry.POSSIBLE_AFFINITIES)) {
            Affinity affinity = AffinityUtil.readAffinityFromIngredientMap(stack, svlevel);
            AffinityUtil.addAffinityByItem(plyToxData, stack, affinity, 1);
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
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return EAT_DURATION;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public SoundEvent getEatingSound() {
        return SoundEvents.GENERIC_EAT;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder {
        protected Properties properties = new Properties();
        protected int tox = 0;
        protected int tolerance = 0;
        protected int tier = 0;
        protected Supplier<ItemStack> returnItem = () -> new ItemStack(Items.AIR);
        protected List<MobEffectInstance> mobEffectInstances = new ArrayList<>();

        public Builder properties(Properties properties){
            this.properties = properties;
            return this;
        }

        public Builder tox(int tox){
            this.tox = tox;
            return this;
        }

        public Builder tolerance(int tolerance){
            this.tolerance = tolerance;
            return this;
        }

        public Builder tier(int tier){
            this.tier = tier;
            return this;
        }

        public Builder effect(MobEffectInstance mobEffectInstance){
            mobEffectInstances.add(mobEffectInstance);
            return this;
        }

        public Builder effects(List<MobEffectInstance> mobEffectInstances){
            this.mobEffectInstances.addAll(mobEffectInstances);
            return this;
        }

        public Builder returnItem(Supplier<ItemStack> returnItem){
            this.returnItem = returnItem;
            return this;
        }

        public ToxGiverItem build(){
            return new ToxGiverItem(properties, this.tox, this.tolerance, this.tier, this.returnItem, this.mobEffectInstances);
        }
    }
}
