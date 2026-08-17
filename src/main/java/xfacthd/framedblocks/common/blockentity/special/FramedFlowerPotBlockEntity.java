package xfacthd.framedblocks.common.blockentity.special;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import xfacthd.framedblocks.api.model.data.FramedBlockData;
import xfacthd.framedblocks.api.block.FramedBlockEntity;
import xfacthd.framedblocks.common.FBContent;

import java.util.List;

public class FramedFlowerPotBlockEntity extends FramedBlockEntity
{
    public static final FramedBlockData.Property<Block> FLOWER_BLOCK = new FramedBlockData.Property<>();

    private Block flowerBlock = Blocks.AIR;

    public FramedFlowerPotBlockEntity(BlockPos pos, BlockState state)
    {
        super(FBContent.BE_TYPE_FRAMED_FLOWER_POT.get(), pos, state);
    }

    public void setFlowerBlock(Block flowerBlock)
    {
        if (flowerBlock != this.flowerBlock)
        {
            this.flowerBlock = flowerBlock;

            setChanged();

            //noinspection ConstantConditions
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public boolean hasFlowerBlock()
    {
        return flowerBlock != Blocks.AIR;
    }

    public Block getFlowerBlock()
    {
        return flowerBlock;
    }

    @Override
    public void addAdditionalDrops(List<ItemStack> drops, boolean dropCamo)
    {
        super.addAdditionalDrops(drops, dropCamo);
        if (flowerBlock != Blocks.AIR)
        {
            drops.add(new ItemStack(flowerBlock));
        }
    }

    @Override
    public Object getRenderAttachmentData()
    {
        return super.getRenderAttachmentData();
    }

    @Override
    protected void writeToDataPacket(CompoundTag nbt)
    {
        super.writeToDataPacket(nbt);
        //noinspection ConstantConditions
        nbt.putString("flower", net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(flowerBlock).toString());
    }

    @Override
    protected boolean readFromDataPacket(CompoundTag nbt)
    {
        Block flower = net.minecraft.core.registries.BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse(nbt.getString("flower"))).orElse(Blocks.AIR);

        boolean update = flower != flowerBlock;
        if (update)
        {
            flowerBlock = flower;
        }

        return super.readFromDataPacket(nbt) || update;
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries)
    {
        CompoundTag nbt = super.getUpdateTag(registries);

        //noinspection ConstantConditions
        nbt.putString("flower", net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(flowerBlock).toString());

        return nbt;
    }

    @Override
    public CompoundTag writeToBlueprint()
    {
        CompoundTag tag = saveWithoutMetadata(level.registryAccess());
        tag.remove("flower");
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag nbt)
    {
        super.handleUpdateTag(nbt);

        Block flower = net.minecraft.core.registries.BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse(nbt.getString("flower"))).orElse(Blocks.AIR);
        if (flower != flowerBlock)
        {
            flowerBlock = flower;
        }
    }

    @Override
    public void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries)
    {
        //noinspection ConstantConditions
        nbt.putString("flower", net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(flowerBlock).toString());
        super.saveAdditional(nbt, registries);
    }

    @Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registries)
    {
        super.loadAdditional(nbt, registries);
        flowerBlock = net.minecraft.core.registries.BuiltInRegistries.BLOCK.getOptional(ResourceLocation.parse(nbt.getString("flower"))).orElse(Blocks.AIR);
    }
}