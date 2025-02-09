package xyz.yfrostyf.toxony.capabilities;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import org.jetbrains.annotations.Nullable;
import xyz.yfrostyf.toxony.api.tox.ToxData;

//
// To serializer to save NBT data of player in the world
//
public class PlayerToxSerializer implements IAttachmentSerializer<CompoundTag, ToxData> {

    // Load data from NBT
    @Override
    public ToxData read(IAttachmentHolder holder, CompoundTag tag, HolderLookup.Provider provider) {
        var toxData = new ToxData((ServerPlayer) holder);
        toxData.loadNBTData(tag, provider);
        return toxData;
    }

    // Save data to NBT
    @Override
    public @Nullable CompoundTag write(ToxData attachment, HolderLookup.Provider provider) {
        var tag = new CompoundTag();
        attachment.saveNBTData(tag, provider);
        return tag;
    }
}

