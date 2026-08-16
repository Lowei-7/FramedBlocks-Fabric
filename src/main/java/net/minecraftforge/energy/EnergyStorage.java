package net.minecraftforge.energy;

/**
 * Fabric compatibility shim for Forge EnergyStorage.
 * Provides a simple energy buffer with receive/extract semantics.
 */
public class EnergyStorage
{
    protected int energy;
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;

    public EnergyStorage(int capacity)
    {
        this(capacity, capacity, capacity, 0);
    }

    public EnergyStorage(int capacity, int maxTransfer)
    {
        this(capacity, maxTransfer, maxTransfer, 0);
    }

    public EnergyStorage(int capacity, int maxReceive, int maxExtract)
    {
        this(capacity, maxReceive, maxExtract, 0);
    }

    public EnergyStorage(int capacity, int maxReceive, int maxExtract, int energy)
    {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.energy = Math.max(0, Math.min(capacity, energy));
    }

    public int receiveEnergy(int maxReceive, boolean simulate)
    {
        if (!canReceive())
        {
            return 0;
        }

        int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));
        if (!simulate)
        {
            energy += energyReceived;
        }
        return energyReceived;
    }

    public int extractEnergy(int maxExtract, boolean simulate)
    {
        if (!canExtract())
        {
            return 0;
        }

        int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
        if (!simulate)
        {
            energy -= energyExtracted;
        }
        return energyExtracted;
    }

    public int getEnergyStored()
    {
        return energy;
    }

    public int getMaxEnergyStored()
    {
        return capacity;
    }

    public boolean canExtract()
    {
        return maxExtract > 0;
    }

    public boolean canReceive()
    {
        return maxReceive > 0;
    }

    public void setEnergy(int energy)
    {
        this.energy = Math.max(0, Math.min(capacity, energy));
    }
}
