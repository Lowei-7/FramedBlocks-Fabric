package xfacthd.framedblocks.common.net;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record OpenSignScreenPacket(BlockPos pos, boolean frontText) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<OpenSignScreenPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("framedblocks", "open_sign_screen"));
    public static final StreamCodec<FriendlyByteBuf, OpenSignScreenPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, OpenSignScreenPacket::pos,
            ByteBufCodecs.BOOL, OpenSignScreenPacket::frontText,
            OpenSignScreenPacket::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
}
