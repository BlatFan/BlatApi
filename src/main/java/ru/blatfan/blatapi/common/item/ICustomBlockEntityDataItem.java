package ru.blatfan.blatapi.common.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public interface ICustomBlockEntityDataItem {
    CompoundTag getCustomBlockEntityData(ItemStack stack, CompoundTag nbt);
}
