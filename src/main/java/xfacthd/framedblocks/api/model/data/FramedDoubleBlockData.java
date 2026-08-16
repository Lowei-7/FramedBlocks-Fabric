package xfacthd.framedblocks.api.model.data;

/**
 * Holds the {@link FramedBlockData} for both halves of a double block for rendering.
 */
public final class FramedDoubleBlockData
{
    private final FramedBlockData left;
    private final FramedBlockData right;

    public FramedDoubleBlockData(FramedBlockData left, FramedBlockData right)
    {
        this.left = left;
        this.right = right;
    }

    public FramedBlockData getLeft()
    {
        return left;
    }

    public FramedBlockData getRight()
    {
        return right;
    }
}