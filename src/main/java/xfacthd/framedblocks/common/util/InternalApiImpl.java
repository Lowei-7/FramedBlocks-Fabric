package xfacthd.framedblocks.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.ImposterProtoChunk;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LightChunk;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.internal.InternalAPI;
import xfacthd.framedblocks.common.FBContent;
import xfacthd.framedblocks.common.util.compat.FlywheelCompat;
import xfacthd.framedblocks.common.util.compat.StarlightCompat;

public final class InternalApiImpl implements InternalAPI
{
    @Override
    @Nullable
    public BlockEntity getExistingBlockEntity(BlockGetter level, BlockPos pos)
    {
        if (FlywheelCompat.isVirtualLevel(level))
        {
            return level.getBlockEntity(pos);
        }
        return getExistingBlockEntity0(level, pos);
    }

    @Override
    public BlockEntity getBlockEntityForLight(BlockGetter level, BlockPos pos)
    {
        return StarlightCompat.getBlockEntityForLight(level, pos);
    }

    @Override
    public void updateCamoNbt(CompoundTag tag, String stateKey, String stackKey, String camoKey)
    {
        if (tag.contains(stateKey))
        {
            CompoundTag stateTag = tag.getCompound(stateKey);
            tag.remove(stateKey);
            tag.remove(stackKey);
            CompoundTag camoTag = new CompoundTag();
            camoTag.putString("type", FBContent.FACTORY_BLOCK.getId().toString());
            camoTag.put("state", stateTag);
            tag.put(camoKey, camoTag);
        }
    }

    @Nullable
    private static BlockEntity getExistingBlockEntity0(BlockGetter blockGetter, BlockPos pos)
    {
        if (blockGetter instanceof Level level)
        {
            int chunkX = SectionPos.blockToSectionCoord(pos.getX());
            int chunkZ = SectionPos.blockToSectionCoord(pos.getZ());
            ChunkSource chunkSource;
            try
            {
                chunkSource = level.getChunkSource();
            }
            catch (Throwable t)
            {
                // Some mods' fake levels think they are funny for throwing in getChunkSource()
                return null;
            }
            LightChunk chunk = chunkSource.getChunkForLighting(chunkX, chunkZ);
            return chunk != null ? getExistingBlockEntity0(chunk, pos) : null;
        }
        else if (blockGetter instanceof LevelChunk chunk)
        {
            return chunk.getBlockEntities().get(pos);
        }
        else if (blockGetter instanceof ImposterProtoChunk chunk)
        {
            return getExistingBlockEntity0(chunk.getWrapped(), pos);
        }
        return blockGetter.getBlockEntity(pos);
    }
}
