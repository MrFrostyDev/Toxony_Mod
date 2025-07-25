package xyz.yfrostyf.toxony.data.datagen.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

import java.util.Random;

public class PiglinBarterLootModifier extends LootModifier {
    private static final Random RANDOM = new Random();
    public static final MapCodec<PiglinBarterLootModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
            // LootModifier#codecStart adds the conditions field.
            LootModifier.codecStart(inst).and(inst.group(
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("drop_item").forGetter(e -> e.dropItem),
                    Codec.INT.fieldOf("min").forGetter(e -> e.min),
                    Codec.INT.fieldOf("max").forGetter(e -> e.max)
            )).apply(inst, PiglinBarterLootModifier::new)
    );

    private final Item dropItem;
    private final int min;
    private final int max;

    /**
     * Constructs a LootModifier.
     *
     * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
     */
    public PiglinBarterLootModifier(LootItemCondition[] conditionsIn, Item dropItem, int min, int max) {
        super(conditionsIn);
        this.dropItem = dropItem;
        this.min = min;
        this.max = max;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        int newMin = this.min;
        int newMax = this.max;

        if(generatedLoot.stream().anyMatch(i -> i.is(Items.FIRE_CHARGE))){
            if(RANDOM.nextInt(2) == 0){
                generatedLoot.clear();
                ItemStack stack = new ItemStack(dropItem, RANDOM.nextInt((newMax+1)-newMin) + newMin);
                generatedLoot.add(stack);
            }
        }
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
