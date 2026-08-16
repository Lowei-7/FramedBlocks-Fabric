package net.minecraftforge.items.wrapper;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

/**
 * Fabric stub for Forge RecipeWrapper
 */
public class RecipeWrapper implements Container {
    private final IItemHandler inv;

    public RecipeWrapper(IItemHandler inv) {
        this.inv = inv;
    }

    @Override
    public int getContainerSize() {
        return inv.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < inv.getSlots(); i++) {
            if (!inv.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return inv.getStackInSlot(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        ItemStack stack = inv.getStackInSlot(slot);
        if (stack.isEmpty()) return ItemStack.EMPTY;
        int toRemove = Math.min(count, stack.getCount());
        if (inv instanceof IItemHandlerModifiable modifiable) {
            ItemStack result = stack.copy();
            result.setCount(toRemove);
            ItemStack remaining = stack.copy();
            remaining.shrink(toRemove);
            modifiable.setStackInSlot(slot, remaining);
            return result;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = inv.getStackInSlot(slot);
        if (inv instanceof IItemHandlerModifiable modifiable) {
            modifiable.setStackInSlot(slot, ItemStack.EMPTY);
        }
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (inv instanceof IItemHandlerModifiable modifiable) {
            modifiable.setStackInSlot(slot, stack);
        }
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        if (inv instanceof IItemHandlerModifiable modifiable) {
            for (int i = 0; i < inv.getSlots(); i++) {
                modifiable.setStackInSlot(i, ItemStack.EMPTY);
            }
        }
    }
}

