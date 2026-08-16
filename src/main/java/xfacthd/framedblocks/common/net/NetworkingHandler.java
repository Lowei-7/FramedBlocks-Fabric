package xfacthd.framedblocks.common.net;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import xfacthd.framedblocks.FramedBlocks;
import xfacthd.framedblocks.api.block.update.CullingUpdatePacket;
import xfacthd.framedblocks.common.blockentity.special.FramedSignBlockEntity;
import xfacthd.framedblocks.common.menu.IFramingSawMenu;

import java.util.List;

public final class NetworkingHandler {

    public static final ResourceLocation OPEN_SIGN_SCREEN_ID = new ResourceLocation("framedblocks", "open_sign_screen");
    public static final ResourceLocation SELECT_FRAMING_SAW_RECIPE_ID = new ResourceLocation("framedblocks", "select_framing_saw_recipe");
    public static final ResourceLocation SIGN_UPDATE_ID = new ResourceLocation("framedblocks", "sign_update");
    public static final ResourceLocation CULLING_UPDATE_ID = new ResourceLocation("framedblocks", "culling_update");

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(SIGN_UPDATE_ID, (ServerPlayNetworking.PlayChannelHandler) (server, player, handler, buf, responseSender) -> {
            SignUpdatePacket packet = SignUpdatePacket.decode(buf);
            handleSignUpdate(player, packet);
        });

        ServerPlayNetworking.registerGlobalReceiver(SELECT_FRAMING_SAW_RECIPE_ID, (ServerPlayNetworking.PlayChannelHandler) (server, player, handler, buf, responseSender) -> {
            SelectFramingSawRecipePacket packet = new SelectFramingSawRecipePacket(buf);
            handleSelectRecipe(player, packet);
        });
    }

    @Environment(EnvType.CLIENT)
    public static void initClient() {
        ClientPlayNetworking.registerGlobalReceiver(CULLING_UPDATE_ID, (client, handler, buf, responseSender) -> {
            CullingUpdatePacket packet = CullingUpdatePacket.decode(buf);
            client.execute(packet::handle);
        });
        ClientPlayNetworking.registerGlobalReceiver(OPEN_SIGN_SCREEN_ID, (client, handler, buf, responseSender) -> {
            OpenSignScreenPacket packet = new OpenSignScreenPacket(buf);
            client.execute(() -> FramedSignScreenOpener.openScreen(client, packet));
        });
    }

    /** Send a {@link SignUpdatePacket} from the client to the server */
    @Environment(EnvType.CLIENT)
    public static void sendSignUpdate(BlockPos pos, boolean front, String[] lines) {
        SignUpdatePacket packet = new SignUpdatePacket(pos, front, lines);
        FriendlyByteBuf buf = PacketByteBufs.create();
        packet.encode(buf);
        ClientPlayNetworking.send(SIGN_UPDATE_ID, buf);
    }

    /** Send a {@link SelectFramingSawRecipePacket} from the client to the server */
    @Environment(EnvType.CLIENT)
    public static void sendSelectFramingSawRecipe(int containerId, int recipeIdx) {
        SelectFramingSawRecipePacket packet = new SelectFramingSawRecipePacket(containerId, recipeIdx);
        FriendlyByteBuf buf = PacketByteBufs.create();
        packet.encode(buf);
        ClientPlayNetworking.send(SELECT_FRAMING_SAW_RECIPE_ID, buf);
    }

    private static void handleSignUpdate(ServerPlayer player, SignUpdatePacket packet) {
        List<net.minecraft.server.network.FilteredText> strippedLines = java.util.stream.Stream.of(packet.lines())
                .map(l -> net.minecraft.server.network.FilteredText.passThrough(net.minecraft.ChatFormatting.stripFormatting(l)))
                .toList();
        if (player.level().hasChunkAt(packet.pos()) && player.level().getBlockEntity(packet.pos()) instanceof FramedSignBlockEntity sign) {
            if (sign.isWaxed() || !player.getUUID().equals(sign.getEditingPlayer())) {
                FramedBlocks.LOGGER.warn("Player {} just tried to change non-editable sign at {}", player.getName().getString(), packet.pos());
                return;
            }
            player.resetLastActionTime();
            sign.updateTextFromPacket(player, packet.front(), strippedLines);
        }
    }

    private static void handleSelectRecipe(ServerPlayer player, SelectFramingSawRecipePacket packet) {
        AbstractContainerMenu menu = player.containerMenu;
        if (menu.containerId == packet.containerId() && menu instanceof IFramingSawMenu) {
            menu.clickMenuButton(player, packet.recipeIdx());
        }
    }

    private NetworkingHandler() { }
}
