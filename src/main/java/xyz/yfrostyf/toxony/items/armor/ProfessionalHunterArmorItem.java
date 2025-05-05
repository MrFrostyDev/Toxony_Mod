package xyz.yfrostyf.toxony.items.armor;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.registries.AttributeRegistry;

public class ProfessionalHunterArmorItem extends ArmorItem {

    public ProfessionalHunterArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        int defense = this.material.value().getDefense(type);
        float toughness = this.material.value().toughness();

        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        EquipmentSlotGroup equipslotgroup = EquipmentSlotGroup.bySlot(type.getSlot());
        ResourceLocation resourcelocation = ResourceLocation.withDefaultNamespace("armor." + type.getName());
        ResourceLocation resourcelocationtoxony = ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID,"armor." + type.getName());
        builder.add(
                Attributes.ARMOR, new AttributeModifier(resourcelocation, defense, AttributeModifier.Operation.ADD_VALUE), equipslotgroup
        );
        builder.add(
                Attributes.ARMOR_TOUGHNESS, new AttributeModifier(resourcelocation, toughness, AttributeModifier.Operation.ADD_VALUE), equipslotgroup
        );
        float f1 = this.material.value().knockbackResistance();
        if (f1 > 0.0F) {
            builder.add(
                    Attributes.KNOCKBACK_RESISTANCE,
                    new AttributeModifier(resourcelocation, f1, AttributeModifier.Operation.ADD_VALUE),
                    equipslotgroup
            );
        }

        builder.add(
                AttributeRegistry.EFFECT_REDUCTION,
                new AttributeModifier(resourcelocationtoxony, getEffectDurationValue(equipslotgroup), AttributeModifier.Operation.ADD_VALUE),
                equipslotgroup
        );

        return builder.build();
    }

    private static double getEffectDurationValue(EquipmentSlotGroup equipSlot){
        return switch (equipSlot) {
            case EquipmentSlotGroup.HEAD -> 2;
            case EquipmentSlotGroup.CHEST -> 2;
            case EquipmentSlotGroup.LEGS -> 2;
            case EquipmentSlotGroup.FEET -> 2;
            default -> 0;
        };
    }
}
