package net.minecraftforge.client.model.generators;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

public class BlockModelBuilder extends ModelBuilder<BlockModelBuilder>
{
    public BlockModelBuilder(ResourceLocation outputLocation, PackOutput output)
    {
        super(outputLocation, output);
    }
}
