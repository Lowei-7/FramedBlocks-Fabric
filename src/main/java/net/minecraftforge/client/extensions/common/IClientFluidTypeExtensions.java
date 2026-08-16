package net.minecraftforge.client.extensions.common;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.FluidState;

public interface IClientFluidTypeExtensions
{
    IClientFluidTypeExtensions DEFAULT = new IClientFluidTypeExtensions() {};

    static IClientFluidTypeExtensions of(FluidState fluidState)
    {
        return DEFAULT;
    }

    default ResourceLocation getStillTexture()
    {
        return new ResourceLocation("minecraft", "block/water_still");
    }

    default ResourceLocation getFlowingTexture()
    {
        return new ResourceLocation("minecraft", "block/water_flow");
    }

    default ResourceLocation getOverlayTexture()
    {
        return null;
    }

    default ResourceLocation getRenderOverlayTexture()
    {
        return null;
    }

    default int getTintColor()
    {
        return 0xFFFFFFFF;
    }

    default int getTintColor(FluidState state, net.minecraft.world.level.BlockAndTintGetter getter, net.minecraft.core.BlockPos pos)
    {
        return getTintColor();
    }
}
