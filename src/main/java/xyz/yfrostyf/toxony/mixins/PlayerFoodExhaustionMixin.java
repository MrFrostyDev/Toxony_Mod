package xyz.yfrostyf.toxony.mixins;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.yfrostyf.toxony.registries.MobEffectRegistry;

@Mixin(Player.class)
public abstract class PlayerFoodExhaustionMixin {

    @Final
    @Shadow
    private Abilities abilities;

    @Shadow
    protected FoodData foodData;

    @Inject(method = "causeFoodExhaustion", at = @At("HEAD"), cancellable = true)
    public void causeFoodExhaustion(float exhaustion, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (!this.abilities.invulnerable && self.hasEffect(MobEffectRegistry.HOLLOW_MUTAGEN)) {
            MobEffectInstance effect = self.getEffect(MobEffectRegistry.HOLLOW_MUTAGEN);
            if (!self.level().isClientSide && effect.getAmplifier() >= 1) {
                this.foodData.addExhaustion(Math.max(exhaustion / 2.0F, 0.01F));
                ci.cancel();
            }
        }
    }
}
