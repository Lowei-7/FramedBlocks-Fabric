package net.minecraftforge.client.gui.overlay;

import net.minecraft.client.gui.GuiGraphics;

public interface IGuiOverlay
{
    void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight);
}
