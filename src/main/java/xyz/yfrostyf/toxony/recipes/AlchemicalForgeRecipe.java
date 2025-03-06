package xyz.yfrostyf.toxony.recipes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import xyz.yfrostyf.toxony.api.affinity.Affinity;
import xyz.yfrostyf.toxony.api.util.AffinityUtil;
import xyz.yfrostyf.toxony.recipes.inputs.AlchemicalForgeRecipeInput;
import xyz.yfrostyf.toxony.registries.AffinityRegistry;
import xyz.yfrostyf.toxony.registries.DataComponentsRegistry;
import xyz.yfrostyf.toxony.registries.RecipeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AlchemicalForgeRecipe implements Recipe<AlchemicalForgeRecipeInput> {
    final ItemStack outputItem;
    final Ingredient mainIngredientRecipe;
    final List<Affinity> affinitiesRecipe;
    final List<Ingredient> auxIngredientsRecipe;

    public AlchemicalForgeRecipe(ItemStack outputItem,
                                 Ingredient mainIngredientRecipe,
                                 List<Affinity> affinitiesRecipe,
                                 List<Ingredient> auxIngredientsRecipe){
        this.outputItem = outputItem;
        this.mainIngredientRecipe = mainIngredientRecipe;
        this.affinitiesRecipe = affinitiesRecipe;
        this.auxIngredientsRecipe = auxIngredientsRecipe;
    }

    @Override
    public boolean matches(AlchemicalForgeRecipeInput input, Level level) {
        if(input.getSolutions().isEmpty() || input.getMainItem().isEmpty()) return false;

        List<ItemStack> inputAuxIngredients = new ArrayList<>(input.getAuxItems());
        List<Affinity> inputAffinities = input.getSolutions().stream()
                .map(holder ->
                        AffinityUtil.readAffinityFromIngredientMap(new ItemStack(holder.getOrDefault(DataComponentsRegistry.AFFINITY_STORED_ITEM, ItemStack.EMPTY.getItemHolder())), level))
                .collect(Collectors.toCollection(ArrayList::new));

        for(Affinity affinity : affinitiesRecipe){
            if(inputAffinities.contains(affinity)){
                inputAffinities.remove(affinity);
                continue;
            }
            return false;
        }

        for(Ingredient ingredient : auxIngredientsRecipe){
            boolean found = false;
            for(int i=0; i<inputAuxIngredients.size();i++){
                if(ingredient.test(inputAuxIngredients.get(i))){
                    inputAuxIngredients.remove(i);
                    found = true;
                    break;
                }
            }
            if(!found) return false;
        }

        return mainIngredientRecipe.test(input.getMainItem());
    }

    @Override
    public ItemStack assemble(AlchemicalForgeRecipeInput input, HolderLookup.Provider registries) {
        return outputItem.copy();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height < 1;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return outputItem.copy();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.createWithCapacity(3);
        nonnulllist.add(this.mainIngredientRecipe);
        for(Ingredient ingredient : auxIngredientsRecipe){
            nonnulllist.add(ingredient);
        }
        return nonnulllist;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeRegistry.ALCHEMICAL_FORGE_RECIPE.get();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.ALCHEMICAL_FORGE_SERIALIZER.get();
    }

    public static class Serializer implements RecipeSerializer<AlchemicalForgeRecipe>{
        public static final MapCodec<AlchemicalForgeRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                ItemStack.STRICT_SINGLE_ITEM_CODEC.fieldOf("result").forGetter(r -> r.outputItem),
                Ingredient.CODEC_NONEMPTY.fieldOf("main").forGetter(r -> r.mainIngredientRecipe),
                AffinityRegistry.AFFINITIES.getRegistry().get().byNameCodec().listOf().fieldOf("affinities").forGetter(r -> r.affinitiesRecipe),
                Ingredient.LIST_CODEC.fieldOf("auxiliary").forGetter(r -> r.auxIngredientsRecipe)
        ).apply(inst, AlchemicalForgeRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, AlchemicalForgeRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        public Serializer() {}

        @Override
        public MapCodec<AlchemicalForgeRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AlchemicalForgeRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, AlchemicalForgeRecipe recipe) {
            ItemStack.STREAM_CODEC.encode(buffer, recipe.outputItem);
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.mainIngredientRecipe);
            Affinity.STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buffer, recipe.affinitiesRecipe);
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buffer, recipe.auxIngredientsRecipe);
        }

        private static AlchemicalForgeRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            ItemStack itemstack = ItemStack.STREAM_CODEC.decode(buffer);
            Ingredient main = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            List<Affinity> affinities = Affinity.STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buffer);
            List<Ingredient> aux = Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buffer);
            return new AlchemicalForgeRecipe(itemstack, main, affinities, aux);
        }
    }
}
