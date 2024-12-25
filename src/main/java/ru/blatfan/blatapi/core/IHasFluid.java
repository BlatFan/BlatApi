package ru.blatfan.blatapi.core;

import net.minecraftforge.fluids.FluidStack;

public interface IHasFluid {

  public FluidStack getFluid();

  public void setFluid(FluidStack fluid);
}
