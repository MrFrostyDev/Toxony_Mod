package xyz.yfrostyf.toxony.recipes;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import xyz.yfrostyf.toxony.registries.DataComponentsRegistry;
import xyz.yfrostyf.toxony.registries.RecipeRegistry;

import java.util.List;
import java.util.stream.Stream;

public class PossibleAffinityIngredient implements ICustomIngredient {
    public static final PossibleAffinityIngredient INSTANCE = new PossibleAffinityIngredient();

    public static final MapCodec<PossibleAffinityIngredient> CODEC = MapCodec.unit(() -> INSTANCE);
    public static final StreamCodec<ByteBuf, PossibleAffinityIngredient> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    public PossibleAffinityIngredient() {}

    @Override
    public boolean test(ItemStack stack) {
        return stack.has(DataComponentsRegistry.POSSIBLE_AFFINITIES.get());
    }

    // Determines whether this ingredient performs NBT or data component matching (false) or not (true).
    // Also determines whether a stream codec is used for syncing, more on this later.
    // We query enchantments on the stack, therefore our ingredient is not simple.
    @Override
    public boolean isSimple() {
        return false;
    }

    // Returns a stream of items that match this ingredient. Mostly for display purposes.
    // There's a few good practices to follow here:
    // - Always include at least one item stack, to prevent accidental recognition as empty.
    // - Include each accepted Item at least once.
    // - If #isSimple is true, this should be exact and contain every item stack that matches.
    //   If not, this should be as exact as possible, but doesn't need to be super accurate.
    @Override
    public Stream<ItemStack> getItems() {
        // Get a list of item stacks that have possible affinities and they contain the specified affinity.
        List<ItemStack> stacks = BuiltInRegistries.ITEM
                .stream()
                .filter(item -> item.components().has(DataComponentsRegistry.POSSIBLE_AFFINITIES.get()))
                .map(ItemStack::new)
                .toList();

        // Return stream variant of the list.
        return stacks.stream();
    }

    @Override
    public IngredientType<?> getType() {
        return RecipeRegistry.POSSIBLE_AFFINITY.get();
    }
}
