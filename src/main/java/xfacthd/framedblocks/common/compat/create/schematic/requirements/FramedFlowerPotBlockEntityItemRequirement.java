package xfacthd.framedblocks.common.compat.create.schematic.requirements;

import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import net.minecraft.world.item.ItemStack;
import xfacthd.framedblocks.api.block.FramedBlockEntity;
import xfacthd.framedblocks.api.compat.create.FramedBlockEntityItemRequirement;
import xfacthd.framedblocks.common.blockentity.special.FramedFlowerPotBlockEntity;

import java.util.List;

public final class FramedFlowerPotBlockEntityItemRequirement extends FramedBlockEntityItemRequirement
{
    @Override
    protected void collectAdditionalRequirements(FramedBlockEntity blockEntity, List<ItemRequirement.StackRequirement> requirements)
    {
        if (blockEntity instanceof FramedFlowerPotBlockEntity pot && pot.hasFlowerBlock())
        {
            requirements.add(consume(new ItemStack(pot.getFlowerBlock())));
        }
    }
}
