package net.minecraftforge.items.wrapper;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class InvWrapper implements IItemHandler {
    private final Container inv;
    
    public InvWrapper(Container inv) {
        this.inv = inv;
    }
    
    @Override
    public int getSlots() { return inv.getContainerSize(); }
    
    @Override
    public ItemStack getStackInSlot(int slot) { return inv.getItem(slot); }
    
    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) return ItemStack.EMPTY;
        ItemStack existing = inv.getItem(slot);
        if (existing.isEmpty()) {
            if (!simulate) inv.setItem(slot, stack.copy());
            return ItemStack.EMPTY;
        }
        return stack;
    }
    
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0) return ItemStack.EMPTY;
        ItemStack existing = inv.getItem(slot);
        if (existing.isEmpty()) return ItemStack.EMPTY;
        int toExtract = Math.min(amount, existing.getCount());
        ItemStack result = existing.copy();
        result.setCount(toExtract);
        if (!simulate) {
            existing.shrink(toExtract);
            if (existing.isEmpty()) inv.setItem(slot, ItemStack.EMPTY);
        }
        return result;
    }
    
    @Override
    public int getSlotLimit(int slot) { return inv.getMaxStackSize(); }
    
    @Override
    public boolean isItemValid(int slot, ItemStack stack) { return true; }
}
