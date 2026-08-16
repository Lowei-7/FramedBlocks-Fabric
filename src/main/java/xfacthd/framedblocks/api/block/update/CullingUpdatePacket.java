package xfacthd.framedblocks.api.block.update;

import it.unimi.dsi.fastutil.longs.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public record CullingUpdatePacket(LongSet positions) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<CullingUpdatePacket> TYPE = new CustomPacketPayload.Type<>(new ResourceLocation("framedblocks", "culling_update"));
    public static final StreamCodec<FriendlyByteBuf, CullingUpdatePacket> STREAM_CODEC = StreamCodec.of(
            (buf, packet) ->
            {
                buf.writeInt(packet.positions().size());
                packet.positions().forEach(buf::writeLong);
            },
            buf ->
            {
                int count = buf.readInt();
                LongSet positions = new LongArraySet(count);
                for (int i = 0; i < count; i++)
                {
                    positions.add(buf.readLong());
                }
                return new CullingUpdatePacket(positions);
            }
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }

    public void handle()
    {
        Long2ObjectMap<CullingUpdateChunk> chunks = new Long2ObjectArrayMap<>();

        positions.forEach(pos ->
        {
            long chunkPos = ChunkPos.asLong(
                    SectionPos.blockToSectionCoord(BlockPos.getX(pos)),
                    SectionPos.blockToSectionCoord(BlockPos.getX(pos))
            );
            CullingUpdateChunk chunk = chunks.computeIfAbsent(chunkPos, cp ->
                    new CullingUpdateChunk(new ChunkPos(cp), new LongArraySet())
            );
            chunk.positions().add(pos);
        });

        ClientCullingUpdateTracker.handleCullingUpdates(chunks.values());
    }
}
