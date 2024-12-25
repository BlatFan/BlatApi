package ru.blatfan.blatapi.cap.item;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import ru.blatfan.blatapi.cap.CustomEnergyStorage;

public class EnergyCapabilityItemStack implements ICapabilityProvider {

  public static final String NBTENERGY = "energy";
  private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
  //  private ItemStack stack;
  private int max;
  private ItemStack stack;

  private IEnergyStorage createEnergy() {
    return new CustomEnergyStorage(max, max / 4) {

      @Override
      public int getEnergyStored() {
        if (stack.hasTag()) {
          return stack.getTag().getInt(tag);
        }
        else {
          return super.getEnergyStored();
        }
      }

      @Override
      public void setEnergy(int energy) {
        if (!stack.hasTag()) {
          stack.setTag(new CompoundTag());
        }
        stack.getTag().putInt(tag, energy);
        super.setEnergy(energy);
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
    if (ForgeCapabilities.ENERGY == capability)
      return energy.cast();
    
    return LazyOptional.empty();
  }
}
