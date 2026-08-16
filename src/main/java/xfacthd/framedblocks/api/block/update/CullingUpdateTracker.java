package xfacthd.framedblocks.api.block.update;

import it.unimi.dsi.fastutil.longs.LongArraySet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.jetbrains.annotations.ApiStatus;
import xfacthd.framedblocks.FramedBlocks;

import java.util.IdentityHashMap;
import java.util.Map;

public final class CullingUpdateTracker
{
    private static final Map<ResourceKey<Level>, LongSet> UPDATED_POSITIONS = new IdentityHashMap<>();

    @ApiStatus.Internal
    public static void onServerLevelTick(final ServerLevel level)
    {
        ResourceKey<Level> dim = level.dimension();
        LongSet positions = UPDATED_POSITIONS.get(dim);
        if (positions != null && !positions.isEmpty())
        {
            for (ServerPlayer player : level.getServer().getPlayerList().getPlayers())
            {
                if (player.level().dimension() == dim)
                {
                    ServerPlayNetworking.send(player, new CullingUpdatePacket(positions));
                }
            }
            positions.clear();
        }
    }

    public static void enqueueCullingUpdate(Level level, BlockPos pos)
    {
        UPDATED_POSITIONS.computeIfAbsent(level.dimension(), $ -> new LongArraySet()).add(pos.asLong());
    }

    private CullingUpdateTracker() { }
}
