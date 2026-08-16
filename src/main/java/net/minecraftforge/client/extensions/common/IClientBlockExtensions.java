package net.minecraftforge.client.extensions.common;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * Fabric stub for Forge IClientBlockExtensions interface
 * Used for block render customization on client
 */
public interface IClientBlockExtensions
{
    default float getAmbientOcclusionLightValue()
    {
        return 1.0F;
    }

    default boolean isAmbientOcclusionEnabled(BlockState state)
    {
        return true;
    }

    default Integer getRenderColor(BlockState state)
    {
        return null;
    }

    default boolean shouldRenderBreathingFace(BlockState state)
    {
        return false;
    }

    default boolean addHitEffects(BlockState state, Level level, HitResult target, ParticleEngine engine)
    {
        return false;
    }

    default boolean addDestroyEffects(BlockState state, Level level, BlockPos pos, ParticleEngine engine)
    {
        return false;
    }
}
