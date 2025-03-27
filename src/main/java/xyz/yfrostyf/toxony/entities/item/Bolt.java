package xyz.yfrostyf.toxony.entities.item;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import xyz.yfrostyf.toxony.api.oils.ItemOil;
import xyz.yfrostyf.toxony.registries.DataComponentsRegistry;
import xyz.yfrostyf.toxony.registries.EntityRegistry;
import xyz.yfrostyf.toxony.registries.ItemRegistry;

import javax.annotation.Nullable;

public class Bolt extends AbstractArrow {
    public Bolt(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
        this.setBaseDamage(1.0);
    }

    public Bolt(Level level, double x, double y, double z, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(EntityRegistry.BOLT.get(), x, y, z, level, pickupItemStack, firedFromWeapon);
        this.setBaseDamage(1.0);
    }

    public Bolt(Level level, LivingEntity owner, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(EntityRegistry.BOLT.get(), owner, level, pickupItemStack, firedFromWeapon);
        this.setBaseDamage(1.0);
    }

    private ItemOil getOilContents() {
        return this.getPickupItemStackOrigin().getOrDefault(DataComponentsRegistry.OIL, ItemOil.EMPTY);
    }

    private PotionContents getPotionContents() {
        return this.getPickupItemStackOrigin().getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
    }

    private void setPotionContents(PotionContents potionContents) {
        this.getPickupItemStackOrigin().set(DataComponents.POTION_CONTENTS, potionContents);
    }

    public void addEffect(MobEffectInstance effectInstance) {
        this.setPotionContents(this.getPotionContents().withEffectAdded(effectInstance));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            if (this.inGround) {
                if (this.inGroundTime % 5 == 0) {
                    this.makeParticle(1);
                }
            } else {
                this.makeParticle(2);
            }
        } else if (this.inGround && this.inGroundTime != 0 && !this.getPotionContents().equals(PotionContents.EMPTY) && this.inGroundTime >= 600) {
            this.level().broadcastEntityEvent(this, (byte)0);
            this.setPickupItemStack(new ItemStack(Items.ARROW));
        }
    }

    private void makeParticle(int particleAmount) {
        if (particleAmount > 0) {
            for (int j = 0; j < particleAmount; j++) {
                this.level().addParticle(
                        ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 1),
                        this.getRandomX(0.5),
                        this.getRandomY(),
                        this.getRandomZ(0.5),
                        0.0, 0.0, 0.0
                );
            }
        }
    }

    @Override
    protected void doPostHurtEffects(LivingEntity living) {
        super.doPostHurtEffects(living);
        ItemOil itemoil = this.getOilContents();
        if (!itemoil.isEmpty()) {
            for (Holder<MobEffect> holder : itemoil.getOil().getEffects()) {
                if (holder.value().isInstantenous()) {
                    holder.value().applyInstantenousEffect(this.getEffectSource(), this.getEffectSource(), living, itemoil.amplifier(), 1.0);
                }
                else{
                    living.addEffect(
                            new MobEffectInstance(holder, itemoil.duration(), itemoil.amplifier()), this.getEffectSource());
                }
            }
        }
        PotionContents potioncontents = this.getPotionContents();
        if (potioncontents.potion().isPresent()) {
            for (MobEffectInstance mobeffectinstance : potioncontents.potion().get().value().getEffects()) {
                if (mobeffectinstance.getEffect().value().isInstantenous()) {
                    mobeffectinstance.getEffect().value().applyInstantenousEffect(this.getEffectSource(), this.getEffectSource(), living, itemoil.amplifier(), 1.0);
                }
                else{
                    living.addEffect(
                            new MobEffectInstance(
                                    mobeffectinstance.getEffect(), mobeffectinstance.getDuration(),
                                    mobeffectinstance.getAmplifier(), mobeffectinstance.isAmbient(),
                                    mobeffectinstance.isVisible()
                            ), this.getEffectSource());
                }
            }
        }

        for (MobEffectInstance mobeffectinst : potioncontents.customEffects()) {
            living.addEffect(mobeffectinst, this.getEffectSource());
        }
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ItemRegistry.BOLT);
    }
}
