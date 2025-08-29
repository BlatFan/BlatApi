package ru.blatfan.blatapi.utils;

import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;

public interface ICapacity<T> {
    void sync(Entity entity);
    
    Tag toNBT();
    void fromNBT(Tag tag);
    
    void copy(T instance);
    T clone();
}