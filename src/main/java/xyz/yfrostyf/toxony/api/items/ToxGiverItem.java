package xyz.yfrostyf.toxony.api.items;

import net.minecraft.core.Holder;
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
import xyz.yfrostyf.toxony.network.SyncToxPacket;
import xyz.yfrostyf.toxony.registries.DataAttachmentRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ToxGiverItem extends Item {
    protected final float tox;
    protected final float tolerance;
    protected final float tier;
    protected final Map<Integer, Integer> affinities;
    protected final List<MobEffectInstance> mobEffectInstances;
    protected static final int EAT_DURATION = 60;

    public ToxGiverItem(Properties properties, float tox, float tolerance, int tier, Map<Integer, Integer> affinities, List<MobEffectInstance> mobEffectInstances) {
        super(properties);
        this.tox = tox;
        this.tolerance = tolerance;
        this.tier = tier;
        this.affinities = affinities;
        this.mobEffectInstances = mobEffectInstances;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof ServerPlayer svplayer) {

            ToxData plyToxData = svplayer.getData(DataAttachmentRegistry.TOX_DATA);
            plyToxData.addTox(tox);

            if(!plyToxData.getDeathState()){
                if(plyToxData.getThreshold() >= tier){
                    plyToxData.addTolerance(tolerance);
                }
            }

            for (Map.Entry<Integer, Integer> entry : affinities.entrySet()) {
                plyToxData.addAffinity(entry.getKey(), entry.getValue());
            }

            PacketDistributor.sendToPlayer(svplayer, new SyncToxPacket(plyToxData));

            mobEffectInstances.forEach((mobEffectInstance) -> {
                boolean hasEffect = svplayer.hasEffect(mobEffectInstance.getEffect());
                Holder<MobEffect> effect = mobEffectInstance.getEffect();
                int effectAmp = mobEffectInstance.getAmplifier();

                int oldEffectDuration = 0;
                int oldEffectAmp = 0;

                if(hasEffect){
                    oldEffectDuration = svplayer.getEffect(mobEffectInstance.getEffect()).getDuration();
                    oldEffectAmp = svplayer.getEffect(mobEffectInstance.getEffect()).getAmplifier();
                    svplayer.removeEffect(effect);
                }

                if (mobEffectInstance.getEffect().value().isInstantenous()) {
                    (mobEffectInstance.getEffect().value()).applyInstantenousEffect(svplayer, svplayer, entity, effectAmp, (double)1.0F);
                } else {
                    svplayer.addEffect(new MobEffectInstance(effect, mobEffectInstance.getDuration() + oldEffectDuration, Math.max(oldEffectAmp, effectAmp)));
                }
            });

            return stack;
        }
        else{
            stack.consume(1, entity);
            return stack;
        }
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
        protected Map<Integer,Integer> affinities = Affinity.initAffinities();
        protected List<MobEffectInstance> mobEffectInstances = new ArrayList<MobEffectInstance>();

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

        public Builder affinity(int affinity, int value){
            int oldValue = affinities.get(affinity);
            affinities.put(affinity, oldValue + value);
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

        public ToxGiverItem build(){
            return new ToxGiverItem(properties, this.tox, this.tolerance, this.tier, this.affinities, this.mobEffectInstances);
        }
    }
}
