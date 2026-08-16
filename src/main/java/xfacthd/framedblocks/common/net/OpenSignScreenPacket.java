package xfacthd.framedblocks.common.net;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public record OpenSignScreenPacket(BlockPos pos, boolean frontText)
{
    public OpenSignScreenPacket(FriendlyByteBuf buffer)
    {
        this(buffer.readBlockPos(), buffer.readBoolean());
    }

    public void encode(FriendlyByteBuf buffer)
    {
        buffer.writeBlockPos(pos);
        buffer.writeBoolean(frontText);
    }
}