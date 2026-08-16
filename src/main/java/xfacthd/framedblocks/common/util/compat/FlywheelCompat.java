package xfacthd.framedblocks.common.util.compat;

import net.minecraft.world.level.BlockGetter;

public final class FlywheelCompat
{
    public static boolean isVirtualLevel(BlockGetter level)
    {
        return false;
    }

    private FlywheelCompat() { }
}
