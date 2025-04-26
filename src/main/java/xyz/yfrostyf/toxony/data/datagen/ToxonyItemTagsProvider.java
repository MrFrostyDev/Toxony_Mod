package xyz.yfrostyf.toxony.data.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import xyz.yfrostyf.toxony.registries.ItemRegistry;
import xyz.yfrostyf.toxony.registries.TagRegistry;

import java.util.concurrent.CompletableFuture;

public class ToxonyItemTagsProvider extends ItemTagsProvider {


    public ToxonyItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookupProvider, blockTags);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        this.tag(TagRegistry.FLAIL_ENCHANTABLE)
                .add(
                        ItemRegistry.FLAIL.get()
                );

        this.tag(ItemTags.WEAPON_ENCHANTABLE)
                .addTags(
                        TagRegistry.FLAIL_ENCHANTABLE
                ).add(
                        ItemRegistry.VENOM_CLUB.get()
                );

        this.tag(ItemTags.SHARP_WEAPON_ENCHANTABLE)
                .addTags(
                        TagRegistry.SCALPEL_ITEM
                )
                .add(
                        ItemRegistry.BONE_SAW.get()
                );

        this.tag(ItemTags.DURABILITY_ENCHANTABLE)
                .addTag(
                        TagRegistry.SCALPEL_ITEM
                )
                .add(
                        ItemRegistry.VENOM_CLUB.get(),
                        ItemRegistry.BONE_SAW.get(),
                        ItemRegistry.CYCLEBOW.get(),
                        ItemRegistry.FLAIL.get()
                );

        this.tag(ItemTags.HEAD_ARMOR)
                .add(
                        ItemRegistry.PLAGUE_DOCTOR_HOOD.get(),
                        ItemRegistry.HUNTER_HAT.get()
                );

        this.tag(ItemTags.CHEST_ARMOR)
                .add(
                        ItemRegistry.PLAGUE_DOCTOR_COAT.get(),
                        ItemRegistry.HUNTER_COAT.get()
                );

        this.tag(ItemTags.LEG_ARMOR)
                .add(
                        ItemRegistry.PLAGUE_DOCTOR_LEGGINGS.get(),
                        ItemRegistry.HUNTER_LEGGINGS.get()
                );

        this.tag(ItemTags.FOOT_ARMOR)
                .add(
                        ItemRegistry.PLAGUE_DOCTOR_BOOTS.get(),
                        ItemRegistry.HUNTER_BOOTS.get()
                );


        this.tag(ItemTags.SWORDS)
                .addTag(
                        TagRegistry.SCALPEL_ITEM
                )
                .add(
                        ItemRegistry.WITCHING_BLADE.get()
                );

        this.tag(TagRegistry.OIL_REPAIRABLE)
                .addTag(
                        ItemTags.DURABILITY_ENCHANTABLE
                );

        this.tag(TagRegistry.OIL_APPLICABLE)
                .addTags(
                        TagRegistry.FLAIL_ENCHANTABLE,
                        ItemTags.WEAPON_ENCHANTABLE
                );

        this.tag(TagRegistry.SCALPEL_ITEM)
                .add(
                        ItemRegistry.COPPER_SCALPEL.get(),
                        ItemRegistry.NETHERITE_SCALPEL.get(),
                        ItemRegistry.LETHAL_DOSE.get()
                );

        this.tag(TagRegistry.POISONOUS_INGREDIENTS_ITEM)
                .add(
                        Items.SPIDER_EYE,
                        Items.PUFFERFISH,
                        ItemRegistry.VENOM_TOOTH.get(),
                        ItemRegistry.TOXIC_SPIT.get(),
                        ItemRegistry.POISON_SAC.get(),
                        ItemRegistry.BOG_BONE.get(),
                        ItemRegistry.ACID_SLIMEBALL.get()
                );

        this.tag(TagRegistry.POISONOUS_PLANTS_ITEM)
                .add(
                        Items.POISONOUS_POTATO,
                        Items.WITHER_ROSE,
                        ItemRegistry.FALSE_BERRIES.get(),
                        ItemRegistry.OCELOT_MINT.get(),
                        ItemRegistry.SNOW_MINT.get(),
                        ItemRegistry.NIGHTSHADE.get(),
                        ItemRegistry.SUNSPOT.get(),
                        ItemRegistry.WATER_HEMLOCK.get(),
                        ItemRegistry.MOONLIGHT_HEMLOCK.get(),
                        ItemRegistry.COLDSNAP.get(),
                        ItemRegistry.WHIRLSNAP.get(),
                        ItemRegistry.BLOODROOT.get(),
                        ItemRegistry.WARPROOT.get()
                );

        this.tag(TagRegistry.BOLTS)
                .add(
                        ItemRegistry.BOLT.get(),
                        ItemRegistry.POISON_BOLT.get(),
                        ItemRegistry.GLOWING_BOLT.get(),
                        ItemRegistry.TOXIN_BOLT.get(),
                        ItemRegistry.SMOKE_BOLT.get(),
                        ItemRegistry.REGENERATION_BOLT.get(),
                        ItemRegistry.WITCHFIRE_BOLT.get()
                );

        this.tag(ItemTags.OCELOT_FOOD)
                .add(
                        ItemRegistry.OCELOT_MINT.get()
                );
    }
}
