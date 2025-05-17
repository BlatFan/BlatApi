package ru.blatfan.blatapi.common.cap.item;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import ru.blatfan.blatapi.common.cap.FloatEnergyStorage;
import ru.blatfan.blatapi.utils.NBTHelper;

public class FloatEnergyCapabilityItemStack implements ICapabilityProvider {
  private final LazyOptional<IEnergyStorage> energy;
  private final int originalCapacity;
  private final ItemStack stack;

  private IEnergyStorage createEnergy() {
    return new FloatEnergyStorage(originalCapacity) {
      private CompoundTag getTag(){
        return NBTHelper.getTagCompound(stack, tag);
      }
      
      @Override
      public int getEnergyStored() {
        return getTag().getInt("energy");
      }
      
      @Override
      public void setEnergy(int energy) {
        getTag().putInt("energy", energy);
        super.setEnergy(energy);
      }
      
      public void setCapacity(int newCapacity){
        getTag().putInt("capacity", newCapacity);
        this.capacity=newCapacity;
      }
      
      public void setMaxReceive(int newMaxReceive){
        getTag().putInt("maxReceive", newMaxReceive);
        this.maxReceive=newMaxReceive;
      }
      
      public void setMaxExtract(int newMaxExtract){
        getTag().putInt("maxExtract", newMaxExtract);
        this.maxExtract=newMaxExtract;
      }
      
      public int getMaxReceive() {
        return getTag().getInt("maxReceive");
      }
      public int getMaxExtract() {
        return getTag().getInt("maxExtract");
      }
    };
  }

  public FloatEnergyCapabilityItemStack(final ItemStack stack, int capacity) {
    this.originalCapacity = capacity;
    this.stack = stack;
    energy = LazyOptional.of(this::createEnergy);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
    if (ForgeCapabilities.ENERGY == capability)
      return energy.cast();
    
    return LazyOptional.empty();
  }
}
