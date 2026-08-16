package xfacthd.framedblocks.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;
import xfacthd.framedblocks.api.model.BakedModelProxy;
import xfacthd.framedblocks.api.model.FramedBlockModel;
import xfacthd.framedblocks.api.model.data.FramedBlockData;
import xfacthd.framedblocks.api.model.data.FramedDoubleBlockData;
import xfacthd.framedblocks.api.model.util.ModelUtils;
import xfacthd.framedblocks.common.data.doubleblock.DoubleBlockStateCache;
import xfacthd.framedblocks.common.block.IFramedDoubleBlock;
import xfacthd.framedblocks.common.util.DoubleBlockTopInteractionMode;

import org.jetbrains.annotations.Nullable;
import java.util.*;
import java.util.function.Supplier;

public class FramedDoubleBlockModel extends BakedModelProxy implements FabricBakedModel
{
    private static final FramedBlockData EMPTY_FRAME = new FramedBlockData.Immutable(
            Blocks.AIR.defaultBlockState(), new boolean[6], false
    );
    private static final FramedBlockData EMPTY_ALT_FRAME = new FramedBlockData.Immutable(
            Blocks.AIR.defaultBlockState(), new boolean[6], true
    );
    private static final java.util.concurrent.ConcurrentMap<BlendMode, RenderMaterial> RENDER_MATERIALS =
            new java.util.concurrent.ConcurrentHashMap<>();
    private static RenderMaterial renderMaterial(BlendMode blendMode)
    {
        return RENDER_MATERIALS.computeIfAbsent(blendMode, mode -> RendererAccess.INSTANCE
                .getRenderer().materialFinder()
                .blendMode(mode)
                .find());
    }

    private final boolean specialItemModel;
    private final DoubleBlockTopInteractionMode particleMode;
    private final Vec3 handTransform;
    private final Tuple<BlockState, BlockState> dummyStates;
    private Tuple<BakedModel, BakedModel> models = null;

    public FramedDoubleBlockModel(
            BlockState state,
            BakedModel baseModel,
            Vec3 handTransform,
            boolean specialItemModel
    )
    {
        super(baseModel);
        DoubleBlockStateCache cache = ((IFramedDoubleBlock) state.getBlock()).getCache(state);
        this.dummyStates = cache.getBlockPair();
        this.particleMode = cache.getTopInteractionMode();
        this.handTransform = handTransform;
        this.specialItemModel = specialItemModel;
    }

    @Override
    public boolean isVanillaAdapter()
    {
        return false;
    }

    public void emitBlockQuads(
            BlockAndTintGetter blockView, BlockState state, BlockPos pos,
            Supplier<RandomSource> randomSupplier, RenderContext context
    )
    {
        FramedDoubleBlockData doubleData;
        Object extraData = null;
        BlockEntity be = blockView.getBlockEntity(pos);
        if (be instanceof RenderAttachmentBlockEntity rabe)
        {
            extraData = rabe.getRenderAttachmentData();
        }
        if (extraData instanceof FramedDoubleBlockData data)
        {
            doubleData = data;
        }
        else
        {
            doubleData = new FramedDoubleBlockData(EMPTY_FRAME, EMPTY_ALT_FRAME);
        }

        QuadEmitter emitter = context.getEmitter();
        for (RenderType renderType : getRenderTypes(state, randomSupplier.get(), doubleData))
        {
            context.pushTransform(q -> true);
            for (Direction dir : Direction.values())
            {
                List<BakedQuad> quads = getQuads(state, dir, randomSupplier.get(), doubleData, renderType);
                for (BakedQuad quad : quads)
                {
                    emitter.fromVanilla(quad.getVertices(), 0, false);
                    emitter.cullFace(quad.getDirection());
                    emitter.colorIndex(quad.getTintIndex());
                    emitter.material(renderMaterial(BlendMode.fromRenderLayer(renderType)));
                    emitter.emit();
                }
            }
            List<BakedQuad> nullQuads = getQuads(state, null, randomSupplier.get(), doubleData, renderType);
            for (BakedQuad quad : nullQuads)
            {
                emitter.fromVanilla(quad.getVertices(), 0, false);
                emitter.cullFace(quad.getDirection());
                emitter.colorIndex(quad.getTintIndex());
                emitter.material(renderMaterial(BlendMode.fromRenderLayer(renderType)));
                emitter.emit();
            }
            context.popTransform();
        }
    }

