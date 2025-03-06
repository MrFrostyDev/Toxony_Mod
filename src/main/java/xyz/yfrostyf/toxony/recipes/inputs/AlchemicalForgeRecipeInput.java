package xyz.yfrostyf.toxony.recipes.inputs;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.List;


public class AlchemicalForgeRecipeInput implements RecipeInput {
    ItemStack mainItem;
    NonNullList<ItemStack> solutions;
    List<ItemStack> auxItems;

    public AlchemicalForgeRecipeInput(ItemStack mainItem, NonNullList<ItemStack> solutions, List<ItemStack> auxItems){
        this.mainItem = mainItem;
        this.solutions = solutions;
        this.auxItems = auxItems;
    }

    @Override
    public ItemStack getItem(int index) {
        switch (index){
            case 0:
                return mainItem;
            case 1:
                return auxItems.getFirst();
            case 2:
                return auxItems.getLast();
            case 3:
                return solutions.get(3);
            case 4:
                return solutions.get(4);
            case 5:
                return solutions.get(5);
            default:
                return mainItem;
        }
    }

    public ItemStack getMainItem(){
        return mainItem;
    }

    public List<ItemStack> getSolutions(){
        return solutions;
    }

    public List<ItemStack> getAuxItems(){
        return auxItems;
    }

    @Override
    public int size() {
        return 6;
    }

    @Override
    public boolean isEmpty() {
        return this.mainItem == null && solutions.isEmpty() && auxItems.isEmpty();
    }
}
