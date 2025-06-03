package xyz.yfrostyf.toxony.mixins;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.registries.MobEffectRegistry;

import javax.annotation.Nullable;

@Mixin(targets = "net.minecraft.world.effect.HealOrHarmMobEffect")
public abstract class HealMobEffectMixin {
    @Shadow
    private boolean isHarm;

    @Inject(method = "applyEffectTick", at = @At("HEAD"), cancellable = true)
    public void applyEffectTickForNecrotic(LivingEntity livingEntity, int amplifier, CallbackInfoReturnable<Boolean> cir) {
        if (!this.isHarm == livingEntity.hasEffect(MobEffectRegistry.NECROTIC_MUTAGEN)) {
            if(livingEntity.getEffect(MobEffectRegistry.NECROTIC_MUTAGEN).getAmplifier() >= 1){
                livingEntity.hurt(livingEntity.damageSources().magic(), (float)(6 << amplifier));
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "applyInstantenousEffect", at = @At("HEAD"), cancellable = true)
    public void applyInstantenousEffectForNecrotic(@Nullable Entity source, @Nullable Entity indirectSource, LivingEntity livingEntity, int amplifier, double health, CallbackInfo ci) {
        ToxonyMain.LOGGER.debug("Test");
        if (!this.isHarm == livingEntity.hasEffect(MobEffectRegistry.NECROTIC_MUTAGEN)) {
            if(livingEntity.getEffect(MobEffectRegistry.NECROTIC_MUTAGEN).getAmplifier() >= 1){
                int j = (int)(health * (double)(6 << amplifier) + 0.5);
                if (source == null) {
                    livingEntity.hurt(livingEntity.damageSources().magic(), (float)j);
                } else {
                    livingEntity.hurt(livingEntity.damageSources().indirectMagic(source, indirectSource), (float)j);
                }
                ci.cancel();
            }
        }
    }
}