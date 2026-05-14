package ru.blatfan.blatapi.common.cap.item;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import ru.blatfan.blatapi.common.cap.CustomEnergyStorage;
import ru.blatfan.blatapi.utils.NBTHelper;

public class EnergyCapabilityItemStack implements ICapabilityProvider {
  private final LazyOptional<IEnergyStorage> energy;
  private final int max;
  private final ItemStack stack;

  private IEnergyStorage createEnergy() {
    return new CustomEnergyStorage(max) {
      private CompoundTag getTag(){
        return NBTHelper.getTagCompound(stack, tag);
      }
      
      @Override
      public int getEnergyStored() {
        return getTag().getInt("energy");
      }

      @Override
      public void setEnergy(int energy) {
        super.setEnergy(energy);
        getTag().putInt(tag, getEnergyStored());
      }
    };
  }

  public EnergyCapabilityItemStack(final ItemStack stack, int capacity) {
    this.max = capacity;
    this.stack = stack;
    energy = LazyOptional.of(this::createEnergy);
  }

  @Override
  public String toString() {
    return "EnergyCapabilityItemStack [energy=" + energy + ", max=" + max + "]";
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
    if (ForgeCapabilities.ENERGY == capability) return energy.cast();
    return LazyOptional.empty();
  }
}