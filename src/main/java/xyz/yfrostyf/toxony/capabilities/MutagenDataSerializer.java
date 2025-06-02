package xyz.yfrostyf.toxony.capabilities;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import org.jetbrains.annotations.Nullable;
import xyz.yfrostyf.toxony.api.mutagens.MutagenData;

//
// To serializer to save NBT mutagen data
//
public class MutagenDataSerializer implements IAttachmentSerializer<CompoundTag, MutagenData> {

    // Load data from NBT
    @Override
    public MutagenData read(IAttachmentHolder holder, CompoundTag tag, HolderLookup.Provider provider) {
        var data = new MutagenData();
        data.loadNBTData(tag, provider);
        return data;
    }

    // Save data to NBT
    @Override
    public @Nullable CompoundTag write(MutagenData attachment, HolderLookup.Provider provider) {
        var tag = new CompoundTag();
        attachment.saveNBTData(tag, provider);
        return tag;
    }
}

