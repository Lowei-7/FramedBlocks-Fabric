package xfacthd.framedblocks.common.util.fabric;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class RegistryObject<T> implements Supplier<T> {
    private final ResourceLocation id;
    private final Supplier<? extends T> sup;
    private T instance;

    public RegistryObject(ResourceLocation id, Supplier<? extends T> sup) {
        this.id = id;
        this.sup = sup;
    }

    public ResourceLocation getId() {
        return id;
    }

    @Override
    public T get() {
        if (instance == null) {
            instance = sup.get();
        }
        return instance;
    }
}
