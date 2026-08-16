package net.minecraftforge.common.capabilities;

import net.minecraft.core.Direction;

public interface ICapabilityProvider {
    default <T> T getCapability(Capability<T> cap, Direction side) {
        return getCapability(cap);
    }
    
    default <T> T getCapability(Capability<T> cap) {
        return null;
    }
}
