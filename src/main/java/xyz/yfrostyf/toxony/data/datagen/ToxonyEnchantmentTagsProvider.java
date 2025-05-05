package xyz.yfrostyf.toxony.data.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.minecraft.tags.EnchantmentTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.data.datagen.enchantments.ToxonyEnchantments;

import java.util.concurrent.CompletableFuture;

public class ToxonyEnchantmentTagsProvider extends EnchantmentTagsProvider {
    public ToxonyEnchantmentTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ToxonyMain.MOD_ID, existingFileHelper);
    }

    @Override
    public void addTags(HolderLookup.Provider lookupProvider) {
        tag(Tags.Enchantments.INCREASE_ENTITY_DROPS)
                .add(ToxonyEnchantments.REFILL);
        tag(Tags.Enchantments.WEAPON_DAMAGE_ENHANCEMENTS)
                .add(ToxonyEnchantments.IMPACT)
                .add(ToxonyEnchantments.ACIDSHOT);
        tag(EnchantmentTags.IN_ENCHANTING_TABLE)
                .add(ToxonyEnchantments.REFILL)
                .add(ToxonyEnchantments.IMPACT)
                .add(ToxonyEnchantments.ACIDSHOT);
    }
}
