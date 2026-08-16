package xfacthd.framedblocks.common.compat.emi;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;

import xfacthd.framedblocks.api.util.Utils;

public final class EmiCompat
{
    public static final ResourceLocation SAW_ID = Utils.rl("framing_saw");

    private static boolean loadedClient = false;

    public static void init()
    {
        if (net.fabricmc.loader.api.FabricLoader.getInstance().getEnvironmentType() == net.fabricmc.api.EnvType.CLIENT && ModList.get().isLoaded("emi"))
        {
            loadedClient = true;
        }
    }

    public static boolean isLoaded()
    {
        return loadedClient;
    }



    private EmiCompat() { }
}
