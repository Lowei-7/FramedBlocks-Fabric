package xfacthd.framedblocks.common.compat.create.schematic.nbt;

import net.minecraft.nbt.CompoundTag;
import xfacthd.framedblocks.api.block.FramedBlockEntity;
import xfacthd.framedblocks.api.compat.create.FramedBlockSafeNbtWriter;
import xfacthd.framedblocks.common.blockentity.special.FramedChiseledBookshelfBlockEntity;

public final class FramedChiseledBookshelfSafeNbtWriter extends FramedBlockSafeNbtWriter
{
    @Override
    protected void cleanupTag(FramedBlockEntity fbe, CompoundTag tag)
    {
        tag.remove(FramedChiseledBookshelfBlockEntity.INVENTORY_NBT_KEY);
        tag.putInt(FramedChiseledBookshelfBlockEntity.LAST_SLOT_NBT_KEY, -1);
    }
}
