package xfacthd.framedblocks.api.model.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import java.util.Set;
import net.minecraft.client.renderer.RenderType;
import xfacthd.framedblocks.api.FramedBlocksClientAPI;
import xfacthd.framedblocks.api.model.FramedBlockModel;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ModelCache
{
    public static final Duration DEFAULT_CACHE_DURATION = Duration.ofMinutes(10);
    private static final Map<Fluid, BakedModel> modelCache = new ConcurrentHashMap<>();

    public static void clear()
    {
        modelCache.clear();
    }

    public static BakedModel getModel(BlockState state)
    {
        if (state.getBlock() instanceof LiquidBlock)
        {
            Fluid fluid = state.getFluidState().getType();
            return modelCache.computeIfAbsent(
                    fluid != Fluids.EMPTY ? fluid : Fluids.WATER,
                    FramedBlocksClientAPI.getInstance()::createFluidModel
            );
        }
        return Minecraft.getInstance().getBlockRenderer().getBlockModel(state);
    }

    public static Set<RenderType> getRenderTypes(BlockState state, RandomSource random, Object data)
    {
        if (state.getBlock() instanceof LiquidBlock)
        {
            return Set.of(ItemBlockRenderTypes.getRenderLayer(state.getFluidState()));
        }
        BakedModel model = getModel(state);
        if (model != null && !(model instanceof FramedBlockModel))
        {
            // Vanilla models render in the block's own render layer (cutout_mipped for grass/leaves, etc.),
            // not always in the solid layer
            return Set.of(ItemBlockRenderTypes.getChunkRenderType(state));
        }
        return ModelUtils.SOLID;
    }

    public static Set<RenderType> getCamoRenderTypes(BlockState state, RandomSource random, Object data)
    {
        if (state.getBlock() instanceof LiquidBlock)
        {
            return Set.of(ItemBlockRenderTypes.getRenderLayer(state.getFluidState()));
        }
        BakedModel camoModel = getModel(state);
        if (camoModel != null)
        {
            if (camoModel instanceof FramedBlockModel fbm)
            {
                data = ModelUtils.getCamoModelData(data);
                return fbm.getRenderTypes(state, random, data);
            }
            return Set.of(ItemBlockRenderTypes.getChunkRenderType(state));
        }

        return ModelUtils.SOLID;
    }

    private ModelCache() { }
}
