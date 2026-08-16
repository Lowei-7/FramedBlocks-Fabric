package net.minecraftforge.fml;

import net.fabricmc.loader.api.FabricLoader;

import java.util.Optional;

public class ModList
{
    private static ModList instance;

    public static ModList get()
    {
        if (instance == null)
        {
            instance = new ModList();
        }
        return instance;
    }

    public boolean isLoaded(String modId)
    {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    public Optional<?> getModContainerById(String modId)
    {
        if (FabricLoader.getInstance().isModLoaded(modId))
        {
            return Optional.of(FabricLoader.getInstance().getModContainer(modId).orElseThrow());
        }
        return Optional.empty();
    }

    public Optional<?> getModContainerByObject(Object obj)
    {
        return Optional.empty();
    }
}
