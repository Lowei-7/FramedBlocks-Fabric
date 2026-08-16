package net.minecraftforge.fluids.capability;

public enum FluidAction {
    EXECUTE, SIMULATE;
    
    public boolean execute() { return this == EXECUTE; }
    public boolean simulate() { return this == SIMULATE; }
}
