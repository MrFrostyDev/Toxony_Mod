package xyz.yfrostyf.toxony.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import xyz.yfrostyf.toxony.ToxonyMain;

public class TagRegistry {

    // |------------------------------------------------------------------------------------|
    // |------------------------------------Block Tags--------------------------------------|
    // |------------------------------------------------------------------------------------|

    public static final TagKey<Block> POISONOUS_PLANTS_BLOCK = TagKey.create(
            // The registry key. The type of the registry must match the generic type of the tag.
            Registries.BLOCK,
            // The location of the tag. This example will put our tag at data/examplemod/tags/blocks/example_tag.json.
            ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "plants/poisonous")
    );


    public static final TagKey<Block> OIL_LAYER_CANNOT_SURVIVE_ON = TagKey.create(
            // The registry key. The type of the registry must match the generic type of the tag.
            Registries.BLOCK,
            // The location of the tag. This example will put our tag at data/examplemod/tags/blocks/example_tag.json.
            ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "oil_layer_cannot_survive_on")
    );

    // |------------------------------------------------------------------------------------|
    // |-------------------------------------Item Tags--------------------------------------|
    // |------------------------------------------------------------------------------------|

    public static final TagKey<Item> SCALPEL_ITEM = TagKey.create(
            Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "scalpels")
    );

    public static final TagKey<Item> POISONOUS_PLANTS_ITEM = TagKey.create(
            Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "plants/poisonous")
    );

    public static final TagKey<Item> POISONOUS_INGREDIENTS_ITEM = TagKey.create(
            Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "ingredients/poisonous")
    );
}
