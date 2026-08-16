package net.minecraftforge.fml.event.lifecycle;

import net.minecraftforge.fml.InterModComms;

import java.util.stream.Stream;

public class InterModProcessEvent
{
    public Stream<InterModComms.IMCMessage> getIMCStream()
    {
        return Stream.empty();
    }

    public Stream<InterModComms.IMCMessage> getIMCStream(String modId)
    {
        return Stream.empty();
    }
}
