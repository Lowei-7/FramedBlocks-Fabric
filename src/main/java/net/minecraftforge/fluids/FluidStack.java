package net.minecraftforge.fluids;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;

public class FluidStack {
    private Fluid fluid;
    private int amount;
    
    public FluidStack(Fluid fluid, int amount) {
        this.fluid = fluid;
        this.amount = amount;
    }
    
    public Fluid getFluid() { return fluid; }
    public int getAmount() { return amount; }
    public boolean isEmpty() { return amount <= 0; }
    public CompoundTag writeToNBT(CompoundTag nbt) { return nbt; }
    public static FluidStack loadFluidStackFromNBT(CompoundTag nbt) { return null; }
}
