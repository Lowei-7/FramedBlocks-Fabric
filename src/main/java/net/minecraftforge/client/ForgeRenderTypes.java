package net.minecraftforge.client;

import net.minecraft.client.renderer.RenderType;
import java.util.function.Supplier;

public class ForgeRenderTypes
{
    public static final Supplier<RenderType> TRANSLUCENT_ON_PARTICLES_TARGET = () -> RenderType.translucent();
}
