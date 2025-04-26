package xyz.yfrostyf.toxony.items;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;
import xyz.yfrostyf.toxony.api.oils.ItemOil;
import xyz.yfrostyf.toxony.entities.item.Bolt;
import xyz.yfrostyf.toxony.entities.item.SmokeBolt;
import xyz.yfrostyf.toxony.entities.item.WitchFireBolt;
import xyz.yfrostyf.toxony.registries.DataComponentsRegistry;
import xyz.yfrostyf.toxony.registries.OilsRegistry;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class BoltItem extends Item implements ProjectileItem {
    public BoltItem(Properties properties) {
        super(properties);
    }

    public BoltItem(Properties properties, Supplier<ItemOil> itemOil) {
        super(properties.component(DataComponentsRegistry.OIL, itemOil.get()));
    }

    public AbstractArrow createBolt(Level level, ItemStack ammo, LivingEntity shooter, @Nullable ItemStack weapon) {
        ItemOil itemoil = ammo.getOrDefault(DataComponentsRegistry.OIL, ItemOil.EMPTY);

        Bolt bolt = new Bolt(level, shooter, ammo.copyWithCount(1), weapon);
        if(itemoil.isEmpty()){
            return bolt;
        }
        else if(itemoil.getOil().equals(OilsRegistry.WITCHFIRE_OIL.get())){
            return new WitchFireBolt(level, shooter, ammo.copyWithCount(1), weapon);
         }
        else if(itemoil.getOil().equals(OilsRegistry.SMOKE_OIL.get())){
            return new SmokeBolt(level, shooter, ammo.copyWithCount(1), weapon);
        }
        else{
            return bolt;
        }
    }

    public Projectile asProjectile(Level level, Position pos, ItemStack ammo, Direction direction) {
        ItemOil itemoil = ammo.getOrDefault(DataComponentsRegistry.OIL, ItemOil.EMPTY);

        Bolt bolt = new Bolt(level, pos.x(), pos.y(), pos.z(), ammo.copyWithCount(1), null);
        if(itemoil.getOil().equals(OilsRegistry.WITCHFIRE_OIL.get())){
            bolt = new WitchFireBolt(level, pos.x(), pos.y(), pos.z(), ammo.copyWithCount(1), null);
        }
        if(itemoil.getOil().equals(OilsRegistry.SMOKE_OIL.get())){
            bolt = new SmokeBolt(level, pos.x(), pos.y(), pos.z(), ammo.copyWithCount(1), null);
        }

        bolt.pickup = AbstractArrow.Pickup.ALLOWED;
        return bolt;
    }

    /**
     * Called to determine if this arrow will be infinite when fired. If an arrow is infinite, then the arrow will never be consumed (regardless of enchantments).
     * <p>
     * Only called on the logical server.
     *
     * @param ammo The ammo stack (containing this item)
     * @param bow  The bow stack
     * @param livingEntity The entity who is firing the bow
     * @return True if the arrow is infinite
     */
    public boolean isInfinite(ItemStack ammo, ItemStack bow, net.minecraft.world.entity.LivingEntity livingEntity) {
        return false;
    }
}
