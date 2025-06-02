package xyz.yfrostyf.toxony.registries;

import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.api.client.ClientToxData;
import xyz.yfrostyf.toxony.api.mutagens.MutagenData;
import xyz.yfrostyf.toxony.api.tox.ToxData;
import xyz.yfrostyf.toxony.capabilities.MutagenDataSerializer;
import xyz.yfrostyf.toxony.capabilities.ToxDataSerializer;

public class DataAttachmentRegistry {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, ToxonyMain.MOD_ID);

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<ToxData>> TOX_DATA = ATTACHMENT_TYPES.register(
            "tox_data",
            () -> AttachmentType.builder((holder) -> {
                if(holder instanceof ServerPlayer svplayer){
                    return new ToxData(svplayer);
                }
                else{
                    return ClientToxData.newToxData();
                }
            }).serialize(new ToxDataSerializer())
                    .copyOnDeath()
                    .build()
    );

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Float>> MOB_TOXIN = ATTACHMENT_TYPES.register(
            "mob_toxin",
            () -> AttachmentType.builder(holder -> 0.0F).serialize(Codec.FLOAT)
                    .copyOnDeath()
                    .build()
    );

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<MutagenData>> MUTAGEN_DATA = ATTACHMENT_TYPES.register(
            "mutagen_data",
            () -> AttachmentType.builder((holder) -> new MutagenData()).serialize(new MutagenDataSerializer())
                    .copyOnDeath()
                    .build()
    );

}
