package xfacthd.framedblocks.client.model.torch;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Set;
import net.minecraft.client.renderer.RenderType;
import xfacthd.framedblocks.api.model.util.ModelCache;

public class FramedSoulTorchModel extends FramedTorchModel
{
    public FramedSoulTorchModel(BlockState state, BakedModel baseModel) { super(state, baseModel); }

    @Override
    protected Set<RenderType> getAdditionalRenderTypes(RandomSource rand, Object extraData)
    {
        return ModelCache.getRenderTypes(Blocks.SOUL_TORCH.defaultBlockState(), rand, null);
    }
}