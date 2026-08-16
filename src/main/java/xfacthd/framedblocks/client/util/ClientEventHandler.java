package xfacthd.framedblocks.client.util;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import xfacthd.framedblocks.common.crafting.FramingSawRecipeCache;

public final class ClientEventHandler
{
    public static void init()
    {
        ClientPlayConnectionEvents.DISCONNECT.register(ClientEventHandler::onClientDisconnect);
        // The client recipe cache is updated via MixinClientPacketListener#framedblocks$onRecipesUpdated
    }

    public static void onClientDisconnect(net.minecraft.client.multiplayer.ClientPacketListener handler, net.minecraft.client.Minecraft client)
    {
        FramingSawRecipeCache.get(true).clear();
    }



    private ClientEventHandler() { }
}
