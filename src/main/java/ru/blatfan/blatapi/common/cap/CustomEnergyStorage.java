package ru.blatfan.blatapi.common.cap;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

public class CustomEnergyStorage extends EnergyStorage implements INBTSerializable<Tag> {

  public final String tag;

  public CustomEnergyStorage(int capacity, int maxTransfer, String tag) {
    super(capacity, maxTransfer);
      this.tag = tag;
  }
  
  public CustomEnergyStorage(int capacity, int maxTransfer){
    this(capacity, maxTransfer, "energy");
  }
  
  public CustomEnergyStorage(int capacity){
    this(capacity, capacity/4, "energy");
  }
  
  public CustomEnergyStorage(int capacity, String tag){
    this(capacity, capacity/4, tag);
  }

  public void setEnergy(int energyIn) {
    if (energyIn < 0) {
      energyIn = 0;
    }
    if (energyIn > getMaxEnergyStored()) {
      energyIn = getEnergyStored();
    }
    this.energy = energyIn;
  }

  @Override
  public CompoundTag serializeNBT() {
    CompoundTag tag = new CompoundTag();
    tag.putInt(this.tag, getEnergyStored());
    return tag;
  }

  @Override
  public void deserializeNBT(Tag nbt) {
    CompoundTag real = (CompoundTag) nbt;
    setEnergy(real.getInt(tag));
  }
}
