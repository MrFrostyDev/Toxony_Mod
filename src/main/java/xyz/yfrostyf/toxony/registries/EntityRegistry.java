package xyz.yfrostyf.toxony.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.entities.GuidedSpiritEntity;
import xyz.yfrostyf.toxony.entities.item.*;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, ToxonyMain.MOD_ID);

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }


    // |----------------------------------------------------------------------------------|
    // |-------------------------------------Entities-------------------------------------|
    // |----------------------------------------------------------------------------------|
    public static final DeferredHolder<EntityType<?>, EntityType<GuidedSpiritEntity>> GUIDED_SPIRIT = ENTITIES.register(
            "guided_spirit",
            () -> EntityType.Builder.<GuidedSpiritEntity>of(GuidedSpiritEntity::new, MobCategory.CREATURE)
                    .fireImmune()
                    .sized(0.3F, 0.4F)
                    .eyeHeight(0.5F)
                    .clientTrackingRange(8)
                    .build("guided_spirit")
    );

    // |----------------------------------------------------------------------------------|
    // |--------------------------------------Items---------------------------------------|
    // |----------------------------------------------------------------------------------|
    public static final DeferredHolder<EntityType<?>, EntityType<ThrownOilPot>> OIL_POT = ENTITIES.register(
            "oil_pot",
            () -> EntityType.Builder.<ThrownOilPot>of(ThrownOilPot::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("oil_pot")
    );

    public static final DeferredHolder<EntityType<?>, EntityType<Bolt>> BOLT = ENTITIES.register(
            "bolt",
            () -> EntityType.Builder.<Bolt>of(Bolt::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("bolt")
    );


    public static final DeferredHolder<EntityType<?>, EntityType<SmokeBolt>> SMOKE_BOLT = ENTITIES.register(
            "smoke_cycle_bolt",
            () -> EntityType.Builder.<SmokeBolt>of(SmokeBolt::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("smoke_cycle_bolt")
    );

    public static final DeferredHolder<EntityType<?>, EntityType<WitchFireBolt>> WITCHFIRE_BOLT = ENTITIES.register(
            "witchfire_bolt",
            () -> EntityType.Builder.<WitchFireBolt>of(WitchFireBolt::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("witchfire_bolt")
    );

    public static final DeferredHolder<EntityType<?>, EntityType<FlailBall>> FLAIL_BALL = ENTITIES.register(
            "flail_ball",
            () -> EntityType.Builder.<FlailBall>of(FlailBall::new, MobCategory.MISC)
                    .noSave()
                    .noSummon()
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(3)
                    .build("flail_ball")
    );

    public static final DeferredHolder<EntityType<?>, EntityType<FlintlockBall>> FLINTLOCK_BALL = ENTITIES.register(
            "flintlock_ball",
            () -> EntityType.Builder.<FlintlockBall>of(FlintlockBall::new, MobCategory.MISC)
                    .noSave()
                    .noSummon()
                    .sized(0.2F, 0.2F)
                    .clientTrackingRange(5)
                    .updateInterval(2)
                    .build("flintlock_ball")
    );

    public static final DeferredHolder<EntityType<?>, EntityType<ToxicCakeProjectile>> TOXIC_CAKE_PROJECTILE = ENTITIES.register(
            "toxic_cake_projectile",
            () -> EntityType.Builder.<ToxicCakeProjectile>of(ToxicCakeProjectile::new, MobCategory.MISC)
                    .sized(0.3F, 0.3F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("toxic_cake_projectile")
    );
}
