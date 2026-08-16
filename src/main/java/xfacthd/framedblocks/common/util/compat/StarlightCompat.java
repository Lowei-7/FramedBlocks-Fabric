package xfacthd.framedblocks.common.util.compat;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public final class StarlightCompat
{
    @Nullable
    public static BlockEntity getBlockEntityForLight(BlockGetter level, BlockPos pos)
    {
        return level.getBlockEntity(pos);
    }

    private StarlightCompat() { }
}
