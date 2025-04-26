package xyz.yfrostyf.toxony.registries;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredRegister;
import xyz.yfrostyf.toxony.ToxonyMain;

import java.util.EnumMap;
import java.util.List;

public class ArmorMaterialRegistry {

    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(Registries.ARMOR_MATERIAL, ToxonyMain.MOD_ID);

    public static void register(IEventBus eventBus) {
        ARMOR_MATERIALS.register(eventBus);
    }

    public static final Holder<ArmorMaterial> PLAGUE_DOCTOR_ARMOR_MATERIAL =
            ARMOR_MATERIALS.register("plague_doctor", () -> new ArmorMaterial(
                    // Determines the defense value of this armor material, depending on what armor piece it is.
                    Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.BOOTS, 1);
                        map.put(ArmorItem.Type.LEGGINGS, 3);
                        map.put(ArmorItem.Type.CHESTPLATE, 5);
                        map.put(ArmorItem.Type.HELMET, 2);
                        map.put(ArmorItem.Type.BODY, 5);
                    }),
                    // Determines the enchantability of the tier. This represents how good the enchantments on this armor will be.
                    // Gold uses 25.
                    12,
                    // Determines the sound played when equipping this armor.
                    // This is wrapped with a Holder.
                    SoundEvents.ARMOR_EQUIP_GENERIC,
                    // Determines the repair item for this armor.
                    () -> Ingredient.of(ItemRegistry.TOXIC_LEATHER.get()),
                    // Determines the texture locations of the armor to apply when rendering
                    // This can also be specified by overriding 'IItemExtension#getArmorTexture' on your item if the armor texture needs to be more dynamic
                    List.of(
                            // Creates a new armor texture that will be located at:
                            // - 'assets/mod_id/textures/models/armor/copper_layer_1.png' for the outer texture
                            // - 'assets/mod_id/textures/models/armor/copper_layer_2.png' for the inner texture (only legs)
                            new ArmorMaterial.Layer(
                                    ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "plague_doctor_armor")
                            ),
                            // Creates a new armor texture that will be rendered on top of the previous at:
                            // - 'assets/mod_id/textures/models/armor/copper_layer_1_overlay.png' for the outer texture
                            // - 'assets/mod_id/textures/models/armor/copper_layer_2_overlay.png' for the inner texture (only legs)
                            // 'true' means that the armor material is dyeable; however, the item must also be added to the 'minecraft:dyeable' tag
                            new ArmorMaterial.Layer(
                                    ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "plague_doctor_armor")
                            )
                    ),
                    // Returns the toughness value of the armor. The toughness value is an additional value included in
                    // damage calculation, for more information, refer to the Minecraft Wiki's article on armor mechanics:
                    // https://minecraft.wiki/w/Armor#Armor_toughness
                    0,
                    // Returns the knockback resistance value of the armor. While wearing this armor, the player is
                    // immune to knockback to some degree. If the player has a total knockback resistance value of 1 or greater
                    // from all armor pieces combined, they will not take any knockback at all.
                    0
            ));

    public static final Holder<ArmorMaterial> HUNTER_ARMOR_MATERIAL =
            ARMOR_MATERIALS.register("hunter", () -> new ArmorMaterial(
                    // Determines the defense value of this armor material, depending on what armor piece it is.
                    Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.BOOTS, 2);
                        map.put(ArmorItem.Type.LEGGINGS, 5);
                        map.put(ArmorItem.Type.CHESTPLATE, 6);
                        map.put(ArmorItem.Type.HELMET, 2);
                        map.put(ArmorItem.Type.BODY, 6);
                    }),
                    9,
                    SoundEvents.ARMOR_EQUIP_GENERIC,
                    () -> Ingredient.of(Tags.Items.LEATHERS),
                    List.of(
                            new ArmorMaterial.Layer(
                                    ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "hunter_armor")),
                            new ArmorMaterial.Layer(
                                    ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "hunter_armor"))
                    ),
                    0,
                    0
            ));

}
