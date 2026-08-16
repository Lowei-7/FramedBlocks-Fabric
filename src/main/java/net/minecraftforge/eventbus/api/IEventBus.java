package net.minecraftforge.eventbus.api;

import java.util.function.Consumer;

public interface IEventBus
{
    <T> void addListener(Consumer<T> consumer);

    default <T> void addListener(EventPriority priority, Consumer<T> consumer)
    {
        addListener(consumer);
    }

    default <T> void addListener(EventPriority priority, boolean receiveCancelled, Consumer<T> consumer)
    {
        addListener(consumer);
    }
}
