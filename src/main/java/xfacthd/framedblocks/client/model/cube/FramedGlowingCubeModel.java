package xfacthd.framedblocks.client.model.cube;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import xfacthd.framedblocks.api.model.util.ModelUtils;

import java.util.*;

public class FramedGlowingCubeModel extends FramedCubeBaseModel
{
    public FramedGlowingCubeModel(BlockState state, BakedModel baseModel)
    {
        super(state, baseModel);
    }

    @Override
    public List<BakedQuad> getQuads(
            BlockState state, Direction side, RandomSource rand, Object extraData, RenderType renderType
    )
    {
        List<BakedQuad> quads = super.getQuads(state, side, rand, extraData, renderType);
        return applyFullbright(quads);
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand)
    {
        List<BakedQuad> quads = super.getQuads(state, side, rand);
        return applyFullbright(quads);
    }

    @Override
    public boolean useAmbientOcclusion()
    {
        return false;
    }

    @Override
    public boolean useAmbientOcclusionWithLightEmission(BlockState state, RenderType layer)
    {
        // Return true despite explicitly not wanting AO on this, simply to avoid the light emission check
        return true;
    }



    private static List<BakedQuad> applyFullbright(List<BakedQuad> quads)
    {
        List<BakedQuad> fullbrightQuads = new ArrayList<>(quads.size());
        quads.forEach(quad ->
        {
            int[] vertexData = quad.getVertices();
            BakedQuad newQuad = new BakedQuad(
                    Arrays.copyOf(vertexData, vertexData.length),
                    quad.getTintIndex(),
                    quad.getDirection(),
                    quad.getSprite(),
                    false
            );
            setMaxEmissivity(newQuad);
            fullbrightQuads.add(newQuad);
        });
        return fullbrightQuads;
    }

    private static void setMaxEmissivity(BakedQuad quad)
    {
        int[] vertexData = quad.getVertices();
        for (int vert = 0; vert < 4; vert++)
        {
            int offset = vert * ModelUtils.QUADS_STRIDE + ModelUtils.STRIDE_UV2;
            vertexData[offset] = LightTexture.pack(15, 15);
        }
    }
}
