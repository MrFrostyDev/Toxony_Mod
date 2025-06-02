package xyz.yfrostyf.toxony;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;
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
        SoundEventRegistry.register(modEventBus);
        MenuRegistry.register(modEventBus);
        RecipeRegistry.register(modEventBus);
        ParticleRegistry.register(modEventBus);
        PotionRegistry.register(modEventBus);
        CreativeTabRegistry.register(modEventBus);
        LootModifierSerializerRegistry.register(modEventBus);

        modEventBus.addListener(DataInitialize::gatherData);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ToxonyMain) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        // NeoForge.EVENT_BUS.register(this);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, ToxonyConfig.SPEC, String.format("%s-common.toml", ToxonyMain.MOD_ID));

    }
}
