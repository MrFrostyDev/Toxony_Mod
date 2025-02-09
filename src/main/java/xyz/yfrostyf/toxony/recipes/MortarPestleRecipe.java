package xyz.yfrostyf.toxony.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import xyz.yfrostyf.toxony.ToxonyMain;
import xyz.yfrostyf.toxony.registries.RecipeRegistry;

import java.util.ArrayList;

public class MortarPestleRecipe implements Recipe<RecipeWrapper> {
    final ItemStack outputItem;
    final NonNullList<Ingredient> recipeIngredients;
    final ItemStack useItem;

    public MortarPestleRecipe(ItemStack outputItem, ItemStack useItem, NonNullList<Ingredient> recipeIngredients){
        this.outputItem = outputItem;
        this.useItem = useItem;
        this.recipeIngredients = recipeIngredients;
    }

    @Override
    public boolean matches(RecipeWrapper input, Level level) {
        ToxonyMain.LOGGER.info("[MortarPestleRecipe matches called]");
        if (input.size() != this.recipeIngredients.size()) return false;

        var nonEmptyItems = new ArrayList<ItemStack>(input.size());
        for (int i=0; i<input.size(); i++){
            ItemStack item = input.getItem(i);
            if (!item.isEmpty()){
                nonEmptyItems.add(item);
            }
        }
        ToxonyMain.LOGGER.info("[MortarPestleRecipe matches]: IngredientList: {}, RecipeMatcher: {}", nonEmptyItems, RecipeMatcher.findMatches(nonEmptyItems, this.recipeIngredients) != null);
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

    public ItemStack getUseItem(){
        return useItem.copy();
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
                ItemStack.STRICT_CODEC.fieldOf("use").forGetter(r -> r.useItem),
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
            ItemStack.STREAM_CODEC.encode(buffer, recipe.useItem);

            buffer.writeVarInt(recipe.recipeIngredients.size());
            for (Ingredient ingredient : recipe.recipeIngredients) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
            }
        }

        private static MortarPestleRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            ItemStack itemstack = ItemStack.STREAM_CODEC.decode(buffer);
            ItemStack usestack = ItemStack.STREAM_CODEC.decode(buffer);
            int i = buffer.readVarInt();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);
            nonnulllist.replaceAll(in -> Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
            return new MortarPestleRecipe(itemstack, usestack, nonnulllist);
        }
    }


}
