package xfacthd.framedblocks.common.util.compat;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelReader;

public final class SupplementariesCompat
{
    public static final ResourceLocation HANGING_MODEL_LOCATION = ResourceLocation.fromNamespaceAndPath("supplementaries", "block/hanging_flower_pot_rope");
    private static boolean loaded = false;

    public static void init()
    {
        loaded = false;
    }

    public static boolean isLoaded()
    {
        return loaded;
    }

    public static boolean canSurviveHanging(LevelReader level, BlockPos pos)
    {
        return false;
    }

    private SupplementariesCompat() { }
}
