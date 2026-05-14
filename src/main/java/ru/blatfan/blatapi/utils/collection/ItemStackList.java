package ru.blatfan.blatapi.utils.collection;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ItemStackList extends ArrayList<ItemStack> {
    public ItemStackList(boolean empty){
        this(empty ? List.of() : List.of(ItemStack.EMPTY));
    }
    private ItemStackList(List<ItemStack> list){
        super(list);
    }
    private ItemStackList() {}
    
    public static ItemStackList of(boolean empty) {
        return new ItemStackList(empty ? List.of() : List.of(ItemStack.EMPTY));
    }
    
    public static ItemStackList of(List<ItemStack> list) {
        ItemStackList newList = new ItemStackList();
        newList.addAll(list);
        return newList;
    }
    
    /**
     * Create a ItemStackList of the provided stacks, automatically removing any empty stacks.
     *
     * @param stacks The {@link ItemStack}s, may be empty but not null
     * @return A new list of all non-empty (valid) stacks
     */
    public static ItemStackList of(ItemStack... stacks) {
        ItemStackList newList = new ItemStackList();
        Collections.addAll(newList, stacks);
        return newList;
    }
    
    /**
     * Create a ItemStackList from the non-empty (valid) stacks in the provided inventory.
     *
     * @param inventory The {@link Container}
     * @return A new list of all non-empty stacks from the inventory
     * @since 3.0.6
     */
    public static ItemStackList from(Container inventory) {
        ItemStackList newList = new ItemStackList();
        for (int i = 0; i < inventory.getContainerSize(); ++i)
            newList.add(inventory.getItem(i));
        return newList;
    }
    
    public static ItemStackList from(Iterable<Tag> tagList) {
        ItemStackList newList = new ItemStackList();
        for (Tag nbt : tagList)
            if (nbt instanceof CompoundTag)
                newList.add(ItemStack.of((CompoundTag) nbt));
        return newList;
    }
    
    public ItemStack firstOfType(Class<?> itemClass) {
        return firstMatch(itemClassMatcher(itemClass));
    }
    
    public ItemStack firstMatch(Predicate<ItemStack> predicate) {
        return stream().filter(predicate).findFirst().orElse(ItemStack.EMPTY);
    }
    
    public ItemStack uniqueOfType(Class<?> itemClass) {
        return uniqueMatch(itemClassMatcher(itemClass));
    }
    
    public ItemStack uniqueMatch(Predicate<ItemStack> predicate) {
        return stream().filter(predicate).collect(Collectors.collectingAndThen(Collectors.toList(),
            list -> list.size() == 1 ? list.get(0) : ItemStack.EMPTY));
    }
    
    public Collection<ItemStack> allOfType(Class<?> itemClass) {
        return allMatches(itemClassMatcher(itemClass));
    }
    
    public Collection<ItemStack> allMatches(Predicate<ItemStack> predicate) {
        return stream().filter(predicate).collect(Collectors.toList());
    }
    
    public int countOfType(Class<?> itemClass) {
        return countOfMatches(itemClassMatcher(itemClass));
    }
    
    public int countOfMatches(Predicate<ItemStack> predicate) {
        return (int) stream().filter(predicate).count();
    }
    
    private static Predicate<ItemStack> itemClassMatcher(Class<?> itemClass) {
        return stack -> itemClass.isInstance(stack.getItem());
    }
    
    @Override
    public boolean add(ItemStack itemStack) {
        return !itemStack.isEmpty() && super.add(itemStack);
    }
    
    @Override
    public boolean addAll(Collection<? extends ItemStack> c) {
        boolean added = false;
        for (ItemStack stack : c)
            if (!stack.isEmpty())
                added |= super.add(stack);
        return added;
    }
    
    @Override
    public boolean addAll(int index, Collection<? extends ItemStack> c) {
        boolean added = false;
        for (ItemStack stack : c)
            if (!stack.isEmpty()) {
                super.add(index, stack);
                added = true;
            }
        return added;
    }
    
    @Override
    public void add(int index, ItemStack element) {
        if (!element.isEmpty())
            super.add(index, element);
    }
}