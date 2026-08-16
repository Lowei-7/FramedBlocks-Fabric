package xfacthd.framedblocks.client.model.cube;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Set;
import net.minecraft.client.renderer.RenderType;
import xfacthd.framedblocks.api.block.FramedProperties;
import xfacthd.framedblocks.api.model.FramedBlockModel;
import xfacthd.framedblocks.api.model.quad.Modifiers;
import xfacthd.framedblocks.api.model.quad.QuadModifier;
import xfacthd.framedblocks.api.util.*;
import xfacthd.framedblocks.api.model.util.ModelUtils;
import xfacthd.framedblocks.common.data.PropertyHolder;
import xfacthd.framedblocks.common.data.property.LatchType;

import java.util.List;
import java.util.Map;

public class FramedChestLidModel extends FramedBlockModel
{
    private final Direction facing;
    private final LatchType latch;
    private final Set<RenderType> addLayers;

    public FramedChestLidModel(BlockState state, BakedModel baseModel)
    {
        super(state, baseModel);
        this.facing = state.getValue(FramedProperties.FACING_HOR);
        this.latch = state.getValue(PropertyHolder.LATCH_TYPE);
        this.addLayers = latch == LatchType.DEFAULT ? ModelUtils.CUTOUT : Set.of();
    }

    @Override
    protected void transformQuad(Map<Direction, List<BakedQuad>> quadMap, BakedQuad quad)
    {
        Direction quadDir = quad.getDirection();
        if (Utils.isY(quad.getDirection()))
        {
            QuadModifier.geometry(quad)
                    .apply(Modifiers.cutTopBottom(1F/16F, 1F/16F, 15F/16F, 15F/16F))
                    .apply(Modifiers.setPosition(quadDir == Direction.UP ? 14F/16F : 7F/16F))
                    .export(quadMap.get(null));
        }
        else
        {
            QuadModifier.geometry(quad)
                    .apply(Modifiers.cutSide(1F/16F, 9F/16F, 15F/16F, 14F/16F))
                    .apply(Modifiers.setPosition(15F/16F))
                    .export(quadMap.get(null));
        }

        if (latch == LatchType.CAMO)
        {
            FramedChestModel.makeChestLatch(quadMap, quad, facing);
        }
    }

    @Override
    protected Set<RenderType> getAdditionalRenderTypes(RandomSource rand, Object extraData)
    {
        return addLayers;
    }

    @Override
    protected void getAdditionalQuads(
            Map<Direction, List<BakedQuad>> quadMap,
            BlockState state,
            RandomSource rand,
            Object data,
            RenderType layer
    )
    {
        List<BakedQuad> quads = baseModel.getQuads(state, null, rand);
        for (BakedQuad quad : quads)
        {
            quadMap.get(null).add(quad);
        }
    }
}