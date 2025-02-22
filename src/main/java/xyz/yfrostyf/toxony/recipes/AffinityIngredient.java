package xyz.yfrostyf.toxony.recipes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.DimensionTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionDefaults;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import xyz.yfrostyf.toxony.api.affinity.Affinity;
import xyz.yfrostyf.toxony.api.registries.ToxonyRegistries;
import xyz.yfrostyf.toxony.api.util.AffinityUtil;
import xyz.yfrostyf.toxony.registries.DataComponentsRegistry;
import xyz.yfrostyf.toxony.registries.RecipeRegistry;

import java.util.List;
import java.util.stream.Stream;

public class AffinityIngredient implements ICustomIngredient {
    private final Affinity affinity;

    // The codec for serializing the ingredient.
    public static final MapCodec<AffinityIngredient> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            ToxonyRegistries.AFFINITY_REGISTRY.byNameCodec().fieldOf("affinity").forGetter(e -> e.affinity)
    ).apply(inst, AffinityIngredient::new));

    // Create a stream codec from the regular codec. In some cases, it might make sense to define
    // a new stream codec from scratch.
    public static final StreamCodec<RegistryFriendlyByteBuf, AffinityIngredient> STREAM_CODEC = StreamCodec.composite(
            Affinity.STREAM_CODEC, e -> e.affinity, AffinityIngredient::new);

    public AffinityIngredient(Affinity affinity) {
        this.affinity = affinity;
    }

    @Override
    public boolean test(ItemStack stack) {
        return stack.has(DataComponentsRegistry.POSSIBLE_AFFINITIES.get())
                && AffinityUtil.readAffinityFromIngredientMap(stack).equals(affinity);
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
                .filter(item -> item.components().has(DataComponentsRegistry.POSSIBLE_AFFINITIES.get()) &&
                        item.components().get(DataComponentsRegistry.POSSIBLE_AFFINITIES.get()).contains(affinity))
                .map(ItemStack::new)
                .toList();

        // Return stream variant of the list.
        return stacks.stream();
    }

    @Override
    public IngredientType<?> getType() {
        return RecipeRegistry.AFFINITY.get();
    }
}
