package xfacthd.framedblocks.client.data.ghost;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.camo.CamoContainer;
import xfacthd.framedblocks.api.camo.EmptyCamoContainer;
import xfacthd.framedblocks.api.ghost.CamoPair;
import xfacthd.framedblocks.api.ghost.GhostRenderBehaviour;

public sealed class DoubleBlockGhostRenderBehaviour implements GhostRenderBehaviour
        permits DoublePanelGhostRenderBehaviour, AdjustableDoubleBlockGhostRenderBehaviour
{
    @Override
    public CamoPair readCamo(ItemStack stack, @Nullable ItemStack proxiedStack, boolean secondPass)
    {
        return readDoubleCamo(stack);
    }

    public static CamoPair readDoubleCamo(ItemStack stack)
    {
        //noinspection ConstantConditions
        CustomData blockEntityData = stack.get(DataComponents.BLOCK_ENTITY_DATA);
        if (blockEntityData != null)
        {
            CompoundTag blockEntityTag = blockEntityData.getUnsafe();
            CamoContainer camo = EmptyCamoContainer.EMPTY;
            CamoContainer camoTwo = EmptyCamoContainer.EMPTY;
            if (blockEntityTag.contains("camo"))
            {
                camo = CamoContainer.load(blockEntityTag.getCompound("camo"));
            }
            if (blockEntityTag.contains("camo_two"))
            {
                camoTwo = CamoContainer.load(blockEntityTag.getCompound("camo_two"));
            }

            return new CamoPair(camo.getState(), camoTwo.getState());
        }
        return CamoPair.EMPTY;
    }
}
