package xyz.yfrostyf.toxony.data.datagen;

import net.minecraft.advancements.critereon.EntityEquipmentPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MobEffectsPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.data.datagen.loot.ToxDropLootModifier;
import xyz.yfrostyf.toxony.registries.ItemRegistry;
import xyz.yfrostyf.toxony.registries.MobEffectRegistry;
import xyz.yfrostyf.toxony.registries.TagRegistry;

import java.util.concurrent.CompletableFuture;


public class ToxonyGlobalLootModifierProvider extends GlobalLootModifierProvider {

    public ToxonyGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, ToxonyMain.MOD_ID);
    }

    @Override
    protected void start() {
        dropUniqueWhilePoisoned("wolf_unique_tox_drop", ItemRegistry.WOLF_TOOTH.get(), 0, 1);
    }

    private void dropUniqueWhilePoisoned(String name, Item dropItem, int min, int max){
        EntityEquipmentPredicate equipmentPredicate = EntityEquipmentPredicate.Builder.equipment().mainhand(
                ItemPredicate.Builder.item().of(TagRegistry.SCALPEL_ITEM_TAG)).build();

        LootItemCondition.Builder attackerHasScalpelPredicate = LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.ATTACKER, EntityPredicate.Builder.entity()
                .equipment(equipmentPredicate));

        LootItemCondition.Builder poisonedEntityCondition = LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity()
                .of(EntityType.WOLF).effects(MobEffectsPredicate.Builder.effects().and(MobEffects.POISON)));

        LootItemCondition.Builder toxinedEntityCondition = LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity()
                .of(EntityType.WOLF).effects(MobEffectsPredicate.Builder.effects().and(MobEffectRegistry.TOXIN)));

        this.add(name,
                new ToxDropLootModifier(new LootItemCondition[]{
                        AnyOfCondition.anyOf(poisonedEntityCondition, toxinedEntityCondition).build(),
                        attackerHasScalpelPredicate.build()
                },
                        dropItem, min, max
                )
        );
    }
}
