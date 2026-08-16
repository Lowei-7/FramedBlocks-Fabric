package net.minecraftforge.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;

public class NetworkHooks {
    public static void openScreen(ServerPlayer player, MenuProvider containerSupplier) {
        player.openMenu(containerSupplier);
    }
}
