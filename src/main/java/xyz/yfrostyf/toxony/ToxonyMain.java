package xyz.yfrostyf.toxony;

import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;
import xyz.yfrostyf.toxony.client.gui.MutagenTransformOverlay;
import xyz.yfrostyf.toxony.data.DataInitialize;

import xyz.yfrostyf.toxony.data.datagen.registries.LootModifierSerializerRegistry;
import xyz.yfrostyf.toxony.registries.*;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(ToxonyMain.MOD_ID)
public class ToxonyMain {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "toxony";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public ToxonyMain(IEventBus modEventBus, ModContainer modContainer) {
        /*
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        */


        ItemRegistry.register(modEventBus);
        EntityRegistry.register(modEventBus);
        ArmorMaterialRegistry.register(modEventBus);
        DataAttachmentRegistry.register(modEventBus);
        DataComponentsRegistry.register(modEventBus);
        BlockRegistry.register(modEventBus);
        AttributeRegistry.register(modEventBus);
        MobEffectRegistry.register(modEventBus);
        AffinityRegistry.register(modEventBus);
        OilsRegistry.register(modEventBus);
        MenuRegistry.register(modEventBus);
        RecipeRegistry.register(modEventBus);
        ParticleRegistry.register(modEventBus);
        CreativeTabRegistry.register(modEventBus);
        LootModifierSerializerRegistry.register(modEventBus);

        modEventBus.addListener(DataInitialize::gatherData);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ToxonyMain) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);
         /*
        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        */
    }
    /*
    private void commonSetup(final FMLCommonSetupEvent event) {
        if (Config.logDirtBlock) LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    */


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @EventBusSubscriber
    public class DebugEvents{
        @SubscribeEvent
        public static void onLivingDamage(LivingDamageEvent.Post event) {
            if(!(event.getSource().getEntity() instanceof Player player))return;
            ToxonyMain.LOGGER.debug("[Damage Attack]: {}", event.getNewDamage());
            if(player.level().isClientSide()){
                MutagenTransformOverlay.startAnimation();
            }
        }
    }
}
