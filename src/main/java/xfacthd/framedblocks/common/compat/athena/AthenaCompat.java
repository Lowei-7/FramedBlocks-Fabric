package xfacthd.framedblocks.common.compat.athena;

import net.minecraftforge.fml.ModList;

public final class AthenaCompat
{
    public static void init()
    {
        // The Fabric port of Athena does not expose a connectivity ModelData property (the
        // Forge `AthenaBakedModel.DATA` field has no Fabric equivalent), so the ConTex
        // texture extension cannot be hooked into Athena models. Athena's own connected
        // textures still work independently on framed blocks.
        ModList.get().isLoaded("athena");
    }

    private AthenaCompat() { }
}
