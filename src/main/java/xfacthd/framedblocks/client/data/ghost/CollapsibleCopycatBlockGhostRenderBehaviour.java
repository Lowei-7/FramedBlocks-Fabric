package xfacthd.framedblocks.client.data.ghost;

import net.minecraftforge.client.model.data.ModelData;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.ghost.GhostRenderBehaviour;
import xfacthd.framedblocks.api.model.data.FramedBlockData;
import xfacthd.framedblocks.common.blockentity.special.FramedCollapsibleCopycatBlockEntity;
import xfacthd.framedblocks.common.data.PropertyHolder;

public final class CollapsibleCopycatBlockGhostRenderBehaviour implements GhostRenderBehaviour
{
    @Override
    @Nullable
    public BlockState getRenderState(
            ItemStack stack,
            @Nullable ItemStack proxiedStack,
            BlockHitResult hit,
            BlockPlaceContext ctx,
            BlockState hitState,
            boolean secondPass
    )
    {
        BlockState state = GhostRenderBehaviour.super.getRenderState(stack, proxiedStack, hit, ctx, hitState, secondPass);
        //noinspection ConstantConditions
        CustomData blockEntityData = stack.get(DataComponents.BLOCK_ENTITY_DATA);
        if (state != null && blockEntityData != null)
        {
            int offsets = blockEntityData.getUnsafe().getInt("offsets");
            int solidFaces = FramedCollapsibleCopycatBlockEntity.computeSolidFaces(offsets);
            state = state.setValue(PropertyHolder.SOLID_FACES, solidFaces);
        }
        return state;
    }

    @Override
    public ModelData appendModelData(
            ItemStack stack,
            @Nullable ItemStack proxiedStack,
            BlockPlaceContext ctx,
            BlockState renderState,
            boolean secondPass,
            ModelData data
    )
    {
        //noinspection ConstantConditions
        CustomData blockEntityData = stack.get(DataComponents.BLOCK_ENTITY_DATA);
        if (blockEntityData != null)
        {
            int offsets = blockEntityData.getUnsafe().getInt("offsets");
            FramedBlockData frame = data.get(FramedBlockData.PROPERTY);
            if (frame != null)
            {
                frame.setPackedOffsets(offsets);
            }
        }
        return data;
    }
}
