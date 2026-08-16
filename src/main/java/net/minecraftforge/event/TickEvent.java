package net.minecraftforge.event;

import net.minecraft.world.level.Level;

public class TickEvent
{
    public enum Type
    {
        CLIENT,
        SERVER,
        LEVEL
    }

    public enum Phase
    {
        START,
        END
    }

    public final Type type;
    public final Phase phase;

    public TickEvent(Type type, Phase phase)
    {
        this.type = type;
        this.phase = phase;
    }

    public static class ClientTickEvent extends TickEvent
    {
        public ClientTickEvent(Phase phase)
        {
            super(Type.CLIENT, phase);
        }
    }

    public static class ServerTickEvent extends TickEvent
    {
        public ServerTickEvent(Phase phase)
        {
            super(Type.SERVER, phase);
        }
    }

    public static class LevelTickEvent extends TickEvent
    {
        public final Level level;

        public LevelTickEvent(Level level, Phase phase)
        {
            super(Type.LEVEL, phase);
            this.level = level;
        }
    }
}
