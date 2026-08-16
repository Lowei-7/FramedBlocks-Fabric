package xfacthd.framedblocks.common.compat.create.schematic.nbt;

import net.createmod.catnip.nbt.NBTProcessors;
import net.minecraft.nbt.CompoundTag;
import xfacthd.framedblocks.api.block.FramedBlockEntity;
import xfacthd.framedblocks.api.compat.create.FramedBlockSafeNbtWriter;
import xfacthd.framedblocks.common.blockentity.special.FramedItemFrameBlockEntity;

public final class FramedItemFrameSafeNbtWriter extends FramedBlockSafeNbtWriter
{
    @Override
    protected void cleanupTag(FramedBlockEntity fbe, CompoundTag tag)
    {
        NBTProcessors.itemProcessor(FramedItemFrameBlockEntity.ITEM_NBT_KEY).apply(tag);
    }
}
