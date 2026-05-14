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
        if (this.energy > this.capacity) this.energy = this.capacity;
    }
    
    public void setMaxReceive(int newMaxReceive) { this.maxReceive = newMaxReceive; }
    public void setMaxExtract(int newMaxExtract) { this.maxExtract = newMaxExtract; }
    public int getMaxReceive() { return this.maxReceive; }
    public int getMaxExtract() { return this.maxExtract; }
    
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbtTag = super.serializeNBT();
        nbtTag.putInt("originalCapacity", this.originalCapacity);
        nbtTag.putInt("originalMaxReceive", this.originalMaxReceive);
        nbtTag.putInt("originalMaxExtract", this.originalMaxExtract);
        return nbtTag;
    }
    
    @Override
    public void deserializeNBT(Tag nbt) {
        if (!(nbt instanceof CompoundTag compound)) return;
        super.deserializeNBT(nbt);
        this.originalCapacity = compound.getInt("originalCapacity");
        this.originalMaxReceive = compound.getInt("originalMaxReceive");
        this.originalMaxExtract = compound.getInt("originalMaxExtract");
    }
}