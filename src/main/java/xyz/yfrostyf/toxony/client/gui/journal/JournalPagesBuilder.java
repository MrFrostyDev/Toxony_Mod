package xyz.yfrostyf.toxony.client.gui.journal;

import com.google.common.collect.ImmutableList;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class JournalPagesBuilder {
    List<PageScreen> pages = new ArrayList<>();
    JournalPages emptyJournal = new JournalPages();
    int indexCount = 0;

    public JournalPagesBuilder() {}

    public JournalPagesBuilder IndexPageScreen(String translateId, Map<String, String> indexedPages, ChatFormatting... formatting){
        pages.add(new IndexPageScreen(translateId, indexedPages, formatting, indexCount, emptyJournal));
        indexCount++;
        return this;
    }

    public JournalPagesBuilder ImagePageScreen(String translateId, String locationId){
        pages.add(new ImagePageScreen(translateId, locationId, indexCount, emptyJournal));
        indexCount++;
        return this;
    }

    public JournalPagesBuilder TextPageScreen(String translateId, ChatFormatting... formatting){
        pages.add(new TextPageScreen(translateId, formatting, indexCount, emptyJournal));
        indexCount++;
        return this;
    }

    // Because of erasure with generics, the names are used accordingly
    public JournalPagesBuilder TextCraftingPageScreen(String translateId, Item output, List<List<Item>> inputs, ChatFormatting... formatting){
        pages.add(new TextCraftingPageScreen(translateId, formatting, output.getDefaultInstance(), ListItemToItemStack(inputs), indexCount, emptyJournal));
        indexCount++;
        return this;
    }

    public JournalPagesBuilder TextCraftingPageScreenIngredients(String translateId, Item output, List<Ingredient> inputs, ChatFormatting... formatting){
        pages.add(new TextCraftingPageScreen(translateId, formatting, output.getDefaultInstance(), ListIngredientsToItemStack(inputs), indexCount, emptyJournal));
        indexCount++;
        return this;
    }

    public JournalPagesBuilder TextCraftingPageScreenItem(String translateId, Item output, List<Item> inputs, ChatFormatting... formatting){
        List<List<Item>> newList = new ArrayList<>();
        for(Item item : inputs) newList.add(List.of(item));
        return TextCraftingPageScreen(translateId, output, newList, formatting);
    }

    public JournalPagesBuilder TextMortarPageScreenIngredients(String translateId, Item output, List<Ingredient> inputs, ChatFormatting... formatting){
        pages.add(new TextMortarPageScreen(translateId, formatting, output.getDefaultInstance(), ListIngredientsToItemStack(inputs), indexCount, emptyJournal));
        indexCount++;
        return this;
    }

    public JournalPagesBuilder TextCruciblePageScreenIngredient(String translateId, Item output, Ingredient input, ChatFormatting... formatting){
        pages.add(new TextCruciblePageScreen(translateId, formatting, output.getDefaultInstance(), Arrays.asList(input.getItems()), indexCount, emptyJournal));
        indexCount++;
        return this;
    }

    public JournalPagesBuilder TextAlembicPageScreenIngredients(String translateId, Item output, List<Ingredient> inputs, ChatFormatting... formatting){
        pages.add(new TextAlembicPageScreen(translateId, formatting, output.getDefaultInstance(), ListIngredientsToItemStack(inputs), indexCount, emptyJournal));
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
