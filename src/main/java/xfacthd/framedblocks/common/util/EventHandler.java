package xfacthd.framedblocks.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.Direction;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.level.ServerLevel;
import xfacthd.framedblocks.api.block.FramedBlockEntity;
import xfacthd.framedblocks.api.block.IFramedBlock;
import xfacthd.framedblocks.api.block.update.CullingUpdateTracker;
import xfacthd.framedblocks.common.crafting.FramingSawRecipeCache;

public final class EventHandler
{
    public static void init()
    {
        AttackBlockCallback.EVENT.register(EventHandler::onBlockLeftClick);
        ServerLifecycleEvents.SERVER_STOPPED.register(EventHandler::onServerShutdown);
        ServerLifecycleEvents.SERVER_STARTED.register(EventHandler::onServerStarted);
        ServerTickEvents.END_SERVER_TICK.register(EventHandler::onServerTick);
    }

    public static void onServerStarted(net.minecraft.server.MinecraftServer server)
    {
        xfacthd.framedblocks.common.data.StateCacheBuilder.ensureStateCachesInitialized();
        FramingSawRecipeCache.get(false).update(server.getRecipeManager());
    }

    public static void onServerTick(net.minecraft.server.MinecraftServer server)
    {
        for (ServerLevel level : server.getAllLevels())
        {
            CullingUpdateTracker.onServerLevelTick(level);
        }
    }

    public static InteractionResult onBlockLeftClick(Player player, Level level, InteractionHand hand, BlockPos pos, Direction direction)
    {
        BlockState state = level.getBlockState(pos);

        if (state.getBlock() instanceof IFramedBlock block)
        {
            if (block.handleBlockLeftClick(state, level, pos, player))
            {
                if (level.isClientSide())
                {
                    // ClientAccess.resetDestroyDelay() - pending Fabric port
                }
                return InteractionResult.CONSUME;
            }

            if (ServerConfig.INSTANCE.enableIntangibleFeature() && block.getBlockType().allowMakingIntangible())
            {
                if (level.getBlockEntity(pos) instanceof FramedBlockEntity be && be.isIntangible(null))
                {
                    return InteractionResult.FAIL;
                }
            }
        }
        return InteractionResult.PASS;
    }

    public static void onServerShutdown(net.minecraft.server.MinecraftServer server)
    {
        FramingSawRecipeCache.get(false).clear();
    }



    private EventHandler() { }
}