    public List<BakedQuad> getQuads(
            @Nullable BlockState state, @Nullable Direction side, RandomSource rand,
            Object extraData, RenderType layer
    )
    {
        FramedDoubleBlockData doubleData = (extraData instanceof FramedDoubleBlockData d) ? d : null;
        FramedBlockData dataLeft = doubleData != null ? doubleData.getLeft() : EMPTY_FRAME;
        FramedBlockData dataRight = doubleData != null ? doubleData.getRight() : EMPTY_ALT_FRAME;

        Tuple<BakedModel, BakedModel> models = getModels();

        List<BakedQuad> quads = new ArrayList<>();
        quads.addAll(getHalfQuads(models.getA(), dummyStates.getA(), side, rand, dataLeft, layer));
        quads.addAll(invertTintIndizes(getHalfQuads(models.getB(), dummyStates.getB(), side, rand, dataRight, layer)));
        return quads;
    }

    private static List<BakedQuad> getHalfQuads(
            BakedModel model, BlockState state, Direction side, RandomSource rand,
            FramedBlockData data, RenderType layer
    )
    {
        if (model instanceof FramedBlockModel framed)
        {
            return framed.getQuads(state, side, rand, data, layer);
        }
        if (layer == null || layer == RenderType.solid())
        {
            return model.getQuads(state, side, rand);
        }
        return List.of();
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand)
    {
        if (specialItemModel)
        {
            return getQuads(state, side, rand, null, RenderType.cutout());
        }
        return super.getQuads(state, side, rand);
    }

    public Set<RenderType> getRenderTypes(BlockState state, RandomSource rand, Object data)
    {
        FramedDoubleBlockData doubleData = (data instanceof FramedDoubleBlockData d) ? d : null;
        FramedBlockData dataLeft = doubleData != null ? doubleData.getLeft() : EMPTY_FRAME;
        FramedBlockData dataRight = doubleData != null ? doubleData.getRight() : EMPTY_ALT_FRAME;

        Tuple<BakedModel, BakedModel> models = getModels();
        return ModelUtils.union(
                getHalfRenderTypes(models.getA(), dummyStates.getA(), rand, dataLeft),
                getHalfRenderTypes(models.getB(), dummyStates.getB(), rand, dataRight)
        );
    }

    private static Set<RenderType> getHalfRenderTypes(
            BakedModel model, BlockState state, RandomSource rand, FramedBlockData data
    )
    {
        if (model instanceof FramedBlockModel framed)
        {
            return framed.getRenderTypes(state, rand, data);
        }
        return ModelUtils.CUTOUT;
    }

    public TextureAtlasSprite getParticleIcon(Object data)
    {
        return switch (particleMode)
        {
            case FIRST -> getSpriteOrDefault(data, false);
            case SECOND -> getSpriteOrDefault(data, true);
            case EITHER ->
            {
                TextureAtlasSprite sprite = getSprite(data, false);
                if (sprite != null)
                {
                    yield sprite;
                }

                sprite = getSprite(data, true);
                if (sprite != null)
                {
                    yield sprite;
                }

                //noinspection deprecation
                yield baseModel.getParticleIcon();
            }
        };
    }

    @Override
    protected void applyInHandTransformation(PoseStack poseStack, ItemDisplayContext ctx)
    {
        if (handTransform != null)
        {
            poseStack.translate(handTransform.x, handTransform.y, handTransform.z);
        }
    }



    protected final Tuple<BakedModel, BakedModel> getModels()
    {
        if (models == null)
        {
            BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
            models = new Tuple<>(
                    dispatcher.getBlockModel(dummyStates.getA()),
                    dispatcher.getBlockModel(dummyStates.getB())
            );
        }
        return models;
    }

    /**
     * Returns the camo-dependent particle texture of the side given by {@code key} when the camo is not air,
     * else returns the basic "framed block" sprite
     */
    protected final TextureAtlasSprite getSpriteOrDefault(Object data, boolean secondary)
    {
        TextureAtlasSprite sprite = getSprite(data, secondary);
        //noinspection deprecation
        return sprite != null ? sprite : baseModel.getParticleIcon();
    }

    protected final TextureAtlasSprite getSprite(Object data, boolean secondary)
    {
        FramedDoubleBlockData doubleData = (data instanceof FramedDoubleBlockData d) ? d : null;
        FramedBlockData halfData = doubleData != null ? (secondary ? doubleData.getRight() : doubleData.getLeft()) : null;
        if (halfData == null)
        {
            return null;
        }
        BakedModel halfModel = secondary ? getModels().getB() : getModels().getA();
        if (halfModel instanceof FramedBlockModel framed)
        {
            return framed.getParticleIcon(halfData);
        }
        return null;
    }

    private static List<BakedQuad> invertTintIndizes(List<BakedQuad> quads)
    {
        List<BakedQuad> inverted = new ArrayList<>(quads.size());
        for (BakedQuad quad : quads)
        {
            inverted.add(ModelUtils.invertTintIndex(quad));
        }
        return inverted;
    }
}
