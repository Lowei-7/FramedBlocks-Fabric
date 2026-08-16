package net.minecraftforge.common;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class ForgeHooks
{
    public static int getBurnTime(ItemStack stack, RecipeType<?> recipeType)
    {
        return 0; // Stub
    }
}
