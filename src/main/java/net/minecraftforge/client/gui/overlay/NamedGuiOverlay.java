package net.minecraftforge.client.gui.overlay;

import net.minecraft.resources.ResourceLocation;

public record NamedGuiOverlay(ResourceLocation id, IGuiOverlay overlay)
{
}
