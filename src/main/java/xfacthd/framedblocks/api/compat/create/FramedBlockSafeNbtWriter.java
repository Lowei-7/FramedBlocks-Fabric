package xfacthd.framedblocks.api.compat.create;

import com.simibubi.create.api.schematic.nbt.SafeNbtWriterRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import xfacthd.framedblocks.api.block.FramedBlockEntity;
import xfacthd.framedblocks.api.block.IFramedDoubleBlockEntity;
import xfacthd.framedblocks.api.camo.CamoContainer;

import java.util.Set;

public class FramedBlockSafeNbtWriter implements SafeNbtWriterRegistry.SafeNbtWriter
{
    public static final FramedBlockSafeNbtWriter INSTANCE = new FramedBlockSafeNbtWriter();

    private final Set<String> keysToClean;

    public FramedBlockSafeNbtWriter(String... keysToClean)
    {
        this.keysToClean = Set.of(keysToClean);
    }

    @Override
    public final void writeSafe(BlockEntity be, CompoundTag tag)
    {
        if (be instanceof FramedBlockEntity fbe)
        {
            tag.merge(be.saveWithFullMetadata());

            CamoContainer camoOne = fbe.getCamo();
            if (!camoOne.getType().isBlock())
            {
                tag.remove(FramedBlockEntity.CAMO_NBT_KEY);
            }
            if (fbe instanceof IFramedDoubleBlockEntity fdbe)
            {
                CamoContainer camoTwo = fdbe.getCamoTwo();
                if (!camoTwo.getType().isBlock())
                {
                    tag.remove(IFramedDoubleBlockEntity.CAMO_TWO_NBT_KEY);
                }
            }
            for (String key : keysToClean)
            {
                tag.remove(key);
            }

            cleanupTag(fbe, tag);
        }
    }

    protected void cleanupTag(FramedBlockEntity fbe, CompoundTag tag) { }
}
