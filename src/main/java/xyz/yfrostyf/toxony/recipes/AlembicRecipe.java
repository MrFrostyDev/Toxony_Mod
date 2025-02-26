package xyz.yfrostyf.toxony.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import xyz.yfrostyf.toxony.recipes.inputs.PairCombineRecipeInput;
import xyz.yfrostyf.toxony.registries.RecipeRegistry;


public class AlembicRecipe implements Recipe<PairCombineRecipeInput> {
    final ItemStack outputItem;
    final Ingredient recipeIngredient;
    final Ingredient recipeIngredientToConvert;
    final int boilTime;

    public AlembicRecipe(ItemStack outputItem, Ingredient recipeIngredient, Ingredient recipeIngredientToConvert, int boilTime){
        this.outputItem = outputItem;
        this.recipeIngredient = recipeIngredient;
        this.recipeIngredientToConvert = recipeIngredientToConvert;
        this.boilTime = boilTime;
    }

    @Override
    public boolean matches(PairCombineRecipeInput input, Level level) {
        if(input.getItem(0).isEmpty() || input.getItemToConvert().isEmpty()) return false;
        return recipeIngredient.test(input.getItem(0)) && recipeIngredientToConvert.test(input.getItemToConvert());
    }

    @Override
    public ItemStack assemble(PairCombineRecipeInput input, HolderLookup.Provider registries) {
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
        NonNullList<Ingredient> nonnulllist = NonNullList.createWithCapacity(2);
        nonnulllist.add(this.recipeIngredient);
        nonnulllist.add(this.recipeIngredientToConvert);
        return nonnulllist;
    }

    public int getBoilTime() {
        return this.boilTime;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeRegistry.ALEMBIC_RECIPE.get();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.ALEMBIC_SERIALIZER.get();
    }

    public static class Serializer implements RecipeSerializer<AlembicRecipe>{
        public static final MapCodec<AlembicRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                ItemStack.STRICT_SINGLE_ITEM_CODEC.fieldOf("result").forGetter(r -> r.outputItem),
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(r -> r.recipeIngredient),
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient_to_convert").forGetter(r -> r.recipeIngredientToConvert),
                Codec.INT.fieldOf("boiltime").orElse(200).forGetter(r -> r.boilTime)
        ).apply(inst, AlembicRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, AlembicRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        public Serializer() {}

        @Override
        public MapCodec<AlembicRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AlembicRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, AlembicRecipe recipe) {
            ItemStack.STREAM_CODEC.encode(buffer, recipe.outputItem);
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.recipeIngredient);
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.recipeIngredientToConvert);
            buffer.writeVarInt(recipe.boilTime);
        }

        private static AlembicRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            ItemStack itemstack = ItemStack.STREAM_CODEC.decode(buffer);
            Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            Ingredient ingredientToConvert = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            int boilTime = buffer.readVarInt();
            return new AlembicRecipe(itemstack, ingredient, ingredientToConvert, boilTime);
        }
    }
}
