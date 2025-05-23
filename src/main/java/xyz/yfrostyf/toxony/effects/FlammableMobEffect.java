package xyz.yfrostyf.toxony.effects;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import xyz.yfrostyf.toxony.registries.TagRegistry;

import java.util.stream.Stream;

public class FlammableMobEffect extends MobEffect {
    private static final int color = 0x6e2f2B;

    public FlammableMobEffect(MobEffectCategory category) {
        super(category, color);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity.level() instanceof ServerLevel svlevel){
            if(!livingEntity.isOnFire()){
                AABB area = new AABB(livingEntity.getOnPos()).inflate(2 + amplifier);
                Stream<BlockState> blocksInArea = svlevel.getBlockStates(area);
                if(blocksInArea.anyMatch(block -> block.is(TagRegistry.OPEN_FLAME))){
                    livingEntity.igniteForSeconds(6 + (amplifier * 3));
                }
            }
        }
        return true;
    }
}
