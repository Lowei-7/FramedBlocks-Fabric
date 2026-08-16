package net.minecraftforge.fml.event.lifecycle;

public class FMLClientSetupEvent
{
    public void enqueueWork(Runnable work)
    {
        work.run();
    }
}
