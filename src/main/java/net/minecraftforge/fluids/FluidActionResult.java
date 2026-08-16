package net.minecraftforge.fluids;

import net.minecraft.world.item.ItemStack;

public class FluidActionResult {
    private final ItemStack result;
    private final boolean success;
    
    public FluidActionResult(ItemStack result) {
        this.result = result;
        this.success = !result.isEmpty();
    }
    
    public ItemStack getResult() { return result; }
    public boolean isSuccess() { return success; }
    
    public static final FluidActionResult FAILURE = new FluidActionResult(ItemStack.EMPTY) {
        @Override
        public boolean isSuccess() { return false; }
    };
}
