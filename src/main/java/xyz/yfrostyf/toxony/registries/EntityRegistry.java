package xyz.yfrostyf.toxony.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.entities.item.Bolt;
import xyz.yfrostyf.toxony.entities.item.SmokeBolt;
import xyz.yfrostyf.toxony.entities.item.ThrownOilPot;
import xyz.yfrostyf.toxony.entities.item.WitchFireBolt;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, ToxonyMain.MOD_ID);

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }

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
}
