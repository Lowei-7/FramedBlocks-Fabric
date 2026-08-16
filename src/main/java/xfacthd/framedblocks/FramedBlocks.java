package xfacthd.framedblocks;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xfacthd.framedblocks.api.FramedBlocksAPI;
import xfacthd.framedblocks.api.util.FramedConstants;
import xfacthd.framedblocks.common.FBContent;
import xfacthd.framedblocks.common.compat.CompatHandler;
import xfacthd.framedblocks.common.net.NetworkingHandler;
import xfacthd.framedblocks.common.util.ApiImpl;

public final class FramedBlocks implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger(FramedConstants.MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("FramedBlocks Fabric Initialization Started");

        // Initialize the API instance before any block/item initialization, as many blocks query it during setup
        FramedBlocksAPI.INSTANCE.accept(new ApiImpl());

        // Initialize registries
        FBContent.init();

        xfacthd.framedblocks.common.data.facepreds.FullFacePredicates.PREDICATES.initialize();
        xfacthd.framedblocks.common.data.skippreds.SideSkipPredicates.PREDICATES.initialize();
        xfacthd.framedblocks.common.data.conpreds.ConnectionPredicates.PREDICATES.initialize();
        
        // Initialize configs, networking and events
        NetworkingHandler.init();
        xfacthd.framedblocks.common.util.EventHandler.init();
        xfacthd.framedblocks.common.util.CommonConfig.INSTANCE.init();
        xfacthd.framedblocks.common.util.ServerConfig.INSTANCE.init();

        // Initialize optional mod integrations (each is gated on the mod being loaded)
        CompatHandler.init();
        CompatHandler.commonSetup();
    }
}
