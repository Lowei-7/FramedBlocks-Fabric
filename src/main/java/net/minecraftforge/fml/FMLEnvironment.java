package net.minecraftforge.fml;

public class FMLEnvironment
{
    public static final boolean production = true;

    public enum Dist
    {
        CLIENT,
        DEDICATED_SERVER;

        public boolean isClient()
        {
            return this == CLIENT;
        }
    }

    public static final Dist dist = Dist.CLIENT;
}