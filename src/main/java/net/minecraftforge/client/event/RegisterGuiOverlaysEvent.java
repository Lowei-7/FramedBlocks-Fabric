package net.minecraftforge.client.event;

import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.client.gui.overlay.NamedGuiOverlay;

public class RegisterGuiOverlaysEvent
{
    public void registerAboveAll(String id, IGuiOverlay overlay)
    {
        // Stub: In Fabric, use HudRenderCallback.EVENT.register()
    }

    public void registerAbove(NamedGuiOverlay other, String id, IGuiOverlay overlay)
    {
        // Stub
    }

    public void registerBelowAll(String id, IGuiOverlay overlay)
    {
        // Stub
    }

    public void registerBelow(NamedGuiOverlay other, String id, IGuiOverlay overlay)
    {
        // Stub
    }
}
