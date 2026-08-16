package net.minecraftforge.client.model.pipeline;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public abstract class VertexConsumerWrapper implements VertexConsumer
{
    protected VertexConsumer parent;

    public VertexConsumerWrapper(VertexConsumer parent)
    {
        this.parent = parent;
    }

    @Override
    public VertexConsumer vertex(double x, double y, double z)
    {
        return parent.vertex(x, y, z);
    }

    @Override
    public VertexConsumer color(int red, int green, int blue, int alpha)
    {
        return parent.color(red, green, blue, alpha);
    }

    @Override
    public VertexConsumer uv(float u, float v)
    {
        return parent.uv(u, v);
    }

    @Override
    public VertexConsumer overlayCoords(int u, int v)
    {
        return parent.overlayCoords(u, v);
    }

    @Override
    public VertexConsumer uv2(int u, int v)
    {
        return parent.uv2(u, v);
    }

    @Override
    public VertexConsumer normal(float x, float y, float z)
    {
        return parent.normal(x, y, z);
    }

    @Override
    public void endVertex()
    {
        parent.endVertex();
    }

    @Override
    public void defaultColor(int defaultR, int defaultG, int defaultB, int defaultA)
    {
        parent.defaultColor(defaultR, defaultG, defaultB, defaultA);
    }

    @Override
    public void unsetDefaultColor()
    {
        parent.unsetDefaultColor();
    }
}
