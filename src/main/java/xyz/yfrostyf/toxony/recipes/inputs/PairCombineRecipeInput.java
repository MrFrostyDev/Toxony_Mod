package xyz.yfrostyf.toxony.recipes.inputs;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;


public class PairCombineRecipeInput implements RecipeInput {
    ItemStack item;
    ItemStack itemToConvert;

    public PairCombineRecipeInput(ItemStack item, ItemStack itemToConvert){
        this.item = item;
        this.itemToConvert = itemToConvert;
    }

    @Override
    public ItemStack getItem(int index) {
        return item;
    }

    public ItemStack getItemToConvert(){
        return itemToConvert;
    }

    @Override
    public int size() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return this.getItem(0).isEmpty() && getItemToConvert().isEmpty();
    }
}
