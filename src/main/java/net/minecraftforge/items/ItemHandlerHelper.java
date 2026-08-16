package net.minecraftforge.items;

import net.minecraft.world.item.ItemStack;

public class ItemHandlerHelper {
    public static ItemStack copyStackWithSize(ItemStack stack, int size) {
        if (stack.isEmpty()) return ItemStack.EMPTY;
        ItemStack copy = stack.copy();
        copy.setCount(size);
        return copy;
    }
    
    public static boolean canItemStacksStack(ItemStack a, ItemStack b) {
        if (a.isEmpty() || b.isEmpty()) return false;
        return ItemStack.isSameItemSameComponents(a, b);
    }
    
    public static ItemStack insertItem(IItemHandler dest, ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) return stack;
        for (int i = 0; i < dest.getSlots(); i++) {
            stack = dest.insertItem(i, stack, simulate);
            if (stack.isEmpty()) return ItemStack.EMPTY;
        }
        return stack;
    }
}
