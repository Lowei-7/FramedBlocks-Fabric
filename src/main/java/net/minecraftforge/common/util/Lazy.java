package net.minecraftforge.common.util;

import java.util.function.Supplier;

public class Lazy<T> implements Supplier<T> {
    private final Supplier<T> supplier;
    private volatile T value;
    
    public static <T> Lazy<T> of(Supplier<T> supplier) {
        return new Lazy<>(supplier);
    }
    
    public static <T> Lazy<T> concurrentOf(Supplier<T> supplier) {
        return new Lazy<>(supplier);
    }
    
    private Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }
    
    @Override
    public T get() {
        if (value == null) {
            value = supplier.get();
        }
        return value;
    }
}
