package xyz.yfrostyf.toxony.entities.item;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import xyz.yfrostyf.toxony.registries.ItemRegistry;

import javax.annotation.Nullable;

public class WitchFireBolt extends Bolt {
    public WitchFireBolt(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public WitchFireBolt(Level level, LivingEntity owner, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(level, owner, pickupItemStack, firedFromWeapon);
    }

    public WitchFireBolt(Level level, double x, double y, double z, ItemStack itemStack, @Nullable ItemStack firedFromWeapon) {
        super(level, x, y, z, itemStack, firedFromWeapon);
    }

    @Override
    public void onAddedToLevel() {
        this.igniteForSeconds(10);
        super.onAddedToLevel();
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ItemRegistry.WITCHFIRE_BOLT);
    }
}
