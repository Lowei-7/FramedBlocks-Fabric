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
    public VertexConsumer addVertex(float x, float y, float z)
    {
        return parent.addVertex(x, y, z);
    }

    @Override
    public VertexConsumer setColor(int red, int green, int blue, int alpha)
    {
        return parent.setColor(red, green, blue, alpha);
    }

    @Override
    public VertexConsumer setUv(float u, float v)
    {
        return parent.setUv(u, v);
    }

    @Override
    public VertexConsumer setUv1(int u, int v)
    {
        return parent.setUv1(u, v);
    }

    @Override
    public VertexConsumer setUv2(int u, int v)
    {
        return parent.setUv2(u, v);
    }

    @Override
    public VertexConsumer setNormal(float x, float y, float z)
    {
        return parent.setNormal(x, y, z);
    }
}
