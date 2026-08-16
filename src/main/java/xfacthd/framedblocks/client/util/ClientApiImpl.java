package xfacthd.framedblocks.client.util;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import xfacthd.framedblocks.api.FramedBlocksClientAPI;
import xfacthd.framedblocks.api.ghost.GhostRenderBehaviour;
import xfacthd.framedblocks.api.model.SolidFrameMode;
import xfacthd.framedblocks.api.type.IBlockType;
import xfacthd.framedblocks.api.predicate.contex.ConTexMode;
import xfacthd.framedblocks.api.render.OutlineRenderer;
import xfacthd.framedblocks.client.data.ConTexDataHandler;
import xfacthd.framedblocks.client.model.FluidModel;
import xfacthd.framedblocks.client.render.special.BlockOutlineRenderer;
import xfacthd.framedblocks.client.render.special.GhostBlockRenderer;

@SuppressWarnings("unused")
public final class ClientApiImpl implements FramedBlocksClientAPI
{
    @Override
    public BlockColor defaultBlockColor()
    {
        return FramedBlockColor.INSTANCE;
    }

    @Override
    public BakedModel createFluidModel(Fluid fluid)
    {
        return FluidModel.create(fluid);
    }

    @Override
    public void registerOutlineRender(IBlockType type, OutlineRenderer render)
    {
        BlockOutlineRenderer.registerOutlineRender(type, render);
    }

    @Override
    public void registerGhostRenderBehaviour(GhostRenderBehaviour behaviour, Block... blocks)
    {
        GhostBlockRenderer.registerBehaviour(behaviour, blocks);
    }

    @Override
    public void registerGhostRenderBehaviour(GhostRenderBehaviour behaviour, Item... items)
    {
        GhostBlockRenderer.registerBehaviour(behaviour, items);
    }

    @Override
    public boolean useDiscreteUVSteps()
    {
        return ClientConfig.INSTANCE.useDiscreteUVSteps();
    }

    @Override
    public ConTexMode getConTexMode()
    {
        return ClientConfig.INSTANCE.conTexMode();
    }

    @Override
    public SolidFrameMode getSolidFrameMode()
    {
        return ClientConfig.INSTANCE.solidFrameMode();
    }

    @Override
    public boolean shouldForceAmbientOcclusionOnGlowingBlocks()
    {
        return ClientConfig.INSTANCE.forceAoOnGlowingBlocks();
    }

    @Override
    public Object extractCTContext(Object data)
    {
        return ConTexDataHandler.extractConTexData(data);
    }
}