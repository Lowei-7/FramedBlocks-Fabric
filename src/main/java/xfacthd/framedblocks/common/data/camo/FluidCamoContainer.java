package xfacthd.framedblocks.common.data.camo;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import xfacthd.framedblocks.api.camo.*;
import xfacthd.framedblocks.api.util.ClientUtils;
import xfacthd.framedblocks.api.util.Utils;
import xfacthd.framedblocks.common.FBContent;

public class FluidCamoContainer extends CamoContainer
{
    private static final int BUCKET_VOLUME = 1000;

    private final FluidState fluidState;

    private FluidCamoContainer(FluidState fluidState)
    {
        super(fluidState.createLegacyBlock());
        this.fluidState = fluidState;
    }

    @Override
    public int getColor(BlockAndTintGetter level, BlockPos pos, int tintIdx)
    {
        if (net.fabricmc.loader.api.FabricLoader.getInstance().getEnvironmentType() == net.fabricmc.api.EnvType.CLIENT)
        {
            return ClientUtils.getFluidColor(level, pos, fluidState);
        }
        throw new UnsupportedOperationException("Block color is not available on the server!");
    }

    @Override
    public ItemStack toItemStack(ItemStack stack)
    {
        if (stack.isEmpty())
        {
            return ItemStack.EMPTY;
        }

        Storage<FluidVariant> storage = FluidStorage.ITEM.find(stack, null);
        if (storage != null)
        {
            FluidVariant variant = FluidVariant.of(fluidState.getType());
            try (Transaction tx = Transaction.openOuter())
            {
                long inserted = storage.insert(variant, BUCKET_VOLUME, tx);
                if (inserted == BUCKET_VOLUME)
                {
                    tx.commit();
                    return stack.copy();
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public Fluid getFluid()
    {
        return fluidState.getType();
    }

    @Override
    public boolean canRotateCamo()
    {
        return false;
    }

    @Override
    public boolean rotateCamo()
    {
        return false;
    }

    @Override
    public SoundType getSoundType()
    {
        return SoundType.WET_GRASS;
    }

    @Override
    public boolean equals(Object o)
    {
        return this == o || (o != null && getClass() == o.getClass() && state == ((CamoContainer) o).getState());
    }

    @Override
    public int hashCode()
    {
        return state.hashCode();
    }

    @Override
    public ContainerType getType()
    {
        return ContainerType.FLUID;
    }

    @Override
    public CamoContainer.Factory getFactory()
    {
        return FBContent.FACTORY_FLUID.get();
    }

    @Override
    public void save(CompoundTag tag)
    {
        tag.put("fluid", NbtUtils.writeFluidState(fluidState));
    }

    @Override
    public void toNetwork(CompoundTag tag)
    {
        tag.putInt("fluid", BuiltInRegistries.FLUID.getId(fluidState.getType()));
    }



    public static final class Factory extends CamoContainer.Factory
    {
        @Override
        public CamoContainer fromNbt(CompoundTag tag)
        {
            FluidState fluidState = Utils.readFluidStateFromNbt(tag.getCompound("fluid"));
            return new FluidCamoContainer(fluidState);
        }

        @Override
        public CamoContainer fromNetwork(CompoundTag tag)
        {
            FluidState fluidState = BuiltInRegistries.FLUID.byId(tag.getInt("fluid")).defaultFluidState();
            return new FluidCamoContainer(fluidState);
        }

        @Override
        public CamoContainer fromItem(ItemStack stack)
        {
            Storage<FluidVariant> storage = FluidStorage.ITEM.find(stack, null);
            if (storage != null)
            {
                for (StorageView<FluidVariant> view : storage)
                {
                    if (!view.isResourceBlank())
                    {
                        FluidVariant variant = view.getResource();
                        if (view.getAmount() >= BUCKET_VOLUME && !variant.getFluid().defaultFluidState().isEmpty())
                        {
                            FluidState state = variant.getFluid().defaultFluidState();
                            if (!state.createLegacyBlock().isAir())
                            {
                                return new FluidCamoContainer(state);
                            }
                        }
                        break;
                    }
                }
            }
            return EmptyCamoContainer.EMPTY;
        }
    }
}
