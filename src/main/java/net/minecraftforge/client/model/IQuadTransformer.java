package net.minecraftforge.client.model;

import net.minecraft.client.renderer.block.model.BakedQuad;

import java.util.List;

public interface IQuadTransformer
{
    void processInPlace(List<BakedQuad> quads);

    static IQuadTransformer empty()
    {
        return quads -> {};
    }
}
