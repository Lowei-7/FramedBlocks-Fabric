package xfacthd.framedblocks.common.compat.nocubes;

import net.minecraft.world.level.block.state.BlockState;

public final class NoCubesCompat
{
    public static boolean mayCullNextTo(BlockState state)
    {
        return true;
    }

    private NoCubesCompat() { }
}
