package net.minecraftforge.client.model;

import net.minecraft.client.renderer.block.model.BakedQuad;

import java.util.List;

public class QuadTransformers
{
    public static IQuadTransformer empty()
    {
        return IQuadTransformer.empty();
    }

    public static IQuadTransformer applyingLightmap(int lightmap)
    {
        return quads ->
        {
            for (BakedQuad quad : quads)
            {
                // Stub: lightmap application would go here
            }
        };
    }
}
