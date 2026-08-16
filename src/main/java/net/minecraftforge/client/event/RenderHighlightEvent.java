package net.minecraftforge.client.event;

import net.minecraft.world.phys.BlockHitResult;

public class RenderHighlightEvent
{
    public static class Block extends RenderHighlightEvent
    {
        private final BlockHitResult target;

        public Block(BlockHitResult target)
        {
            this.target = target;
        }

        public BlockHitResult getTarget()
        {
            return target;
        }
    }
}
