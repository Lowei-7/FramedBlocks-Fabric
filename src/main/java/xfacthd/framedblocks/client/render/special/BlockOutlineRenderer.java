package xfacthd.framedblocks.client.render.special;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import xfacthd.framedblocks.FramedBlocks;
import xfacthd.framedblocks.api.block.IFramedBlock;
import xfacthd.framedblocks.api.type.IBlockType;
import xfacthd.framedblocks.api.render.OutlineRenderer;
import xfacthd.framedblocks.api.util.TestProperties;
import xfacthd.framedblocks.client.util.ClientConfig;

import java.util.*;

public final class BlockOutlineRenderer
{
    private static final Map<IBlockType, OutlineRenderer> OUTLINE_RENDERERS = new HashMap<>();
    private static final Set<IBlockType> ERRORED_TYPES = new HashSet<>();
    private static boolean locked = false;

    public static void register()
    {
        WorldRenderEvents.BLOCK_OUTLINE.register(BlockOutlineRenderer::onBlockOutline);
    }

    public static boolean onBlockOutline(WorldRenderContext world, WorldRenderContext.BlockOutlineContext event)
    {
        if (!ClientConfig.fancyHitboxes() && !TestProperties.ENABLE_OCCLUSION_SHAPE_DEBUG_RENDERER)
        {
            return true;
        }

        //noinspection ConstantConditions
        BlockState state = Minecraft.getInstance().level.getBlockState(event.blockPos());
        if (!(state.getBlock() instanceof IFramedBlock block))
        {
            return true;
        }

        BlockPos pos = event.blockPos();
        PoseStack mstack = world.matrixStack();
        Vec3 offset = Vec3.atLowerCornerOf(pos).subtract(world.camera().getPosition());

        if (TestProperties.ENABLE_OCCLUSION_SHAPE_DEBUG_RENDERER)
        {
            VertexConsumer builder = world.consumers().getBuffer(net.minecraft.client.renderer.RenderType.lines());
            VoxelShape shape = state.getOcclusionShape();
            mstack.pushPose();
            mstack.translate(offset.x, offset.y, offset.z);
            ShapeRenderer.renderShape(mstack, builder, shape, 0D, 0D, 0D, 0x66000000);
            mstack.popPose();
            return false;
        }

        IBlockType type = block.getBlockType();
        if (!type.hasSpecialHitbox())
        {
            return true;
        }

        OutlineRenderer renderer = OUTLINE_RENDERERS.get(type);
        if (renderer == null)
        {
            if (ERRORED_TYPES.add(type))
            {
                FramedBlocks.LOGGER.error("IBlockType '{}' requests custom outline rendering but no OutlineRender was registered!", type.getName());
            }
            return true;
        }

        VertexConsumer builder = world.consumers().getBuffer(net.minecraft.client.renderer.RenderType.lines());

        mstack.pushPose();
        mstack.translate(offset.x, offset.y, offset.z);
        mstack.translate(.5, .5, .5);
        renderer.rotateMatrix(mstack, state);
        mstack.translate(-.5, -.5, -.5);

        renderer.draw(state, Minecraft.getInstance().level, pos, mstack, builder);

        mstack.popPose();
        return false;
    }

    public static synchronized void registerOutlineRender(IBlockType type, OutlineRenderer render)
    {
        Preconditions.checkState(!locked, "OutlineRenderer registry is locked!");

        if (!type.hasSpecialHitbox())
        {
            throw new IllegalArgumentException(String.format(
                    "Type %s doesn't return true from IBlockType#hasSpecialHitbox()", type
            ));
        }

        OUTLINE_RENDERERS.put(type, render);
    }

    public static void lockRegistration()
    {
        locked = true;
    }

    public static boolean hasOutlineRenderer(IBlockType type)
    {
        return OUTLINE_RENDERERS.containsKey(type);
    }



    private BlockOutlineRenderer() { }
}
