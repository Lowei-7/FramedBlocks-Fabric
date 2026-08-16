package xfacthd.framedblocks.common.net;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SelectFramingSawRecipePacket(int containerId, int recipeIdx) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<SelectFramingSawRecipePacket> TYPE = new CustomPacketPayload.Type<>(new ResourceLocation("framedblocks", "select_framing_saw_recipe"));
    public static final StreamCodec<FriendlyByteBuf, SelectFramingSawRecipePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SelectFramingSawRecipePacket::containerId,
            ByteBufCodecs.INT, SelectFramingSawRecipePacket::recipeIdx,
            SelectFramingSawRecipePacket::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
}
