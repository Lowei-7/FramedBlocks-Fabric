package net.minecraftforge.common.util;

import java.util.function.Function;
import java.util.function.Supplier;

public class LazyOptional<T> {
    private final T value;
    
    private LazyOptional(T value) {
        this.value = value;
    }
    
    public static <T> LazyOptional<T> of(Supplier<T> supplier) {
        return new LazyOptional<>(supplier.get());
    }
    
    public static <T> LazyOptional<T> empty() {
        return new LazyOptional<>(null);
    }
    
    public <R> LazyOptional<R> map(Function<T, R> mapper) {
        if (value == null) return empty();
        return of(() -> mapper.apply(value));
    }
    
    public T orElse(T other) {
        return value != null ? value : other;
    }
    
    public boolean isPresent() {
        return value != null;
    }
}
