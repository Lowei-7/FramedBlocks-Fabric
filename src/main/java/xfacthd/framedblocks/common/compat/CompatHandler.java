package xfacthd.framedblocks.common.compat;

import xfacthd.framedblocks.common.compat.athena.AthenaCompat;
import xfacthd.framedblocks.common.compat.create.CreateCompat;
import xfacthd.framedblocks.common.compat.emi.EmiCompat;
import xfacthd.framedblocks.common.compat.jei.JeiCompat;
import xfacthd.framedblocks.common.compat.rei.ReiCompat;
import xfacthd.framedblocks.common.compat.supplementaries.SupplementariesCompat;

public final class CompatHandler
{
    public static void init()
    {
        AthenaCompat.init();
        CreateCompat.init();
        EmiCompat.init();
        JeiCompat.init();
        ReiCompat.init();
        SupplementariesCompat.init();
    }

    public static void commonSetup()
    {
        CreateCompat.commonSetup();
    }



    private CompatHandler() { }
}
