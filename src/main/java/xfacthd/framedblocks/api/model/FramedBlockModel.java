package xfacthd.framedblocks.api.model;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.base.Preconditions;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import java.util.Set;
import org.jetbrains.annotations.ApiStatus;
import xfacthd.framedblocks.api.FramedBlocksAPI;
import xfacthd.framedblocks.api.FramedBlocksClientAPI;
import xfacthd.framedblocks.api.block.FramedProperties;
import xfacthd.framedblocks.api.block.cache.StateCache;
import xfacthd.framedblocks.api.model.data.*;
import xfacthd.framedblocks.api.model.quad.QuadModifier;
import xfacthd.framedblocks.api.model.util.ModelCache;
import xfacthd.framedblocks.api.model.util.ModelUtils;
import xfacthd.framedblocks.api.predicate.contex.ConTexMode;
import xfacthd.framedblocks.api.predicate.fullface.FullFacePredicate;
import xfacthd.framedblocks.api.type.IBlockType;
import xfacthd.framedblocks.api.block.IFramedBlock;
import xfacthd.framedblocks.api.util.TestProperties;
import xfacthd.framedblocks.api.util.Utils;

import java.util.*;

@SuppressWarnings("deprecation")
public abstract class FramedBlockModel extends BakedModelProxy implements FabricBakedModel
{
    private static final boolean DISABLE_QUAD_CACHE = net.fabricmc.loader.api.FabricLoader.getInstance().isDevelopmentEnvironment() && TestProperties.DISABLE_MODEL_QUAD_CACHE;
    private static final FramedBlockData DEFAULT_DATA = new FramedBlockData.Immutable(
            Blocks.AIR.defaultBlockState(), new boolean[6], false
    );
    protected static final Set<RenderType> BASE_MODEL_RENDER_TYPES = ModelUtils.CUTOUT;
    public static final ResourceLocation REINFORCEMENT_LOCATION = Utils.rl("block/framed_reinforcement");
    private static BakedModel reinforcementModel = null;

    private final Cache<QuadCacheKey, QuadTable> quadCache = Caffeine.newBuilder()
            .expireAfterAccess(ModelCache.DEFAULT_CACHE_DURATION)
            .build();
    private final Cache<QuadCacheKey, CachedRenderTypes> renderTypeCache = Caffeine.newBuilder()
            .expireAfterAccess(ModelCache.DEFAULT_CACHE_DURATION)
            .build();
    protected final BlockState state;
    private final IBlockType type;
    private final boolean cacheFullRenderTypes;
    private final boolean forceUngeneratedBaseModel;
    private final boolean useBaseModel;
    private final boolean transformAllQuads;
    private final boolean useSolidBase;
    private final StateCache stateCache;

