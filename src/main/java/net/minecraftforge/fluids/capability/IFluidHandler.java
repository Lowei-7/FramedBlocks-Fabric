package net.minecraftforge.fluids.capability;

import net.minecraftforge.fluids.FluidStack;

public interface IFluidHandler {
    int getTanks();
    FluidStack getFluidInTank(int tank);
    int getTankCapacity(int tank);
    boolean isFluidValid(int tank, FluidStack stack);
    int fill(FluidStack resource, FluidAction action);
    FluidStack drain(FluidStack resource, FluidAction action);
    FluidStack drain(int maxDrain, FluidAction action);
}
