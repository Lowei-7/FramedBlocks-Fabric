package net.minecraftforge.items;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;

public class ItemStackHandler implements IItemHandlerModifiable {
    private NonNullList<ItemStack> stacks;
    
    public ItemStackHandler() {
        this(1);
    }
    
    public ItemStackHandler(int size) {
        stacks = NonNullList.withSize(size, ItemStack.EMPTY);
    }
    
    @Override
    public int getSlots() { return stacks.size(); }
    
    @Override
    public ItemStack getStackInSlot(int slot) {
        validateSlotIndex(slot);
        return stacks.get(slot);
    }
    
    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) return ItemStack.EMPTY;
        validateSlotIndex(slot);
        ItemStack existing = stacks.get(slot);
        if (existing.isEmpty()) {
            int limit = Math.min(stack.getCount(), getSlotLimit(slot));
            if (!simulate) {
                stacks.set(slot, copyStackWithSize(stack, limit));
                onContentsChanged(slot);
            }
            return limit >= stack.getCount() ? ItemStack.EMPTY : copyStackWithSize(stack, stack.getCount() - limit);
        }
        if (!ItemHandlerHelper.canItemStacksStack(stack, existing)) return stack;
        int limit = Math.min(getSlotLimit(slot), existing.getMaxStackSize());
        if (existing.getCount() >= limit) return stack;
        int accepted = Math.min(limit - existing.getCount(), stack.getCount());
        if (!simulate) {
            existing.grow(accepted);
            onContentsChanged(slot);
        }
        return accepted >= stack.getCount() ? ItemStack.EMPTY : copyStackWithSize(stack, stack.getCount() - accepted);
    }
    
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0) return ItemStack.EMPTY;
        validateSlotIndex(slot);
        ItemStack existing = stacks.get(slot);
        if (existing.isEmpty()) return ItemStack.EMPTY;
        int toExtract = Math.min(amount, existing.getMaxStackSize());
        if (existing.getCount() <= toExtract) {
            if (!simulate) {
                stacks.set(slot, ItemStack.EMPTY);
                onContentsChanged(slot);
            }
            return existing;
        }
        if (!simulate) {
            stacks.set(slot, copyStackWithSize(existing, existing.getCount() - toExtract));
            onContentsChanged(slot);
        }
        return copyStackWithSize(existing, toExtract);
    }
    
    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }
    
    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return true;
    }
    
    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        validateSlotIndex(slot);
        stacks.set(slot, stack);
        onContentsChanged(slot);
    }
    
    protected void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= stacks.size())
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + stacks.size() + ")");
    }
    
    protected void onContentsChanged(int slot) {}

    private ItemStack copyStackWithSize(ItemStack stack, int size) {
        ItemStack copy = stack.copy();
        copy.setCount(size);
        return copy;
    }
}
