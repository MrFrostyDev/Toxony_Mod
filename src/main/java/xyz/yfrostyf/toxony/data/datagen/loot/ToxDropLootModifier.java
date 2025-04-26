package xyz.yfrostyf.toxony.data.datagen.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

import java.util.Random;

public class ToxDropLootModifier extends LootModifier {
    private static Random RANDOM = new Random();
    public static final MapCodec<ToxDropLootModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
            // LootModifier#codecStart adds the conditions field.
            LootModifier.codecStart(inst).and(inst.group(
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("drop_item").forGetter(e -> e.dropItem),
                    Codec.INT.fieldOf("min").forGetter(e -> e.min),
                    Codec.INT.fieldOf("max").forGetter(e -> e.max)
            )).apply(inst, ToxDropLootModifier::new)
    );

    private final Item dropItem;
    private final int min;
    private final int max;

    /**
     * Constructs a LootModifier.
     *
     * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
     */
    public ToxDropLootModifier(LootItemCondition[] conditionsIn, Item dropItem, int min, int max) {
        super(conditionsIn);
        this.dropItem = dropItem;
        this.min = min;
        this.max = max;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        int newMin = this.min + Math.max(getAttackerLootingLevel(context) - 1, 0);
        int newMax = this.max + getAttackerLootingLevel(context);

        ItemStack stack = new ItemStack(dropItem, RANDOM.nextInt((newMax+1)-newMin) + newMin);
        generatedLoot.add(stack);
        return generatedLoot;
    }

    private static int getAttackerLootingLevel(LootContext context){
        if(!context.hasParam(LootContextParams.ATTACKING_ENTITY)
                || !(context.getParam(LootContextParams.ATTACKING_ENTITY) instanceof Player player)) return 0;

        return player.getMainHandItem()
                .getEnchantmentLevel(context.getLevel()
                        .registryAccess()
                        .holderOrThrow(Enchantments.LOOTING)
                );
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
