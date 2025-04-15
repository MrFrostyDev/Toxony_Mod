package xyz.yfrostyf.toxony.client.gui.journal;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import xyz.yfrostyf.toxony.api.affinity.Affinity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class JournalPagesBuilder {
    List<PageScreen> pages = new ArrayList<>();
    JournalPages emptyJournal = new JournalPages();
    int indexCount = 0;

    public JournalPagesBuilder() {}

    public JournalPagesBuilder IndexPageScreen(String translateId, Map<String, String> indexedPages){
        pages.add(new IndexPageScreen(translateId, indexedPages, indexCount, emptyJournal));
        indexCount++;
        return this;
    }

    public JournalPagesBuilder ImagePageScreen(String translateId, String locationId){
        pages.add(new ImagePageScreen(translateId, locationId, indexCount, emptyJournal));
        indexCount++;
        return this;
    }

    public JournalPagesBuilder TextSingleItemTopScreen(String translateId, ItemLike itemLike){
        pages.add(new TextSingleItemTopScreen(translateId, indexCount, new ItemStack(itemLike), emptyJournal));
        indexCount++;
        return this;
    }

    public JournalPagesBuilder TextPageScreen(String translateId){
        pages.add(new TextPageScreen(translateId, indexCount, emptyJournal));
        indexCount++;
        return this;
    }

    // Because of erasure with generics, the names are used accordingly
    public JournalPagesBuilder TextCraftingPageScreen(String translateId, Item output, List<List<Item>> inputs){
        pages.add(new TextCraftingPageScreen(translateId, output.getDefaultInstance(), ListItemToItemStack(inputs), indexCount, emptyJournal));
        indexCount++;
        return this;
    }

    public JournalPagesBuilder TextCraftingPageScreenStacks(String translateId, Item output, List<List<ItemStack>> inputs){
        pages.add(new TextCraftingPageScreen(translateId, output.getDefaultInstance(), inputs, indexCount, emptyJournal));
        indexCount++;
        return this;
    }

    // Because of erasure with generics, the names are used accordingly
    public JournalPagesBuilder TextCraftingPageScreen(String translateId, ItemStack output, List<List<Item>> inputs){
        pages.add(new TextCraftingPageScreen(translateId, output, ListItemToItemStack(inputs), indexCount, emptyJournal));
        indexCount++;
        return this;
    }

    public JournalPagesBuilder TextCraftingPageScreenIngredients(String translateId, Item output, List<Ingredient> inputs){
        pages.add(new TextCraftingPageScreen(translateId, output.getDefaultInstance(), ListIngredientsToItemStack(inputs), indexCount, emptyJournal));
        indexCount++;
        return this;
    }

    public JournalPagesBuilder TextCraftingPageScreenItem(String translateId, Item output, List<Item> inputs){
        List<List<Item>> newList = new ArrayList<>();
        for(Item item : inputs) newList.add(List.of(item));
        return TextCraftingPageScreen(translateId, output, newList);
    }

    public JournalPagesBuilder TextCraftingPageScreenStack(String translateId, Item output, List<ItemStack> inputs){
        List<List<ItemStack>> newList = new ArrayList<>();
        for(ItemStack item : inputs) newList.add(List.of(item));
        return TextCraftingPageScreenStacks(translateId, output, newList);
    }

    public JournalPagesBuilder TextMortarPageScreen(String translateId, Item output, List<ItemLike> inputs){
        return TextMortarPageScreenIngredients(translateId, output, inputs.stream().map(Ingredient::of).toList());
    }

    public JournalPagesBuilder TextMortarPageScreenIngredients(String translateId, Item output, List<Ingredient> inputs){
        pages.add(new TextMortarPageScreen(translateId, output.getDefaultInstance(), ListIngredientsToItemStack(inputs), indexCount, emptyJournal));
        indexCount++;
        return this;
    }

    public JournalPagesBuilder TextCruciblePageScreenIngredient(String translateId, Item output, Ingredient input){
        pages.add(new TextCruciblePageScreen(translateId, output.getDefaultInstance(), Arrays.asList(input.getItems()), indexCount, emptyJournal));
        indexCount++;
        return this;
    }

    public JournalPagesBuilder TextAlembicPageScreenIngredients(String translateId, Item output, List<Ingredient> inputs){
        pages.add(new TextAlembicPageScreen(translateId, output.getDefaultInstance(), ListIngredientsToItemStack(inputs), indexCount, emptyJournal));
        indexCount++;
        return this;
    }

    public JournalPagesBuilder GraftingPageScreen(String translateId, Map<Pair<ItemStack, ItemStack>, Affinity> inputs){
        pages.add(new GraftingPageScreen(translateId, inputs, indexCount, emptyJournal));
        indexCount++;
        return this;
    }


    public JournalPages build(String translateId){
        return emptyJournal.fill(ImmutableList.copyOf(pages), translateId);
    }

    public static List<List<ItemStack>> ListItemToItemStack(List<List<Item>> inputs){
        List<List<ItemStack>> newList = new ArrayList<>();
        for(List<Item> inputItem : inputs) newList.add(inputItem.stream().map(Item::getDefaultInstance).toList());
        return newList;
    }

    public static List<List<ItemStack>> ListIngredientsToItemStack(List<Ingredient> inputs){
        List<List<ItemStack>> newList = new ArrayList<>();
        for(Ingredient inputItem : inputs) newList.add(Arrays.asList(inputItem.getItems()));
        return newList;
    }
}
