package ru.blatfan.blatapi.common.cap;

import lombok.Getter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

@Getter
public class FloatEnergyStorage extends CustomEnergyStorage {
    private int originalCapacity;
    private int originalMaxReceive;
    private int originalMaxExtract;
    
    public FloatEnergyStorage(int capacity, int maxTransfer, String tag) {
        super(capacity, maxTransfer, tag);
        this.originalCapacity = capacity;
        this.originalMaxReceive = maxTransfer;
        this.originalMaxExtract = maxTransfer;
    }
    
    public FloatEnergyStorage(int capacity, int maxTransfer) {
        this(capacity, maxTransfer, "energy");
    }
    
    public FloatEnergyStorage(int capacity, String tag) {
        this(capacity, capacity / 4, tag);
    }
    
    public FloatEnergyStorage(int capacity) {
        this(capacity, capacity / 4);
    }
    
    public void setCapacity(int newCapacity) {
        this.capacity = newCapacity;
    }
    
    public void setMaxReceive(int newMaxReceive) {
        this.maxReceive = newMaxReceive;
    }
    
    public void setMaxExtract(int newMaxExtract) {
        this.maxExtract = newMaxExtract;
    }
    
    public int getMaxReceive() {
        return maxReceive;
    }
    
    public int getMaxExtract() {
        return maxExtract;
    }
    
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = super.serializeNBT();
        tag.putInt("capacity", capacity);
        tag.putInt("originalCapacity", originalCapacity);
        tag.putInt("maxReceive", maxReceive);
        tag.putInt("originalMaxReceive", originalMaxReceive);
        tag.putInt("maxExtract", maxExtract);
        tag.putInt("originalMaxExtract", originalMaxExtract);
        return tag;
    }
    
    @Override
    public void deserializeNBT(Tag nbt) {
        super.deserializeNBT(nbt);
        CompoundTag tag = (CompoundTag) nbt;
        capacity = tag.getInt("capacity");
        originalCapacity = tag.getInt("originalCapacity");
        maxReceive = tag.getInt("maxReceive");
        originalMaxReceive = tag.getInt("originalMaxReceive");
        maxExtract = tag.getInt("maxExtract");
        originalMaxExtract = tag.getInt("originalMaxExtract");
    }
}
