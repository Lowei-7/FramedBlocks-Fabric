package net.minecraftforge.fluids.capability;

import net.minecraft.world.item.ItemStack;

public interface IFluidHandlerItem extends IFluidHandler {
    ItemStack getContainer();
}
