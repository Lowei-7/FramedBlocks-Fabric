package net.minecraftforge.registries;

import net.minecraft.resources.ResourceLocation;

public interface IForgeRegistry<T>
{
    T getValue(ResourceLocation key);

    boolean containsKey(ResourceLocation key);

    ResourceLocation getKey(T value);
}
