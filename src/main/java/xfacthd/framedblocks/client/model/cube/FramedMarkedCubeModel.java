package xfacthd.framedblocks.client.model.cube;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Set;
import net.minecraft.client.renderer.RenderType;
import xfacthd.framedblocks.api.model.data.FramedBlockData;
import xfacthd.framedblocks.api.model.util.ModelUtils;
import xfacthd.framedblocks.api.util.Utils;
import xfacthd.framedblocks.client.util.ClientConfig;

import java.util.*;

public class FramedMarkedCubeModel extends FramedCubeBaseModel
{
    public static final ResourceLocation SLIME_FRAME_LOCATION = Utils.rl("block/slime_frame");
    public static final ResourceLocation REDSTONE_FRAME_LOCATION = Utils.rl("block/redstone_frame");
    private final BakedModel frameModel;

    public FramedMarkedCubeModel(
            BlockState state,
            BakedModel baseModel,
            Map<ResourceLocation, BakedModel> registry,
            ResourceLocation frameLocation
    )
    {
        super(state, baseModel);
        frameModel = registry.get(frameLocation);
    }

    @Override
    protected Set<RenderType> getAdditionalRenderTypes(RandomSource rand, Object extraData)
    {
        FramedBlockData fbData = (extraData instanceof FramedBlockData fbd) ? fbd : null;
        if (fbData != null && !fbData.getCamoState().isAir())
        {
            return ModelUtils.CUTOUT;
        }
        return Set.of();
    }

    @Override
    protected void getAdditionalQuads(
            ArrayList<BakedQuad> quads,
            Direction side,
            BlockState state,
            RandomSource rand,
            Object data,
            RenderType renderType
    )
    {
        FramedBlockData fbData = (data instanceof FramedBlockData fbd) ? fbd : null;
        if (fbData != null && !fbData.getCamoState().isAir())
        {
            Utils.copyAll(frameModel.getQuads(state, side, rand), quads);
        }
    }



    public static FramedCubeBaseModel slime(
            BlockState state, BakedModel baseModel, Map<ResourceLocation, BakedModel> registry
    )
    {
        if (ClientConfig.INSTANCE.showSpecialCubeOverlay())
        {
            return new FramedMarkedCubeModel(state, baseModel, registry, SLIME_FRAME_LOCATION);
        }
        return new FramedCubeBaseModel(state, baseModel);
    }

    public static FramedCubeBaseModel redstone(
            BlockState state, BakedModel baseModel, Map<ResourceLocation, BakedModel> registry
    )
    {
        if (ClientConfig.INSTANCE.showSpecialCubeOverlay())
        {
            return new FramedMarkedCubeModel(state, baseModel, registry, REDSTONE_FRAME_LOCATION);
        }
        return new FramedCubeBaseModel(state, baseModel);
    }
}
