package xfacthd.framedblocks.api.model.data;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.model.data.ModelProperty;

public sealed class FramedBlockData permits FramedBlockData.Immutable
{
    public static final class Property<T> extends ModelProperty<T> { }
    public static final Property<FramedBlockData> PROPERTY = new Property<>();
    public static final Property<Object> CAMO_DATA = new Property<>();

    protected final boolean[] hidden = new boolean[6];
    protected BlockState camoState = Blocks.AIR.defaultBlockState();
    protected boolean altModel = false;
    protected boolean reinforced = false;
    protected int packedOffsets = 0;

    public FramedBlockData() { }

    public void setCamoState(BlockState camoState)
    {
        this.camoState = camoState;
    }

    public void setSideHidden(Direction side, boolean hide)
    {
        hidden[side.ordinal()] = hide;
    }

    public void setUseAltModel(boolean altModel)
    {
        this.altModel = altModel;
    }

    public void setReinforced(boolean reinforced)
    {
        this.reinforced = reinforced;
    }

    public BlockState getCamoState()
    {
        return camoState;
    }

    public boolean isSideHidden(Direction side)
    {
        return hidden[side.ordinal()];
    }

    public boolean useAltModel()
    {
        return altModel;
    }

    public boolean isReinforced()
    {
        return reinforced;
    }

    public int getPackedOffsets()
    {
        return packedOffsets;
    }

    public void setPackedOffsets(int packedOffsets)
    {
        this.packedOffsets = packedOffsets;
    }



    public static final class Immutable extends FramedBlockData
    {
        public Immutable(BlockState camoState, boolean[] hidden, boolean altModel)
        {
            this.camoState = camoState;
            System.arraycopy(hidden, 0, this.hidden, 0, this.hidden.length);
            this.altModel = altModel;
        }

        @Override
        public void setCamoState(BlockState camoState)
        {
            throw new UnsupportedOperationException("Immutable");
        }

        @Override
        public void setSideHidden(Direction side, boolean hide)
        {
            throw new UnsupportedOperationException("Immutable");
        }

        @Override
        public void setUseAltModel(boolean altModel)
        {
            throw new UnsupportedOperationException("Immutable");
        }

        @Override
        public void setReinforced(boolean reinforced)
        {
            throw new UnsupportedOperationException("Immutable");
        }
    }
}