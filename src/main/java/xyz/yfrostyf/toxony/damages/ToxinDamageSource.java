package xyz.yfrostyf.toxony.damages;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ToxinDamageSource extends DamageSource {

    public ToxinDamageSource(Holder<DamageType> type, @Nullable Entity entity) {
        super(type, entity);
    }

    @Override
    public @NotNull Component getLocalizedDeathMessage(LivingEntity victim){
        return Component.translatable("death.toxony.damage.toxin", victim.getDisplayName());
    }
}
