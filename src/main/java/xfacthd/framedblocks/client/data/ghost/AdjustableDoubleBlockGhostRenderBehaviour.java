package xfacthd.framedblocks.client.data.ghost;

import net.minecraftforge.client.model.data.ModelData;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.model.data.FramedBlockData;
import xfacthd.framedblocks.common.blockentity.FramedDoubleBlockEntity;
import xfacthd.framedblocks.common.blockentity.doubled.FramedAdjustableDoubleBlockEntity;

public final class AdjustableDoubleBlockGhostRenderBehaviour extends DoubleBlockGhostRenderBehaviour
{
    private final FramedAdjustableDoubleBlockEntity.OffsetPacker offsetPacker;

    private AdjustableDoubleBlockGhostRenderBehaviour(FramedAdjustableDoubleBlockEntity.OffsetPacker offsetPacker)
    {
        this.offsetPacker = offsetPacker;
    }

    @Override
    public ModelData appendModelData(ItemStack stack, @Nullable ItemStack proxiedStack, BlockPlaceContext ctx, BlockState renderState, boolean secondPass, ModelData data)
    {
        int offsetsLeft = offsetPacker.pack(renderState, FramedAdjustableDoubleBlockEntity.CENTER_PART_HEIGHT, false);
        int offsetsRight = offsetPacker.pack(renderState, FramedAdjustableDoubleBlockEntity.CENTER_PART_HEIGHT, true);

        ModelData dataLeft = (ModelData) data.get(FramedDoubleBlockEntity.DATA_LEFT);
        if (dataLeft != null)
        {
            FramedBlockData frame = dataLeft.get(FramedBlockData.PROPERTY);
            if (frame != null)
            {
                frame.setPackedOffsets(offsetsLeft);
            }
        }
        ModelData dataRight = (ModelData) data.get(FramedDoubleBlockEntity.DATA_RIGHT);
        if (dataRight != null)
        {
            FramedBlockData frame = dataRight.get(FramedBlockData.PROPERTY);
            if (frame != null)
            {
                frame.setPackedOffsets(offsetsRight);
            }
        }
        return data;
    }



    public static AdjustableDoubleBlockGhostRenderBehaviour standard()
    {
        return new AdjustableDoubleBlockGhostRenderBehaviour(FramedAdjustableDoubleBlockEntity::getPackedOffsetsStandard);
    }

    public static AdjustableDoubleBlockGhostRenderBehaviour copycat()
    {
        return new AdjustableDoubleBlockGhostRenderBehaviour(FramedAdjustableDoubleBlockEntity::getPackedOffsetsCopycat);
    }
}
