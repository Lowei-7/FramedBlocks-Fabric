package xfacthd.framedblocks.common.net;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public record SignUpdatePacket(BlockPos pos, boolean front, String[] lines)
{
    public static SignUpdatePacket decode(FriendlyByteBuf buffer)
    {
        BlockPos pos = buffer.readBlockPos();
        boolean front = buffer.readBoolean();

        int count = buffer.readByte();
        String[] lines = new String[count];
        for (int i = 0; i < count; i++)
        {
            lines[i] = buffer.readUtf(384);
        }

        return new SignUpdatePacket(pos, front, lines);
    }

    public void encode(FriendlyByteBuf buffer)
    {
        buffer.writeBlockPos(pos);
        buffer.writeBoolean(front);

        buffer.writeByte(lines.length);
        for (String line : lines)
        {
            buffer.writeUtf(line);
        }
    }
}
