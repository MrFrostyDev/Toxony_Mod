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
import xyz.yfrostyf.toxony.registries.RecipeRegistry;


public class AlembicRecipe implements Recipe<SingleRecipeInput> {
    final ItemStack outputItem;
    final Ingredient ingredient;
    final int boilTime;

    public AlembicRecipe(ItemStack outputItem, Ingredient ingredient, int boilTime){
        this.outputItem = outputItem;
        this.ingredient = ingredient;
        this.boilTime = boilTime;
    }

    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        if (ingredient == null) return false;

        return ingredient.test(input.item());
    }

    @Override
    public ItemStack assemble(SingleRecipeInput input, HolderLookup.Provider registries) {
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
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.ingredient);
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
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(r -> r.outputItem),
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(r -> r.ingredient),
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
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredient);
            buffer.writeVarInt(recipe.boilTime);
        }

        private static AlembicRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            ItemStack itemstack = ItemStack.STREAM_CODEC.decode(buffer);
            Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            int boilTime = buffer.readVarInt();
            return new AlembicRecipe(itemstack, ingredient, boilTime);
        }
    }
}
