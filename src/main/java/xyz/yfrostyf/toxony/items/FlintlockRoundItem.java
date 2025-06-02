package xyz.yfrostyf.toxony.items;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import xyz.yfrostyf.toxony.ToxonyConfig;
import xyz.yfrostyf.toxony.data.datagen.enchantments.effects.Acidshot;
import xyz.yfrostyf.toxony.entities.item.FlintlockBall;
import xyz.yfrostyf.toxony.registries.DataComponentsRegistry;

import java.util.concurrent.atomic.AtomicInteger;

public class FlintlockRoundItem extends Item implements ProjectileItem {
    private float damage;

    public FlintlockRoundItem(Properties properties, float damage) {
        super(properties);
        this.damage = damage;
    }

    @Override
    public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
        AtomicInteger value = new AtomicInteger(0);

        EnchantmentHelper.runIterationOnItem(stack, (enchantmentHolder, enchantLevel) -> {
            Acidshot acidshot = enchantmentHolder.value().effects().get(DataComponentsRegistry.ACIDSHOT.get());
            if(acidshot != null){
                value.set(enchantLevel);
            }
        });

        FlintlockBall flintlockBall = new FlintlockBall(level, stack, ToxonyConfig.IRON_ROUND_DAMAGE.get().floatValue(), value.get());
        return flintlockBall;
    }
}
