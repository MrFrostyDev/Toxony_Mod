package xyz.yfrostyf.toxony.recipes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import xyz.yfrostyf.toxony.registries.RecipeRegistry;

import java.util.ArrayList;
import java.util.Optional;

public class MortarPestleRecipe implements Recipe<RecipeWrapper> {
    final ItemStack outputItem;
    final NonNullList<Ingredient> recipeIngredients;
    final Optional<ItemStack> useItem;

    public MortarPestleRecipe(ItemStack outputItem, Optional<ItemStack> useItem, NonNullList<Ingredient> recipeIngredients){
        this.outputItem = outputItem;
        this.useItem = useItem;
        this.recipeIngredients = recipeIngredients;
    }

    @Override
    public boolean matches(RecipeWrapper input, Level level) {
        if (input.size() != this.recipeIngredients.size()) return false;

        var nonEmptyItems = new ArrayList<ItemStack>(input.size());
        for (int i=0; i<input.size(); i++){
            ItemStack item = input.getItem(i);
            if (!item.isEmpty()){
                nonEmptyItems.add(item);
            }
        }
        return RecipeMatcher.findMatches(nonEmptyItems, this.recipeIngredients) != null;
    }

    @Override
    public ItemStack assemble(RecipeWrapper input, HolderLookup.Provider registries) {
        return outputItem.copy();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= this.recipeIngredients.size();
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return outputItem.copy();
    }

    public ItemStack getResultItem() {
        return outputItem.copy();
    }

    public Optional<ItemStack> getUseItem(){
        return useItem;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.recipeIngredients;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeRegistry.MORTAR_PESTLE_RECIPE.get();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.MORTAR_PESTLE_SERIALIZER.get();
    }

    public static class Serializer implements RecipeSerializer<MortarPestleRecipe>{
        public static final MapCodec<MortarPestleRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(r -> r.outputItem),
                ItemStack.STRICT_SINGLE_ITEM_CODEC.optionalFieldOf("use").forGetter(r -> r.useItem),
                Ingredient.LIST_CODEC_NONEMPTY.fieldOf("ingredients").xmap(ingredients -> {
                    NonNullList<Ingredient> nonNullList = NonNullList.create();
                    nonNullList.addAll(ingredients);
                    return nonNullList;
                }, ingredients -> ingredients).forGetter(MortarPestleRecipe::getIngredients)
        ).apply(inst, MortarPestleRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, MortarPestleRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        public Serializer() {}

        @Override
        public MapCodec<MortarPestleRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, MortarPestleRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, MortarPestleRecipe recipe) {
            ItemStack.STREAM_CODEC.encode(buffer, recipe.outputItem);
            ItemStack.STREAM_CODEC.apply(ByteBufCodecs::optional).encode(buffer, recipe.useItem);

            buffer.writeVarInt(recipe.recipeIngredients.size());
            for (Ingredient ingredient : recipe.recipeIngredients) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
            }
        }

        private static MortarPestleRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            ItemStack itemstack = ItemStack.STREAM_CODEC.decode(buffer);
            Optional<ItemStack> usestack = ItemStack.STREAM_CODEC.apply(ByteBufCodecs::optional).decode(buffer);
            int i = buffer.readVarInt();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);
            nonnulllist.replaceAll(in -> Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
            return new MortarPestleRecipe(itemstack, usestack, nonnulllist);
        }
    }


}