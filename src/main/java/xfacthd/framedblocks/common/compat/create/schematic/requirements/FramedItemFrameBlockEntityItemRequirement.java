package xfacthd.framedblocks.common.compat.create.schematic.requirements;

import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import net.createmod.catnip.nbt.NBTProcessors;
import net.minecraft.world.item.ItemStack;
import xfacthd.framedblocks.api.block.FramedBlockEntity;
import xfacthd.framedblocks.api.compat.create.FramedBlockEntityItemRequirement;
import xfacthd.framedblocks.common.blockentity.special.FramedItemFrameBlockEntity;

import java.util.List;

public final class FramedItemFrameBlockEntityItemRequirement extends FramedBlockEntityItemRequirement
{
    @Override
    protected void collectAdditionalRequirements(FramedBlockEntity blockEntity, List<ItemRequirement.StackRequirement> requirements)
    {
        if (blockEntity instanceof FramedItemFrameBlockEntity itemFrame && itemFrame.hasItem())
        {
            ItemStack stack = NBTProcessors.withUnsafeNBTDiscarded(itemFrame.getItem().copy());
            requirements.add(consumeStrict(stack));
        }
    }
}
