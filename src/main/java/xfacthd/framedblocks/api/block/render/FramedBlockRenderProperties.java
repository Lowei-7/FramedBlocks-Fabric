package xfacthd.framedblocks.api.block.render;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.extensions.common.IClientBlockExtensions;
import xfacthd.framedblocks.api.block.FramedBlockEntity;
import xfacthd.framedblocks.api.block.IFramedBlock;

public class FramedBlockRenderProperties implements IClientBlockExtensions
{
    public static final FramedBlockRenderProperties INSTANCE = new FramedBlockRenderProperties();

    protected FramedBlockRenderProperties() { }

    @Override
    public boolean addHitEffects(BlockState state, Level level, HitResult target, ParticleEngine engine)
    {
        if (target instanceof BlockHitResult hit && level.getBlockEntity(hit.getBlockPos()) instanceof FramedBlockEntity be)
        {
            return addHitEffectsUnsuppressed(state, level, hit, be, engine);
        }
        return suppressParticles(state, level, target instanceof BlockHitResult hit ? hit.getBlockPos() : null);
    }

    protected boolean addHitEffectsUnsuppressed(
            BlockState state, Level level, BlockHitResult hit, FramedBlockEntity be, ParticleEngine engine
    )
    {
        return true;
    }

    @Override
    public boolean addDestroyEffects(BlockState state, Level level, BlockPos pos, ParticleEngine engine)
    {
        if (level.getBlockEntity(pos) instanceof FramedBlockEntity be)
        {
            return addDestroyEffectsUnsuppressed(state, level, pos, be, engine);
        }
        return suppressParticles(state, level, pos);
    }

    protected boolean addDestroyEffectsUnsuppressed(
            BlockState state, Level level, BlockPos pos, FramedBlockEntity be, ParticleEngine engine
    )
    {
        return true;
    }

    protected static boolean suppressParticles(BlockState state, Level level, BlockPos pos)
    {
        if (state.getBlock() instanceof IFramedBlock block && block.getBlockType().allowMakingIntangible())
        {
            return block.isIntangible(state, level, pos, null);
        }
        return false;
    }
}