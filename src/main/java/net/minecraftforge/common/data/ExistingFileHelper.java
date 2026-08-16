package net.minecraftforge.common.data;

import net.minecraft.resources.ResourceLocation;

public class ExistingFileHelper
{
    public boolean exists(ResourceLocation loc, ResourceType type)
    {
        return true;
    }

    public enum ResourceType
    {
        DATA,
        ASSETS
    }
}
