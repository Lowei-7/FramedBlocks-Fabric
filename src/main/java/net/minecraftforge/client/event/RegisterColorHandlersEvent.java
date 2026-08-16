package net.minecraftforge.client.event;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class RegisterColorHandlersEvent
{
    public static class Block extends RegisterColorHandlersEvent
    {
        public void register(BlockColor color, net.minecraft.world.level.block.Block... blocks)
        {
            // Stub: In Fabric, use ColorProviderRegistry.BLOCK.register()
        }
    }

    public static class Item extends RegisterColorHandlersEvent
    {
        public void register(ItemColor color, Item... items)
        {
            // Stub: In Fabric, use ColorProviderRegistry.ITEM.register()
        }
    }
}
