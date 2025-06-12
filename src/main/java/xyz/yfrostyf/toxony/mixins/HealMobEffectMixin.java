package xyz.yfrostyf.toxony.mixins;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.yfrostyf.toxony.registries.MobEffectRegistry;

import javax.annotation.Nullable;

@Mixin(targets = "net.minecraft.world.effect.HealOrHarmMobEffect")
public abstract class HealMobEffectMixin {
    @Shadow
    private boolean isHarm;

    @Inject(method = "applyEffectTick", at = @At("HEAD"), cancellable = true)
    public void applyEffectTickForNecrotic(LivingEntity livingEntity, int amplifier, CallbackInfoReturnable<Boolean> cir) {
        boolean hasNecrotic = livingEntity.hasEffect(MobEffectRegistry.NECROTIC_MUTAGEN);
        if(hasNecrotic){
            if (!this.isHarm) {
                if(livingEntity.getEffect(MobEffectRegistry.NECROTIC_MUTAGEN).getAmplifier() >= 1){
                    livingEntity.hurt(livingEntity.damageSources().magic(), (float)(6 << amplifier));
                }
            }
            else{
                livingEntity.heal((float)Math.max(4 << amplifier, 0));
            }
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "applyInstantenousEffect", at = @At("HEAD"), cancellable = true)
    public void applyInstantenousEffectForNecrotic(@Nullable Entity source, @Nullable Entity indirectSource, LivingEntity livingEntity, int amplifier, double health, CallbackInfo ci) {
        boolean hasNecrotic = livingEntity.hasEffect(MobEffectRegistry.NECROTIC_MUTAGEN);
        if(hasNecrotic) {
            if (!this.isHarm) {
                if(livingEntity.getEffect(MobEffectRegistry.NECROTIC_MUTAGEN).getAmplifier() >= 1){
                    int j = (int)(health * (double)(6 << amplifier) + 0.5);
                    if (source == null) {
                        livingEntity.hurt(livingEntity.damageSources().magic(), (float)j);
                    } else {
                        livingEntity.hurt(livingEntity.damageSources().indirectMagic(source, indirectSource), (float)j);
                    }

                }
            }
            else{
                int i = (int)(health * (double)(4 << amplifier) + 0.5);
                livingEntity.heal((float)i);
            }
            ci.cancel();
        }
    }
}