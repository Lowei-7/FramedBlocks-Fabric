package net.minecraftforge.registries;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class ForgeRegistries {
    public static final net.minecraft.core.Registry<Item> ITEMS = BuiltInRegistries.ITEM;
    public static final net.minecraft.core.Registry<Fluid> FLUIDS = BuiltInRegistries.FLUID;
    public static final net.minecraft.core.Registry<Block> BLOCKS = BuiltInRegistries.BLOCK;
}
