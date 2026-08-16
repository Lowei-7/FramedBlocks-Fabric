package net.minecraftforge.registries;

import java.util.function.Supplier;

public class RegistryObject<T> implements Supplier<T> {
    private final T value;
    
    public RegistryObject(T value) {
        this.value = value;
    }
    
    @Override
    public T get() { return value; }
}
