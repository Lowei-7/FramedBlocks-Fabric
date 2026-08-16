package net.minecraftforge.client.gui.overlay;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GameRenderer;

/**
 * Fabric compatibility shim for ForgeGui.
 * Provides a minimal subset of the Forge Gui used by the overlays.
 */
public class ForgeGui extends Gui
{
    public ForgeGui(net.minecraft.client.Minecraft minecraft)
    {
        super(minecraft);
    }

    public void setupOverlayRenderState(boolean blend, boolean depthTest)
    {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        if (depthTest)
        {
            RenderSystem.enableDepthTest();
        }
        else
        {
            RenderSystem.disableDepthTest();
        }
    }
}
