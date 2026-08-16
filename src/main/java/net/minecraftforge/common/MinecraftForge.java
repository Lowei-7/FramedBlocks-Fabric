package net.minecraftforge.common;

import net.minecraftforge.eventbus.api.IEventBus;

public class MinecraftForge
{
    public static final IEventBus EVENT_BUS = new IEventBus()
    {
        @Override
        public <T> void addListener(java.util.function.Consumer<T> consumer)
        {
            // Stub: In Fabric, events are registered differently
        }

        @Override
        public <T> void addListener(net.minecraftforge.eventbus.api.EventPriority priority, java.util.function.Consumer<T> consumer)
        {
            addListener(consumer);
        }

        @Override
        public <T> void addListener(net.minecraftforge.eventbus.api.EventPriority priority, boolean receiveCancelled, java.util.function.Consumer<T> consumer)
        {
            addListener(consumer);
        }
    };
}
