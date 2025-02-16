package ru.blatfan.blatapi.recipe;

import java.util.Set;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.PartialNBTIngredient;

public class NBTIngredient extends PartialNBTIngredient {
    public NBTIngredient(ItemStack stack) {
        super(Set.of(stack.getItem()), stack.getTag());
    }
}