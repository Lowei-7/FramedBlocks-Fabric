package xfacthd.framedblocks.common.compat.contex;

import net.minecraftforge.fml.ModList;

import xfacthd.contex.api.utils.Constants;
import xfacthd.framedblocks.client.data.ConTexDataHandler;

public final class ConTexCompat
{
    public static void init()
    {
        if (net.fabricmc.loader.api.FabricLoader.getInstance().getEnvironmentType() == net.fabricmc.api.EnvType.CLIENT && ModList.get().isLoaded("contex"))
        {
            GuardedClientAccess.init();
        }
    }

    private static final class GuardedClientAccess
    {
        public static void init()
        {
            ConTexDataHandler.addConTexProperty(Constants.CT_STATE_PROPERTY);
        }
    }



    private ConTexCompat() { }
}
