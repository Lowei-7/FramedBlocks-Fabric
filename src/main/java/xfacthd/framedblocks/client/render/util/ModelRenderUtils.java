package xfacthd.framedblocks.client.render.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.FastColor;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import xfacthd.framedblocks.api.model.FramedBlockModel;
import xfacthd.framedblocks.client.model.FramedDoubleBlockModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Fabric renderer API has no concept of per-model render data like Forge's {@code ModelData}. Instead the framed
 * models read their render data from the {@code RenderAttachmentBlockEntity} at the rendered position. For special
 * renderers (ghost blocks, chest lids, debug outlines, ...) the data has to be injected manually by querying the
 * quads through the internal {@code getQuads(...)} overloads and writing them into the vertex consumer directly.
 */
public final class ModelRenderUtils
{
    private static final Direction[] DIRECTIONS = Direction.values();

    public static Set<RenderType> getRenderTypes(BakedModel model, BlockState state, RandomSource random, Object data)
    {
        if (model instanceof FramedBlockModel fbm)
        {
            return fbm.getRenderTypes(state, random, data);
        }
        if (model instanceof FramedDoubleBlockModel fdm)
        {
            return fdm.getRenderTypes(state, random, data);
        }
        return Set.of(RenderType.cutout());
    }

    public static void emitModelQuads(
            PoseStack poseStack,
            VertexConsumer consumer,
            BlockState state,
            BlockPos pos,
            BlockAndTintGetter level,
            RandomSource random,
            BakedModel model,
            Object data,
            int packedLight,
            int packedOverlay,
            RenderType layer
    )
    {
        List<BakedQuad> quads = new ArrayList<>();
        if (model instanceof FramedBlockModel fbm)
        {
            for (Direction dir : DIRECTIONS)
            {
                quads.addAll(fbm.getQuads(state, dir, random, data, layer));
            }
            quads.addAll(fbm.getQuads(state, null, random, data, layer));
        }
        else if (model instanceof FramedDoubleBlockModel fdm)
        {
            for (Direction dir : DIRECTIONS)
            {
                quads.addAll(fdm.getQuads(state, dir, random, data, layer));
            }
            quads.addAll(fdm.getQuads(state, null, random, data, layer));
        }
        else
        {
            for (Direction dir : DIRECTIONS)
            {
                quads.addAll(model.getQuads(state, dir, random));
            }
            quads.addAll(model.getQuads(state, null, random));
        }

        BlockColors blockColors = Minecraft.getInstance().getBlockColors();
        PoseStack.Pose pose = poseStack.last();
        for (BakedQuad quad : quads)
        {
            float red = 1F, green = 1F, blue = 1F;
            if (quad.isTinted())
            {
                int tint = blockColors.getColor(state, level, pos, quad.getTintIndex());
                red = FastColor.ARGB32.red(tint) / 255F;
                green = FastColor.ARGB32.green(tint) / 255F;
                blue = FastColor.ARGB32.blue(tint) / 255F;
            }
            consumer.putBulkData(pose, quad, red, green, blue, 1F, packedLight, packedOverlay);
        }
    }



    private ModelRenderUtils() { }
}
