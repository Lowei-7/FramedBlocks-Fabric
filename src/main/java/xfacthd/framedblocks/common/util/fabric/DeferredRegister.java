package xfacthd.framedblocks.common.util.fabric;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class DeferredRegister<T> {
    private final Registry<T> registry;
    private final String modid;
    private final List<RegistryObject<T>> entries = new ArrayList<>();

    public static <T> DeferredRegister<T> create(Registry<T> registry, String modid) {
        return new DeferredRegister<>(registry, modid);
    }

    private DeferredRegister(Registry<T> registry, String modid) {
        this.registry = registry;
        this.modid = modid;
    }

    public <I extends T> RegistryObject<I> register(String name, Supplier<? extends I> sup) {
        ResourceLocation id = new ResourceLocation(modid, name);
        RegistryObject<I> ro = new RegistryObject<>(id, sup);
        entries.add((RegistryObject<T>) ro);
        return ro;
    }

    public Collection<RegistryObject<T>> getEntries() {
        return entries;
    }

    public void register() {
        for (RegistryObject<T> entry : entries) {
            Registry.register(registry, entry.getId(), entry.get());
        }
    }
}
