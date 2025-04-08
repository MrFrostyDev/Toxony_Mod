package xyz.yfrostyf.toxony.data.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.registries.SoundEventRegistry;

public class ToxonySoundDefinitionsProvider extends SoundDefinitionsProvider {
    public ToxonySoundDefinitionsProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, ToxonyMain.MOD_ID, helper);
    }

    @Override
    public void registerSounds() {
        // Accepts a Supplier<SoundEvent>, a SoundEvent, or a ResourceLocation as the first parameter.
        add(SoundEventRegistry.MUTAGEN_TRANSFORM, SoundDefinition.definition()
                // Add sound objects to the sound definition. Parameter is a vararg.
                .with(
                        // Accepts either a string or a ResourceLocation as the first parameter.
                        // The second parameter can be either SOUND or EVENT, and can be omitted if the former.
                        sound(ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "mutagen_transform"))
                                // Sets the volume. Also has a double counterpart.
                                .volume(0.8f)
                                // Sets the pitch. Also has a double counterpart.
                                .pitch(1.0f)
                                // Sets the weight.
                                .weight(2)
                                // Sets the attenuation distance.
                                .attenuationDistance(6)
                                // Enables streaming. Useful if audio is longer than a few seconds.
                                // Also has a parameterless overload that defers to stream(true).
                                .stream(false)
                                // If true, the sound will be loaded into memory on pack load, instead of when the sound is played.
                                // Vanilla uses this for underwater ambience sounds. Defaults to false.
                                // Also has a parameterless overload that defers to preload(true).
                                .preload(false),
                        // The shortest we can get.
                        sound("toxony:mutagen_transform")
                )
                // Sets the subtitle.
                .subtitle("sound.toxony.mutagen_transform")
                // Enables replacing.
                .replace(false)
        );
    }
}