    public FramedBlockModel(BlockState state, BakedModel baseModel)
    {
        super(baseModel);
        this.state = state;
        this.type = ((IFramedBlock) state.getBlock()).getBlockType();
        this.cacheFullRenderTypes = canFullyCacheRenderTypes();
        this.forceUngeneratedBaseModel = forceUngeneratedBaseModel();
        this.useBaseModel = useBaseModel();
        this.transformAllQuads = transformAllQuads(state);
        this.useSolidBase = useSolidNoCamoModel();
        this.stateCache = ((IFramedBlock) state.getBlock()).getCache(state);

        Preconditions.checkState(
                this.useBaseModel || !this.forceUngeneratedBaseModel,
                "FramedBlockModel::useBaseModel() must return true when FramedBlockModel::forceUngeneratedBaseModel() returns true"
        );
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    private static final java.util.concurrent.ConcurrentMap<BlendMode, net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial> RENDER_MATERIALS =
            new java.util.concurrent.ConcurrentHashMap<>();
    private static net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial renderMaterial(BlendMode blendMode)
    {
        return RENDER_MATERIALS.computeIfAbsent(blendMode, mode -> net.fabricmc.fabric.api.renderer.v1.RendererAccess.INSTANCE
                .getRenderer().materialFinder()
                .blendMode(mode)
                .find());
    }

    public void emitBlockQuads(net.minecraft.world.level.BlockAndTintGetter blockView, BlockState state, BlockPos pos, java.util.function.Supplier<RandomSource> randomSupplier, RenderContext context)
    {
        Object extraData = null;
        net.minecraft.world.level.block.entity.BlockEntity be = blockView.getBlockEntity(pos);
        if (be instanceof net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity rabe)
        {
            extraData = rabe.getRenderAttachmentData();
        }
        if (extraData == null) {
            extraData = DEFAULT_DATA;
        }

        net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter emitter = context.getEmitter();
        for (RenderType renderType : getRenderTypes(state, randomSupplier.get(), extraData)) {
            context.pushTransform(q -> {
                return true;
            });
            for (Direction dir : Direction.values()) {
                List<BakedQuad> quads = getQuads(state, dir, randomSupplier.get(), extraData, renderType);
                for (BakedQuad quad : quads) {
                    emitter.fromVanilla(quad.getVertices(), 0, false);
                    emitter.cullFace(quad.getDirection());
                    emitter.colorIndex(quad.getTintIndex());
                    emitter.material(renderMaterial(BlendMode.fromRenderLayer(renderType)));
                    emitter.emit();
                }
            }
            List<BakedQuad> nullQuads = getQuads(state, null, randomSupplier.get(), extraData, renderType);
            for (BakedQuad quad : nullQuads) {
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
            BlockState state, Direction side, RandomSource rand, Object extraData, RenderType renderType
    )
    {
        BlockState camoState = Blocks.AIR.defaultBlockState();

        if (state == null)
        {
            state = this.state;
        }

        FramedBlockData data = (extraData instanceof FramedBlockData fbData) ? fbData : null;
        if (data != null && renderType != null)
        {
            if (side != null && data.isSideHidden(side))
            {
                return Collections.emptyList();
            }

            camoState = data.getCamoState();
            if (camoState != null && !camoState.isAir())
            {
                List<BakedQuad> quads = getCamoQuads(state, camoState, side, rand, extraData, data, renderType);
            return quads;
            }
        }

        if (data == null)
        {
            data = DEFAULT_DATA;
        }
        if (renderType == null)
        {
            renderType = RenderType.cutout();
        }
        if (camoState == null || camoState.isAir())
        {
            return getCamoQuads(state, null, side, rand, extraData, data, renderType);
        }

        return Collections.emptyList();
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand)
    {
        if (state == null)
        {
            state = this.state;
        }
        return getCamoQuads(state, null, side, rand, null, DEFAULT_DATA, RenderType.cutout());
    }

    public Set<RenderType> getRenderTypes(BlockState state, RandomSource rand, Object data)
    {
        FramedBlockData fbData = (data instanceof FramedBlockData fbd) ? fbd : null;
        if (fbData == null)
        {
            fbData = DEFAULT_DATA;
        }

        BlockState camoState = fbData.getCamoState();
        BlockState keyState = camoState;
        if (camoState == null || camoState.isAir())
        {
            camoState = Blocks.AIR.defaultBlockState();
            keyState = getNoCamoModelState(FramedBlocksAPI.getInstance().defaultModelState(), fbData);
        }

        CachedRenderTypes cachedTypes = getCachedRenderTypes(keyState, camoState, rand, data);
        if (cacheFullRenderTypes)
        {
            return cachedTypes.allTypes;
        }

        Set<RenderType> renderTypes = cachedTypes.camoTypes;
        Set<RenderType> overlayTypes = getAdditionalRenderTypes(rand, data);
        if (!overlayTypes.isEmpty())
        {
            Set<RenderType> union = new java.util.HashSet<>(renderTypes);
            union.addAll(overlayTypes);
            renderTypes = union;
        }
        return renderTypes;
    }

    private CachedRenderTypes getCachedRenderTypes(
            BlockState keyState, BlockState camoState, RandomSource rand, Object data
    )
    {
        return renderTypeCache.get(
                makeCacheKey(keyState, null, data),
                key -> buildRenderTypeCache(camoState, rand, data)
        );
    }

    private CachedRenderTypes buildRenderTypeCache(BlockState camoState, RandomSource rand, Object data)
    {
        Set<RenderType> camoTypes = BASE_MODEL_RENDER_TYPES;
        if (!camoState.isAir())
        {
            Set<RenderType> union = new java.util.HashSet<>(ModelCache.getRenderTypes(camoState, rand, null));
            union.addAll(ModelCache.getCamoRenderTypes(camoState, rand, data));
            camoTypes = union;
        }
        return new CachedRenderTypes(camoTypes, cacheFullRenderTypes ? getAdditionalRenderTypes(rand, data) : Set.of());
    }

    private List<BakedQuad> getCamoQuads(
            BlockState state,
            BlockState camoState,
            Direction side,
            RandomSource rand,
            Object extraData,
            FramedBlockData fbData,
            RenderType renderType
    )
    {
        Object camoData;
        BakedModel model;
        boolean noProcessing;
        boolean noCamo = camoState == null;
        boolean needCtCtx;
        boolean camoInRenderType;
        boolean addReinforcement;
        boolean altModel;

        if (noCamo)
        {
            needCtCtx = false;
            camoState = getNoCamoModelState(FramedBlocksAPI.getInstance().defaultModelState(), fbData);
            addReinforcement = useBaseModel && fbData.isReinforced();
            altModel = useBaseModel && fbData.useAltModel();
            camoInRenderType = BASE_MODEL_RENDER_TYPES.contains(renderType);
            noProcessing = (camoInRenderType && forceUngeneratedBaseModel) || stateCache.isFullFace(side);
            model = getCamoModel(camoState, useBaseModel, altModel);
            camoData = null;
        }
        else
        {
            noProcessing = stateCache.isFullFace(side);
            needCtCtx = type.supportsConnectedTextures() && needCtContext(noProcessing, type.getMinimumConTexMode());
            model = getCamoModel(camoState, false, false);
            camoData = needCtCtx ? ModelUtils.getCamoModelData(extraData) : null;
            camoInRenderType = getCachedRenderTypes(camoState, camoState, rand, camoData).camoTypes.contains(renderType);
            addReinforcement = false;
            altModel = false;
        }

        if (noProcessing)
        {
            Set<RenderType> addLayers = getAdditionalRenderTypes(rand, extraData);
            boolean additionalQuads = addLayers.contains(renderType);
            if (!camoInRenderType && !additionalQuads && !addReinforcement)
            {
                return List.of();
            }

            ArrayList<BakedQuad> quads = new ArrayList<>();

            if (camoInRenderType)
            {
                List<BakedQuad> modelQuads = ModelUtils.getCullableQuads(model, camoState, rand, camoData, renderType, side);
                Utils.copyAll(modelQuads, quads);
            }

            if (additionalQuads)
            {
                getAdditionalQuads(quads, side, state, rand, extraData, renderType);
            }

            if (addReinforcement && renderType == RenderType.cutout())
            {
                Utils.copyAll(ModelUtils.getCullableQuads(reinforcementModel, camoState, rand, camoData, renderType, side), quads);
            }

            return quads;
        }
        else
        {
            Object ctCtx = needCtCtx ? FramedBlocksClientAPI.getInstance().extractCTContext(camoData) : null;
            if (DISABLE_QUAD_CACHE)
            {
                return buildQuadCache(state, camoState, rand, extraData, ctCtx != null ? camoData : null, noCamo, addReinforcement, altModel)
                        .getQuads(renderType, side);
            }
            List<BakedQuad> cachedQuads = quadCache.get(
                    makeCacheKey(camoState, ctCtx, extraData),
                    key -> buildQuadCache(state, key.state(), rand, extraData, ctCtx != null ? camoData : null, noCamo, addReinforcement, altModel)
            ).getQuads(renderType, side);
            return cachedQuads;
        }
    }

    private static boolean needCtContext(boolean noProcessing, ConTexMode minMode)
    {
        ConTexMode mode = FramedBlocksClientAPI.getInstance().getConTexMode();
        if (mode == ConTexMode.NONE)
        {
            return false;
        }
        return noProcessing || (mode.atleast(ConTexMode.FULL_EDGE) && mode.atleast(minMode));
    }

    /**
     * Builds a {@link RenderType} -> {@link Direction} -> {@link List<BakedQuad>} table with all render types used by this model
     */
    private QuadTable buildQuadCache(
            BlockState state,
            BlockState camoState,
            RandomSource rand,
            Object data,
            Object camoData,
            boolean noCamo,
            boolean addReinforcement,
            boolean altModel
    )
    {
        QuadTable quadTable = new QuadTable();

        Set<RenderType> modelLayers = getRenderTypes(state, rand, data);
        Set<RenderType> camoLayers = BASE_MODEL_RENDER_TYPES;
        if (!noCamo)
        {
            camoLayers = ModelCache.getRenderTypes(camoState, rand, camoData);
        }
        else
        {
            // Make sure the RenderType set being iterated actually contains the no-camo layers in case getQuads()
            // was called with a null RenderType while a camo is provided (i.e. block breaking overlay)
            Set<RenderType> union = new java.util.HashSet<>(modelLayers);
            union.addAll(camoLayers);
            modelLayers = union;
        }

        BakedModel camoModel = getCamoModel(camoState, noCamo && useBaseModel, altModel);

        for (RenderType renderType : modelLayers)
        {
            boolean camoInRenderType = camoLayers.contains(renderType);

            makeQuadsForLayer(
                    quadTable,
                    state,
                    camoState,
                    camoModel,
                    rand,
                    data,
                    camoData,
                    renderType,
                    camoInRenderType,
                    addReinforcement && renderType == RenderType.cutout()
            );
        }
        quadTable.bindRenderType(null);

        return quadTable;
    }

    /**
     * Builds the list of quads per side for a given {@linkplain BlockState camo state} and {@link RenderType}
     */
    private void makeQuadsForLayer(
            QuadTable quadMap,
            BlockState state,
            BlockState camoState,
            BakedModel camoModel,
            RandomSource rand,
            Object data,
            Object camoData,
            RenderType renderType,
            boolean camoInRenderType,
            boolean addReinforcement
    )
    {
        quadMap.initializeForLayer(renderType);

        if (camoInRenderType)
        {
            ArrayList<BakedQuad> quads = (ArrayList<BakedQuad>) ModelUtils.getAllCullableQuads(camoModel, camoState, rand, camoData, renderType);
            if (addReinforcement)
            {
                Utils.copyAll(ModelUtils.getAllCullableQuads(reinforcementModel, camoState, rand, camoData, renderType), quads);
            }
            if (!transformAllQuads)
            {
                quads.removeIf(q -> stateCache.isFullFace(q.getDirection()));
            }

            for (BakedQuad quad : quads)
            {
                transformQuad(quadMap, quad, data);
            }
            postProcessQuads(quadMap);
        }

        Set<RenderType> addLayers = getAdditionalRenderTypes(rand, data);
        if (addLayers.contains(renderType))
        {
            getAdditionalQuads(quadMap, state, rand, data, renderType);
        }
    }

    /**
     * Called for each {@link BakedQuad} of the camo block's model for whose side this block's
     * {@link FullFacePredicate#test(BlockState, Direction)} returns {@code false}.
     * @param quadMap The target map to put all final quads into
     * @param quad The source quad. Must not be modified directly, use {@link QuadModifier}s to
     *             modify the quad
     * @param data The extra data
     */
    protected void transformQuad(Map<Direction, List<BakedQuad>> quadMap, BakedQuad quad, Object data)
    {
        transformQuad(quadMap, quad);
    }

    /**
     * Called for each {@link BakedQuad} of the camo block's model for whose side this block's
     * {@link FullFacePredicate#test(BlockState, Direction)} returns {@code false}.
     * @param quadMap The target map to put all final quads into
     * @param quad The source quad. Must not be modified directly, use {@link QuadModifier}s to
     *             modify the quad
     */
    protected abstract void transformQuad(Map<Direction, List<BakedQuad>> quadMap, BakedQuad quad);

    /**
     * Called after all quads have been piped through {@link FramedBlockModel#transformQuad(Map, BakedQuad)}
     * to apply bulk modifications to all quads, like transformation or rotation
     */
    protected void postProcessQuads(Map<Direction, List<BakedQuad>> quadMap) { }

    /**
     * Return true if the base model loaded from JSON should be used when no camo is applied without going
     * through the quad manipulation process
     */
    protected boolean forceUngeneratedBaseModel()
    {
        return false;
    }

    /**
     * Return true if the base model loaded from JSON should be used instead of the Framed Cube model
     * when no camo is applied. Quad manipulation will still be done if
     * {@link FramedBlockModel#forceUngeneratedBaseModel()} returns false
     * @apiNote Must return true if {@link FramedBlockModel#forceUngeneratedBaseModel()} returns true
     */
    protected boolean useBaseModel()
    {
        return forceUngeneratedBaseModel();
    }

    /**
     * {@return whether the model should use a solid model when no camo is applied}
     * @apiNote Only has an effect if {@link #useBaseModel()} returns {@code false}
     */
    protected boolean useSolidNoCamoModel()
    {
        return false;
    }

    /**
     * Return true if all quads should be submitted for transformation, even if their cull-face would be filtered
     * by the {@link FullFacePredicate}
     */
    protected boolean transformAllQuads(BlockState state)
    {
        return false;
    }

    /**
     * Return true if the full set of {@link RenderType}s including overlay render types returned by
     * {@link FramedBlockModel#getAdditionalRenderTypes(RandomSource, Object)} are only dependent on the
     * {@link BlockState} associated with this model and/or the camo BlockState in the model data and can
     * therefore be cached based on the camo BlockState
     */
    @SuppressWarnings("MethodMayBeStatic")
    protected boolean canFullyCacheRenderTypes()
    {
        return true;
    }

    @ApiStatus.Internal
    protected BlockState getNoCamoModelState(BlockState camoState, FramedBlockData fbData)
    {
        if (fbData.useAltModel())
        {
            camoState = camoState.setValue(FramedProperties.ALT, true);
        }
        if (fbData.isReinforced())
        {
            camoState = camoState.setValue(FramedProperties.REINFORCED, true);
        }
        if (FramedBlocksClientAPI.getInstance().getSolidFrameMode().useSolidFrame(useSolidBase))
        {
            camoState = camoState.setValue(FramedProperties.SOLID_BG, true);
        }
        return camoState;
    }

    /**
     * Return the {@link BakedModel} to use as the camo model for the given camoState
     *
     * @param camoState The {@link BlockState} used as camo
     * @param useBaseModel If true, the {@link BakedModelProxy#baseModel} is requested instead of the model of the given state
     * @param useAltModel If true, an alternate base model is requested such as for use as the second component of a double block
     *
     * @apiNote Most models shouldn't need to override this. If the model loaded from JSON should be used when no camo
     * is applied, return true from {@link FramedBlockModel#useBaseModel()}. If the model loaded from JSON should be
     * used without applying any quad modifications when no camo is applied, return true from
     * {@link FramedBlockModel#forceUngeneratedBaseModel()} as well
     */
    protected BakedModel getCamoModel(BlockState camoState, boolean useBaseModel, boolean useAltModel)
    {
        return getCamoModel(camoState, useBaseModel);
    }

    /**
     * Return the {@link BakedModel} to use as the camo model for the given camoState
     *
     * @param camoState The {@link BlockState} used as camo
     * @param useBaseModel If true, the {@link BakedModelProxy#baseModel} is requested instead of the model of the given state
     *
     * @apiNote Most models shouldn't need to override this. If the model loaded from JSON should be used when no camo
     * is applied, return true from {@link FramedBlockModel#useBaseModel()}. If the model loaded from JSON should be
     * used without applying any quad modifications when no camo is applied, return true from
     * {@link FramedBlockModel#forceUngeneratedBaseModel()} as well
     */
    protected BakedModel getCamoModel(BlockState camoState, boolean useBaseModel)
    {
        if (useBaseModel)
        {
            return baseModel;
        }
        return ModelCache.getModel(camoState);
    }

    protected Set<RenderType> getAdditionalRenderTypes(RandomSource rand, Object extraData)
    {
        return Set.of();
    }

    /**
     * Add additional quads to faces that return {@code true} from {@link FullFacePredicate#test(BlockState, Direction)}<br>
     * The result of this method will NOT be cached, execution should therefore be as fast as possible
     */
    protected void getAdditionalQuads(
            ArrayList<BakedQuad> quads,
            Direction side,
            BlockState state,
            RandomSource rand,
            Object data,
            RenderType renderType
    )
    {
        getAdditionalQuads((List<BakedQuad>) quads, side, state, rand, data, renderType);
    }

    /**
     * Add additional quads to faces that return {@code true} from {@link FullFacePredicate#test(BlockState, Direction)}<br>
     * The result of this method will NOT be cached, execution should therefore be as fast as possible
     * @deprecated Use overload with {@link ArrayList} parameter instead to allow use of {@link Utils#copyAll(List, ArrayList)}
     * as a faster replacement for {@link ArrayList#addAll(Collection)}
     */
    @Deprecated(forRemoval = true)
    @SuppressWarnings("unused")
    protected void getAdditionalQuads(
            List<BakedQuad> quads,
            Direction side,
            BlockState state,
            RandomSource rand,
            Object data,
            RenderType renderType
    )
    { }

    /**
     * Add additional quads to faces that return {@code false} from {@link FullFacePredicate#test(BlockState, Direction)}<br>
     * The result of this method will be cached, processing time is therefore not critical
     */
    protected void getAdditionalQuads(
            Map<Direction, List<BakedQuad>> quadMap,
            BlockState state,
            RandomSource rand,
            Object data,
            RenderType renderType
    )
    { }

    /**
     * Return a custom {@link QuadCacheKey} that holds additional metadata which influences the resulting quads.
     * @implNote The resulting object must at least store the given {@link BlockState} and connected textures context object
     * and should either be a record or have an otherwise properly implemented {@code hashCode()} and {@code equals()}
     * implementation
     * @param state The {@link BlockState} of the camo applied to the block
     * @param ctCtx The current connected textures context object, may be null
     * @param data The frame data
     */
    protected QuadCacheKey makeCacheKey(BlockState state, Object ctCtx, Object data)
    {
        return new SimpleQuadCacheKey(state, ctCtx);
    }

    /**
     * Controls the AO behaviour of light emitting blocks
     * @return true if AO should be used even if the block emits light or false for the vanilla behavior of disabling AO
     *         when the block emits light
     */
    public boolean useAmbientOcclusionWithLightEmission(BlockState state, RenderType layer)
    {
        return FramedBlocksClientAPI.getInstance().shouldForceAmbientOcclusionOnGlowingBlocks();
    }

    /* 
    @Override
    public final ModelData getModelData(BlockAndTintGetter level, BlockPos pos, BlockState state, ModelData tileData)
    {
        if (!type.supportsConnectedTextures())
        {
            return tileData;
        }

        FramedBlockData data = tileData.get(FramedBlockData.PROPERTY);
        if (data == null)
        {
            return tileData;
        }

        BlockState camoState = data.getCamoState();
        if (!camoState.isAir() && needCtContext(stateCache.hasAnyFullFace(), type.getMinimumConTexMode()))
        {
            BakedModel model = ModelCache.getModel(camoState);
            ModelData camoData;
            try
            {
                // Try getting camo data with the enclosing state, some mods may not like that
                // This option provides better CT behaviour
                camoData = model.getModelData(level, pos, state, tileData);
            }
            catch (Throwable t)
            {
                // Fall back to getting camo data with the camo state if a mod didn't like it
                // This option may cause some CT weirdness
                camoData = model.getModelData(level, pos, camoState, tileData);
            }
            tileData = tileData.derive().with(FramedBlockData.CAMO_DATA, camoData).build();
        }
        return tileData;
    }
    */

    public TextureAtlasSprite getParticleIcon(Object data)
    {
        FramedBlockData fbdata = (data instanceof FramedBlockData fbd) ? fbd : null;
        if (fbdata != null)
        {
            BlockState camoState = fbdata.getCamoState();
            if (!camoState.isAir())
            {
                return getCamoModel(camoState, false, fbdata.useAltModel()).getParticleIcon();
            }
        }
        return baseModel.getParticleIcon();
    }

    public final void clearCache()
    {
        quadCache.invalidateAll();
        renderTypeCache.invalidateAll();
    }



    public static void captureReinforcementModel(Map<ResourceLocation, BakedModel> models)
    {
        reinforcementModel = models.get(REINFORCEMENT_LOCATION);
    }



    @SuppressWarnings("unused")
    protected interface QuadCacheKey
    {
        BlockState state();

        Object ctCtx();
    }

    /**
     * @param state The {@link BlockState} of the camo applied to the block
     * @param ctCtx The connected textures context data used by the camo model, may be null
     */
    private record SimpleQuadCacheKey(BlockState state, Object ctCtx) implements QuadCacheKey { }

    private record CachedRenderTypes(Set<RenderType> camoTypes, Set<RenderType> overlayTypes, Set<RenderType> allTypes)
    {
        public CachedRenderTypes(Set<RenderType> camoTypes, Set<RenderType> overlayTypes)
        {
            this(camoTypes, overlayTypes, union(camoTypes, overlayTypes));
        }
        
        private static Set<RenderType> union(Set<RenderType> a, Set<RenderType> b) {
            Set<RenderType> u = new java.util.HashSet<>(a);
            u.addAll(b);
            return u;
        }
    }
}