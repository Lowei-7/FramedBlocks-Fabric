package net.createmod.catnip.nbt;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.function.UnaryOperator;

/**
 * Fabric port stub for the Create (Catnip) NBT processor helpers used by FramedBlocks.
 * The Create Fabric port does not ship the net.createmod.catnip package, so the two
 * methods used by the FramedBlocks Create integration are provided here.
 */
public final class NBTProcessors
{
    public static ItemStack withUnsafeNBTDiscarded(ItemStack stack)
    {
        return stack;
    }

    public static UnaryOperator<CompoundTag> itemProcessor(String itemKey)
    {
        return UnaryOperator.identity();
    }

    private NBTProcessors() { }
}
