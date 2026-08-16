package net.minecraftforge.network;

import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;

import java.util.function.Supplier;

public class NetworkEvent
{
    public static class Context
    {
        private final Connection networkManager;
        private final PacketFlow direction;

        public Context(Connection networkManager, PacketFlow direction)
        {
            this.networkManager = networkManager;
            this.direction = direction;
        }

        public PacketFlow getDirection()
        {
            return direction;
        }

        public void enqueueWork(Runnable runnable)
        {
            runnable.run();
        }

        public void setPacketHandled(boolean handled)
        {
            // Stub
        }
    }
}
