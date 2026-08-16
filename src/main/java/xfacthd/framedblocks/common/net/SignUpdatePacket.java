package xfacthd.framedblocks.common.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;

public record SignUpdatePacket(BlockPos pos, boolean front, String[] lines) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<SignUpdatePacket> TYPE = new CustomPacketPayload.Type<>(new ResourceLocation("framedblocks", "sign_update"));
    private static final StreamCodec<ByteBuf, String[]> LINES_CODEC = ByteBufCodecs.STRING_UTF8
            .apply(ByteBufCodecs.list())
            .map(lines -> lines.toArray(String[]::new), Arrays::asList);
    public static final StreamCodec<FriendlyByteBuf, SignUpdatePacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, SignUpdatePacket::pos,
            ByteBufCodecs.BOOL, SignUpdatePacket::front,
            LINES_CODEC, SignUpdatePacket::lines,
            SignUpdatePacket::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
}
