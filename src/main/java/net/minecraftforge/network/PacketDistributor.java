package net.minecraftforge.network;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;

public class PacketDistributor
{
    public static final PacketTarget PLAYER = new PacketTarget();
    public static final PacketTarget ALL = new PacketTarget();
    public static final PacketTarget TRACKING_CHUNK = new PacketTarget();
    public static final PacketTarget SERVER = new PacketTarget();
    public static final PacketTarget TRACKING_ENTITY = new PacketTarget();
    public static final PacketTarget TRACKING_ENTITY_AND_SELF = new PacketTarget();
    public static final PacketTarget ALL_NO_DIMENSION = new PacketTarget();

    public static final DimensionTarget DIMENSION = new DimensionTarget();

    public static class PacketTarget
    {
        public void send(Object packet)
        {
            // Stub: In Fabric, use ServerPlayNetworking.send()
        }

        public PacketTarget with(Object target)
        {
            return this;
        }
    }

    public static class DimensionTarget extends PacketTarget
    {
        @Override
        public PacketTarget with(Object target)
        {
            return this;
        }
    }
}
