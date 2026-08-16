package xfacthd.framedblocks.common.util;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import xfacthd.framedblocks.common.util.fabric.RegistryObject;

import java.util.function.Supplier;

public record RegisteredBE<T extends BlockEntity>(RegistryObject<BlockEntityType<T>> value) implements Supplier<BlockEntityType<T>>
{
    @Override
    public BlockEntityType<T> get()
    {
        return value.get();
    }
}
