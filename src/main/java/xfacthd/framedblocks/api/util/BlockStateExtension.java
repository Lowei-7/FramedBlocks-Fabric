package xfacthd.framedblocks.api.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class providing fallback implementations for Forge BlockState extension methods
 * that don't exist in vanilla Minecraft.
 */
public final class BlockStateExtension
{
    public static float getExplosionResistance(BlockState state, Level level, BlockPos pos, Explosion explosion)
    {
        return state.getBlock().getExplosionResistance();
    }

    public static boolean isFlammable(BlockState state, Level level, BlockPos pos, Direction face)
    {
        // Vanilla doesn't expose flammability through BlockState directly
        // FireBlock handles this internally through private maps
        return false;
    }

    public static int getFlammability(BlockState state, Level level, BlockPos pos, Direction face)
    {
        return 0;
    }

    public static int getFireSpreadSpeed(BlockState state, Level level, BlockPos pos, Direction face)
    {
        return 0;
    }

    public static boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter level, BlockPos pos, FluidState fluid)
    {
        return false;
    }

    public static float getFriction(BlockState state, LevelReader level, BlockPos pos, @Nullable Entity entity)
    {
        return state.getBlock().getFriction();
    }

    public static boolean canEntityDestroy(BlockState state, BlockGetter level, BlockPos pos, Entity entity)
    {
        return true;
    }

    public static float[] getBeaconColorMultiplier(BlockState state, LevelReader level, BlockPos pos, BlockPos beaconPos)
    {
        // Default implementation - returns white (no tint)
        // Concrete implementations may override this for specific blocks
        return null;
    }

    private BlockStateExtension() { }
}
