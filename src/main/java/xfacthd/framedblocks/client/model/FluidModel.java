package xfacthd.framedblocks.client.model;

import com.google.common.base.Preconditions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.util.Utils;

import java.util.*;
import java.util.function.Function;

@SuppressWarnings("deprecation")
public final class FluidModel implements BakedModel
{
    public static final ResourceLocation BARE_MODEL = Utils.rl("fluid/bare");
    public static final ResourceLocation BARE_MODEL_SINGLE = Utils.rl("fluid/bare_single");
    private static BakedModel bareModel;
    private static BakedModel bareModelSingle;
    private static final Function<ResourceLocation, TextureAtlasSprite> SPRITE_GETTER = (loc ->
            Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(loc)
    );
    private final RenderType fluidLayer;
    private final Set<RenderType> fluidLayerSet;
    private final Map<Direction, List<BakedQuad>> quads;
    private final TextureAtlasSprite particles;

    private FluidModel(RenderType fluidLayer, Map<Direction, List<BakedQuad>> quads, TextureAtlasSprite particles)
    {
        this.fluidLayer = fluidLayer;
        this.fluidLayerSet = Set.of(fluidLayer);
        this.quads = quads;
        this.particles = particles;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource random)
    {
        return getQuads(state, side, random, null, RenderType.translucent());
    }

    public List<BakedQuad> getQuads(
            @Nullable BlockState state,
            @Nullable Direction side,
            RandomSource rand,
            Object extraData,
            RenderType layer
    )
    {
        if (side == null || layer != fluidLayer)
        {
            return Collections.emptyList();
        }
        return quads.get(side);
    }

    public Set<RenderType> getRenderTypes(BlockState state, RandomSource rand, Object data)
    {
        return fluidLayerSet;
    }

    @Override
    public boolean useAmbientOcclusion()
    {
        return false;
    }

    @Override
    public boolean isGui3d()
    {
        return false;
    }

    @Override
    public boolean usesBlockLight()
    {
        return false;
    }

    @Override
    public boolean isCustomRenderer()
    {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon()
    {
        return particles;
    }

    @Override
    public ItemTransforms getTransforms()
    {
        return ItemTransforms.NO_TRANSFORMS;
    }

    @Override
    public ItemOverrides getOverrides()
    {
        return ItemOverrides.EMPTY;
    }



    public static FluidModel create(Fluid fluid)
    {
        ResourceLocation stillTexture = getFluidTexture(fluid, 0);
        ResourceLocation flowingTexture = getFluidTexture(fluid, 1);
        Preconditions.checkNotNull(
                stillTexture,
                "Fluid %s returned null from FluidRenderHandler#getFluidSprites()",
                fluid
        );
        Preconditions.checkNotNull(
                flowingTexture,
                "Fluid %s returned null from FluidRenderHandler#getFluidSprites()",
                fluid
        );

        boolean singleTexture = flowingTexture.equals(stillTexture);
        BakedModel bakedModel = singleTexture ? bareModelSingle : bareModel;
        Preconditions.checkNotNull(bakedModel, "Bare fluid model not loaded!");

        Map<Direction, List<BakedQuad>> quads = new EnumMap<>(Direction.class);
        BlockState defState = fluid.defaultFluidState().createLegacyBlock();
        RandomSource random = RandomSource.create();
        RenderType layer = ItemBlockRenderTypes.getRenderLayer(fluid.defaultFluidState());

        for (Direction side : Direction.values())
        {
            quads.put(side, bakedModel.getQuads(defState, side, random));
        }

        return new FluidModel(layer, quads, SPRITE_GETTER.apply(stillTexture));
    }

    public static void cacheBareModels(Map<ResourceLocation, BakedModel> registry)
    {
        bareModel = registry.get(BARE_MODEL);
        bareModelSingle = registry.get(BARE_MODEL_SINGLE);
    }

    private static ResourceLocation getFluidTexture(Fluid fluid, int idx)
    {
        TextureAtlasSprite sprite = getFluidSprite(fluid, idx);
        return sprite != null ? sprite.contents().name() : null;
    }

    private static TextureAtlasSprite getFluidSprite(Fluid fluid, int idx)
    {
        FluidRenderHandler handler = FluidRenderHandlerRegistry.INSTANCE.get(fluid);
        TextureAtlasSprite[] sprites = handler.getFluidSprites(
                Minecraft.getInstance().level,
                null,
                fluid.defaultFluidState()
        );
        if (sprites == null || sprites.length <= idx)
        {
            return null;
        }
        return sprites[idx];
    }
}
