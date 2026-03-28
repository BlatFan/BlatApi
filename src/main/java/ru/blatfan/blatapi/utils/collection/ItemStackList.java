package ru.blatfan.blatapi.utils.collection;

import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemStackList extends ArrayList<ItemStack> {
    public ItemStackList(boolean empty){
        this(empty ? List.of() : List.of(ItemStack.EMPTY));
    }
    public ItemStackList(List<ItemStack> list){
        super(list);
    }
}