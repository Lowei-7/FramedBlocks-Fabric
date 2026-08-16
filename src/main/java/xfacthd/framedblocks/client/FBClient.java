package xfacthd.framedblocks.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import xfacthd.framedblocks.FramedBlocks;
import xfacthd.framedblocks.api.FramedBlocksClientAPI;
import xfacthd.framedblocks.api.block.IFramedBlock;
import xfacthd.framedblocks.api.util.ClientUtils;
import xfacthd.framedblocks.client.data.BlockOutlineRenderers;
import xfacthd.framedblocks.client.data.GhostRenderBehaviours;
import xfacthd.framedblocks.client.model.FramedModelBaking;
import xfacthd.framedblocks.client.render.block.*;
import xfacthd.framedblocks.client.render.item.BlueprintPropertyOverride;
import xfacthd.framedblocks.client.render.special.BlockOutlineRenderer;
import xfacthd.framedblocks.client.render.special.CollapsibleBlockIndicatorRenderer;
import xfacthd.framedblocks.client.render.special.GhostBlockRenderer;
import xfacthd.framedblocks.client.render.util.AnimationSplitterSource;
import xfacthd.framedblocks.client.screen.*;
import xfacthd.framedblocks.client.util.*;
import xfacthd.framedblocks.common.FBContent;
import xfacthd.framedblocks.common.data.BlockType;
import xfacthd.framedblocks.mixin.MenuScreensAccessor;

public final class FBClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        // ---- Key mappings ----
        KeyBindingHelper.registerKeyBinding(KeyMappings.KEYMAPPING_UPDATE_CULLING.get());
        KeyBindingHelper.registerKeyBinding(KeyMappings.KEYMAPPING_WIPE_CACHE.get());
        ClientTickEvents.END_CLIENT_TICK.register(KeyMappings::onClientTick);

        // ---- Client tick (delayed tasks) ----
        ClientTickEvents.END_CLIENT_TICK.register(ClientUtils::onClientTick);

        // ---- Client events ----
        ClientEventHandler.init();

        // ---- Networking ----
        xfacthd.framedblocks.common.net.NetworkingHandler.initClient();

        // ---- Model loading ----
        FramedModelBaking.register();

        // ---- Sprite sources ----
        AnimationSplitterSource.register();

        // ---- Screens ----
        MenuScreensAccessor.framedblocks$register(FBContent.MENU_TYPE_FRAMED_STORAGE.get(), FramedStorageScreen::new);
        MenuScreensAccessor.framedblocks$register(FBContent.MENU_TYPE_FRAMING_SAW.get(), FramingSawScreen::new);
        MenuScreensAccessor.framedblocks$register(FBContent.MENU_TYPE_POWERED_FRAMING_SAW.get(), PoweredFramingSawScreen::new);

        // ---- Render layers ----
        BlockRenderLayerMap.INSTANCE.putBlock(FBContent.BLOCK_FRAMING_SAW.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(FBContent.BLOCK_POWERED_FRAMING_SAW.get(), RenderType.translucent());

        // ---- Block colors ----
        Block[] blocks = FBContent.getRegisteredBlocks().stream()
                .map(reg -> (Block) reg.get())
                .filter(IFramedBlock.class::isInstance)
                .map(IFramedBlock.class::cast)
                .filter(b -> b.getBlockType() != BlockType.FRAMED_TARGET)
                .toArray(Block[]::new);
        ColorProviderRegistry.BLOCK.register(FramedBlockColor.INSTANCE, blocks);

        // ---- Item colors ----
        ColorProviderRegistry.ITEM.register(FramedTargetBlockColor.INSTANCE, FBContent.BLOCK_FRAMED_TARGET.get());

        // ---- Block entity renderers ----
        BlockEntityRendererRegistry.register(FBContent.BE_TYPE_FRAMED_SIGN.get(), FramedSignRenderer::new);
        BlockEntityRendererRegistry.register(FBContent.BE_TYPE_FRAMED_HANGING_SIGN.get(), FramedHangingSignRenderer::new);
        BlockEntityRendererRegistry.register(FBContent.blockEntityTypeFramedChest.get(), FramedChestRenderer::new);
        BlockEntityRendererRegistry.register(FBContent.BE_TYPE_FRAMED_ITEM_FRAME.get(), FramedItemFrameRenderer::new);

        // ---- Blueprint property override ----
        BlueprintPropertyOverride.register();

        // ---- Model loading ----
        // OverlayLoader and model replacement handled by FramedModelBaking

        // ---- Outline renderers ----
        BlockOutlineRenderer.register();
        BlockOutlineRenderers.register();

        // ---- Ghost render / special renderers ----
        GhostBlockRenderer.init();
        GhostRenderBehaviours.register();
        CollapsibleBlockIndicatorRenderer.register();

        FramedBlocks.LOGGER.info("FramedBlocks client initialized");
    }

    static
    {
        FramedBlocksClientAPI.INSTANCE.accept(new ClientApiImpl());
        FramedBlocks.LOGGER.info("FramedBlocksClientAPI initialized");
    }

    public FBClient() { }
}
