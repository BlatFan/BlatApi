package ru.blatfan.blatapi.common.cap;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.energy.EnergyStorage;

public class CustomEnergyStorage extends EnergyStorage {
  public final String tag;
  
  public CustomEnergyStorage(int capacity, int maxTransfer, String tag) {
    super(capacity, maxTransfer, maxTransfer);
    this.tag = tag;
  }
  
  public CustomEnergyStorage(int capacity, int maxTransfer) {
    this(capacity, maxTransfer, "energy");
  }
  
  public CustomEnergyStorage(int capacity, String tag) {
    this(capacity, capacity / 4, tag);
  }
  
  public CustomEnergyStorage(int capacity) {
    this(capacity, capacity / 4, "energy");
  }
  
  public void setEnergy(int energyIn) {
    this.energy = Math.max(0, Math.min(energyIn, this.getMaxEnergyStored()));
  }
  
  @Override
  public CompoundTag serializeNBT() {
    CompoundTag nbtTag = new CompoundTag();
    nbtTag.putInt(this.tag, this.energy);
    nbtTag.putInt("capacity", this.capacity);
    nbtTag.putInt("maxReceive", this.maxReceive);
    nbtTag.putInt("maxExtract", this.maxExtract);
    return nbtTag;
  }
  
  @Override
  public void deserializeNBT(Tag nbt) {
    if (!(nbt instanceof CompoundTag compound)) return;
    this.energy = compound.getInt(this.tag);
    this.capacity = compound.getInt("capacity");
    this.maxReceive = compound.getInt("maxReceive");
    this.maxExtract = compound.getInt("maxExtract");
  }
}