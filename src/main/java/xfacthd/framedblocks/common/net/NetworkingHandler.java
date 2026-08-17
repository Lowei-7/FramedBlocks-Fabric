package xfacthd.framedblocks.common.net;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import xfacthd.framedblocks.FramedBlocks;
import xfacthd.framedblocks.api.block.update.CullingUpdatePacket;
import xfacthd.framedblocks.common.blockentity.special.FramedSignBlockEntity;
import xfacthd.framedblocks.common.crafting.FramingSawRecipeCache;
import xfacthd.framedblocks.common.menu.IFramingSawMenu;

import java.util.List;

public final class NetworkingHandler {

    public static void init() {
        PayloadTypeRegistry.playC2S().register(SignUpdatePacket.TYPE, SignUpdatePacket.STREAM_CODEC);
        PayloadTypeRegistry.playC2S().register(SelectFramingSawRecipePacket.TYPE, SelectFramingSawRecipePacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(OpenSignScreenPacket.TYPE, OpenSignScreenPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(CullingUpdatePacket.TYPE, CullingUpdatePacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(ClientboundFramingSawRecipesPayload.TYPE, ClientboundFramingSawRecipesPayload.STREAM_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(SignUpdatePacket.TYPE, (packet, context) -> {
            handleSignUpdate(context.player(), packet);
        });

        ServerPlayNetworking.registerGlobalReceiver(SelectFramingSawRecipePacket.TYPE, (packet, context) -> {
            handleSelectRecipe(context.player(), packet);
        });
    }

    @Environment(EnvType.CLIENT)
    public static void initClient() {
        ClientPlayNetworking.registerGlobalReceiver(CullingUpdatePacket.TYPE, (packet, context) -> {
            context.client().execute(packet::handle);
        });
        ClientPlayNetworking.registerGlobalReceiver(OpenSignScreenPacket.TYPE, (packet, context) -> {
            context.client().execute(() -> FramedSignScreenOpener.openScreen(context.client(), packet));
        });
        ClientPlayNetworking.registerGlobalReceiver(ClientboundFramingSawRecipesPayload.TYPE, (packet, context) -> {
            context.client().execute(() -> FramingSawRecipeCache.get(true).update(packet.recipes()));
        });
    }

    /** Send a {@link SignUpdatePacket} from the client to the server */
    @Environment(EnvType.CLIENT)
    public static void sendSignUpdate(BlockPos pos, boolean front, String[] lines) {
        ClientPlayNetworking.send(new SignUpdatePacket(pos, front, lines));
    }

    /** Send a {@link SelectFramingSawRecipePacket} from the client to the server */
    @Environment(EnvType.CLIENT)
    public static void sendSelectFramingSawRecipe(int containerId, int recipeIdx) {
        ClientPlayNetworking.send(new SelectFramingSawRecipePacket(containerId, recipeIdx));
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
