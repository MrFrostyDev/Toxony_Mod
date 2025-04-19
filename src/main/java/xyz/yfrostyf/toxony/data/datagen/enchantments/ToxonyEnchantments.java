package xyz.yfrostyf.toxony.data.datagen.enchantments;

import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.data.datagen.enchantments.effects.Impact;
import xyz.yfrostyf.toxony.data.datagen.enchantments.effects.Refill;
import xyz.yfrostyf.toxony.registries.DataComponentsRegistry;
import xyz.yfrostyf.toxony.registries.ItemRegistry;
import xyz.yfrostyf.toxony.registries.TagRegistry;

import java.util.List;
import java.util.Optional;

public class ToxonyEnchantments {
    public static final ResourceKey<Enchantment> IMPACT =  ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "impact"));
    public static final ResourceKey<Enchantment> REFILL =  ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(ToxonyMain.MOD_ID, "refill"));


    public static void bootstrap(BootstrapContext<Enchantment> context){
        context.register(
                // Define the ResourceKey for our enchantment.
                IMPACT,
                new Enchantment(
                        // The text Component that specifies the enchantment's name.
                        Component.literal("Impact"),
                        // Specify the enchantment definition of for our enchantment.
                        new Enchantment.EnchantmentDefinition(
                                // A HolderSet of Items that the enchantment will be compatible with.
                                BuiltInRegistries.ITEM.getOrCreateTag(TagRegistry.FLAIL_ENCHANTABLE),
                                // An Optional<HolderSet> of items that the enchantment considers "primary".
                                Optional.empty(),
                                // The weight of the enchantment.
                                30,
                                // The maximum level this enchantment can be.
                                3,
                                // The minimum cost of the enchantment. The first parameter is base cost, the second is cost per level.
                                new Enchantment.Cost(3, 1),
                                // The maximum cost of the enchantment. As above.
                                new Enchantment.Cost(4, 2),
                                // The anvil cost of the enchantment.
                                3,
                                // A list of EquipmentSlotGroups that this enchantment has effects in.
                                List.of(EquipmentSlotGroup.MAINHAND)
                        ),
                        // A HolderSet of incompatible other enchantments.
                        HolderSet.empty(),
                        // A DataComponentMap of the enchantment effect components associated with this enchantment and their values.
                        DataComponentMap.builder()
                                .set(DataComponentsRegistry.IMPACT, new Impact())
                                .build()
                )
        );

        context.register(
                REFILL,
                new Enchantment(
                        Component.literal("Refill"),
                        new Enchantment.EnchantmentDefinition(
                                HolderSet.direct(ItemRegistry.POTION_FLASK, ItemRegistry.TOXIN_FLASK),
                                Optional.empty(),
                                30,
                                2,
                                new Enchantment.Cost(3, 1),
                                new Enchantment.Cost(4, 2),
                                3,
                                List.of(EquipmentSlotGroup.ANY)
                        ),
                        HolderSet.empty(),
                        DataComponentMap.builder()
                                .set(DataComponentsRegistry.REFILL, new Refill(0.5F))
                                .build()
                )
        );
    }
}
