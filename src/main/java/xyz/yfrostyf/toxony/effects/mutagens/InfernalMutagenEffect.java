package xyz.yfrostyf.toxony.effects.mutagens;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import xyz.yfrostyf.toxony.api.mutagens.MutagenEffect;
import xyz.yfrostyf.toxony.registries.MobEffectRegistry;

import java.util.List;

public class InfernalMutagenEffect extends MutagenEffect {
    private static List<SmeltingRecipe> charcoalRecipeCache = null;

    public InfernalMutagenEffect(MobEffectCategory category) {
        super(category, 0xffffff);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if(amplifier >= 2){
            entity.clearFire();
            if(entity.tickCount % 20 == 0){
                if(entity.isInWaterRainOrBubble()){
                    entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 0, true, false, false));
                }
            }
        }
        return true;
    }

    @EventBusSubscriber
    public static class InfernalMutagenEvents {

        @SubscribeEvent
        public static void onMutagenDamaged(LivingDamageEvent.Pre event){
            MobEffectInstance victimMutagen = event.getEntity().getEffect(MobEffectRegistry.INFERNAL_MUTAGEN);
            Holder<DamageType> damageType = event.getSource().typeHolder();

            if(victimMutagen != null){
                if(victimMutagen.getAmplifier() >= 0 && victimMutagen.getAmplifier() < 2){
                    if (damageType.is(DamageTypes.ON_FIRE)){
                        event.setNewDamage(event.getOriginalDamage() * 0.5F);
                    }
                }
                if(victimMutagen.getAmplifier() >= 1 && event.getSource().getEntity() instanceof LivingEntity attackerEntity){
                    attackerEntity.igniteForSeconds(6);
                }
                if(victimMutagen.getAmplifier() >= 2){
                    if (damageType.is(DamageTypeTags.IS_FIRE)){
                        event.setNewDamage(0);
                    }
                }
            }
        }

        @SubscribeEvent
        public static void onDamageMutagenAttacker(LivingDamageEvent.Post event){
            LivingEntity victimEntity = event.getEntity();

            if(event.getSource().getEntity() instanceof LivingEntity attackerEntity){
                MobEffectInstance attackerMutagen = attackerEntity.getEffect(MobEffectRegistry.INFERNAL_MUTAGEN);

                if(attackerMutagen != null && attackerMutagen.getAmplifier() >= 1) {
                    victimEntity.igniteForSeconds(6);
                }
            }
        }

        @SubscribeEvent
        public static void onRightClickItemStack(PlayerInteractEvent.RightClickItem event){
            if(!event.getEntity().isCrouching()) return;
            MobEffectInstance mutagen = event.getEntity().getEffect(MobEffectRegistry.INFERNAL_MUTAGEN);
            if (mutagen == null || mutagen.getAmplifier() < 1) return;

            boolean matched = false;

            //Cache recipes that have charcoal as an output
            if(charcoalRecipeCache == null) {
                RecipeManager recipeManager = event.getEntity().level().getRecipeManager();
                charcoalRecipeCache = recipeManager.getAllRecipesFor(RecipeType.SMELTING).stream()
                        .filter(holder -> holder.value().getResultItem(null).is(Items.CHARCOAL))
                        .map(RecipeHolder::value)
                        .toList();
            }

            for(SmeltingRecipe recipe : charcoalRecipeCache){
                if(recipe.getIngredients().getFirst().test(event.getItemStack())){
                    matched = true;
                    break;
                }
            }

            if(matched){
                event.getItemStack().consume(1, event.getEntity());
                event.getEntity().getInventory().add(new ItemStack(Items.CHARCOAL, 1));
                if(event.getEntity().level() instanceof ServerLevel svlevel){
                    Vec3 vec3 = event.getEntity().getViewVector(1.0F);
                    Vector3f vector3foffset = vec3.toVector3f().rotate(new Quaternionf().setAngleAxis((11.0F * (float) (Math.PI / 180.0)), vec3.x, vec3.y, vec3.z));
                    svlevel.sendParticles(ParticleTypes.FLAME,
                            event.getEntity().getX() + vector3foffset.x, event.getEntity().getEyeY() - 0.3F + vector3foffset.y, event.getEntity().getZ() + vector3foffset.z,
                            6, 0.05, 0.05, 0.05, 0.02);
                    svlevel.playSound(
                            null, event.getEntity(),
                            SoundEvents.FIRECHARGE_USE, SoundSource.NEUTRAL,
                            1.0F, 1.0F
                    );
                }
            }
        }
    }
}
