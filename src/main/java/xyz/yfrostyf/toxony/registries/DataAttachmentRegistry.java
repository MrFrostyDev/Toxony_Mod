package xyz.yfrostyf.toxony.registries;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.client.ClientToxData;
import xyz.yfrostyf.toxony.api.tox.ToxData;
import xyz.yfrostyf.toxony.capabilities.PlayerToxSerializer;

public class DataAttachmentRegistry {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, ToxonyMain.MOD_ID);

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<ToxData>> TOX_DATA = ATTACHMENT_TYPES.register(
            "tox_data",

            // The object data that's stored in the ATTACHMENT_TYPES registry, which is a AttachmentType<ToxData>
            // ServerOnly
            () -> AttachmentType.builder((holder) -> holder instanceof ServerPlayer svplayer ? new ToxData(svplayer) : ClientToxData.getToxData()).serialize(new PlayerToxSerializer())
                    .copyOnDeath()
                    .build()
    );
}
