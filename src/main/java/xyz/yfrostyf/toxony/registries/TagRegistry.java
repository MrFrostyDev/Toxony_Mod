package xyz.yfrostyf.toxony.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
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
            Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "oil_layer_cannot_survive_on")
    );

    public static final TagKey<Block> OPEN_FLAME = TagKey.create(
            Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "open_flame")
    );

    // |------------------------------------------------------------------------------------|
    // |-------------------------------------Item Tags--------------------------------------|
    // |------------------------------------------------------------------------------------|

    public static final TagKey<Item> OIL_APPLICABLE = TagKey.create(
            Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "oil_applicable")
    );

    public static final TagKey<Item> OIL_REPAIRABLE = TagKey.create(
            Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "oil_repairable")
    );

    public static final TagKey<Item> CAN_REFILL_OIL = TagKey.create(
            Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "oil_refiller")
    );


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

    public static final TagKey<Item> BOLTS = TagKey.create(
            Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "bolts")
    );

    public static final TagKey<Item> ROUNDS = TagKey.create(
            Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "rounds")
    );

    public static final TagKey<Item> FLAIL_ENCHANTABLE = TagKey.create(
            Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "enchantable/flail")
    );

    // |------------------------------------------------------------------------------------|
    // |------------------------------------Entity Tags-------------------------------------|
    // |------------------------------------------------------------------------------------|

    public static final TagKey<EntityType<?>> SILVER_VULNERABLE = TagKey.create(
            Registries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "silver_vulnerable")
    );
}
