package net.minecraftforge.client.model.geometry;

import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;

public interface IGeometryBakingContext
{
    String getModelName();

    boolean hasMaterial(String name);

    Material getMaterial(String name);

    boolean isGui3d();

    boolean useBlockLight();

    boolean useAmbientOcclusion();

    ItemTransforms getTransforms();

    Transformation getRootTransform();

    ResourceLocation getModelId();
}
