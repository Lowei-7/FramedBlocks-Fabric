package net.minecraftforge.items;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;

/**
 * Fabric compatibility shim for Forge SlotItemHandler.
 * A Slot backed by an {@link IItemHandler}.
 */
public class SlotItemHandler extends Slot
{
    private final IItemHandler itemHandler;
    private final int index;

    public SlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition)
    {
        super(new RecipeWrapper(itemHandler), index, xPosition, yPosition);
        this.itemHandler = itemHandler;
        this.index = index;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack)
    {
        return !stack.isEmpty() && itemHandler.isItemValid(index, stack);
    }

    @Override
    @NotNull
    public ItemStack getItem()
    {
        return itemHandler.getStackInSlot(index);
    }

    @Override
    public void set(@NotNull ItemStack stack)
    {
        if (itemHandler instanceof IItemHandlerModifiable modifiable)
        {
            modifiable.setStackInSlot(index, stack);
        }
        setChanged();
    }

    @Override
    public void setChanged()
    {
        super.setChanged();
    }

    @Override
    public int getMaxStackSize()
    {
        return itemHandler.getSlotLimit(index);
    }

    @Override
    public int getMaxStackSize(@NotNull ItemStack stack)
    {
        ItemStack maxAdd = stack.copy();
        int maxStackSize = getMaxStackSize();
        maxAdd.setCount(maxStackSize);
        ItemStack currentStack = itemHandler.getStackInSlot(index);
        if (currentStack.isEmpty())
        {
            return maxStackSize;
        }
        ItemStack remainder = itemHandler.insertItem(index, maxAdd, true);
        return maxStackSize - remainder.getCount();
    }

    @Override
    public boolean mayPickup(Player player)
    {
        return !itemHandler.extractItem(index, 1, true).isEmpty();
    }

    @Override
    @NotNull
    public ItemStack remove(int amount)
    {
        return itemHandler.extractItem(index, amount, false);
    }

    public boolean isSameInventory(Slot other)
    {
        if (other instanceof SlotItemHandler otherSlot)
        {
            return this.itemHandler == otherSlot.itemHandler;
        }
        return false;
    }
}
